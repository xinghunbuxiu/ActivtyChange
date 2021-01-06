Android使用AccessibilityService监听用户操作
原文链接<https://blog.csdn.net/DeMonliuhui/article/details/81634566>


UI(this@BaseActivity) {
        swipeBack {
            this?.setEdgeTrackingEnabled(EDGE_LEFT)
        }
        slideMenu(TestSlideView(activity!!)) {
            isFollowing = true
        }
        title {
            title = "nihao"
        }
        body<LinearLayout>(layoutId, true) {

        }
    }