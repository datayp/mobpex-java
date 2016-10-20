function loadOrder(param, callback) {
	//initPage(param);
	imgObj = document.getElementsByTagName("img");
	for (var i = 0; i < imgObj.length; i++) {
		imgObj[i].onclick = function() {
			
			if(this.getAttribute("id")!="noHref"){
			  //记录用户选择的支付渠道和支付类型
			  param.payChannel = this.getAttribute("data-value");
			  param.payType = this.getAttribute("payType");
			  callback(param);
			
			  if(param.payType == "NATIVE"){
			     js_weixingcon.style.display = "block";
	             js_pay.style.display = "none";
			  }
		   }

		};
	}
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
	
	//2016-07-15 隐藏域的codeUrl有值时，就不必调用接口获取生成二维码
	 if(document.getElementById("codeUrl").value!="" && payType=="NATIVE"){
		qr(document.getElementById("codeUrl").value);
		return;
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
	
	//2016-07-04 某些浏览器有安全机制，会阻止在回调函数里使用window.open，为了绕开此机制，先打开一个空窗口，之后再在回调函数里设置它的location
	var winRef;
	if(payType!="NATIVE"){
	     winRef = window.open("", "_blank");
	}

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
			
				  mobpexJsSdk.letsPay(xhr.responseText, successCallBack2,errorCallBack,winRef);
				  //在新窗口打开某个支付方式的页面后，在本页面弹出弹出框，带有支付成功按钮和选择其他支付方式按钮
				  
				  if(payType!="NATIVE"){
				     var opr={
				          title:'支付确认',
				          yesBtn:"支付成功",
				          meg:"请在新打开的页面完成支付，<b>支付完成前，请不要关闭此窗口</b>。",
				          noBtn:"重新选择支付方式",
				          ok:function(){
				          }
				      };
			        dialog(opr);
				     endWait();// 关闭等待UI的显示
				  }
			 
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
function successCallBack2(msg, ext) {
	//alert(msg);
	window.location.href = "../cashier/cashier-payok.html";
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

//微信扫码支付对应的生成二维码代码
function toUtf8(str) {
	var out, i, len, c;
	out = "";
	len = str.length;
	for (i = 0; i < len; i++) {
		c = str.charCodeAt(i);
		if ((c >= 0x0001) && (c <= 0x007F)) {
			out += str.charAt(i);
		} else if (c > 0x07FF) {
			out += String.fromCharCode(0xE0 | ((c >> 12) & 0x0F));
			out += String.fromCharCode(0x80 | ((c >> 6) & 0x3F));
			out += String.fromCharCode(0x80 | ((c >> 0) & 0x3F));
		} else {
			out += String.fromCharCode(0xC0 | ((c >> 6) & 0x1F));
			out += String.fromCharCode(0x80 | ((c >> 0) & 0x3F));
		}
	}
	return out;
}

//微信扫码支付对应的生成二维码代码
function qr(codeUrl) {
	$("#qrPic").empty();
	var str = toUtf8(codeUrl);

	$("#qrPic").qrcode({
		render : "canvas",
		width : 170,
		height : 170,
		text : str
	});
	document.getElementById("codeUrl").value = codeUrl;
}