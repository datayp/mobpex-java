package com.yeepay.g3.app.mashup.demo.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.gson.Gson;
import com.mobpex.srvsdk.utils.StringUtils;
import com.mobpex.srvsdk.utils.WebUtils;
import com.yeepay.g3.app.mashup.demo.constants.Configuration;

@Controller
@RequestMapping(value = "/alipay")
public class AlipayPaymentAction {

 	 	/**
	 * 网页授权获取用户信息
	 * 支付宝服务号支付需要参照本类获取用户的支付宝userId
	 * @param request
	 * @param response
	 */
	
	
	/**
	 取userId
	 */
	@RequestMapping("/geUserId")
	public void pay(HttpServletRequest request, HttpServletResponse response){
		String app_id=request.getParameter("app_id");
		String  auth_code=request.getParameter("auth_code");
		String alipay_user_id  = null;
		String privateKey = "支付宝开放平台商户私钥";
		try {
			Map <String,String> requestMap = new HashMap<String, String>();
			requestMap.put("app_id", app_id);
			requestMap.put("method", "alipay.system.oauth.token");
			requestMap.put("charset", "GBK");
			requestMap.put("timestamp", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			requestMap.put("version", "1.0");
			requestMap.put("code", auth_code);
			requestMap.put("grant_type", "authorization_code");
			requestMap.put("sign_type", "RSA");
	        String parastr =   createLinkString(requestMap);
	        String sign = com.yeepay.g3.app.mashup.demo.utils.RSA.sign(parastr, privateKey, "utf-8");
	        requestMap.put("sign", sign);
	        try {
				String content = WebUtils.doPost("https://openapi.alipay.com/gateway.do", requestMap,6000,6000);
				Gson gson = new Gson();
				Map map = gson.fromJson(content, HashMap.class);
				Map result = (Map) map.get("alipay_system_oauth_token_response");
				if(result!=null){
					alipay_user_id = (String)result.get("user_id");//alipay_user_id
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			request.getSession(true).setAttribute("alipay_user_id", alipay_user_id);
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
	public static String createLinkString(Map<String, String> params) throws UnsupportedEncodingException {
			List<String> keys = new ArrayList<String>(params.keySet());
			Collections.sort(keys);
			StringBuffer buffer = new StringBuffer();
			for (int i = 0; i < keys.size(); i++) {
				String key = keys.get(i);
				String value = params.get(key);
				if("sign".equals(key)){
					buffer.append(key).append("=").append(URLEncoder.encode(value, "UTF-8"));
				}else{
					buffer.append(key).append("=").append(value);
				}
				if (i != (keys.size() - 1)) {
					buffer.append("&");
				}
			}
			return buffer.toString();
	}
}
