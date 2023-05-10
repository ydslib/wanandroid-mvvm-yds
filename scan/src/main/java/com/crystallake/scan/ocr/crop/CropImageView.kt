package com.crystallake.scan.ocr.crop

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent
import java.util.ArrayList

class CropImageView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) :
    ImageViewTouchBase(context, attrs, defStyle) {
    var highlightViews = ArrayList<HighlightView>()
    var motionHighlightView: HighlightView? = null

    //    var context: Context? = null
    private var lastX = 0f
    private var lastY = 0f
    private var motionEdge = 0
    private var validPointerId = 0

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (bitmapDisplayed.bitmap != null) {
            for (hv in highlightViews) {
                hv.matrix?.set(unrotatedMatrix)
                hv.invalidate()
                if (hv.hasFocus()) {
                    centerBasedOnHighlightView(hv)
                }
            }
        }
    }

    override fun zoomTo(scale: Float, centerX: Float, centerY: Float) {
        super.zoomTo(scale, centerX, centerY)
        for (hv in highlightViews) {
            hv.matrix?.set(unrotatedMatrix)
            hv.invalidate()
        }
    }

    override fun zoomIn() {
        super.zoomIn()
        for (hv in highlightViews) {
            hv.matrix?.set(unrotatedMatrix)
            hv.invalidate()
        }
    }

    override fun zoomOut() {
        super.zoomOut()
        for (hv in highlightViews) {
            hv.matrix?.set(unrotatedMatrix)
            hv.invalidate()
        }
    }

    override fun postTranslate(deltaX: Float, deltaY: Float) {
        super.postTranslate(deltaX, deltaY)
        for (hv in highlightViews) {
            hv.matrix?.postTranslate(deltaX, deltaY)
            hv.invalidate()
        }
    }

    private fun getActivity(): CropImageActivity? {
        var mContext = context
        while (mContext is ContextWrapper){
            if (mContext is CropImageActivity){
                return mContext
            }
            mContext = (context as? ContextWrapper)?.baseContext
        }
        return null
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val cropImageActivity = getActivity()
        if (cropImageActivity == null || cropImageActivity.isSaving) {
            return false
        }
        when (event.action) {
            MotionEvent.ACTION_DOWN -> for (hv in highlightViews) {
                val edge = hv.getHit(event.x, event.y)
                if (edge != HighlightView.GROW_NONE) {
                    motionEdge = edge
                    motionHighlightView = hv
                    lastX = event.x
                    lastY = event.y
                    // Prevent multiple touches from interfering with crop area re-sizing
                    validPointerId = event.getPointerId(event.actionIndex)
                    motionHighlightView?.setMode(if (edge == HighlightView.MOVE) HighlightView.ModifyMode.Move else HighlightView.ModifyMode.Grow)
                    break
                }
            }
            MotionEvent.ACTION_UP -> {
                motionHighlightView?.let {
                    centerBasedOnHighlightView(it)
                    it.setMode(HighlightView.ModifyMode.None)
                }
                motionHighlightView = null
                center()
            }
            MotionEvent.ACTION_MOVE -> {
                if (motionHighlightView != null && event.getPointerId(event.actionIndex) == validPointerId) {
                    motionHighlightView!!.handleMotion(
                        motionEdge, event.x
                                - lastX, event.y - lastY
                    )
                    lastX = event.x
                    lastY = event.y
                }

                // If we're not zoomed then there's no point in even allowing the user to move the image around.
                // This call to center puts it back to the normalized location.
                if (scale == 1f) {
                    center()
                }
            }
        }
        return true
    }

    // Pan the displayed image to make sure the cropping rectangle is visible.
    private fun ensureVisible(hv: HighlightView) {
        val r = hv.drawRect
        val panDeltaX1 = Math.max(0, left - (r?.left ?: 0))
        val panDeltaX2 = Math.min(0, right - (r?.right ?: 0))
        val panDeltaY1 = Math.max(0, top - (r?.top ?: 0))
        val panDeltaY2 = Math.min(0, bottom - (r?.bottom ?: 0))
        val panDeltaX = if (panDeltaX1 != 0) panDeltaX1 else panDeltaX2
        val panDeltaY = if (panDeltaY1 != 0) panDeltaY1 else panDeltaY2
        if (panDeltaX != 0 || panDeltaY != 0) {
            panBy(panDeltaX.toFloat(), panDeltaY.toFloat())
        }
    }

    // If the cropping rectangle's size changed significantly, change the
    // view's center and scale according to the cropping rectangle.
    private fun centerBasedOnHighlightView(hv: HighlightView) {
        val drawRect = hv.drawRect
        val width = drawRect?.width()?.toFloat() ?: 1f
        val height = drawRect?.height()?.toFloat() ?: 1f
        val thisWidth = getWidth().toFloat()
        val thisHeight = getHeight().toFloat()
        val z1 = thisWidth / width * .6f
        val z2 = thisHeight / height * .6f
        var zoom = Math.min(z1, z2)
        zoom = zoom * this.scale
        zoom = Math.max(1f, zoom)
        if (Math.abs(zoom - scale) / zoom > .1) {
            val coordinates = floatArrayOf(hv.cropRect?.centerX() ?: 0f, hv.cropRect?.centerY() ?: 0f)
            unrotatedMatrix.mapPoints(coordinates)
            zoomTo(zoom, coordinates[0], coordinates[1], 300f)
        }
        ensureVisible(hv)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        for (highlightView in highlightViews) {
            highlightView.draw(canvas)
        }
    }

    fun add(hv: HighlightView) {
        highlightViews.add(hv)
        invalidate()
    }
}