Mobpex java SDK
============

### 简介
 1. docs 目录下为 Mobpex Java SDK 的使用文档。
 2. example 目录下面为一个 Eclipse IDE 的示例工程。
 3. libs 为 Mobpex Java SDK 的 jar 包。


### 版本要求

Java SDK 要求 JDK 版本 1.6 及以上
  
### 安装

##### 手动安装
将 libs/ 下面的 jar 包导入工程

    
#### 使用示例	
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
