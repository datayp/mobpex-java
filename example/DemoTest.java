package com;

import junit.framework.TestCase;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mobpex.srvsdk.client.MobpexClient;
import com.mobpex.srvsdk.client.MobpexConfig;
import com.mobpex.srvsdk.client.MobpexRequest;
import com.mobpex.srvsdk.client.MobpexResponse;
import com.mobpex.srvsdk.dto.PrePayRequestDTO;
import com.mobpex.srvsdk.dto.RefundRequestDTO;

import java.io.IOException;
import java.util.Random;

/**
 * 
 * 
 * @Copyright: Copyright (c)2015
 * @company 湖南易宝天创
 * @author yaojun.deng
 * @since 2016年2月29日
 * @version 1.0
 *
 *
 */
public class DemoTest extends TestCase {
	// https://220.181.25.235/yop-center"; 测试地址  
	//https://www.mobpex.com/yop-center 正式环境地址
    private String serverRoot = "https://www.mobpex.com/yop-center";
	private String secretKey = "2121221";
	private String appId = "15122404366710489367";
	private String userId = "lofwqhen-1@ye12pay.com";
	boolean ignoreSSLCheck = true;//测试时使用 正式上线应该设置为false 
	// 查询渠道
	public void testQueryChannel() throws IOException {
		MobpexConfig.setAppId(appId);// yop应用
		MobpexConfig.setSecretKey(secretKey); // 对应账户基本信息中 4个密钥之一 
		MobpexConfig.setServerRoot(serverRoot);
		MobpexConfig.setIgnoreSSLCheck(ignoreSSLCheck);
		MobpexRequest request = new MobpexRequest();
		request.setUserId(userId);
		MobpexResponse response = MobpexClient.post("/rest/v1.0/query/findChannelInfoByAppId", request);
		if(response.isValidSign())
		{
			System.out.println("签名验证通过");
		}
		else {
			System.out.println("签名验证失败！！");
		}
		System.out.println( response.getContent());
	}

