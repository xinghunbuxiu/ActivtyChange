function EventBuilder() {}

EventBuilder.prototype = {
	constructor: EventBuilder,
	/**
	 * 模拟点击事件
	 *
	 * @param nodeInfo nodeInfo
	 */
	performViewClick: function(nodeInfo) {
		AndroidEventEngine.performViewClick(nodeInfo);
	},

	/**
	 * 模拟点击事件by 文本
	 */
	clickTextViewByText: function(text) {
		AndroidEventEngine.clickTextViewByText(text);
	}

	/**
	 * 模拟点击事件by id
	 */
	clickTextViewByID: function(id) {
		AndroidEventEngine.clickTextViewByID(id);
	}

	/**
	 * 模拟返回操作
	 */
	performBackClick: function() {
		AndroidEventEngine.performBackClick();

	},

	/**
	 * 模拟下滑操作
	 */
	performScrollBackward: function() {
		AndroidEventEngine.performScrollBackward();
	},

	/**
	 * 模拟上滑操作
	 */
	performScrollForward: function() {
		AndroidEventEngine.performScrollForward();
	},

	/**
	 * 查找对应文本的View
	 *
	 * @param text text
	 * @return AccessibilityNodeInfo
	 */
	findViewByText: function(text) {
		var nodeInfo = AndroidEventEngine.findViewByText(text);
		return nodeInfo;
	},

	/**
	 * 查找对应文本的View
	 *
	 * @param text      text
	 * @param clickable 该View是否可以点击
	 * @return AccessibilityNodeInfo
	 */
	findViewByText: function(text, clickable) {
		var nodeInfo = AndroidEventEngine.findViewByText(text, clickable);
		return nodeInfo;
	},

	/**
	 * 查找对应ID的View
	 *
	 * @param id id
	 * @return AccessibilityNodeInfo
	 */
	findViewByID: function(id) {
		var nodeInfo = AndroidEventEngine.findViewByID(id);
		return nodeInfo;
	},
	/**
	 * 输入文本
	 * @param  {[type]} nodeInfo [description]
	 * @param  {[type]} text     [description]
	 * @return {[type]}          [description]
	 */
	inputText: function(nodeInfo, text) {
		AndroidEventEngine.inputText(nodeInfo， text);
	}
}