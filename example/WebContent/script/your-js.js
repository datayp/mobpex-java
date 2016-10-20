window.onload=function(){
  var paynow = document.getElementById("paynow"),
      payChannel = document.querySelector(".pay-channel"),
      modelback = document.querySelector(".modelback");
      //wechat = payChannel.querySelector(".wechat");
  loadOrder(initParam, submitPay);

  EventUtil.addHandler(paynow,"click",function(event){//选择其它方式支付
    event=EventUtil.getEvent(event);//获取事件对象
    var target = EventUtil.getTarget(event);//获取事件对象目标
    modelback.style.display="block";
    payChannel.style.display="block";
   
  });
  
  EventUtil.addHandler(modelback,"click",function(event){//点击黑色背景
    event=EventUtil.getEvent(event);//获取事件对象
    var target = EventUtil.getTarget(event);//获取事件对象目标
    payChannel.style.display="none";
    modelback.style.display="none"; 
  }); 
  
};
function loadOrder(param, callback) {
	initPage(param);
	buttonObj = document.getElementsByTagName("button");
	for (var i = 0; i < buttonObj.length; i++) {
		if(buttonObj[i].getAttribute("payType")!=undefined){
			
			
			buttonObj[i].onclick = function() {
			//记录用户选择的支付渠道和支付类型
				param.payChannel = this.getAttribute("data-value");
				param.payType = this.getAttribute("payType");
				callback(param);

			};
	   }
	}
}
function initPage(param) {
	channelHtml = ""; 
	for (var i = 0; i < param.channel.length; i++) {
		channelHtml = channelHtml
					
					+ '<button class="btn btn-default" type="button" data-value="'
					+param.channel[i].payChannel
					+'" payType="'+param.channel[i].payType
					+'" >'
					+ '<img  src="'+param.channel[i].logo+'" class="yinliang">'
					+param.channel[i].channelName+'</button>';
		//<button class="btn btn-default" type="button" data-value="ALIPAY" payType="WAP"><i class="iconfont alipay">&#xe600;</i>支付宝&nbsp;&nbsp;&nbsp;&nbsp;</button>
		
	}
	document.getElementById("channels").innerHTML = channelHtml;
	document.getElementById("productName").innerHTML = param.orderInfo.productName;
	
	document.getElementById("amount").innerHTML = "<span>￥</span>"+param.orderInfo.amount ;
	//document.getElementById("payee").innerHTML = param.orderInfo.payee;

}
function submitPay(orderJson) {
	showWait();// 显示等待UI
	var orderJsonObject = {};
	if (typeof orderJson == "string") {
		try {
			orderJsonObject = JSON.parse(orderJson);// 解悉提交支付的JSON参数串
		} catch (err) {
			endWait();// 关闭等待UI的显示
			errorCallBack("发起支付失败!", "initParam为非法JSON串");
			return;
		}
	} else {
		orderJsonObject = orderJson;
	}

	if (!validateOrderJson(orderJson)) {
		endWait();// 关闭等待UI的显示
		// errorCallBack("发起支付失败!","initParam为非法JSON串");
		return;
	}
	var payChannel = orderJsonObject.payChannel;
	var payType = orderJsonObject.payType;
	if(mobpexJsSdk._is_weixin()){
		if (payChannel == 'ECITIC_WECHAT' 
	    		&& payType == 'WAP' ) {  
			alert("微信wap支付只能在外部浏览器环境下进行支付");
			endWait();
			return;
		}
	}
	if(mobpexJsSdk._is_alipay()){
		if (payChannel == 'ECITIC_WECHAT' 
	    		|| payChannel == 'WAP') {  
			alert("微信支付不能在支付宝服务号环境下进行支付");
			endWait();
			return;
		}
	}
	if ( !mobpexJsSdk._is_alipay()) {  
		if(payChannel == 'ECITIC_ALIPAY' 
	    		&& payType == 'PUB' ){
			alert("支付宝服务号支付需要在支付宝app服务号环境下进行支付。");
			endWait();
			return;
		}
	}
	if ( !mobpexJsSdk._is_weixin()) {  
		if (payChannel == 'ECITIC_WECHAT' 
	    		&& payType == 'PUB') {  
			alert("微信公众号支付只能在微信公众号环境下进行支付");
			endWait();
			return;
		}
		if (payChannel == 'WECHAT' 
	    		&& payType == 'PUB') {  
			alert("微信公众号支付只能在微信公众号环境下进行支付");
			endWait();
			return;
		}
	}
	var orderInfo = orderJsonObject.orderInfo;
	var orderNo = orderInfo.orderNo;
	var submitPayUrl = orderInfo.submitPayUrl;
	var postBody = encodeURI("payType="+payType+"&payChannel=" + payChannel
			+ "&orderNo=" + orderNo);
	var xhr = new XMLHttpRequest();
	xhr.open("POST", submitPayUrl, true);
	xhr.setRequestHeader("Content-type",
			"application/x-www-form-urlencoded;charset=utf-8");
	xhr.send(postBody);

	xhr.onreadystatechange = function() {
		if (xhr.readyState == 4 && xhr.status == 200) {
			var prePayJsonObject = JSON.parse(xhr.responseText);
			if (prePayJsonObject.state != 'SUCCESS') {
				if (hasProperty.call(prePayJsonObject, "error")
						&& hasProperty.call(prePayJsonObject.error, "code")) {
					endWait();// 关闭等待UI的显示
					errorCallBack("获取支付凭证失败!错误码" + prePayJsonObject.error.code);
				} else {
					endWait();// 关闭等待UI的显示
					errorCallBack("获取支付凭证失败!");
				}
				return;
			} else {
				
				mobpexJsSdk.letsPay(xhr.responseText, successCallBack,errorCallBack);
				endWait();// 关闭等待UI的显示
			}
		} else if (xhr.readyState == 4) {
			endWait();// 关闭等待UI的显示
			errorCallBack("获取支付凭证失败!");
			return;
		}
	};
}
function validateOrderJson(orderJson) {
	var orderJsonObject = {};
	if (typeof orderJson == "string") {
		try {
			orderJsonObject = JSON.parse(orderJson);
		} catch (err) {
			errorCallBack("fail", this._error("json_string_not_valid"));
			return false;
		}
	} else {
		orderJsonObject = orderJson;
	}
	if (!hasProperty.call(orderJsonObject, 'orderInfo')) {
		errorCallBack("invalid_order_json", "orderInfo");
		return false;
	}
	if (!hasProperty.call(orderJsonObject, 'payChannel')) {
		errorCallBack("invalid_order_json", "payChannel");
		return false;
	}
	if (!hasProperty.call(orderJsonObject, 'merchantUrl')) {
		errorCallBack("invalid_order_json", "merchantUrl");
		return false;
	}
	if (!hasProperty.call(orderJsonObject.orderInfo, 'orderNo')) {
		errorCallBack("invalid_order_json", "orderInfo.orderNo");
		return false;
	}
	if (!hasProperty.call(orderJsonObject.orderInfo, 'submitPayUrl')) {
		errorCallBack("invalid_order_json", "orderInfo.submitPayUrl");
		return false;
	}

	return true;
}

