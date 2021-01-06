function XPosedBuilder() {}

XPosedBuilder.prototype = {
	constructor: XPosedBuilder,
	/*
	 * 查找类
	 *
	 * @param nodeInfo nodeInfo
	 */
	findClass: function (name) {
		XPosedEngine.callMethod("");
	},


	/*
	日志输出
	 */
	log: function (msg) {
		XPosedEngine.printLog(msg);
	}
}
