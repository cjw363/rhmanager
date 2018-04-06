<%@ page contentType="text/html; charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<% String name = request.getParameter("name");
    String id = request.getParameter("id");
    String token = request.getParameter("token");
%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <%@include file="../resource/inc/incCss.jsp" %>
    <%@include file="../resource/inc/incJs.jsp" %>
    <style type="text/css">
        body {
            background-color: #fff;
        }
        .box-content{
            width: 80%;
            margin-left: 10%;
            margin-right: 10%;
        }
        .divider{
            width: 100%;
            height: 1px;
            background-color: #e6e6e6;
            margin-top: 10px;
            margin-bottom: 10px;
        }
        .font-18{
            font-size: 18px;
        }
        .font-16{
            font-size: 18px;
        }
        .bold{
            font-weight: bold;
        }
        .right{
            right: 0;
        }
    </style>
</head>
<div class="container">
    <h3 class="nav-title background-color">大学生出租屋租借平台
        <small>&nbsp;&nbsp;(管理系统)</small>
        <img class="protrait" src="${pageContext.request.contextPath}/resource/images/head_protrait.png">
        <span class="protrait name"></span>
    </h3>
    <div v-cloak id="detail" class="box-content">
        <h2 class="bold">{{data.title}}
            <small>{{data.time | formatDate}}</small>
        </h2>
        <div class="divider"></div>
        <img class="title-img img-responsive" v-bind:src="baseUrl+data.title_img">

        <h3 class="bold">房屋信息</h3>
        <div class="divider"></div>
        <div class="row">
            <span class="col-md-4 font-18" v-if="data.type===1">类型：整租</span>
            <span class="col-md-4 font-18" v-if="data.type===2">类型：单间</span>
            <span class="col-md-4 font-18" v-if="data.type===3">类型：日租</span>
            <span class="col-md-4 font-18" v-if="data.type===4">类型：办公</span>
            <span class="col-md-4 font-18" v-if="data.type===5">类型：校园</span>
            <span class="col-md-4 font-18" v-if="data.type===6">类型：其他</span>
            <span class="col-md-4 font-18">户型：{{data.house_type}}</span>
            <span class="col-md-4 font-18">租金：{{data.amount}}￥/月</span>
        </div>
        <div class="row">
            <span class="col-md-4 font-18">地点：{{data.location}}</span>
            <span class="col-md-4 font-18">面积：{{data.area}}平米</span>
            <span class="col-md-4 font-18">标签：{{data.label}}</span>
        </div>
        <h3 class="bold">房屋信息</h3>
        <div class="divider"></div>
        <p class="lead font-16">{{data.content}}</p>
        <h3 class="bold">留言区</h3>
        <div class="divider"></div>
    </div>
    <div v-cloak id="bbsList"  class="box-content">
        <div v-for="data in items">{{data.name}}：{{data.content}}<span class="right">{{data.time | formatDate}}</span></div>
    </div>
</div>
<body>
</body>
<script type="text/javascript">
    var name = '<%=name%>';
    var token = '<%=token%>';
    var id = '<%=id%>';
    $('.protrait.name').html(name);

    var vm = new Vue({
        el: "#detail",
        data: {
            data: [],
            searching: true,
            baseUrl: '${pageContext.request.contextPath}/rh/loadFile?token=' + token + '&filePath='
        },
        filters: {
            formatDate: function (time) {
                var data = new Date(time);
                return formatDate(data, 'yyyy-MM-dd hh:mm:ss');
            }
        }
    });

    var bbsVm = new Vue({
        el: "#bbsList",
        data: {
            items: [],
            searching: true,
            baseUrl: '${pageContext.request.contextPath}/rh/loadFile?token=' + token + '&filePath='
        },
        filters: {
            formatDate: function (time) {
                var data = new Date(time);
                return formatDate(data, 'yyyy-MM-dd hh:mm:ss');
            }
        }
    });

    ajaxDetail(id);
    function ajaxDetail(id) {
        var loading = myLoading();
        ajaxDataByPost({
            url: "${pageContext.request.contextPath}/rh/detailRent?token=" + token,
            data: {
                rent_id: id
            },
            success: function (data) {
                layer.close(loading);
                if (data.code == 1) {
                    vm.searching = false;
                    vm.data = data.result;
                } else {
                    var message = data.message;
                    myAlert("获取数据失败<br/>" + message);
                }
            }
        });
    }

    ajaxBBsList(id);
    function ajaxBBsList() {
        var loading = myLoading();
        ajaxDataByPost({
            url: "${pageContext.request.contextPath}/rh/bbsList?token=" + token,
            data: {
                rent_id: id
            },
            success: function (data) {
                layer.close(loading);
                if (data.code == 1) {
                    bbsVm.searching = false;
                    l(data)
                    myCopyArray(data.result, bbsVm.items);
                } else {
                    var message = data.message;
                    myAlert("获取数据失败<br/>" + message);
                }
            }
        });
    }
</script>
</html>