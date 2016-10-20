package com.yeepay.g3.app.mashup.demo.controller;

import java.io.IOException;
import java.util.Map;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.mobpex.srvsdk.client.MobpexClient;
import com.mobpex.srvsdk.client.MobpexConfig;
import com.mobpex.srvsdk.client.MobpexConstants;
import com.mobpex.srvsdk.client.MobpexRequest;
import com.mobpex.srvsdk.client.MobpexResponse;
import com.mobpex.srvsdk.dto.PrePayRequestDTO;
import com.mobpex.srvsdk.dto.RefundRequestDTO;
import com.mobpex.srvsdk.enums.ResponseFormatType;
import com.mobpex.srvsdk.error.MobpexError;
import com.mobpex.srvsdk.unmarshaller.MobpexMarshallerUtils;
import com.yeepay.g3.app.mashup.demo.constants.YopBizCodeConsts;
import com.yeepay.g3.app.mashup.demo.form.SubmitOrderParam;

/**

 */
@Controller
@RequestMapping(value = "/demo")
public class DemoPayController {
	private final String appId = "15122404366710489367";
	private final String secretKey = "LS_1bilscsAFBWqFnc3f61ssiors7qira";
	private final String yopServerRoot = "https://220.181.25.235/yop-center";

	@ResponseBody
	@RequestMapping(value = "/callBack", method = { RequestMethod.POST, RequestMethod.GET })
	public String callBack(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// return "此地址是模拟商户后台系统接受回调，本次收到的回调数据为：data=" +
		// request.getParameter("data");
		String json = request.getParameter("data");
		JSONObject jsonObject = JSON.parseObject(json);
		boolean isValidSign = com.mobpex.srvsdk.encrypt.MobpexSignUtils.isValidCallBackSign(json, "mobpex应用中心-密钥管理-公钥",
				"MD5");
		String amount = jsonObject.getString("amount");// 支付金额
		String appId = jsonObject.getString("appId");// 开发者的AppId
		String eventType = jsonObject.getString("eventType");// 事件类型,支付结果通知时eventType值为：PAY
																// ,退款结果通知时eventType值为：REFUND
		String orderStatus = jsonObject.getString("orderStatus");// INIT("INIT",
																	// "待支付"),
																	// PAYING("PAYING","支付中"),
																	// PAID("PAID","已支付"),
																	// TIMEOUT("TIMEOUT","已关闭"),
																	// FAIL("FAIL","支付失败"),
																	// REFUNDING("REFUNDING","退款中"),
																	// REFUND("REFUND",
																	// "已退款");
		String payOrderId = jsonObject.getString("payOrderId");// 第三方支付交易订单号,成功时才会有
		String productDescription = jsonObject.getString("productDescription");
		String productName = jsonObject.getString("productName");// 产品名称
		String tradeNo = jsonObject.getString("tradeNo");// 商户交易流水号
		String transactionCloseTime = jsonObject.getString("transactionCloseTime");// 交易结束时间
		String ts = jsonObject.getString("ts");// 时间戳，是时间戳相对于1970年1月1日00:00:00的毫秒数
		String txnTime = jsonObject.getString("txnTime");// 接收到订单时间
		String errMsg = jsonObject.getString("errMsg");// 错误信息
		String errCode = jsonObject.getString("errCode");// 错误码
		if(true){// 商户保存相关订单信息，处理成功后返回SUCCESS
			response.getWriter().print("SUCCESS");
			return "SUCCESS";
		}
		else { 
			return "FAIL";
		}
	}
	// TODO Auto-generated method stub

