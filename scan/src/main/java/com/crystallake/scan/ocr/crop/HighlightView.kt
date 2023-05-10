/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.crystallake.scan.ocr.crop

import android.util.TypedValue
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.os.Build
import android.view.View
import kotlin.math.max
import kotlin.math.min
import com.crystallake.scan.R

/*
 * Modified from version in AOSP.
 *
 * This class is used to display a highlighted cropping rectangle
 * overlayed on the image. There are two coordinate spaces in use. One is
 * image, another is screen. computeLayout() uses matrix to map from image
 * space to screen space.
 */
class HighlightView(  // View displaying image
    private val viewContext: View?
) {
    enum class ModifyMode {
        None, Move, Grow
    }

    enum class HandleMode {
        Changing, Always, Never
    }

    var cropRect // Image space
            : RectF? = null
    var drawRect // Screen space
            : Rect? = null
    var matrix: Matrix? = null
    private var imageRect // Image space
            : RectF? = null
    private val outsidePaint = Paint()
    private val outlinePaint = Paint()
    private val handlePaint = Paint()
    private var showThirds = false
    private var showCircle = false
    private var highlightColor = 0
    private var modifyMode = ModifyMode.None
    private var handleMode = HandleMode.Changing
    private var maintainAspectRatio = false
    private var initialAspectRatio = 0f
    private var handleRadius = 0f
    private var outlineWidth = 0f
    private var isFocused = false
    private fun initStyles(context: Context?) {
        val outValue = TypedValue()
        context?.theme?.resolveAttribute(R.attr.cropImageStyle, outValue, true)
        val attributes = context?.obtainStyledAttributes(outValue.resourceId, R.styleable.CropImageView)
        try {
            showThirds = attributes?.getBoolean(R.styleable.CropImageView_showThirds, false) ?: false
            showCircle = attributes?.getBoolean(R.styleable.CropImageView_showCircle, false) ?: false
            highlightColor = attributes?.getColor(
                R.styleable.CropImageView_highlightColor,
                DEFAULT_HIGHLIGHT_COLOR
            ) ?: 0
            handleMode = HandleMode.values()[attributes?.getInt(R.styleable.CropImageView_showHandles, 0) ?: 0]
        } finally {
            attributes?.recycle()
        }
    }

    fun setup(m: Matrix?, imageRect: Rect?, cropRect: RectF?, maintainAspectRatio: Boolean) {
        matrix = Matrix(m)
        this.cropRect = cropRect
        this.imageRect = RectF(imageRect)
        this.maintainAspectRatio = maintainAspectRatio
        initialAspectRatio = (this.cropRect?.width() ?: 0f) / (this.cropRect?.height() ?: 1f)
        drawRect = computeLayout()
        outsidePaint.setARGB(125, 50, 50, 50)
        outlinePaint.style = Paint.Style.STROKE
        outlinePaint.isAntiAlias = true
        outlineWidth = dpToPx(OUTLINE_DP)
        handlePaint.color = highlightColor
        handlePaint.style = Paint.Style.FILL
        handlePaint.isAntiAlias = true
        handleRadius = dpToPx(HANDLE_RADIUS_DP)
        modifyMode = ModifyMode.None
    }

    private fun dpToPx(dp: Float): Float {
        return dp * (viewContext?.resources?.displayMetrics?.density ?: 1f)
    }

    fun draw(canvas: Canvas) {
        canvas.save()
        val path = Path()
        outlinePaint.strokeWidth = outlineWidth
        if (!hasFocus()) {
            outlinePaint.color = Color.BLACK
            drawRect?.let {
                canvas.drawRect(it, outlinePaint)
            }
        } else {
            val viewDrawingRect = Rect()
            viewContext?.getDrawingRect(viewDrawingRect)
            path.addRect(RectF(drawRect), Path.Direction.CW)
            outlinePaint.color = highlightColor
            if (isClipPathSupported(canvas)) {
                canvas.clipPath(path, Region.Op.DIFFERENCE)
                canvas.drawRect(viewDrawingRect, outsidePaint)
            } else {
                drawOutsideFallback(canvas)
            }
            canvas.restore()
            canvas.drawPath(path, outlinePaint)
            if (showThirds) {
                drawThirds(canvas)
            }
            if (showCircle) {
                drawCircle(canvas)
            }
            if (handleMode == HandleMode.Always ||
                handleMode == HandleMode.Changing && modifyMode == ModifyMode.Grow
            ) {
                drawHandles(canvas)
            }
        }
    }

    /*
     * Fall back to naive method for darkening outside crop area
     */
    private fun drawOutsideFallback(canvas: Canvas) {
        val drawRectTop = drawRect?.top?.toFloat()?:0f
        val drawRectBottom = drawRect?.bottom?.toFloat()?:0f
        val drawRectLeft = drawRect?.left?.toFloat()?:0f
        val drawRectRight = drawRect?.right?.toFloat()?:0f
        canvas.drawRect(0f, 0f, canvas.width.toFloat(), drawRectTop, outsidePaint)
        canvas.drawRect(0f, drawRectBottom, canvas.width.toFloat(), canvas.height.toFloat(), outsidePaint)
        canvas.drawRect(0f, drawRectTop, drawRectLeft, drawRectBottom, outsidePaint)
        canvas.drawRect(drawRectRight, drawRectTop, canvas.width.toFloat(), drawRectBottom, outsidePaint)
    }

    /*
     * Clip path is broken, unreliable or not supported on:
     * - JellyBean MR1
     * - ICS & ICS MR1 with hardware acceleration turned on
     */
    @SuppressLint("NewApi")
    private fun isClipPathSupported(canvas: Canvas): Boolean {
        return if (Build.VERSION.SDK_INT == Build.VERSION_CODES.JELLY_BEAN_MR1) {
            false
        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH
            || Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1
        ) {
            true
        } else {
            !canvas.isHardwareAccelerated
        }
    }

    private fun drawHandles(canvas: Canvas) {
        val drawRectLeft = drawRect?.left?:0
        val drawRectRight = drawRect?.right?:0
        val drawRectTop = drawRect?.top?:0
        val drawRectBottom = drawRect?.bottom?:0
        val xMiddle = drawRectLeft + (drawRectRight - drawRectLeft) / 2
        val yMiddle = drawRectTop + (drawRectBottom - drawRectTop) / 2
        canvas.drawCircle(drawRectLeft.toFloat(), yMiddle.toFloat(), handleRadius, handlePaint)
        canvas.drawCircle(xMiddle.toFloat(), drawRectTop.toFloat(), handleRadius, handlePaint)
        canvas.drawCircle(drawRectRight.toFloat(), yMiddle.toFloat(), handleRadius, handlePaint)
        canvas.drawCircle(xMiddle.toFloat(), drawRectBottom.toFloat(), handleRadius, handlePaint)
    }

    private fun drawThirds(canvas: Canvas) {
        val drawRectRight = drawRect?.right?:0;
        val drawRectLeft = drawRect?.left?:0;
        val drawRectTop = drawRect?.top?:0;
        val drawRectBottom = drawRect?.bottom?:0;

        outlinePaint.strokeWidth = 1f
        val xThird = ((drawRectRight - drawRectLeft) / 3).toFloat()
        val yThird = ((drawRectBottom - drawRectTop) / 3).toFloat()
        canvas.drawLine(
            drawRectLeft + xThird, drawRectTop.toFloat(),
            drawRectLeft + xThird, drawRectBottom.toFloat(), outlinePaint
        )
        canvas.drawLine(
            drawRectLeft + xThird * 2, drawRectTop.toFloat(),
            drawRectLeft + xThird * 2, drawRectBottom.toFloat(), outlinePaint
        )
        canvas.drawLine(
            drawRectLeft.toFloat(), drawRectTop + yThird,
            drawRectRight.toFloat(), drawRectTop + yThird, outlinePaint
        )
        canvas.drawLine(
            drawRectLeft.toFloat(), drawRectTop + yThird * 2,
            drawRectRight.toFloat(), drawRectTop + yThird * 2, outlinePaint
        )
    }

    private fun drawCircle(canvas: Canvas) {
        outlinePaint.strokeWidth = 1f
        canvas.drawOval(RectF(drawRect), outlinePaint)
    }

    fun setMode(mode: ModifyMode?) {
        if (mode != modifyMode) {
            modifyMode = mode ?: ModifyMode.None
            viewContext?.invalidate()
        }
    }

    // Determines which edges are hit by touching at (x, y)
    fun getHit(x: Float, y: Float): Int {
        val r = computeLayout()
        val hysteresis = 20f
        var retval = GROW_NONE

        // verticalCheck makes sure the position is between the top and
        // the bottom edge (with some tolerance). Similar for horizCheck.
        val verticalCheck = (y >= r.top - hysteresis
                && y < r.bottom + hysteresis)
        val horizCheck = (x >= r.left - hysteresis
                && x < r.right + hysteresis)

        // Check whether the position is near some edge(s)
        if (Math.abs(r.left - x) < hysteresis && verticalCheck) {
            retval = retval or GROW_LEFT_EDGE
        }
        if (Math.abs(r.right - x) < hysteresis && verticalCheck) {
            retval = retval or GROW_RIGHT_EDGE
        }
        if (Math.abs(r.top - y) < hysteresis && horizCheck) {
            retval = retval or GROW_TOP_EDGE
        }
        if (Math.abs(r.bottom - y) < hysteresis && horizCheck) {
            retval = retval or GROW_BOTTOM_EDGE
        }

        // Not near any edge but inside the rectangle: move
        if (retval == GROW_NONE && r.contains(x.toInt(), y.toInt())) {
            retval = MOVE
        }
        return retval
    }

    // Handles motion (dx, dy) in screen space.
    // The "edge" parameter specifies which edges the user is dragging.
    fun handleMotion(edge: Int, dx: Float, dy: Float) {
        var dx = dx
        var dy = dy
        val r = computeLayout()
        if (edge == MOVE) {
            // Convert to image space before sending to moveBy()
            moveBy(
                dx * ((cropRect?.width() ?: 0f) / r.width()),
                dy * ((cropRect?.height() ?: 0f) / r.height())
            )
        } else {
            if (GROW_LEFT_EDGE or GROW_RIGHT_EDGE and edge == 0) {
                dx = 0f
            }
            if (GROW_TOP_EDGE or GROW_BOTTOM_EDGE and edge == 0) {
                dy = 0f
            }

            // Convert to image space before sending to growBy()
            val xDelta = dx * ((cropRect?.width() ?: 0f) / r.width())
            val yDelta = dy * ((cropRect?.height() ?: 0f) / r.height())
            growBy(
                (if (edge and GROW_LEFT_EDGE != 0) -1 else 1) * xDelta,
                (if (edge and GROW_TOP_EDGE != 0) -1 else 1) * yDelta
            )
        }
    }

    // Grows the cropping rectangle by (dx, dy) in image space
    fun moveBy(dx: Float, dy: Float) {
        val invalRect = Rect(drawRect)
        cropRect?.offset(dx, dy)

        // Put the cropping rectangle inside image rectangle
        cropRect?.offset(
            max(0f, (imageRect?.left ?: 0f) - (cropRect?.left ?: 0f)),
            max(0f, (imageRect?.top ?: 0f) - (cropRect?.top ?: 0f))
        )
        cropRect?.offset(
            min(0f, (imageRect?.right ?: 0f) - (cropRect?.right ?: 0f)),
            min(0f, (imageRect?.bottom ?: 0f) - (cropRect?.bottom ?: 0f))
        )
        drawRect = computeLayout()
        drawRect?.let {
            invalRect.union(it)
        }

        invalRect.inset(-handleRadius.toInt(), -handleRadius.toInt())
        viewContext?.invalidate(invalRect)
    }

    // Grows the cropping rectangle by (dx, dy) in image space.
    fun growBy(dx: Float, dy: Float) {
        var dx = dx
        var dy = dy
        if (maintainAspectRatio) {
            if (dx != 0f) {
                dy = dx / initialAspectRatio
            } else if (dy != 0f) {
                dx = dy * initialAspectRatio
            }
        }

        // Don't let the cropping rectangle grow too fast.
        // Grow at most half of the difference between the image rectangle and
        // the cropping rectangle.
        val r = RectF(cropRect)
        if (dx > 0f && r.width() + 2 * dx > (imageRect?.width() ?: 0f)) {
            dx = ((imageRect?.width() ?: 0f) - r.width()) / 2f
            if (maintainAspectRatio) {
                dy = dx / initialAspectRatio
            }
        }
        if (dy > 0f && r.height() + 2 * dy > (imageRect?.height()?:0f)) {
            dy = ((imageRect?.height() ?: 0f) - r.height()) / 2f
            if (maintainAspectRatio) {
                dx = dy * initialAspectRatio
            }
        }
        r.inset(-dx, -dy)

        // Don't let the cropping rectangle shrink too fast
        val widthCap = 25f
        if (r.width() < widthCap) {
            r.inset(-(widthCap - r.width()) / 2f, 0f)
        }
        val heightCap = if (maintainAspectRatio) widthCap / initialAspectRatio else widthCap
        if (r.height() < heightCap) {
            r.inset(0f, -(heightCap - r.height()) / 2f)
        }

        val imageRectLeft = imageRect?.left ?: 0f
        val imageRectRight = imageRect?.right ?: 0f
        val imageRectTop = imageRect?.top ?: 0f
        val imageRectBottom = imageRect?.bottom ?: 0f
        // Put the cropping rectangle inside the image rectangle
        if (r.left < imageRectLeft) {
            r.offset(imageRectLeft - r.left, 0f)
        } else if (r.right > imageRectRight) {
            r.offset(-(r.right - imageRectRight), 0f)
        }
        if (r.top < imageRectTop) {
            r.offset(0f, imageRectTop - r.top)
        } else if (r.bottom > imageRectBottom) {
            r.offset(0f, -(r.bottom - imageRectBottom))
        }
        cropRect?.set(r)
        drawRect = computeLayout()
        viewContext?.invalidate()
    }

    // Returns the cropping rectangle in image space with specified scale
    fun getScaledCropRect(scale: Float): Rect {
        val cropRectLeft = cropRect?.left ?: 0f
        val cropRectTop = cropRect?.top ?: 0f
        val cropRectRight = cropRect?.right ?: 0f
        val cropRectBottom = cropRect?.bottom ?: 0f
        return Rect((cropRectLeft * scale).toInt(), (cropRectTop * scale).toInt(), (cropRectRight * scale).toInt(), (cropRectBottom * scale).toInt())
    }

    // Maps the cropping rectangle from image space to screen space
    private fun computeLayout(): Rect {

        val r = RectF(
            cropRect?.left ?: 0f, cropRect?.top ?: 0f,
            cropRect?.right ?: 0f, cropRect?.bottom ?: 0f
        )
        matrix?.mapRect(r)
        return Rect(
            Math.round(r.left), Math.round(r.top),
            Math.round(r.right), Math.round(r.bottom)
        )
    }

    fun invalidate() {
        drawRect = computeLayout()
    }

    fun hasFocus(): Boolean {
        return isFocused
    }

    fun setFocus(isFocused: Boolean) {
        this.isFocused = isFocused
    }

    companion object {
        const val GROW_NONE = 1 shl 0
        const val GROW_LEFT_EDGE = 1 shl 1
        const val GROW_RIGHT_EDGE = 1 shl 2
        const val GROW_TOP_EDGE = 1 shl 3
        const val GROW_BOTTOM_EDGE = 1 shl 4
        const val MOVE = 1 shl 5
        private const val DEFAULT_HIGHLIGHT_COLOR = -0xcc4a1b
        private const val HANDLE_RADIUS_DP = 12f
        private const val OUTLINE_DP = 2f
    }

    init {
        initStyles(viewContext?.context)
    }
}