	// 统一下单
	public void testProOder() throws IOException {
		MobpexConfig.setAppId(appId);// yop应用
		MobpexConfig.setSecretKey(secretKey);// yop应用密钥，需要和短信通知应用的密钥保持一致才行，否则验证签名不通过
		MobpexConfig.setServerRoot(serverRoot);
		MobpexConfig.setIgnoreSSLCheck(ignoreSSLCheck);
		MobpexRequest mobpexRequest = new MobpexRequest();
		mobpexRequest.setUserId(userId);
		PrePayRequestDTO prePayRequestDTO = new PrePayRequestDTO();
		prePayRequestDTO.setTradeNo("" + new Random().nextInt(Integer.MAX_VALUE) );//此处填商户内部系统订单号
		prePayRequestDTO.setPayChannel("ALIPAY"); // WECHAwechat ALIPAY 
		prePayRequestDTO.setPayType("APP");
		prePayRequestDTO.setProductName("维多利亚的秘密");
		prePayRequestDTO.setProductDescription("G cup");
		prePayRequestDTO.setAmount("1.00");
		prePayRequestDTO.setRequestIp("127.0.0.1");
		mobpexRequest.addParam("prePayRequest", new Gson().toJson(prePayRequestDTO));
		MobpexResponse mobpexResponse = MobpexClient.post("/rest/v1.0/pay/unifiedOrder".toLowerCase(), mobpexRequest);
		GsonBuilder gb = new GsonBuilder();
		gb.disableHtmlEscaping();
		Gson gg = gb.create();
		// prePayResponse = new Gson().fromJson(mobpexResponse.getStringResult(),  PrePayResponseDTO.class );
		
		System.out.println("输入参数:"+gg.toJson(mobpexRequest));
		if(mobpexResponse.isValidSign())
		{
			System.out.println("签名验证通过");
		}
		else {
			System.out.println("签名验证失败！！");
		}
		System.out.println("输出参数:"+mobpexResponse.getContent());
	
	}
	// 统一下单
	public void testRefund() throws IOException {
		MobpexConfig.setAppId(appId);// yop应用
		MobpexConfig.setSecretKey(secretKey);// yop应用密钥，需要和短信通知应用的密钥保持一致才行，否则验证签名不通过
		// YopConfig.setServerRoot("https://open.yeepay.com/yop-center/");//生产环境
		// MobpexConfig.setServerRoot("http://172.17.102.188:8093/yop-center");
		MobpexConfig.setServerRoot(serverRoot);
		MobpexConfig.setIgnoreSSLCheck(ignoreSSLCheck);
		MobpexRequest request = new MobpexRequest();
		Random r = new Random();
		request.setUserId(userId);//  
		RefundRequestDTO refundRequest = new RefundRequestDTO();

		refundRequest.setTradeNo("834153959836" ); // 商户订单号（商户退款流水号）
		refundRequest.setDeviceId("" + r.nextInt(Integer.MAX_VALUE)); // 设备标识
		refundRequest.setDeviceType("MOBILE"); // 设备类型
		refundRequest.setRequestIp("127.0.0.1"); // 请求者终端IP
		refundRequest.setRequestIdentification("apple"); // 请求者标识
		refundRequest.setRefundNo(""+ r.nextInt(Integer.MAX_VALUE)); // 商户订单号，支付时的订单号
		refundRequest.setAmount("0.01"); // 退款金额，单位元保留两位小数
		refundRequest.setDescription("我要退款~~~");
		request.addParam("refundRequest", new Gson().toJson(refundRequest));
		System.out.println(request.getParam("refundRequest"));

		MobpexResponse response = MobpexClient.post("/rest/v1.0/pay/refund".toLowerCase(), request);
		if(response.isValidSign())
		{
			System.out.println("签名验证通过");
		}
		else {
			System.out.println("签名验证失败！！");
		}
		System.out.println("输出参数:"+response.getContent());
	
	}

	// 查询退款
	public void testRefundQuery() throws IOException {
		MobpexConfig.setAppId(appId);// yop应用
		MobpexConfig.setSecretKey(secretKey);// yop应用密钥，需要和短信通知应用的密钥保持一致才行，否则验证签名不通过
		// YopConfig.setServerRoot("https://open.yeepay.com/yop-center/");//生产环境
		MobpexConfig.setServerRoot(serverRoot);
		MobpexConfig.setIgnoreSSLCheck(true);
		MobpexRequest request = new MobpexRequest();
		request.setUserId(userId);
		 
		request.addParam("externalOrderId", "669251114954");
		request.addParam("externalRefundId", "201601261053502690728");

		MobpexResponse response = MobpexClient.post("/rest/v1.0/pay/queryRefundOrder".toLowerCase(), request);
		if(response.isValidSign())
		{
			System.out.println("签名验证通过");
		}
		else {
			System.out.println("签名验证失败！！");
		}
		System.out.println("输出参数:"+response.getContent());
	
	}

	// 查询支付
	public void testPayQuery() throws IOException {

		MobpexConfig.setAppId(appId);// yop应用
		MobpexConfig.setSecretKey(secretKey);// yop应用密钥，需要和短信通知应用的密钥保持一致才行，否则验证签名不通过
		MobpexConfig.setServerRoot(serverRoot);
		MobpexConfig.setIgnoreSSLCheck(true);
		MobpexRequest request = new MobpexRequest();
		request.setUserId(userId);
		request.addParam("externalOrderId", "669251114954");
		MobpexResponse response = MobpexClient.post("/rest/v1.0/pay/queryPaymentOrder".toLowerCase(), request);
		if(response.isValidSign())
		{
			System.out.println("签名验证通过");
		}
		else {
			System.out.println("签名验证失败！！");
		}
		System.out.println("输出参数:"+response.getContent());
	
	} 
}
