// JavaScript Document
  

/*==============================
文件名称：MObpex收银台JS
创建时间：2016-06-28
创 建 者：lunjiao.peng
版本：V1.0
功能描述：厦航收银台JS
==============================*/
window.onload=function(){
	document.getElementById("codeUrl").value = "";
	
  var closeBtn = document.getElementById("closeBtn"),
      commodityInfor = document.getElementById("commodityInfor"),
      paylist = document.querySelector(".paylist"),
      js_pay = document.getElementById("js_pay"),
      js_weixingpay = document.getElementById("js_weixingpay"),
      js_weixingcon = document.getElementById("js_weixingcon"),
      js_otherpay = document.getElementById("js_otherpay"),
      openHtml = '展开详情 <i class="iconfont">&#xe602;</i>',
      closeHtml = '收起详情 <i class="iconfont">&#xe601;</i>';
  EventUtil.addHandler(closeBtn,"click",function(event){//展开详情
    if(commodityInfor.style.display == "none" || commodityInfor.style.display == ""){
      commodityInfor.style.display="block";
      closeBtn.innerHTML=closeHtml;
    }else{
      commodityInfor.style.display="none";
      closeBtn.innerHTML=openHtml;
    };
  });
  
  
  var initParam = {//初始化页面参数
			"orderInfo" : {// 订单信息
				"orderNo" : 'test20160616180311',
				"submitPayUrl" : "../demo/submitOrderForPc"//提交订单url        
			},
			"merchantUrl" : "https://www.mobpex.com"//商户url
		};
 
    	  loadOrder(initParam, submitPay);

  EventUtil.addHandler(js_otherpay,"click",function(event){//选择其它支付方式
    js_weixingcon.style.display = "none";
    js_pay.style.display = "block";
  });

 
  

};
//-------------------------------------------------------------------------------------------------------------------
// 对话框
//-------------------------------------------------------------------------------------------------------------------

