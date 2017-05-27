<%@ page import="beans.Image" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>表情包派对</title>
    <link rel="stylesheet" type="text/css" href="/EmojiParty/stylesheets/iconfont.css">
    <link rel="stylesheet" type="text/css" href="/EmojiParty/stylesheets/common.css">
    <link rel="stylesheet" type="text/css" href="/EmojiParty/stylesheets/show.css">
</head>
<body>

<div class="show-container">
    <div class="show-head-block">
        <a href="/EmojiParty/index.jsp"><img src="/EmojiParty/images/logo.png" class="logo-img"></a>
        <form action="/EmojiParty/servlet/SearchController" method="get" class="search-block">
            <input type="text" name="keywords" placeholder="请输入表情包关键字(多个关键字以英文,分割)" class="search-input"><!--
            --><button type="submit" title="搜索" class="search-btn">
            <i class="iconfont icon-sousuo-sousuo"></i>
        </button>
        </form>
        <button id="id-dialog-btn" title="上传" class="dialog-btn">
            <i class="iconfont icon-uploading"></i>
        </button>
    </div>
    <div class="emoji-container">
        <%
            List<Image> reuslt = (List<Image>) request.getAttribute("result");
            for (int i = 0; i < reuslt.size(); i++) {
        %>
        <div class="emoji-block">
            <div class="emoji-block-content">
                <span></span><img src="/EmojiParty/emoji/<%= reuslt.get(i).getID() + "." + reuslt.get(i).getFormat() %>">
            </div>
            <div class="emoji-block-footer">
                <form action="/EmojiParty/emoji/<%= reuslt.get(i).getID() + "." + reuslt.get(i).getFormat() %>" method="get" class="download-form">
                    <button title="下载" class="download-btn">
                        <i class="iconfont icon-xiazai"></i>
                    </button>
                </form>
                <span class="emoji-size"><%= reuslt.get(i).getWidth() %> x <%= reuslt.get(i).getHeight() %></span>
                <span class="emoji-time"><%= reuslt.get(i).getUploadtime().toString() %></span>
            </div>
        </div>
        <%
            }
            if (reuslt.size() == 0) {
        %>
        <span class="hint">对不起!找不到您所需要的表情包QAQ</span>
        <%
            }
        %>
    </div>
</div>

<div id="id-dialog" style="display: none" class="dialog-container">
    <div id="id-upload" class="upload-block">
        <form action="/EmojiParty/servlet/UploadController" method="post" enctype="multipart/form-data" class="upload-from">
            <button title="选择文件" class="selectfile-btn">
                <i class="iconfont icon-xuanzewenjian"></i>
                <input id="id-upload-file" type="file" name="uploadfile" accept="image/gif, image/jpeg, image/png, image/bmp"/>
            </button><!--
            --><input id="id-filepath" type="text" placeholder="只能上传图片,大小不超过1MB" readonly="readonly" class="filepath-show"/>
            <input name="keywords" placeholder="请输入表情包关键字(多个关键字以英文,分割)" class="upload-input">
            <br/>
            <button type="submit" title="上传" class="upload-btn">
                <i class="iconfont icon-uploading"></i>
            </button>
        </form>
    </div>
</div>

<script>
    document.getElementById("id-upload-file").onchange = function() {
        document.getElementById("id-filepath").value = this.value;
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