// JavaScript Document
  

/*==============================
文件名称：mobpex微信公众号DEMO JS
创建时间：2016-06-16
创 建 者：lunjiao.peng
版本：V1.0
功能描述：mobpex微信公众号DEMO JS
==============================*/

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
// 判断是否是微信浏览器
//-------------------------------------------------------------------------------------------------------------------
function isWeiXin(){ 
  var ua = window.navigator.userAgent.toLowerCase(); 
  if(ua.match(/micromessenger/i) == 'micromessenger'){ 
    return true; 
  }else{ 
    return false; 
  } 
} 


