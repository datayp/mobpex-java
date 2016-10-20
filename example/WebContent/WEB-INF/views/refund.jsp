<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>退款</title>

</head>
<body>
	 
	<form action="${pageContext.request.contextPath}/demo/refund" method="post">
		<table>
			<tr>
				<td colspan="2" align="center">请求退款</td> 
			</tr>
			<tr>
				
				<td align="right">用户email：</td>
				<td align="right"><input type="text" name="email" value=""></td>
			</tr>
			<tr>
				<td align="right">appId：</td>
				<td align="right"><input type="text" name="appId" value=""></td>
			</tr>
			<tr>
				<td align="right">用户live密钥：</td>
				<td align="right"><input type="text" name="key" value=""></td>
			</tr>
			<tr>
				<td align="right">订单号：</td>
				<td align="right"><input type="text" name="tradeNo" value=""></td>
			</tr>
			<tr>
				<td align="right">退款金额：</td>
				<td align="right"><input type="text" name="amount" value=""></td>
			</tr>
			<tr>
				<td colspan="2" align="center"><input type="submit" value＝"提交" /></td> 
			</tr>
		</table>  
	</form>
</body>
</html>