function dialog(opr) {
  /*  var opr={
  *   title:'关闭角色',
  *   meg:'确定要关闭这个角色吗？',//提示信息，默认为空，可省
  *   yesBtn:"是",//是按钮名称，默认为是，可省
  *   noBtn:"否",//否按钮名称，默认为否，可省
  *   ok:function(){}
  *   opr.Title 标题名
  * opr.Content:显示内容
  * opr.Class 弹出框类型 启用："success",关闭："danger"
  *   opr.ok() 单击是执行的函数
  *   opr.nook() 单击否执行的函数
  * dialog(opr);调用函数
  */
  var title = opr.title,yesBtn,noBtn,meg;
  opr.yesBtn == undefined ? yesBtn ="是" : yesBtn = opr.yesBtn;
  opr.noBtn == undefined ? noBtn ="否" : noBtn = opr.noBtn;
  opr.meg == undefined ? meg ="" : meg = '<p>'+opr.meg+'</p>';
  var dialogHtml = '<div class="dialogue"><article><header class="dialogue-header">'
                + title
                + '<button type="button" class="btn-close" id="btnClose">×</button></header>'
                + '<div class="dialogue-body">'
                + meg
                + '</div></article><footer class="dialogue-footer"><button type="button" id="YDialog" class="btn btn-info YDialog" data-dismiss="alert" aria-hidden="true">'
                + yesBtn
                + '</button>&nbsp;&nbsp;&nbsp;&nbsp;<button type="button" id="NDialog" class="btn btn-default NDialog" data-dismiss="alert" aria-hidden="flase">'
                + noBtn
                + '</button></footer></div>'
                +'<div class="modal-backdrop fade in"></div>';
  var lastDiv = document.createElement("div");
  document.body.appendChild(lastDiv);
  lastDiv.innerHTML = dialogHtml;
  // document.body.appendHTML(dialogHtml);
  var dialogue = document.querySelector(".dialogue"),
      modalBackdrop = document.querySelector(".modal-backdrop");
  EventUtil.addHandler(dialogue,"click",function(event){//否
    event=EventUtil.getEvent(event);
    var target = EventUtil.getTarget(event);
    switch(target.getAttribute("id")){
      case "YDialog"://是
        
    	  var queryPaymentUrl= "../demo/queryPaymentOrderForPc";
  		var postBody = encodeURI("payType=&payChannel=&orderNo=");
  		var xhr = new XMLHttpRequest();
  		xhr.open("POST", queryPaymentUrl, true);
  		xhr.setRequestHeader("Content-type",
  				"application/x-www-form-urlencoded;charset=utf-8");
  		xhr.send(postBody);

  		xhr.onreadystatechange = function() {
  			if (xhr.readyState == 4 && xhr.status == 200) {
  				var queryJsonObject = JSON.parse(xhr.responseText);
  				if (queryJsonObject.state != 'SUCCESS') {
  					
  					if (hasProperty.call(queryJsonObject, "error")
  							&& hasProperty.call(queryJsonObject.error, "code")) {
  						endWait();// 关闭等待UI的显示
  						errorCallBack("您并未完成支付!错误码" + queryJsonObject.error.code);
  					} else {
  						endWait();// 关闭等待UI的显示
  						errorCallBack("您并未完成支付!");
  					}
  					return;
  				} else {
  					
  					//mobpexJsSdk.letsPay(xhr.responseText, successCallBack2,errorCallBack);
  					var paymentsJson;
  					if(typeof xhr.responseText == "string"){
  					      try{
  					           paymentsJson = JSON.parse(xhr.responseText);
  					      }catch(err){
  					        this._errorCallback("fail",  "json_fail");
  					        return;
  					      }
  					}else{
  					         paymentsJson = xhr.responseText;
  					}
  					
  					var result = paymentsJson['result'];
  				    
  				    if (result.payStatus!="PAID") {  
  				    	
  				    	alert("您尚未完成支付，请选择一个支付方式完成支付");
  				    }
  				    else{
  				    	successCallBack2();
  				    }
  					
  					endWait();// 关闭等待UI的显示
  				}
  			} else if (xhr.readyState == 4) {
  				endWait();// 关闭等待UI的显示
  				errorCallBack("获取支付凭证失败!");
  				return;
  			}
  			
  			
  			dialogue.parentNode.removeChild(dialogue);
  	        modalBackdrop.parentNode.removeChild(modalBackdrop);
  	        if(opr.ok != undefined ){
  	          opr.ok();
  	        }
  	         
  		};
  		break; 
    	  
      case "NDialog"://否
      case "btnClose"://关闭
        dialogue.parentNode.removeChild(dialogue);
        modalBackdrop.parentNode.removeChild(modalBackdrop);
        if(opr.nook != undefined ){ 
          opr.nook();
        }
        break;   
    }   
  }); 
};


//-------------------------------------------------------------------------------------------------------------------
// 跨浏览器的事件、事件对象处理程序
//-------------------------------------------------------------------------------------------------------------------
var EventUtil={// 
  addHandler:function(element,type,handler){               
    if(element.addEventListener){ //IE9、Firefox,Safari,Chrome和Opera支持DOM2级事件处理程序
      element.addEventListener(type,handler,false);
    }else if(element.attachEvent){//IE,Opera
      element.attachEvent("on"+type,handler);
    }else{//DOM0级
      element["on"+type]=handler;
    }
  },
  removeHandler:function(element,type,handler){
    if(element.removeEventListenter){
      element.removeEventListenter(type,handler,false);
    }else if(element.detachEvent){
      element.detachEvent("on"+type,handler);
    }else{
      element["on"+type]=null;
    }
  },
  getEvent:function(event){
    return event ? event:window.event;
  },
  getTarget:function(event){
    return event.target || event.srcElement;
  },
  preventDefault:function(event){
    if(event.preventDefault){
      event.preventDefault();
    }else{
      event.returnValue=false;
    }
  },
  stopPropagation:function(event){
    if(event.stopPropagation){
      event.stopPropagation();
    }else{
      event.cancelBubble = true;
    }
  }
};


//-------------------------------------------------------------------------------------------------------------------
// appendHTML方法
//-------------------------------------------------------------------------------------------------------------------
HTMLElement.prototype.appendHTML = function(htmlStr){
  var fragment = document.createDocumentFragment();
  var htmlNode = document.createElement("div");
  htmlNode.innerHTML = htmlStr;
  // for (var i=0; i< htmlNode ; i++){}

}