/**
 * 商户可以在函数内此实现对支付正常完成后的业务逻辑，例如跳转到成功页面，为安全起见，商户的订单的已支付状态应该要以后台回调通知为准
 * @param msg
 * @param ext
 */
function successCallBack(msg, ext) {
	//alert(msg);
	window.location.href = "../page/payOk.html";
}

/**
 * 商户在此实现对支付失败后的业务逻辑，例如跳转到自己设计的出错页面，或者显示一个友好的错误信息，这里演示只是简单的弹出错误信息
 * @param msg
 * @param ext
 */
function errorCallBack(msg, ext) {

	alert(msg);
}

/**
 * 商户可重新实现此函数 ，此函数主要是为防止重复支付，在用户点击某支付渠道后禁用掉支付按钮
 * 商户可以再实现更友好的方式，例如显示一个运动的齿轮或滚动条
 */
function showWait() {
	buttonObj = document.getElementsByTagName("button");
	for (var i = 0; i < buttonObj.length; i++) {
		buttonObj[i].disabled = true;
	}
}

/**
 * 商户在此定义关闭showWait显示的等待UI
 */
function endWait() {
	buttonObj = document.getElementsByTagName("button");
	for (var i = 0; i < buttonObj.length; i++) {
		buttonObj[i].disabled = false;
	}
}