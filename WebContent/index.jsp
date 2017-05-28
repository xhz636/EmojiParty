<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>表情包派对</title>
    <link rel="stylesheet" type="text/css" href="/EmojiParty/stylesheets/iconfont.css">
    <link rel="stylesheet" type="text/css" href="/EmojiParty/stylesheets/common.css">
    <link rel="stylesheet" type="text/css" href="/EmojiParty/stylesheets/index.css">
</head>
<body>

<div class="index-container">
    <div class="logo-block">
        <img src="/EmojiParty/images/logo.png">
        <span class="logo-title">表情包派对</span>
    </div>
    <form action="/EmojiParty/servlet/SearchController" method="get" class="search-block">
        <input type="text" name="keywords" placeholder="请输入表情包关键字(以,分割 超过32字节的关键字无效)" class="search-input"><!--
        --><button type="submit" title="搜索" class="search-btn">
            <i class="iconfont icon-sousuo-sousuo"></i>
        </button>
    </form>
    <button id="id-dialog-btn" title="上传" class="dialog-btn">
        <i class="iconfont icon-uploading"></i>
    </button>
</div>

<div id="id-dialog" style="display: none" class="dialog-container">
    <div id="id-upload" class="upload-block">
        <form action="/EmojiParty/servlet/UploadController" method="post" enctype="multipart/form-data" class="upload-form">
            <button type="button" onclick="document.getElementById('id-upload-file').click()" title="选择文件" class="selectfile-btn">
                <i class="iconfont icon-xuanzewenjian"></i>
                <input id="id-upload-file" type="file" name="uploadfile" accept="image/gif, image/jpeg, image/png, image/bmp" style="display: none"/>
            </button><!--
            --><input id="id-filepath" type="text" placeholder="只能上传图片,大小不超过1MB" readonly="readonly" class="filepath-show"/>
            <input name="keywords" placeholder="请输入表情包关键字(以,分割 超过32字节的关键字无效)" class="upload-input">
            <br/>
            <button type="submit" title="上传" class="upload-btn">
                <i class="iconfont icon-uploading"></i>
            </button>
        </form>
    </div>
</div>

<% if (request.getAttribute("message") != null) { %>
<%     if (request.getAttribute("message").equals("succeed")) { %>
<script>alert("表情包上传成功!")</script>
<%     } else if (request.getAttribute("message").equals("keyword")) { %>
<script>alert("请输入有效的关键词!")</script>
<%     } else if (request.getAttribute("message").equals("exist")) { %>
<script>alert("您上传的表情包已经存在!")</script>
<%     } else if (request.getAttribute("message").equals("format")) { %>
<script>alert("表情包图片格式不正确!")</script>
<%     } else if (request.getAttribute("message").equals("file")) { %>
<script>alert("请选择有效的表情包图片!")</script>
<%     }
   }
%>

<script>
    document.getElementById("id-upload-file").onchange = function() {
        var str = this.value.replace(/\\/g, "/");
        var index = str.lastIndexOf("/");
        document.getElementById("id-filepath").value = this.value.substr(index + 1);
    }
    document.getElementById("id-dialog-btn").onclick = function() {
        document.getElementById("id-dialog").style = "";
    }
    document.getElementById("id-upload").onclick = function(event) {
        event.stopPropagation();
    }
    document.getElementById("id-dialog").onclick = function() {
        document.getElementById("id-dialog").style = "display: none";
    }
</script>

</body>
</html>