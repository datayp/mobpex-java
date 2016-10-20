<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<html>
<head>
<title>微信扫码支付</title>
<style type="text/css">
.demo {
  width: 400px; margin: 40px auto 0 auto; min-height: 250px;
}
.demo p {
  line-height: 30px
}

#qrPic {
  margin-top: 10px
}
</style>
<script type="text/javascript" src="script/jquery-1.4.2.min.js"></script>
<script type="text/javascript" src="script/jquery.qrcode.min.js"></script>
<script type="text/javascript">
	function toUtf8(str) {
		var out, i, len, c;
		out = "";
		len = str.length;
		for (i = 0; i < len; i++) {
			c = str.charCodeAt(i);
			if ((c >= 0x0001) && (c <= 0x007F)) {
				out += str.charAt(i);
			} else if (c > 0x07FF) {
				out += String.fromCharCode(0xE0 | ((c >> 12) & 0x0F));
				out += String.fromCharCode(0x80 | ((c >> 6) & 0x3F));
				out += String.fromCharCode(0x80 | ((c >> 0) & 0x3F));
			} else {
				out += String.fromCharCode(0xC0 | ((c >> 6) & 0x1F));
				out += String.fromCharCode(0x80 | ((c >> 0) & 0x3F));
			}
		}
		return out;
	}

	function qr() {
		$("#qrPic").empty();
		var str = toUtf8($("#codeUrl").val());

		$("#qrPic").qrcode({
			render : "canvas",
			width : 200,
			height : 200,
			text : str
		});
	}
</script>
</head>
<body>
  <div id="main">
    <div class="demo">
      <li>
              <label class="text_tit">二维码图片链接：</label>
                <input type="text" id="codeUrl" class="input_text" />
            </li>
      <p>
        <input type="button" id="btn" onclick="qr()" value="生成二维码">
      </p>
      <div id="qrPic"></div>
    </div>
  </div>
</body>
</html>
