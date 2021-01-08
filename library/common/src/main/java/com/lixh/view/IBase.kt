package com.lixh.view

import androidx.annotation.IntDef


interface OnSlideListener {
    fun slideState(@State state: Int)
}

/**
 * Callback interface invoked when a tab's selection state changes.
 */
interface OnTabSelectedListener {

    /**
     * Called when a tab enters the selected state.
     *
     * @param position The position of the tab that was selected
     */
    fun onTabSelected(position: Int): Boolean

    /**
     * Called when a tab exits the selected state.
     *
     * @param position The position of the tab that was unselected
     */
    fun onTabUnselected(position: Int): Boolean

    /**
     * Called when a tab that is already selected is chosen again by the user. Some applications
     * may use this action to return to the top level of a category.
     *
     * @param position The position of the tab that was reselected.
     */
    fun onTabReselected(position: Int): Boolean
}

/**
 *  分页管理
 */
interface OnPageListener {
    fun load(page: Int)
}


@IntDef(WindowType.ACTIVITY, WindowType.FRAGMENT)
@kotlin.annotation.Retention(AnnotationRetention.SOURCE)
annotation class WindowType {
    companion object {
        const val FRAGMENT = 1
        const val ACTIVITY = 2
    }
}

@IntDef(State.OPEN, State.CLOSE)
@kotlin.annotation.Retention(AnnotationRetention.SOURCE)
annotation class State {
    companion object {
        const val OPEN = 0
        const val CLOSE = 1
    }
}

@IntDef(Slide.NONE, Slide.LEFT, Slide.RIGHT)
@kotlin.annotation.Retention(AnnotationRetention.SOURCE)
annotation class Slide {
    companion object {
        const val NONE = -1
        const val LEFT = 1 shl 0
        const val RIGHT = 1 shl 1
    }
}

