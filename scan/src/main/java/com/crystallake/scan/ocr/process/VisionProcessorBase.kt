package com.crystallake.scan.ocr.process

import android.app.ActivityManager
import android.content.Context
import android.graphics.Bitmap
import android.os.SystemClock
import android.util.Log
import com.crystallake.scan.ocr.ScopedExecutor
import com.crystallake.scan.utils.BitmapUtils
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.TaskExecutors
import com.google.android.gms.tasks.Tasks
import com.google.android.odml.image.BitmapMlImageBuilder
import com.google.mlkit.common.MlKitException
import com.google.mlkit.vision.common.InputImage
import java.util.*
import kotlin.math.max
import kotlin.math.min

abstract class VisionProcessorBase<T>(private val context: Context) : VisionImageProcessor {

    companion object {
        const val TAG = "VisionProcessorBase"
    }

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

            }

        }, 0, 1000)
    }

    override fun processBitmap(bitmap: Bitmap, context: Context) {
        val frameStartMs = SystemClock.elapsedRealtime()
        if (isMlImageEnabled(context)) {
            val mlImage = BitmapMlImageBuilder(bitmap).build()
            requestDetectInImage(frameStartMs)
            mlImage.close()
            return
        }
        requestDetectInImage(InputImage.fromBitmap(bitmap, 0), frameStartMs)
    }

    private fun requestDetectInImage(
        image: InputImage,
        frameStartMs: Long
    ): Task<T>? {
        return setUpListener(detectInImage(image), frameStartMs)
    }

    private fun requestDetectInImage(
        frameStartMs: Long
    ): Task<T>? {
        return setUpListener(
            Tasks.forException(
                MlKitException(
                    "MlImage is currently not demonstrated for this feature",
                    MlKitException.INVALID_ARGUMENT
                )
            ), frameStartMs
        )
    }

    private fun setUpListener(task: Task<T>?, frameStartMs: Long): Task<T>? {
        val detectorStartMs = SystemClock.elapsedRealtime()
        return task?.addOnSuccessListener(
            executor
        ) { result ->
            val endMs = SystemClock.elapsedRealtime()
            val currentFrameLatencyMs = endMs - frameStartMs
            val currentDetectorLatencyMs = endMs - detectorStartMs
            if (numRuns >= 500) {
                resetLatencyStats()
            }
            numRuns++
            frameProcessedInOneSecondInterval++
            totalFrameMs += currentFrameLatencyMs
            maxFrameMs = max(currentFrameLatencyMs, maxFrameMs)
            minFrameMs = min(currentFrameLatencyMs, minFrameMs)
            totalDetectorMs += currentDetectorLatencyMs
            maxDetectorMs = max(currentDetectorLatencyMs, maxDetectorMs)
            minDetectorMs = min(currentDetectorLatencyMs, minDetectorMs)


            // Only log inference info once per second. When frameProcessedInOneSecondInterval is
            // equal to 1, it means this is the first frame processed during the current second.
            if (frameProcessedInOneSecondInterval == 1) {
                Log.d(VisionProcessorBase.TAG, "Num of Runs: $numRuns")
                Log.d(
                    VisionProcessorBase.TAG,
                    "Frame latency: max="
                            + maxFrameMs
                            + ", min="
                            + minFrameMs
                            + ", avg="
                            + totalFrameMs / numRuns
                )
                Log.d(
                    VisionProcessorBase.TAG,
                    ("Detector latency: max="
                            + maxDetectorMs
                            + ", min="
                            + minDetectorMs
                            + ", avg="
                            + (totalDetectorMs / numRuns))
                )
                val mi = ActivityManager.MemoryInfo()
                activityManager.getMemoryInfo(mi)
                val availableMegs = mi.availMem / 0x100000L
                Log.d(VisionProcessorBase.TAG, "Memory available in system: $availableMegs MB")
            }
            onSuccess(result)
        }?.addOnFailureListener(executor) {
            val error = "Failed to process. Error: " + it.getLocalizedMessage()
            Log.d(TAG, error)
            onFailure(it)
        }
    }

    override fun stop() {
        executor.shutdown()
        isShutdown = true
        resetLatencyStats()
        fpsTimer.cancel()
    }

    abstract fun onSuccess(results: T)
    abstract fun onFailure(e: Exception)
    internal abstract fun detectInImage(image: InputImage?): Task<T>?

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
}