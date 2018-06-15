<!DOCTYPE html>
<html>
<head>
<meta />
<title>Node.js Web Server</title>
<style type="text/css">
<!--
html,body {
    margin: 0;
    padding: 0;
    height: 100%;
    width: 100%;
    background: #fff;
    position: relative;
    font-family: "Arial Unicode MS" ;
    box-sizing: border-box;
}
#container {
    position: absolute;
    top:50%;
    left: 50%;
    margin-top: -150px;
    margin-left: -240px;
}
a img {
	border:none;
    display: block;
    margin: auto;
}
h1{
    text-align: center;
    margin: 0;
    padding: 1em 0 0;
    box-sizing: border-box;
    color: #8CC84C;
}
-->
</style>
</head>
<body>
<h1>PDF Viewer</h1><hr>
<div id="container">
<h3>文件上传：</h3>
选择一个文件上传: <br />
<form action="/file_pdf_upload.do" method="post" enctype="multipart/form-data">
<input type="file" name="pdf" size="150000000" />
<br />
<input type="submit" value="上传文件" />
</form>
</div>
</body>
</html>
