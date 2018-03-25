<%@ page contentType="text/html; charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<% String name = request.getParameter("name");
    String token = request.getParameter("token");
%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <%@include file="../resource/inc/incCss.jsp" %>
    <%@include file="../resource/inc/incJs.jsp" %>
    <style type="text/css">
        .title-img {
            width: 80px;
            height: 70px;
        }
    </style>
</head>
<body>
<div class="container">
    <h3 class="nav-title background-color">大学生出租屋租借平台
        <small>&nbsp;&nbsp;(管理系统)</small>
        <img class="protrait" src="${pageContext.request.contextPath}/resource/images/head_protrait.png">
        <p class="protrait name"></p>
    </h3>

    <div class="table-responsive">
        <table class="table">
            <thead>
            <tr>
                <th>ID</th>
                <th>主图</th>
                <th>标题</th>
                <th>内容</th>
                <th>状态</th>
                <th>时间</th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody v-cloak id="list">
            <tr v-for="data in items" v-on:click="onClickItem(data)">
                <th scope="row">{{data.id}}</th>
                <td>
                    <img class="title-img" v-bind:src="baseUrl+data.title_img">
                </td>
                <td>{{data.title}}</td>
                <td>{{data.content}}</td>
                <td>{{data.status}}</td>
                <td>{{data.time | formatDate}}</td>
                <td><a  v-on:click="onClickHandle(data,$event)">操作</a></td>
            </tr>
            </tbody>
        </table>
    </div>

    <div class="page-footer" id="pageNav"></div>
</div>
</div>
</body>
<script type="text/javascript">
    var name = '<%=name%>';
    var token = '<%=token%>';
    $('.protrait.name').html(name);

    var listVm = new Vue({
        el: "#list",
        data: {
            items: [],
            searching: true,
            baseUrl: '${pageContext.request.contextPath}/rh/loadFile?token=' + token + '&filePath='
        },
        methods: {
            onClickItem: function (data) {
                onClickItem(data);
            },
            onClickHandle: function (data,event) {
                event.stopPropagation();
                onClickHandle(data);
            }
        },
        filters: {
            formatDate: function (time) {
                var data = new Date(time);
                return formatDate(data, 'yyyy-MM-dd hh:mm:ss');
            }
        }
    });

    var page = $("#pageNav").page({
        remote: {
            url: "${pageContext.request.contextPath}/rh/campusList?token=" + token,
            success: function (data, pageCur) {
                if (data.success) {
                    pageCur = this.pageCur;
                    listVm.searching = false;
                    var pageList = data.pageList;
                    myCopyArray(pageList, listVm.items);
                } else {
                    var message = data.message;
                    myAlert("获取数据失败<br/>" + message);
                }
            }
        }
    });

    function onClickHandle(data) {
        l(data);
    }
</script>
</html>
