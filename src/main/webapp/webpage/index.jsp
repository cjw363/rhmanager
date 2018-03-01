<%@ page contentType="text/html; charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <%@include file="../resource/inc/incCss.jsp" %>
    <%@include file="../resource/inc/incJs.jsp" %>

    <style type="text/css">
        * {
            margin: 0;
            padding: 0;
        }

        html, body {
            height: 100%;
            width: 100%;
        }

        .container {
            height: 100%;
            width: 100%;
        }

        .background-color {
            background: #282537;
            background-image: -webkit-radial-gradient(top, circle cover, #3c3b52 0%, #252233 80%);
            background-image: -moz-radial-gradient(top, circle cover, #3c3b52 0%, #252233 80%);
            background-image: -o-radial-gradient(top, circle cover, #3c3b52 0%, #252233 80%);
            background-image: radial-gradient(top, circle cover, #3c3b52 0%, #252233 80%);
        }

        .nav-title, .nav-title small {
            color: white;
        }

        form.login {
            width: 60%;
            max-width: 450px;
            margin: auto;
            top: 0;
            left: 0;
            right: 0;
            bottom: 0;
        }

        button.login {
            width: 100%;
        }
    </style>
</head>
<body>
<div class="container background-color">
    <h3 class="nav-title">大学生出租屋租借平台
        <small>&nbsp;&nbsp;(管理系统)</small>
    </h3>
    <hr class="nav-divider"/>

    <form class="login">
        <div class="form-group">
            <input type="text" class="form-control" id="account" placeholder="账号">
        </div>
        <div class="form-group">
            <input type="password" class="form-control" id="password" placeholder="密码">
        </div>
        <button type="button" class="btn btn-primary login">登录</button>
    </form>
</div>
<script type="text/javascript">
    $('button.login').click(function () {
        var account = $('input#account').val().trim();
        var password = $('input#password').val().trim();
        if (!account)
            myAlert("账号不能为空");

        if (!password)
            myAlert("密码不能为空");

    });
</script>
</body>
</html>
