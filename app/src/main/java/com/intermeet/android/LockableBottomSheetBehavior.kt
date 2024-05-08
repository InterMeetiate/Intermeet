package com.intermeet.android

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior

class LockableBottomSheetBehavior<V : View>(context: Context, attrs: AttributeSet) : BottomSheetBehavior<V>(context, attrs) {

    private var _draggable: Boolean = true  // Maintain the draggable state
    private var handleBar: View? = null  // Reference to the handle bar view
    private var handleBarTouchArea: Int = 0  // Area around the handle bar to allow dragging
    private var lastInterceptedEvent: MotionEvent? = null

    fun setHandleBar(view: View, touchArea: Int = 50) {
        handleBar = view
        handleBarTouchArea = touchArea
    }

    override fun isDraggable(): Boolean {
        return _draggable
    }

    override fun setDraggable(value: Boolean) {
        _draggable = value
    }

    override fun onInterceptTouchEvent(parent: CoordinatorLayout, child: V, event: MotionEvent): Boolean {
        val isHandleBarTouched = isTouchEventOnHandleBar(child, event)
        val shouldIntercept = isHandleBarTouched || _draggable

        if (shouldIntercept) {
            lastInterceptedEvent = MotionEvent.obtain(event)  // Keep track of the last intercepted event
            return super.onInterceptTouchEvent(parent, child, event)
        }
        return false
    }

    override fun onTouchEvent(parent: CoordinatorLayout, child: V, event: MotionEvent): Boolean {
        val isHandleBarTouched = isTouchEventOnHandleBar(child, event)
        val shouldHandle = isHandleBarTouched || _draggable

        // Reset last intercepted event on ACTION_UP or ACTION_CANCEL
        if (event.action == MotionEvent.ACTION_UP || event.action == MotionEvent.ACTION_CANCEL) {
            lastInterceptedEvent = null
        }

        if (shouldHandle) {
            // Ensure that ACTION_DOWN is always received first
            if (lastInterceptedEvent == null && event.action == MotionEvent.ACTION_MOVE) {
                val downEvent = MotionEvent.obtain(
                    event.downTime, event.eventTime, MotionEvent.ACTION_DOWN, event.x, event.y, event.metaState
                )
                super.onTouchEvent(parent, child, downEvent)
                downEvent.recycle()
            }
            return super.onTouchEvent(parent, child, event)
        }
        return false
    }

    private fun isTouchEventOnHandleBar(child: V, event: MotionEvent): Boolean {
        handleBar?.let {
            val handleBarCoords = IntArray(2)
            it.getLocationOnScreen(handleBarCoords)
            val xHandle = handleBarCoords[0]
            val yHandle = handleBarCoords[1]

            // Adjust touch area
            val left = xHandle - handleBarTouchArea
            val right = xHandle + it.width + handleBarTouchArea
            val top = yHandle - handleBarTouchArea
            val bottom = yHandle + it.height + handleBarTouchArea

            // Check if touch event is within the handle bar region
            val eventX = event.rawX.toInt()
            val eventY = event.rawY.toInt()
            return eventX in left..right && eventY in top..bottom
        }
        return false
    }

    companion object {
        @Suppress("UNCHECKED_CAST")
        fun <V : View> from(view: V): LockableBottomSheetBehavior<V> {
            val params = view.layoutParams as? CoordinatorLayout.LayoutParams
                ?: throw IllegalArgumentException("The view is not a child of CoordinatorLayout")
            if (params.behavior !is LockableBottomSheetBehavior<*>) {
                throw IllegalArgumentException("The view's behavior is not LockableBottomSheetBehavior")
            }
            return params.behavior as LockableBottomSheetBehavior<V>
        }
    }
}
