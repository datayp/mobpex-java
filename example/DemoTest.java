package com;

import junit.framework.TestCase;


import com.mobpex.srvsdk.client.MobpexClient;
import com.mobpex.srvsdk.client.MobpexConfig;
import com.mobpex.srvsdk.client.MobpexRequest;
import com.mobpex.srvsdk.client.MobpexResponse;
import com.mobpex.srvsdk.dto.MerPayRequestDTO;
import com.mobpex.srvsdk.dto.PrePayRequestDTO;
import com.mobpex.srvsdk.dto.RefundRequestDTO;
import com.mobpex.srvsdk.enums.ResponseFormatType;
import com.mobpex.srvsdk.unmarshaller.MobpexMarshallerUtils;
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
		//request.setLiveMode(false);
		//request.setFormat(ResponseFormatType.xml);
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
		//mobpexRequest.setLiveMode(false);
		PrePayRequestDTO prePayRequestDTO = new PrePayRequestDTO();
		prePayRequestDTO.setTradeNo("" + new Random().nextInt(Integer.MAX_VALUE));//此处填商户内部系统订单号
		prePayRequestDTO.setPayChannel("JDPAY"); // WECHAwechat ALIPAY 
		prePayRequestDTO.setPayType("WAP"); 
		prePayRequestDTO.setProductName(" SH201607061428341189");
		prePayRequestDTO.setProductDescription("G cup");
		prePayRequestDTO.setAmount("0.01");
		//prePayRequestDTO.setExtra(null);
		//prePayRequestDTO.setMetadata(null);

		//prePayRequestDTO.setRequestIp("127.0.0.1");
		//prePayRequestDTO.getExtra().put("openid", "ogiZrwJdUAA5SKVNvl2DeFD9BLdU");
		//prePayRequestDTO.setReturnUrl("http://mygou.cc/app/index.php?i=3&c=entry&p=pay_Paypay&op=return&orderid=286&do=order&m=ewei_shop");
		String json =MobpexMarshallerUtils.marshal(ResponseFormatType.json, prePayRequestDTO);
		mobpexRequest.addParam("prePayRequest", json);
		MobpexResponse mobpexResponse = MobpexClient.post("/rest/v1.0/pay/unifiedOrder" , mobpexRequest);
		 
		// prePayResponse = new Gson().fromJson(mobpexResponse.getStringResult(),  PrePayResponseDTO.class );
		
		System.out.println("输入参数:"+ MobpexMarshallerUtils.marshal(ResponseFormatType.json, mobpexRequest));
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

		refundRequest.setTradeNo("322523512494" ); // 商户订单号（商户退款流水号）
		refundRequest.setDeviceId("" + r.nextInt(Integer.MAX_VALUE)); // 设备标识
		refundRequest.setDeviceType("MOBILE"); // 设备类型
		refundRequest.setRequestIp("127.0.0.1"); // 请求者终端IP
		refundRequest.setRequestIdentification("apple"); // 请求者标识
		refundRequest.setRefundNo(""+ r.nextInt(Integer.MAX_VALUE)); // 商户订单号，支付时的订单号
		refundRequest.setAmount("0.01"); // 退款金额，单位元保留两位小数
		refundRequest.setDescription("我要退款~~~");
		request.addParam("refundRequest",MobpexMarshallerUtils.marshal(ResponseFormatType.json,refundRequest));
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
		request.addParam("tradeNo", "338215098");
		request.addParam("refundNo", "760173609");

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
		//request.setLiveMode(false);
		request.setUserId(userId);
		request.addParam("tradeNo", "338215098");
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
	
	

	// 企业付款
	public void testCorpPay() throws IOException {
		MobpexConfig.setAppId(appId);// yop应用
		MobpexConfig.setSecretKey(secretKey);// yop应用密钥，需要和短信通知应用的密钥保持一致才行，否则验证签名不通过
		MobpexConfig.setServerRoot(serverRoot);
		MobpexConfig.setIgnoreSSLCheck(ignoreSSLCheck);
		MobpexRequest mobpexRequest = new MobpexRequest();
		mobpexRequest.setUserId(userId);
		MerPayRequestDTO merPayRequestDTO = new MerPayRequestDTO();		
		merPayRequestDTO.setPayChannel("WECHAT");//发起支付的渠道
		merPayRequestDTO.setPayType("CORP");		 //发起支付的类型
		merPayRequestDTO.setAmount("1.00");
		merPayRequestDTO.getExtra().put("openid","ogiZrwJdUAA5SKVNvl2DeFD9BLdU");
		merPayRequestDTO.setOrderNo( ""+new Random().nextInt());	//商户订单号（商户交易流水号）
		merPayRequestDTO.setPayDesc("代付");
		String json =MobpexMarshallerUtils.marshal(ResponseFormatType.json, merPayRequestDTO);
		mobpexRequest.addParam("transferRequest", json);
		MobpexResponse mobpexResponse = MobpexClient.post("/rest/v1.0/transfer/transfer" , mobpexRequest);
		 
		// prePayResponse = new Gson().fromJson(mobpexResponse.getStringResult(),  PrePayResponseDTO.class );
		
		System.out.println("输入参数:"+ MobpexMarshallerUtils.marshal(ResponseFormatType.json, mobpexRequest));
		if(mobpexResponse.isValidSign())
		{
			System.out.println("签名验证通过");
		}
		else {
			System.out.println("签名验证失败！！");
		}
		
		System.out.println("输出参数:"+mobpexResponse.getContent());
	
	}

	// 企业付款订单查询
	public void testTransferOrderQuery() throws IOException {
		MobpexConfig.setAppId(appId);// yop应用
		MobpexConfig.setSecretKey(secretKey);// yop应用密钥，需要和短信通知应用的密钥保持一致才行，否则验证签名不通过
		MobpexConfig.setServerRoot(serverRoot);
		MobpexConfig.setIgnoreSSLCheck(ignoreSSLCheck);
		MobpexRequest mobpexRequest = new MobpexRequest();
		mobpexRequest.setUserId(userId);
		mobpexRequest.addParam("orderNo", "484141449");
		MobpexResponse mobpexResponse = MobpexClient.post("/rest/v1.0/transfer/queryOrder" , mobpexRequest);
		// prePayResponse = new Gson().fromJson(mobpexResponse.getStringResult(),  PrePayResponseDTO.class );
		
		System.out.println("输入参数:"+ MobpexMarshallerUtils.marshal(ResponseFormatType.json, mobpexRequest));
		if(mobpexResponse.isValidSign())
		{
			System.out.println("签名验证通过");
		}
		else {
			System.out.println("签名验证失败！！");
		}
		System.out.println("输出参数:"+mobpexResponse.getContent());
	}
	
	// 企业付款订单查询
	public void testTransferQueryBalance() throws IOException {
		MobpexConfig.setAppId(appId);// yop应用
		MobpexConfig.setSecretKey(secretKey);// yop应用密钥，需要和短信通知应用的密钥保持一致才行，否则验证签名不通过
		MobpexConfig.setServerRoot(serverRoot);
		MobpexConfig.setIgnoreSSLCheck(ignoreSSLCheck);
		MobpexRequest mobpexRequest = new MobpexRequest();
		mobpexRequest.setUserId(userId);
		mobpexRequest.addParam("payChannel", "YEEPAY");
		mobpexRequest.addParam("payType", "HELPBUY");
		MobpexResponse mobpexResponse = MobpexClient.post("/rest/v1.0/transfer/queryBalance", mobpexRequest);
		// prePayResponse = new
		// Gson().fromJson(mobpexResponse.getStringResult(),
		// PrePayResponseDTO.class );
		System.out.println("输入参数:" + MobpexMarshallerUtils.marshal(ResponseFormatType.json, mobpexRequest));
		if (mobpexResponse.isValidSign()) {
			System.out.println("签名验证通过");
		} else {
			System.out.println("签名验证失败！！");
		}
		System.out.println("输出参数:" + mobpexResponse.getContent());
	}
}
