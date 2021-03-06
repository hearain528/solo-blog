<!DOCTYPE html>
<html>
<head>
    <meta />
    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://cdn.bootcss.com/bootstrap/4.0.0-beta.2/css/bootstrap.min.css" integrity="sha384-PsH8R72JQ3SOdhVi3uxftmaW6Vc51MKb0q5P2rRUpPvrszuE4W1povHYgTpBfshb" crossorigin="anonymous">
    <title>Node.js Web Server</title>
    <style type="text/css">
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
    </style>
</head>
<body>
<h1>PDF Viewer</h1><hr>
<div class="container">
    <table class="table">
        <thead>
        <tr>
            <th>文件名称</th>
            <th>文件大小</th>
            <th>上次预览页数</th>
            <th>操作</th>
            <th>
                <a href="/pdf-upload.do">
                    <button type="button" class="btn btn-primary">上传文件</button>
                </a>
            </th>
        </tr>
        </thead>
        <tbody id="box">
        </tbody>
    </table>
</div>
</body>
<script src="pdfview/static/jquery.slim.min.js"></script>
<script src="pdfview/static/popper.min.js"></script>
<script src="pdfview/static/bootstrap.min.js"></script>
<script src="pdfview/static/jquery-3.2.1.min.js"></script>
<script src="pdfview/static/index.js"></script>
</html>
