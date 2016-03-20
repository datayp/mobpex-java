#README



Mobpex Java SDK 采用https与Mobpex通信，支持RSA、MD5、SHA、SHA1等安全算法， 提供了API的请求封装、摘要签名、加解密、响应解释等功能，使用SDK可以轻松完成API的调用及结果的获取。

### 快速指引

首先您需要先完成以下几步：1、注册Mobpex用户;2、创建应用。

然后就可以开始开发程序了:

1、商户服务端接收用户支付请求；

2、商户服务端通过Server SDK发起预支付请求(见下文)；

3、商户服务端将Server SDK返回的内容(支付凭证)发送给客户端；

4、客户端调用Client SDK支付接口,传入支付凭证作为参数；

5、Client SDK将唤醒相应渠道支付控件引导用户完成支付。



开发程序的同时可以进行如下工作：

1、Mobpex官网提交企业认证、签约；

2、在各支付渠道开通账户、签约相关服务；

3、在Mobpex官网配置各支付渠道相关参数。

### 预支付请求示例

调用示例（JAVA语言）


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
    prePayRequestDTO.setProductName("维多利亚的秘密");                   
    prePayRequestDTO.setProductDescription("G cup");                   
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
    
返回的支付凭证：

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