	@ResponseBody
	@RequestMapping(value = "/submitOrder", method = { RequestMethod.POST, RequestMethod.GET })
	public String submitOrder(SubmitOrderParam para, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		// TODO Auto-generated method stub

		para.setTradeNo("" + new Random().nextInt(Integer.MAX_VALUE));
		para.setAppId(appId);
		para.setSecretKey(secretKey);
		para.setPayType(para.getPayType().toUpperCase());
		String openId = null; 
		if ("PUB".equalsIgnoreCase(para.getPayType())) {
			openId = (String) request.getSession(true).getAttribute("openId");
		}
		if (para.getAmount() == null) {
			para.setAmount("0.01");
		}
		MobpexConfig.setAppId(para.getAppId());// yop应用
		MobpexConfig.setSecretKey(para.getSecretKey());// yop应用密钥，需要和短信通知应用的密钥保持一致才行，否则验证签名不通过
		MobpexConfig.setServerRoot(yopServerRoot);
		MobpexConfig.setIgnoreSSLCheck(true);
		MobpexRequest req = new MobpexRequest();
		//req.setLiveMode();
		req.setUserId(para.getUserId());
		PrePayRequestDTO requestPara = new PrePayRequestDTO();
		requestPara.setTradeNo(para.getTradeNo());// ""+r.nextInt(Integer.MAX_VALUE));
		// requestPara.setRequestIp("127.0.0.1");
		requestPara.setRequestIp("127.0.0.1");
		requestPara.setPayChannel(para.getPayChannel()); // "ALIPAY"
		requestPara.setReturnUrl("http://www.baidu.com");
		requestPara.setNotifyUrl("http://www.baidu.com");
		if (openId != null) {
			requestPara.getExtra().put("openid", openId);
		}
		requestPara.setPayType(para.getPayType());
		requestPara.setProductName("测试商品");
		requestPara.setProductDescription("描述");
		requestPara.setAmount(para.getAmount());
		req.addParam("prePayRequest", MobpexMarshallerUtils.marshal(ResponseFormatType.json, requestPara));
		MobpexResponse resp = MobpexClient.post("/rest/v1.0/pay/unifiedOrder".toLowerCase(), req);
		System.out.println(resp.getContent());
		response.addHeader("Access-Control-Allow-Origin", "*");
		response.setCharacterEncoding("UTF-8");
		return resp.getContent();
	}
	@ResponseBody
	@RequestMapping(value = "/submitOrderForPc", method = { RequestMethod.POST, RequestMethod.GET })
	public String submitOrderForPc(SubmitOrderParam para, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		// TODO Auto-generated method stub


		if (para.getTradeNo() == null) {
			para.setTradeNo("" + new Random().nextInt(Integer.MAX_VALUE));
		}

		if (para.getAppId() == null) {
			para.setAppId(appId);
		}
		if (para.getSecretKey() == null) {
			para.setSecretKey(secretKey);
		}

		if (para.getPayType() == null) {
			para.setPayType("WEB");
		} else {
			para.setPayType(para.getPayType().toUpperCase());
		}


		//para.setPayType("WEB");
		
 

		if (para.getAmount() == null) {
			para.setAmount("0.01");
		}
 

		MobpexConfig.setAppId(para.getAppId());// yop应用
		MobpexConfig.setSecretKey(para.getSecretKey());// yop应用密钥，需要和短信通知应用的密钥保持一致才行，否则验证签名不通过
		MobpexConfig.setServerRoot(yopServerRoot);
		MobpexRequest req = new MobpexRequest();
		PrePayRequestDTO requestPara = new PrePayRequestDTO();
		Random r = new Random();
		requestPara.setTradeNo(para.getTradeNo());// ""+r.nextInt(Integer.MAX_VALUE));
		requestPara.setRequestIp("127.0.0.1");
		requestPara.setDeviceId("" + r.nextInt(Integer.MAX_VALUE));
		requestPara.setPayChannel(para.getPayChannel()); // "ALIPAY"
		requestPara.setReturnUrl(para.getReturnUrl());
		requestPara.setNotifyUrl(para.getNotifyUrl());
		requestPara.setPayType(para.getPayType());
		requestPara.setProductName("测试");
		requestPara.setAmount(para.getAmount());
		req.addParam("prePayRequest", MobpexMarshallerUtils.marshal(ResponseFormatType.json, requestPara));
		
		System.out.println("req.getParam('prePayRequest')="+req.getParam("prePayRequest"));		
		System.out.println("req.getParams()="+req.getParams());
		//2016-06-30 将tradeNo放入session，让pc网页收银台页面可以获取该值，从而让该页面的弹出框上面的“支付完成”按钮，可以获取tradeNo作为参数之一去进行是否支付完成的查询
		request.getSession(true).setAttribute("queryPaymentTradeNo", para.getTradeNo());
		MobpexResponse resp = MobpexClient.post("/rest/v1.0/pay/unifiedOrder".toLowerCase(), req);
		System.out.println(resp.getContent());
		response.addHeader("Access-Control-Allow-Origin", "*");
		response.setCharacterEncoding("UTF-8");
		// response.getWriter().write(resp.getContent());
		return resp.getContent();
	}
	
	@RequestMapping(value = "/toRefund", method = { RequestMethod.POST, RequestMethod.GET })
	public String toRefund() throws ServletException, IOException {
		return "refund";
	}

	@ResponseBody
	@RequestMapping(value = "/refund", method = { RequestMethod.POST, RequestMethod.GET })
	public String refund(HttpServletRequest rqst, HttpServletResponse response) throws ServletException, IOException {
		rqst.setCharacterEncoding("UTF-8");
		String email = rqst.getParameter("email");
		// UserDTO ud = userFacade.findByEmail(email);
		String tradeNo = rqst.getParameter("tradeNo");
		String key = new String(rqst.getParameter("key").getBytes("ISO-8859-1"), "UTF-8");
		String appId = rqst.getParameter("appId");
		String amount = rqst.getParameter("amount");

		MobpexConfig.setAppId(appId);// yop应用
		MobpexConfig.setSecretKey(key);// yop应用密钥，需要和短信通知应用的密钥保持一致才行，否则验证签名不通过
		MobpexConfig.setServerRoot(yopServerRoot);
		MobpexConfig.setIgnoreSSLCheck(true);
		MobpexRequest request = new MobpexRequest();
		Random r = new Random();
		request.setLiveMode(true);
		request.setUserId(email);
		RefundRequestDTO refundRequest = new RefundRequestDTO();
		// 商户APPID
		refundRequest.setRefundNo("" + r.nextInt(Integer.MAX_VALUE)); // 商户订单号（商户退款流水号）
		refundRequest.setRequestIp("127.0.0.1"); // 请求者终端IP
		refundRequest.setTradeNo(tradeNo); // 商户订单号，支付时的订单号
		refundRequest.setAmount(amount); // 退款金额，单位元保留两位小数
		refundRequest.setDescription("我要退款~~~");
		request.addParam("refundRequest", new Gson().toJson(refundRequest));
		MobpexResponse rsps = MobpexClient.post("/rest/v1.0/pay/refund".toLowerCase(), request);
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
		if (rsps.isSuccess()) {

			String transUrl = null;
			try {
				transUrl = com.mobpex.srvsdk.utils.StringUtils.getValueFromJSON(rsps.getStringResult(), "transUrl");
				if (transUrl != null) {
					return "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">"
							+ "						<html><head></head><body><a href='" + transUrl
							+ "'>继续</a></body></html>";
				}
			} catch (Exception e) {
			}
			// 此处用重定向时发现被浏览器自动 urlencode了，支付宝不支持这种转换，所以只能用超连接的方式
		}
		String result = rsps.getState();
		if (rsps.getError() != null) {
			result += rsps.getError().getMessage();

		}
		return result;
	}

}
