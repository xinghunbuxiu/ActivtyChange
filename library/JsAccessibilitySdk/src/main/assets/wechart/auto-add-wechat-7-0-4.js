var name = getNames();
var sendText = getSendText();
var request = new EventBuilder();
var tempIndex = -1;
var lastPage;
var isEnd = false;
//睡眠
function sleep(milliSeconds) {
	milliSeconds = 1000;
	var startTime = new Date().getTime();
	while (new Date().getTime() < startTime + milliSeconds);
};
//页面切换
function onPageChanged(event) {
	var name = event.name;
	request.log('lastPage' + '----' + lastPage);
	request.log('name' + '----' + name + '---' + isEnd);
	request.log('lastPage' + '----' + lastPage != name);
	if (name == 'com.tencent.mm.ui.base.p') {
		return;
	}
	if (lastPage != null && lastPage != name) {
		request.performBackClick();
		return;
	}
	lastPage = null;
	if (name == "com.tencent.mm.ui.LauncherUI") {
		request.log('onLauncherUi');
		if (isEnd) {
			return;
		}
		onLauncherUi();
	} else if (name == "com.tencent.mm.plugin.fts.ui.FTSMainUI") {
		request.log('FTSMainUI');
		if (isEnd) {
			return;
		}
		FTSMainUI();
	} else if (name == "com.tencent.mm.ui.widget.a.c") {
		request.log('addError');
		addError(event);
	} else if (name == "com.tencent.mm.plugin.profile.ui.ContactInfoUI") {
		request.log('onContactInfoUI');
		onContactInfoUI();
	} else if (name == "com.tencent.mm.plugin.profile.ui.SayHiWithSnsPermissionUI") {
		request.log('SayHiWithSnsPermissionUI');
		SayHiWithSnsPermissionUI();
	} else {
		var node = request.findViewByID("com.tencent.mm:id/l3");
		if (node != null && request.equals(node, "搜索")) {
			if (isEnd) {
				return;
			}
			FTSMainUI();
		}
	}
}

function addError(event) {
	var texts = event.text;
	if (texts != null && texts.length > 0) {
		request.log(texts[0]);
		request.performBackClick();
	}
}

//首页 点击搜索
function onLauncherUi() {
	sleep(500);
	request.log('点击搜索');
	tempIndex = -1;
	var node = request.findViewByNodeIndex("com.tencent.mm:id/k1", 1);
	request.performViewClick(node)
}
//搜索页面
function FTSMainUI() {
	request.log(tempIndex + "---" + names.length);
	tempIndex += 1;
	if (names.length > tempIndex) {
		add_friend(names[tempIndex]);
	} else {
		isEnd = true;
		tempIndex = -1;
		request.log('全部处理完毕');
	}
}
//添加朋友
function add_friend(name) {
	request.log('开始添加' + name);
	var node = request.findViewByID("com.tencent.mm:id/l3");
	if (node == null) {
		request.log('null---------------------------------------->' + name);
		return
	}
	request.log('开输入---------------------------------------->' + name);
	sleep(500);
	request.inputText(node, name);
	sleep(500);
	request.log('点击添加---------------------------------------->');
	request.clickTextViewByID("com.tencent.mm:id/bym")
}
//添加到通讯录
function onContactInfoUI() {
	sleep(500);
	var node = request.findViewByText('发消息');
	if (node != null) {
		request.log('此为消息页面---------------------------------------->');
		request.performBackClick();
	} else {
		request.log('添加到通讯录---------------------------------------->');
		request.clickTextViewByID("com.tencent.mm:id/ct");
	}

}
// 发送申请
function SayHiWithSnsPermissionUI() {
	request.log('发送申请文本填充' + name);
	var node = request.findViewByID("com.tencent.mm:id/e49");
	request.log('填充发送内容---------------------------------------->' + sendText);
	request.inputText(node, sendText);
	sleep(500);
	request.log('点击发送---------------------------------------->');
	lastPage = "com.tencent.mm.plugin.fts.ui.FTSMainUI";
	request.log('停留---------------------------------------->' + lastPage);
	request.clickTextViewByID("com.tencent.mm:id/ki")；

}
