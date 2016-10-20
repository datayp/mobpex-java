package com.yeepay.g3.app.mashup.demo.form;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * 
 * @Copyright: Copyright (c)2015
 * @company 湖南易宝天创
 * @author yaojun.deng
 * @since 2016年3月9日
 * @version 1.0
 *
 *
 */
/**
 * 
 * @Copyright: Copyright (c)2015
 * @company 湖南易宝天创
 * @author yaojun.deng
 * @since 2016年4月20日
 * @version 1.0
 *
 *
 */
public class SubmitOrderParam  {

	private static final long serialVersionUID = 644216775919851338L;

	protected String appId ;
	protected String liveMode;
	protected String secretKey;
	protected String userId;
	/**
	 * 商户交易订单号
	 */
	protected String tradeNo;

	/**
	 * 产品名称
	 */
	protected String productName;

	/**
	 * 产品描述
	 */
	protected String productDescription;

	/**
	 * 支付类型
	 */
	protected String payType;

	/**
	 * 支付通道
	 */
	protected String payChannel;

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getLiveMode() {
		return liveMode;
	}

	public void setLiveMode(String liveMode) {
		this.liveMode = liveMode;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * 支付币种
	 */
	protected String payCurrency = "CNY";

	/**
	 * 商户订单金额
	 */
	protected String amount;

	/**
	 * 魔派支付服务器主动通知商户网站里指定的页面http/https路径
	 */
	protected String notifyUrl;

	/**
	 * 魔派支付处理完支付后，当前页面自动跳转到商户网站里指定页面的http/https路径
	 */
	protected String returnUrl;


	
	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductDescription() {
		return productDescription;
	}

	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public String getPayChannel() {
		return payChannel;
	}

	public void setPayChannel(String payChannel) {
		this.payChannel = payChannel;
	}

	public String getPayCurrency() {
		return payCurrency;
	}

	public void setPayCurrency(String payCurrency) {
		this.payCurrency = payCurrency;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getTradeNo() {
		return tradeNo;
	}

	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public String getReturnUrl() {
		return returnUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}

	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}
 
}
