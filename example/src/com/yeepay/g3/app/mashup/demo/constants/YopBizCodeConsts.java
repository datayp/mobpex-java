package com.yeepay.g3.app.mashup.demo.constants;

/**
 * 定义业务系统的结果编码；常量代码8位 , [xxYYYzzz]
 *
 * <pre>
 * xx=biz.code
 * YYY=业务编码
 * zzz=内部编号
 * </pre>
 *
 * @author wang.bao
 */
public class YopBizCodeConsts {

	/**
	 * 业务处理成功
	 */
	public final static int SUCCESS = 99999999;

	/**
	 * 分布式业务已经处理成功
	 */
	public final static int DIS_TX_SUCCESS = 99999998;

	/**
	 * 系统异常
	 */
	public final static int SYSTEM_EXCEPTION = 99999000;

	// ---------------- 99100zzz 参数校验错误码
	// 出现了NULL
	public final static int VAL_IS_NULL_ERROR = 99100001;
	public final static int VAL_IS_BLANK_ERROR = 99100002;
	public final static int VAL_MATCH_PATTERN_ERROR = 99100003;
	public final static int VAL_MAX_LENGTH_ERROR = 99100004;
	public final static int VAL_MIN_LENGTH_ERROR = 99100005;
	public final static int VAL_MAX_RANGE_ERROR = 99100006;
	public final static int VAL_MIN_RANGE_ERROR = 99100007;
	public final static int VAL_EMAIL_FORMAT_ERROR = 99100008;
	public final static int VAL_MOBILE_FORMAT_ERROR = 99100009;
	public final static int VAL_URL_FORMAT_ERROR = 99100010;
	// 不是有效的整数
	public final static int VAL_NOT_INT_ERROR = 99100011;
	// 不是有效的数字
	public final static int VAL_NOT_NUMBER_ERROR = 99100012;
	// 不是一个有效的货币值
	public final static int VAL_NOT_MONEY_ERROR = 99100013;

	// ---------------- 99001zzz 框架级错误码
	/**
	 * 应用{0}无效或不存在
	 */
	public static final String INVALID_APP_KEY = "99001001";

	/**
	 * 服务不存在
	 */
	public final static String SERVICE_NOT_EXIST = "99001002";

	/**
	 * 服务不可用
	 */
	public static final String SERVICE_UNAVAILABLE = "99001003";

	/**
	 * 权限不够、非法访问
	 */
	public static final String PERMISSION_DENIED = "99001004";

	/**
	 * 不允许的HTTP方法：{0}
	 */
	public static final String HTTP_METHOD_NOT_ALLOWED = "99001005";

	/**
	 * 缺少必要的参数:{0}
	 */
	public static final String MISSING_ARGUMENTS = "99001006";

	/**
	 * 参数无效，格式不对、非法值、越界等
	 */
	public static final String INVALID_ARGUMENTS = "99001007";

	/**
	 * 签名无效
	 */
	public static final String INVALID_SIGNATURE = "99001008";

	/**
	 * 编码错误
	 */
	public static final String INVALID_ENCODING = "99001010";

	/**
	 * 业务异常
	 */
	public static final String BUSINESS_LOGIC_ERROR = "99001011";

	/**
	 * 会话无效
	 */
	public static final String INVALID_SESSION = "99001012";

	/**
	 * 调用超时
	 */
	public static final String INVOKE_TIMEOUT = "99001013";

	/**
	 * 调用次数超限
	 */
	public static final String CAL_TIMES_LIMITED = "99001014";

	/**
	 * 调用并发数超限
	 */
	public static final String CAL_CONCURRENT_LIMITED = "99001015";

	/**
	 * 调用频次超限
	 */
	public static final String CAL_FREQUENCY_LIMITED = "99001016";

	/**
	 * 余量不足
	 */
	public static final String CAL_BALANCE_LIMITED = "99001017";

	/**
	 * 非白名单
	 */
	public static final String CAL_WHITELIST_LIMITED = "99001021";

	/**
	 * 文件上传失败:{0}
	 */
	public static final String UPLOAD_FAIL = "99001018";

	/**
	 * 文件格式不对，正确格式为：<文件格式>@<文件内容>
	 */
	public static final String FILE_FORMAT_ERROR = "99001019";

	/**
	 * 报文加解密出错
	 */
	public static final String ENCRYPT_DECRYPT_ERROR = "99001020";
	
	/**
	 * 密钥不在有效期
	 */
	public static final String KEY_EXPRIE = "99001021";

}
