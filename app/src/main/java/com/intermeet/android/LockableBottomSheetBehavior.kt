package com.intermeet.android

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior

class LockableBottomSheetBehavior<V : View>(context: Context, attrs: AttributeSet) : BottomSheetBehavior<V>(context, attrs) {

    private var _draggable: Boolean = true  // Maintain the draggable state

    override fun isDraggable(): Boolean {
        return _draggable
    }

    override fun setDraggable(value: Boolean) {
        _draggable = value
    }

    override fun onInterceptTouchEvent(parent: CoordinatorLayout, child: V, event: MotionEvent): Boolean {
        return if (_draggable) super.onInterceptTouchEvent(parent, child, event) else false
    }

    override fun onTouchEvent(parent: CoordinatorLayout, child: V, event: MotionEvent): Boolean {
        return if (_draggable) super.onTouchEvent(parent, child, event) else false
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
