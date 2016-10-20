package com.yeepay.g3.app.mashup.demo.constants;

/**
 * 微信配置信息
 * @author：changkun.deng
 * @since：2015年11月10日 上午10:19:21
 * @version:
 */
public class Configuration {

		//微信APPID
		public static final String APPID = "wx1ca12412411cd06b";
		//应用秘钥
		public static final String APP_SECRET = "60421412441214789";
		
		//微信授权后跳转的我方地址（该地址实现统一下单处理）
		public static final String BACK_URI = "wechat/geOpenId";
		//支付宝授权后跳转的我方地址（该地址实现统一下单处理）
		public static final String ALIPAY_AUTH_BACK_URI = "alipay/geUserId";
		//微信支付结果回调通知地址（该地址实现接收结果通知处理）
		public static final String NOTIFY_URI = "wx/notify";
		//微信授权用户信息地址
		public static final String WX_OAUTH_URL = "https://open.weixin.qq.com/connect/oauth2/authorize";
		//微信获取下单token地址
		public static final String ACCESS_TOKEN = "https://api.weixin.qq.com/sns/oauth2/access_token";
		//微信预下单地址
}
