<%@ page contentType="text/html; charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <%@include file="../resource/inc/incCss.jsp" %>
    <%@include file="../resource/inc/incJs.jsp" %>

    <style type="text/css">
        form.login {
            width: 60%;
            max-width: 450px;
            margin: auto;
            top: 0;
            left: 0;
            right: 0;
            bottom: 0;
            margin-top: 20%;
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
        <img class="protrait" src="${pageContext.request.contextPath}/resource/images/head_protrait.png">
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
        var name = $('input#account').val().trim();
        var password = $('input#password').val().trim();
        var pattern = /^[0-9a-zA-Z]{4,12}$/;
        if (!name)
            myAlert("账号不能为空");
        else if (!password)
            myAlert("密码不能为空");
        else if (!pattern.test(name))
            myAlert("账号只能是数字或者字母的");
        else if (!pattern.test(password))
            myAlert("密码只能是数字或者字母");
        else
            ajaxLogin(name, password);
    });

    function ajaxLogin(name, password) {
        var loading = myLoading();
        ajaxDataByPost({
            url: "${pageContext.request.contextPath}/rh/login",
            data: {
                name: name,
                password: password
            },
            success: function (data) {
                layer.close(loading);
                if (data.code == 1) {
                    window.location.href = "webpage/main.jsp?name=" + data.result.name + "&token=" + data.result.token;
                }
            }
        });
    }
</script>
</body>
</html>
