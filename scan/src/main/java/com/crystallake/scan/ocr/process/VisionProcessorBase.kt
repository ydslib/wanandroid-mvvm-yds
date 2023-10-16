package com.crystallake.scan.ocr.process

import android.app.ActivityManager
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.os.SystemClock
import android.util.Log
import android.widget.Toast
import androidx.annotation.GuardedBy
import androidx.annotation.RequiresApi
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageProxy
import com.crystallake.scan.ocr.ScopedExecutor
import com.crystallake.scan.ocr.live.CameraImageGraphic
import com.crystallake.scan.ocr.live.GraphicOverlay
import com.crystallake.scan.ocr.live.InferenceInfoGraphic
import com.crystallake.scan.ocr.live.VisionImageProcessor
import com.crystallake.scan.utils.BitmapUtils
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.TaskExecutors
import com.google.android.gms.tasks.Tasks
import com.google.android.odml.image.BitmapMlImageBuilder
import com.google.android.odml.image.ByteBufferMlImageBuilder
import com.google.android.odml.image.MediaMlImageBuilder
import com.google.android.odml.image.MlImage
import com.google.mlkit.common.MlKitException
import com.google.mlkit.vision.common.InputImage
import java.nio.ByteBuffer
import java.util.*

abstract class VisionProcessorBase<T>(private val context: Context) : VisionImageProcessor {

    companion object {
        const val TAG = "VisionProcessorBase"
    }

    @GuardedBy("this")
    private var latestImage: ByteBuffer? = null
    @GuardedBy("this")
    private var latestImageMetaData: FrameMetadata? = null

    @GuardedBy("this")
    private var processingImage: ByteBuffer? = null
    @GuardedBy("this")
    private var processingMetaData: FrameMetadata? = null

    // Used to calculate latency, running in the same thread, no sync needed.
    private var numRuns = 0
    private var totalFrameMs: Long = 0
    private var maxFrameMs: Long = 0
    private var minFrameMs = Long.MAX_VALUE
    private var totalDetectorMs: Long = 0
    private var maxDetectorMs: Long = 0
    private var minDetectorMs = Long.MAX_VALUE
    private var isShutdown = false

    private var frameProcessedInOneSecondInterval = 0
    private var framesPerSecond = 0

    val activityManager by lazy {
        context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    }

    val executor by lazy {
        ScopedExecutor(TaskExecutors.MAIN_THREAD)
    }

    val fpsTimer by lazy {
        Timer()
    }

