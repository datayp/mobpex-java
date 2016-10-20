package com.yeepay.g3.app.mashup.demo.controller;

import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mobpex.srvsdk.utils.StringUtils;
import com.mobpex.srvsdk.utils.WebUtils;
import com.yeepay.g3.app.mashup.demo.constants.Configuration;

@Controller
@RequestMapping(value = "/wechat")
public class WechatPaymentAction {

 	 	/**
	 * 网页授权获取用户信息
	 * 微信公众号支付需要参照本类 先获取openid
	 * @param request
	 * @param response
	 */
	@RequestMapping("/topay")
	public String toPay(HttpServletRequest request, HttpServletResponse response ){
		try {
			//Mozilla/5.0 (iPhone; CPU iPhone OS 9_3_5 like Mac OS X) AppleWebKit/601.1.46 (KHTML, like Gecko) Mobile/13G36 MicroMessenger/6.3.25 NetType/WIFI Language/zh_CN
			// = Mozilla/5.0 (iPhone; CPU iPhone OS 9_3_5 like Mac OS X) AppleWebKit/601.1.46 (KHTML, like Gecko) Mobile/13G36 ChannelId(7) Nebula PSDType(1) AlipayDefined(nt:WIFI,ws:375|603|3.0) AliApp(AP/9.6.0.041106) AlipayClient/9.6.0.041106 Language/zh-Hans
			String userAgent = request.getHeader("user-agent");
			if(userAgent!=null && userAgent.indexOf("MicroMessenger")>-1){
				//2016-06-24 判断session里有没有openId，如果有直接跳转到页面
				if( null==request.getSession(true).getAttribute("openId")){
				   response.sendRedirect(request.getContextPath()+"/page/wapPay.html");
				   return null;
				}
				String appId = "132xx13213";//此处请使用自己的微信appid
				String backUrl = getBasePath(request) + Configuration.BACK_URI ;
				backUrl = URLEncoder.encode(backUrl, "UTF-8");
				//scope 参数视各自需求而定，这里用scope=snsapi_base 不弹出授权页面直接授权目的只获取统一支付接口的openid
				String url = Configuration.WX_OAUTH_URL+"?" +
						"appid=" + appId+
						"&redirect_uri=" +
						backUrl+
						"&response_type=code&scope=snsapi_base&state=123#wechat_redirect";
				response.sendRedirect(url);
				return null;
			}else if(userAgent!=null && userAgent.indexOf("AliApp")>-1){
				alitoPay(request,response);
			}
			else {
				response.sendRedirect(request.getContextPath()+"/page/wapPay.html");
			}
			return null;
			
		} catch (Exception e) {
			request.setAttribute("errorMsg", e.getMessage());
			return "error";
		}
	}
	
	@RequestMapping("/alitopay")
	public Object alitoPay(HttpServletRequest request, HttpServletResponse response){
		try {
			//2016-06-24 判断session里有没有alipay_user_id，如果有直接跳转到页面
			if( null==request.getSession(true).getAttribute("alipay_user_id")){
			   response.sendRedirect(request.getContextPath()+"/page/wapPay.html");
			   return null;
			}
			String appId ="12331251";//此处请使用支付宝开放平台appId
			String backUrl = getBasePath(request) + Configuration.ALIPAY_AUTH_BACK_URI+"?appId="+appId;
			backUrl = URLEncoder.encode(backUrl, "UTF-8");
			//scope 参数视各自需求而定，这里用scope=snsapi_base 不弹出授权页面直接授权目的只获取统一支付接口的openid
			String url = "https://openauth.alipay.com/oauth2/publicAppAuthorize.htm?app_id="
					+ appId+"&scope=auth_base&redirect_uri="
							+ backUrl;
			response.sendRedirect(url);
			return null;
		} catch (Exception e) {
			request.setAttribute("errorMsg", e.getMessage());
			return "error";
		}
	}
	
	/**
	 取openId
	 */
	@RequestMapping("/geOpenId")
	public void pay(HttpServletRequest request, HttpServletResponse response){
		//微信处理授权后的code参数值,依据response_type定义的键来获取
		String code = request.getParameter("code");
		try {
			// 网页授权后获取传递的参数
			String openId = null;
			//2016-06-24  openId过期前，不要通过接口调用获取openId
			if( null!=request.getSession(true).getAttribute("openId") ){
				openId = (String)request.getSession(true).getAttribute("openId");
			}
			else {
				String URL = Configuration.ACCESS_TOKEN + "?appid=" + "此处填微信appid" + "&secret=" +"此处填微信密钥"   
						+ "&code=" + code + "&grant_type=authorization_code";
				String jsonStr = WebUtils.doGet(URL,null);
				openId =  StringUtils.getValueFromJSON(jsonStr, "openid") ;
			}
			request.getSession(true).setAttribute("openId", openId);
			response.sendRedirect(request.getContextPath()+"/page/wapPay.html");
			return  ;
		} catch (Exception e) {
			request.setAttribute("errorMsg", "支付预处理异常:"+e.getMessage());
			return ; 
		}
	}
	 
	private static String getBasePath(HttpServletRequest request){
		String path = request.getContextPath();
		String basePath = request.getScheme()+"://"+request.getServerName();
		int port = request.getServerPort();
		if(port != 80){
			basePath += ":"+request.getServerPort();
		}
		basePath += path + "/";
//		String basePath = "http://www.yeepdata.com/unitepay-api/";
		return basePath;
	}
}
