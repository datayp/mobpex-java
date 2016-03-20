
# Mobpex java SDK

### 简介

​        Mobpex Java SDK 采用https与Mobpex通信，支持RSA、MD5、SHA、SHA1等安全算法， SDK提供了API的请求封装、摘要签名、加解密、响应解释等基础功能，通过SDK能实现查询APP可用支付渠道列表、发起预支付请求、发起退款请求、支付查询、退款查询等业务。

1. docs 目录下为 Mobpex Java SDK 的使用文档。
2. example 目录下面为一个 Eclipse IDE 的示例工程。
3. libs 为 Mobpex Java SDK 的 jar 包。

### 版本要求

Java SDK 要求 JDK 版本 1.6 及以上

### 安装

##### 手动安装

将 libs/ 下面的 jar 包导入工程



### 快速指引

首先您需要先完成以下几步：1、注册Mobpex用户;2、创建应用。

然后就可以使用SDK开发程序了:

1、商户服务端接收用户支付请求后通过Server SDK发起预支付请求；

2、商户服务端将Server SDK返回的内容(支付凭证)发送给客户端；

3、客户端调用Client SDK支付接口,传入支付凭证作为参数；

4、Client SDK将唤醒相应渠道支付控件引导用户完成支付。




#### 查询可用支付渠道示例	
    //Mobpex对外提供的接口地址
    MobpexConfig.setServerRoot("https://220.181.25.235/yop-center"); 
    //是否忽略响应服务器的域名检查
    MobpexConfig.setIgnoreSSLCheck(true);     
    //企业在Mobpex注册的应用ID；
    MobpexConfig.setAppId("15120409211235616710");   
    //密钥值，参见公共参数说明  
    MobpexConfig.setSecretKey("fwefwe1ffdsa2");  
    //新建一个请求对象
    MobpexRequest request = new MobpexRequest();                     
    //设置用户id
    request.setUserId( "test168888@datayp.com");   
     //发起请求                 
    MobpexResponse response = MobpexClient.post("/rest/v1.0/query/findChannelInfoByAppId",request);
    //判断验签是否通过
    if(response.isValidSign()){
    	//response.getContent()可得到响应报文
    	System.out.println( response.getContent());               
    }
    
    参考 example/ 示例代码。改工程提供了付款、退款、查询相关的方法调用示例。
### 预支付请求示例

调用示例（JAVA语言）

```
MobpexConfig.setAppId("151224043667104893");   //商户在Mobpex的应用ID
MobpexConfig.setSecretKey("key值");            //密钥。请登录官网查询或修改密钥
MobpexConfig.setServerRoot("https://220.181.25.235/yop-center");//Mobpex sdk接入地址
//测试地址https://220.181.25.235/yop-center
MobpexConfig.setIgnoreSSLCheck(true);  //是否忽略响应域名检查，使用测试地址时需要设置为true
MobpexRequest mobpexRequest = new MobpexRequest();              //新建一个请求对象
mobpexRequest.setUserId( "test168888@datayp.com");              //设置用户id
PrePayRequestDTO prePayRequestDTO = new PrePayRequestDTO();      //新建一个预支付DTO
//此处传商户系统支付请求流水，支付请求流水应该与商户订单号区分开来，一般的一笔订单可产生多笔支付流水。
prePayRequestDTO.setTradeNo(""+(new Random().nextInt(Integer.MAX_VALUE)));
prePayRequestDTO.setPayChannel("WECHAT");                          
prePayRequestDTO.setPayType("APP");                                
prePayRequestDTO.setProductName("维多利亚");                   
prePayRequestDTO.setProductDescription("G");                   
prePayRequestDTO.setAmount("1.00");                                
prePayRequestDTO.setRequestIp("127.0.0.1");                        
//将预支付请求dto序列化为json格式，作为参数添加到请求对象中
mobpexRequest.addParam("prePayRequest",new Gson().toJson(prePayRequestDTO));
//发起请求
MobpexResponse mobpexResponse = MobpexClient.post("/rest/v1.0/pay/unifiedOrder",mobpexRequest);
//判断验签是否通过
if(mobpexResponse.isValidSign()){
   System.out.println( mobpexResponse.getContent());                       //mobpexResponse.getContent()可得到响应报文
}
```

返回的支付凭证：

```
{
"state":"SUCCESS",                                  //操作是否成功，如果为SUCCESS则为成功
"result":{                                          //result的内容为业务的响应结果
    "appId":"15122404177104893671",                 //应用id
    "liveMode":false,                               //是否Live模式
    "errCode":"1",                                  //错误码  为1表示成功
    "errMsg":"成功",                                 //描述
    "orderId":"PO21b9gq09AFBWkQVJt4mdh23a6t3r6j80", //Mobpex订单号 
    "tradeNo":"1098836026",                         //商户系统支付请求流水号 
    "paymentParams":{                               //第三方支付系统支付凭证
      "appId":"wx_appId_10010010",
      "noncestr":"1452836638609",
      "package":"Sign=WXPay",
      "partnerid":"wx_mchid_10010010",
      "prepayid":1.452836638609E12,
      "sign":"signature_test_100100010",
      "timestamp":"1452836638"
    }
},
"ext" : {                                            //扩展参数
  "payType" : "APP",                                 //支付方式
  "payChannel" : "ALIPAY"                            //支付渠道
 }，
"ts":1452836639116,                                  //时间戳
"sign":"2be8437156718320f50dd3407d07585f",           //签名,必要验证签名正确才能认为信息有效
"format":"json",                                     //格式
"validSign":true //SDK对签名进行验证的结果 为了安全起见商户系统需要当validSign为true才进行下一步操作 
}
```