    init {
        fpsTimer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                framesPerSecond = frameProcessedInOneSecondInterval
                frameProcessedInOneSecondInterval = 0
            }

        }, 0, 1000)
    }

    override fun processBitmap(bitmap: Bitmap, graphicOverlay: GraphicOverlay) {
        val frameStartMs = SystemClock.elapsedRealtime()
        if (isMlImageEnabled(context)) {
            val mlImage = BitmapMlImageBuilder(bitmap).build()
            requestDetectInImage(mlImage, graphicOverlay, null, false, frameStartMs)
            mlImage.close()
            return
        }
        requestDetectInImage(InputImage.fromBitmap(bitmap, 0), graphicOverlay, bitmap, false, frameStartMs)
    }

    private fun requestDetectInImage(
        image: InputImage,
        graphicOverlay: GraphicOverlay?,
        originalCameraImage: Bitmap?,
        shouldShowFps: Boolean,
        frameStartMs: Long
    ): Task<T>? {
        return setUpListener(
            detectInImage(image),
            graphicOverlay,
            originalCameraImage,
            shouldShowFps,
            frameStartMs
        )
    }

    private fun requestDetectInImage(
        image: MlImage,
        graphicOverlay: GraphicOverlay?,
        originalCameraImage: Bitmap?,
        shouldShowFps: Boolean,
        frameStartMs: Long
    ): Task<T>? {
        return setUpListener(
            detectInImage(image),
            graphicOverlay,
            originalCameraImage,
            shouldShowFps,
            frameStartMs
        )
    }

    override fun stop() {
        executor.shutdown()
        isShutdown = true
        resetLatencyStats()
        fpsTimer.cancel()
    }

    abstract fun onSuccess(results: T?, graphicOverlay: GraphicOverlay?)
    abstract fun onFailure(e: Exception)
    internal abstract fun detectInImage(image: InputImage?): Task<T>?
    protected open fun detectInImage(image: MlImage): Task<T> {
        return Tasks.forException(
            MlKitException(
                "MlImage is currently not demonstrated for this feature",
                MlKitException.INVALID_ARGUMENT
            )
        )
    }

    private fun resetLatencyStats() {
        numRuns = 0
        totalFrameMs = 0
        maxFrameMs = 0
        minFrameMs = Long.MAX_VALUE
        totalDetectorMs = 0
        maxDetectorMs = 0
        minDetectorMs = Long.MAX_VALUE
    }

    internal open fun isMlImageEnabled(context: Context): Boolean {
        return false
    }

    override fun processByteBuffer(data: ByteBuffer?, frameMetadata: FrameMetadata?, graphicOverlay: GraphicOverlay?) {
        latestImage = data
        latestImageMetaData = frameMetadata
        if (processingImage == null && processingMetaData == null) {
            processLatestImage(graphicOverlay)
        }
    }

    @Synchronized
    private fun processLatestImage(graphicOverlay: GraphicOverlay?) {
        processingImage = latestImage
        processingMetaData = latestImageMetaData
        latestImage = null
        latestImageMetaData = null
        if (processingImage != null && processingMetaData != null && !isShutdown) {
            processImage(processingImage!!, processingMetaData!!, graphicOverlay)
        }
    }

    private fun processImage(
        data: ByteBuffer,
        frameMetadata: FrameMetadata,
        graphicOverlay: GraphicOverlay?
    ) {
        val frameStartMs = SystemClock.elapsedRealtime()
        // If live viewport is on (that is the underneath surface view takes care of the camera preview
        // drawing), skip the unnecessary bitmap creation that used for the manual preview drawing.
        val bitmap =
            if (PreferenceUtils.isCameraLiveViewportEnabled(graphicOverlay!!.context)) null
            else BitmapUtils.getBitmap(data, frameMetadata)

        if (isMlImageEnabled(graphicOverlay.context)) {
            val mlImage =
                ByteBufferMlImageBuilder(
                    data,
                    frameMetadata.width,
                    frameMetadata.height,
                    MlImage.IMAGE_FORMAT_NV21
                )
                    .setRotation(frameMetadata.rotation)
                    .build()
            requestDetectInImage(mlImage, graphicOverlay, bitmap, /* shouldShowFps= */ true, frameStartMs)
                ?.addOnSuccessListener(executor) { processLatestImage(graphicOverlay) }

            // This is optional. Java Garbage collection can also close it eventually.
            mlImage.close()
            return
        }

        requestDetectInImage(
            InputImage.fromByteBuffer(
                data,
                frameMetadata.width,
                frameMetadata.height,
                frameMetadata.rotation,
                InputImage.IMAGE_FORMAT_NV21
            ),
            graphicOverlay,
            bitmap,
            /* shouldShowFps= */ true,
            frameStartMs
        )
            ?.addOnSuccessListener(executor) { processLatestImage(graphicOverlay) }
    }

    private fun setUpListener(
        task: Task<T>?,
        graphicOverlay: GraphicOverlay?,
        originalCameraImage: Bitmap?,
        shouldShowFps: Boolean,
        frameStartMs: Long
    ): Task<T>? {
        val detectorStartMs = SystemClock.elapsedRealtime()
        return task
            ?.addOnSuccessListener(
                executor,
                OnSuccessListener { results: T ->
                    val endMs = SystemClock.elapsedRealtime()
                    val currentFrameLatencyMs = endMs - frameStartMs
                    val currentDetectorLatencyMs = endMs - detectorStartMs
                    if (numRuns >= 500) {
                        resetLatencyStats()
                    }
                    numRuns++
                    frameProcessedInOneSecondInterval++
                    totalFrameMs += currentFrameLatencyMs
                    maxFrameMs = Math.max(currentFrameLatencyMs, maxFrameMs)
                    minFrameMs = Math.min(currentFrameLatencyMs, minFrameMs)
                    totalDetectorMs += currentDetectorLatencyMs
                    maxDetectorMs = Math.max(currentDetectorLatencyMs, maxDetectorMs)
                    minDetectorMs = Math.min(currentDetectorLatencyMs, minDetectorMs)

                    // Only log inference info once per second. When frameProcessedInOneSecondInterval is
                    // equal to 1, it means this is the first frame processed during the current second.
                    if (frameProcessedInOneSecondInterval == 1) {
                        Log.d(TAG, "Num of Runs: $numRuns")
                        Log.d(
                            TAG,
                            "Frame latency: max=" +
                                    maxFrameMs +
                                    ", min=" +
                                    minFrameMs +
                                    ", avg=" +
                                    totalFrameMs / numRuns
                        )
                        Log.d(
                            TAG,
                            "Detector latency: max=" +
                                    maxDetectorMs +
                                    ", min=" +
                                    minDetectorMs +
                                    ", avg=" +
                                    totalDetectorMs / numRuns
                        )
                        val mi = ActivityManager.MemoryInfo()
                        activityManager.getMemoryInfo(mi)
                        val availableMegs: Long = mi.availMem / 0x100000L
                        Log.d(TAG, "Memory available in system: $availableMegs MB")
                    }
                    graphicOverlay?.clear()
                    if (originalCameraImage != null) {
                        graphicOverlay?.add(CameraImageGraphic(graphicOverlay, originalCameraImage))
                    }
                    this@VisionProcessorBase.onSuccess(results, graphicOverlay)
                    if (!PreferenceUtils.shouldHideDetectionInfo(graphicOverlay!!.context)) {
                        graphicOverlay.add(
                            InferenceInfoGraphic(
                                graphicOverlay,
                                currentFrameLatencyMs,
                                currentDetectorLatencyMs,
                                if (shouldShowFps) framesPerSecond else null
                            )
                        )
                    }
                    graphicOverlay.postInvalidate()
                }
            )
            ?.addOnFailureListener(
                executor,
                OnFailureListener { e: Exception ->
                    graphicOverlay?.clear()
                    graphicOverlay?.postInvalidate()
                    val error = "Failed to process. Error: " + e.localizedMessage
                    Toast.makeText(
                        graphicOverlay?.context,
                        """
          $error
          Cause: ${e.cause}
          """.trimIndent(),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    Log.d(TAG, error)
                    e.printStackTrace()
                    this@VisionProcessorBase.onFailure(e)
                }
            )
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @ExperimentalGetImage
    override fun processImageProxy(image: ImageProxy, graphicOverlay: GraphicOverlay) {
        val frameStartMs = SystemClock.elapsedRealtime()
        if (isShutdown) {
            return
        }
        var bitmap: Bitmap? = null
        if (!PreferenceUtils.isCameraLiveViewportEnabled(graphicOverlay.context)) {
            bitmap = BitmapUtils.getBitmap(image)
        }

        if (isMlImageEnabled(graphicOverlay.context)) {
            val mlImage =
                MediaMlImageBuilder(image.image!!).setRotation(image.imageInfo.rotationDegrees).build()
            requestDetectInImage(
                mlImage,
                graphicOverlay,
                /* originalCameraImage= */ bitmap,
                /* shouldShowFps= */ true,
                frameStartMs
            )
                // When the image is from CameraX analysis use case, must call image.close() on received
                // images when finished using them. Otherwise, new images may not be received or the camera
                // may stall.
                // Currently MlImage doesn't support ImageProxy directly, so we still need to call
                // ImageProxy.close() here.
                ?.addOnCompleteListener { image.close() }

            return
        }

        requestDetectInImage(
            InputImage.fromMediaImage(image.image!!, image.imageInfo.rotationDegrees),
            graphicOverlay,
            /* originalCameraImage= */ bitmap,
            /* shouldShowFps= */ true,
            frameStartMs
        )
            // When the image is from CameraX analysis use case, must call image.close() on received
            // images when finished using them. Otherwise, new images may not be received or the camera
            // may stall.
            ?.addOnCompleteListener { image.close() }
    }
}