(function(){
var
  version = "1.0",
  hasProperty = {}.hasOwnProperty,
  MobpexJsSDK = function(){},
  channels = {
    alipay: 'ALIPAY',
    yeepay: 'YEEPAY',
    unpay: 'UPACP',
    wechat: 'WECHAT'
  },
  payTypes = {
		    wap: 'WAP',
		    app: 'APP'
		  };
MobpexJsSDK.prototype = {
  

  version: version,

  _errorCallback: undefined,
  _successCallback: undefined,
  _validatePrePayJson:function(prePayJson){
	  var prePayJsonObj = {};
	  if(typeof prePayJson == "string"){
	      try{
	    	  prePayJsonObj = JSON.parse(prePayJson);
	      }catch(err){
	        this._errorCallback("fail", "json_fail");
	        return false;
	      }
	    }else{
	    	prePayJsonObj = prePayJson;
	    }
	   
	    if(!hasProperty.call(prePayJsonObj, 'state')){
	      this._errorCallback("fail", "invalid_payment_json:state");
	      return false;
	    }
	    if(prePayJsonObj.state!='SUCCESS'){
	    	 this._errorCallback("获取的支付凭证状态不正确");
		      return false;
	    }
	    
	    if(!hasProperty.call(prePayJsonObj, 'ext')){
		      this._errorCallback("fail", "invalid_payment_json:ext");
		      return false;
		}
	    
	    if(!hasProperty.call(prePayJsonObj.ext, 'payChannel')){
	        this._errorCallback("fail", "invalid_payment_json:payChannel");
	        return false;
	    }
	    if(!hasProperty.call(prePayJsonObj.ext, 'payType')){
	        this._errorCallback("fail", "invalid_payment_json:payType" );
	        return false;
	    }
	    if(!hasProperty.call(prePayJsonObj, 'result')){
	        this._errorCallback("fail",  "invalid_payment_json:result" );
	        return false;
	    }
	  
      return true;
  },
  _validateWechatPayJson:function(prePayJson){
	   
	  var prePayJsonObj = {};
	  if(typeof prePayJson == "string"){
	      try{
	    	  prePayJsonObj = JSON.parse(prePayJson);
	      }catch(err){
	        this._errorCallback("fail",  "json_fail" );
	        return false;
	      }
	    }else{
	    	prePayJsonObj = prePayJson;
	    } 
	  
	    if(!hasProperty.call(prePayJsonObj.result, 'paymentParams')){
	      this._errorCallback("fail",  "invalid_payment_json:paymentParams");
	      return false;
	    }
	    if(!hasProperty.call(prePayJsonObj.result.paymentParams, 'appId')){
		      this._errorCallback("fail",  "invalid_payment_json:paymentParams");
		      return false;
	    }
	    if(!hasProperty.call(prePayJsonObj.result.paymentParams, 'nonceStr')){
		      this._errorCallback("fail",  "invalid_payment_json:paymentParams");
		      return false;
	    }
	    if(!hasProperty.call(prePayJsonObj.result.paymentParams, 'package')){
		      this._errorCallback("fail", "invalid_payment_json:paymentParams");
		      return false;
	    }
	    if(!hasProperty.call(prePayJsonObj.result.paymentParams, 'timeStamp')){
		      this._errorCallback("fail", "invalid_payment_json:paymentParams");
		      return false;
	    }
	    if(!hasProperty.call(prePayJsonObj.result.paymentParams, 'sign')){
		      this._errorCallback("fail",  "invalid_payment_json:paymentParams");
		      return false;
	    } 
	    
	    if(prePayJsonObj.state!='SUCCESS'||prePayJsonObj.result.errCode!='1'){
	    	 this._errorCallback("获取的支付凭证状态不正确");
		      return false;
	    }
	  
      return true;
  },
  letsPay: function(prePay_json,successCallBack,errorCallBack,winRef) {
	  this._errorCallback=errorCallBack;
	  this._successCallback=successCallBack
    var paymentsJson;
    if(typeof prePay_json == "string"){
      try{
        paymentsJson = JSON.parse(prePay_json);
      }catch(err){
        this._errorCallback("fail",  "json_fail");
        return;
      }
    }else{
      paymentsJson = prePay_json;
    }
    
    if(!mobpexJsSdk._validatePrePayJson(paymentsJson)){
  	  return ;
    }
    var state = paymentsJson['state'];
    var channel = paymentsJson.ext['payChannel'];
    var payType = paymentsJson.ext['payType'];
    var result = paymentsJson['result'];
    
    if (channel==channels.alipay  && this._is_weixin()) {  
    	_AP.pay(result.paymentParams.transUrl);
      return;
    }else  if (channel==channels.wechat){
    	    	
    	    	//2016-07-15 微信扫码支付  生成二维码
    	    		var codeUrlTemp = result.paymentParams.codeUrl;
    	    		document.getElementById("codeUrl").value = codeUrlTemp;
    	    	   qr(codeUrlTemp);
    	    	   

    	      return;
    }
    else  {
    	//window.location=result.paymentParams.transUrl;
    	//window.open(result.paymentParams.transUrl,"_blank");
    	//2016-07-04 某些浏览器有安全机制，会阻止在回调函数里使用window.open，为了绕开此机制，先打开一个空窗口，之后再在回调函数里设置它的location

    	winRef.location = result.paymentParams.transUrl;
      return;
    }
    
    
  },
  _wechatPayInWechat: function(prePay_json){
	  
	if(!mobpexJsSdk._validateWechatPayJson(prePay_json)){
		return;
	}
    var self = this;
  
      WeixinJSBridge.invoke(
        'getBrandWCPayRequest',
        {
        	   "appId":prePay_json.result.paymentParams.appId,     //公众号名称，由商户传入     
               "timeStamp":prePay_json.result.paymentParams.timeStamp,         //时间戳，自1970年以来的秒数     
               "nonceStr":prePay_json.result.paymentParams.nonceStr, //随机串     
               "package":prePay_json.result.paymentParams.package,     
               "signType":"MD5",         //微信签名方式：     
               "paySign":prePay_json.result.paymentParams.sign //微信签名 
        },
        function(res){
          if(res.err_msg == 'get_brand_wcpay_request:ok'){
             self._successCallback("success",prePay_json);
          }else if(res.err_msg == 'get_brand_wcpay_request:cancel'){
            //self._errorCallback("cancel");
          }else{
            self._errorCallback("fail", res.err_msg);
          }
        }
      );
    
  },
  _is_weixin:function(){
		var ua = navigator.userAgent.toLowerCase();
		if(ua.match(/MicroMessenger/i)=="micromessenger") {
			return true;
	 	} else {
			return false;
		}
  },
  _errorCallback: function(result, err) {
    if (typeof this._errorCallback == "function") {
      if (typeof err == "undefined") {
        err = this._error();
      }
      this._errorCallback(result, err);
    }
  }
};
 
MobpexJsSDK.prototype.payment = MobpexJsSDK.prototype.createPayment;
window.mobpexJsSdk = new MobpexJsSDK();
window.hasProperty=hasProperty;
// aliases
//window.PINGPP_PAY_SDK = window.PINGPP_WX_PUB = window.pingpp;
})();
