package com.lixh.view.refresh

interface ImplPull {

    val height: Int
    var refreshView: SpringView

    enum class StateType {
        NONE,
        PULL,
        RELEASE,
        LOADING,
        LOAD_CLOSE,
        ONCLICK
    }

    enum class ScrollState {
        TOP, NONE, BOTTOM
    }

    /**
     * @param maxY 滑动的 最大 y
     * @param y
     */
    fun scroll(maxY: Int, y: Int)

    fun onScrollChange(state: StateType)
}