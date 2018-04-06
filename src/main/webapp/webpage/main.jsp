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
        body {
            background-color: #fff;
        }

        .title-img {
            width: 80px;
            height: 70px;
        }

        h1, h2, h3 {
            margin-top: 0;
        }

        .select_item {
            padding-bottom: 10px;
            padding-top: 10px;
        }

        .select_item:hover {
            background-color: rgb(0, 121, 194);
            color: #fff;
        }

        .limit-length {
            max-width: 300px;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
        }
    </style>
</head>
<body>
<div class="container">
    <h3 class="nav-title background-color">大学生出租屋租借平台
        <small>&nbsp;&nbsp;(管理系统)</small>
        <img class="protrait" src="${pageContext.request.contextPath}/resource/images/head_protrait.png">
        <span class="protrait name"></span>
    </h3>

    <div class="table-responsive">
        <table class="table">
            <thead>
            <tr>
                <th>ID</th>
                <th>预览图</th>
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
                    <img class="title-img img-rounded" v-bind:src="baseUrl+data.title_img">
                </td>
                <td class="limit-length">{{data.title}}</td>
                <td class="limit-length">{{data.content}}</td>
                <td>
                    <div v-if="data.status===0">审核中</div>
                    <div v-if="data.status===1">上架中</div>
                    <div v-if="data.status===2">审核未过</div>
                    <div v-if="data.status===3">主动下架</div>
                    <div v-if="data.status===4">违规下架</div>
                    <div v-if="data.status===5">已下架</div>
                </td>
                <td>{{data.time | formatDate}}</td>
                <td>
                    <Button class="btn btn-primary" v-on:click=" onClickOperate(data,$event)">操作</Button>
                </td>
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

    var pageCur;
    var layerDialog;

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
            onClickOperate: function (data, event) {
                event.stopPropagation();
                onClickOperate(data);
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
                    this.pageCur = pageCur;
                    listVm.searching = false;
                    myCopyArray(data.pageList, listVm.items);
                } else {
                    var message = data.message;
                    myAlert("获取数据失败<br/>" + message);
                }
            }
        }
    });

    function onClickItem(data) {
        var pageDialog = myPageDialog('detail.jsp?id=' + data.id + "&token=" + token + "&name=" + name);
        listenHistory(pageDialog);
    }

    //0 审核中，1 上架中，2 审核未过，3 主动下架，4 违规下架，5 已下架
    function getContentHtml(id, status) {
        var content;
        switch (status) {
            case 0:
                content =
                    '<div class="select_item" onclick="onClick(' + id + ', 2)">审核未过</div>' +
                    '<div class="select_item" onclick="onClick(' + id + ', 1)">审核通过</div>' +
                    '<div class="select_item" onclick="onDelete(' + id + ')">删除</div>';
                break;
            case 1:
                content =
                    '<div class="select_item" onclick="onClick(' + id + ', 5)">下架</div>' +
                    '<div class="select_item" onclick="onClick(' + id + ', 4)">违规下架</div>' +
                    '<div class="select_item" onclick="onDelete(' + id + ')">删除</div>';
                break;
            case 2:
                content =
                    '<div class="select_item" onclick="onClick(' + id + ', 1)">审核通过</div>' +
                    '<div class="select_item" onclick="onDelete(' + id + ')">删除</div>';
                break;
            case 3:
                content =
                    '<div class="select_item" onclick="onDelete(' + id + ')">删除</div>';
                break;
            case 4:
                content =
                    '<div class="select_item" onclick="onClick(' + id + ', 1)">上架</div>' +
                    '<div class="select_item" onclick="onDelete(' + id + ')">删除</div>';
                break;
            case 5:
                content =
                    '<div class="select_item" onclick="onClick(' + id + ', 1)">上架</div>' +
                    '<div class="select_item" onclick="onDelete(' + id + ')">删除</div>';
                break;
        }

        return content;
    }

    function onClick(id, newStatus) {
        ajaxUpdateStatus(id, newStatus);
        layer.close(layerDialog);
        page.page('remote', pageCur);
    }

    function onDelete(id) {
        ajaxDeleteRent(id);
        layer.close(layerDialog);
        page.page('remote', pageCur);
    }

    function ajaxUpdateStatus(id, status) {
        var loading = myLoading();
        ajaxDataByPost({
            url: "${pageContext.request.contextPath}/rh/updateStatusRent?token=" + token,
            data: {
                rent_id: id,
                status: status
            },
            success: function (data) {
                layer.close(loading);
                if (data.code == 1) {
                    myAlert("操作成功");
                }
            }
        });
    }
    function ajaxDeleteRent(id) {
        var loading = myLoading();
        ajaxDataByPost({
            url: "${pageContext.request.contextPath}/rh/deleteRent?token=" + token,
            data: {
                rent_id: id
            },
            success: function (data) {
                layer.close(loading);
                if (data.code == 1) {
                    myAlert("操作成功");
                }
            }
        });
    }

    function onClickOperate(data) {
        layerDialog = layer.open({
            title: [
                '请选择操作',
                'background-color: #FF4351; color:#fff;'
            ]
            , content: getContentHtml(data.id, data.status)
        });
    }

</script>
</html>
