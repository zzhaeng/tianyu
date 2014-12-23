<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
request.setCharacterEncoding("UTF-8");
String htmlData = request.getParameter("content1") != null ? request.getParameter("content1") : "";
%>
<!doctype html>
<html>
<head>
	<meta charset="utf-8" />
	<title>KindEditor JSP</title>
	<link rel="stylesheet" href="../themes/default/default.css" />
	<script charset="utf-8" src="../kindeditor.js"></script>
	<script>
			KindEditor.ready(function(K) {
				var uploadbutton = K.uploadbutton({
					button : K('#uploadButton')[0],
					fieldName : 'imgFile',
					url : '../jsp/upload_json.jsp',//文件加参数dir=file
					afterUpload : function(data) {
						if (data.error === 0) {
							var url = K.formatUrl(data.url, 'absolute');
							K('#url').val(url);
							K('#divimgurl').html("");
							K('#divimgurl').append('<img class="img" src="' +url+ '"/>');
						} else {
							alert(data.message);
						}
					},
					afterError : function(str) {
						alert('自定义错误信息: ' + str);
					}
				});
				uploadbutton.fileBox.change(function(e) {
					uploadbutton.submit();
				});
			});
	</script>
</head>
<body>
			<input class="ke-input-text" type="text" id="url" value="" readonly="readonly" /> <input type="button" id="uploadButton" value="Upload" />
		<div id="divimgurl"></div>
</body>
</html>
 