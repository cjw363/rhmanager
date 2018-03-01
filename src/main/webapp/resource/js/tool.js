function ajaxDataByPost(jsonObj) {
    var url = jsonObj.url;
    var data = jsonObj.data;
    var success = jsonObj.success;
    var complete = jsonObj.complete;
    var error = jsonObj.error;
    var xhr = $.ajax({
        type: "POST",
        url: url,
        data: data,
        async: true,
        success: success,
        complete: complete,
        error: error
    });
    return xhr;
}

//循环中断队列中的请求
function abortAjax(queue) {
    var list = queue.quere();
    while (list.length != 0) {
        var xhr = list.pop();
        if (xhr.readyState != 4) {//未完成
            xhr.abort();
        }
    }
}

function myCloneObj(srcObj, targetObj) {
    for (k in srcObj) {
        targetObj[k] = srcObj[k];
    }
}

function myCopyArray(srcObj, targetObj) {
    targetObj.splice(0, targetObj.length);
    for (var index = 0; index < srcObj.length; index++) {
        targetObj.push(srcObj[index]);
    }
}

function myClearObj(targetObj) {
    for (var i in targetObj) {
        delete targetObj[i];
    }
}

function mySetPageCur(srcObj, toPage) {
    var hasPageCur = 0;
    for (var index = 0; index < srcObj.length; index++) {
        if (srcObj[index].name == "pageCur") {
            srcObj[index].value = toPage;
            hasPageCur++;
        }
    }
    if (hasPageCur == 0) {
        var pageObj = {};
        pageObj.name = "pageCur";
        pageObj.value = toPage || 1;
        srcObj.push(pageObj);
    }
    return srcObj;
}

function myAlert(msg) {
    layer.open({
        content: msg,
        time: 3
    });
}

//提示
function myTip(msg) {
    layer.open({
        content: msg,
        skin: 'msg',
        time: 3
    });
}

function myParentLoading() {
    var loading = parent.layer.open({
        type: 2
    });
    return loading;
}

function myLoading() {
    var loading = layer.open({
        type: 2,
        shadeClose: true
    });
    return loading;
}

//loading带文字
function myLoading2(content) {
    var loading = layer.open({
        type: 2,
        content: content,
        shadeClose: true
    });
    return loading;
}

function myLoadingWithTime(time) {
    var loading = layer.open({
        type: 2,
        time: time,
        shadeClose: false
    });
    return loading;
}

function myDialog1(content) {
    layer.open({
        anim: 'up',
        content: content,
        btn: ['确认', '取消']
    });
}

function myDialog2(title, content) {
    layer.open({
        title: title,
        anim: 'up',
        content: content,
        btn: ['确认', '取消']
    });
}

function myDialog3(title, content, btnName) {
    layer.open({
        title: title,
        anim: 'up',
        content: content,
        btn: [btnName, '取消']
    });
}

function myDialog4(content) {
    layer.open({
        anim: 'up',
        content: content,
        btn: ['关闭']
    });
}

function mySiderPanel(html) {
    var siderPanel = layer.open({
        type: 1,
        content: html,
        anim: 'right',
        style: 'position:fixed;right:0;top:0;width:80%;height:100%;border:none;-webkit-animation-duration:10.5s;animation-duration:10.5s;'
    });
    return siderPanel;
}

function myPageDialog(url) {
    var height = $(window).height();
    var pageDialog = layer.open({
        type: 1,
        content: '<iframe width="100%" height="' + height + '" frameborder="0" src="' + url + '"></iframe>',
        anim: 'right',
        style: 'position:fixed;right:0;top:0;width:100%;height:100%;border:none;-webkit-animation-duration:10.5s;animation-duration:10.5s;'
    });
    return pageDialog;
}

function listenHistory(pageDialog) {
    history.pushState({pageDialogUrl: "true"}, "");
    window.addEventListener("popstate", function (e) {
        layer.close(pageDialog);
    }, false);
}

function myPageDialogByParent(url) {
    var height = $(parent.window).height();
    var pageDialog = parent.layer.open({
        type: 1,
        content: '<iframe width="100%" height="' + height + '" frameborder="0" src="' + url + '"></iframe>',
        anim: 'right',
        style: 'position:fixed;right:0;top:0;width:100%;height:100%;border:none;-webkit-animation-duration:10.5s;animation-duration:10.5s;'
    });
    return pageDialog;
}

function listenHistoryByParent(pageDialog) {
    history.pushState({pageDialogUrl: "true"}, "");
    window.addEventListener("popstate", function (e) {
        parent.layer.close(pageDialog);
    }, false);
}

function MyDataGrid(listVm, jsonObject, viewModel) {
    var _this = this;
    var pageCur = 1;
    var pageNav;
    if (viewModel) {
        pageNav = new Vue({
            el: viewModel,
            data: {
                pageCur: 1,
                pageMax: 1,
                totalNum: 1,
                prevEnable: "",
                nextEnable: "",
                pageNavLinkArray: []
            },
            methods: {
                navClick: function (page) {
                    _this.query(page);
                },
                homeNavClick: function () {
                    _this.query(1);
                },
                prevNavClick: function () {
                    var toPage = pageNav.pageCur - 1;
                    if (toPage >= 1) {
                        _this.query(toPage);
                    }
                },
                nextNavClick: function () {
                    var toPage = pageNav.pageCur + 1;
                    if (toPage <= pageNav.pageMax) {
                        _this.query(toPage);
                    }
                },
                lastNavClick: function () {
                    var toPage = pageNav.pageMax;
                    _this.query(toPage);
                }
            }
        });
    }

    this.query = function (toPage) {
        pageCur = toPage || pageCur;
        var waitDialog = myLoading();
        var toPostData = jsonObject.data || [];
        toPostData = mySetPageCur(toPostData, toPage);
        ajaxDataByPost({
            url: jsonObject.url,
            data: toPostData,
            success: function (data) {
                if (data && data["pageList"]) {
                    myCopyArray(data["pageList"], listVm.items);
                    listVm.searching = false;
                    if (pageNav) {
                        pageNav.totalNum = data.totalNum;
                        pageNav.pageCur = data.pageCur;
                        pageNav.pageMax = data.pageMax;
                        pageNav.prevEnable = pageNav.pageCur == 1 ? "disabled" : "";
                        pageNav.pageNavLinkArray = [];
                        for (var i = data.pageCur - 2; i <= data.pageCur + 2; i++) {
                            if (i >= 1 && i <= data.pageMax) {
                                pageNav.pageNavLinkArray.push({
                                    active: data.pageCur == i ? "active" : "",
                                    page: i
                                });
                            }
                        }
                        pageNav.nextEnable = (data.pageCur + 1 > data.pageMax) ? "disabled" : "";
                    }
                    if (jsonObject.success) {
                        jsonObject.success(data)
                    }
                } else {
                    myAlert("操作异常");
                }
                layer.close(waitDialog);
            }
        });
    };
}

function closeParentLayer() {
    var index = parent.layer.getFrameIndex(window.name);
    parent.layer.close(index);
}

//添加cookie
function addCookie(name, value, expiresHours) {
    var cookieString = name + "=" + escape(value);
    if (expiresHours > 0) {
        var date = new Date();
        date.setTime(date.getTime() + expiresHours * 1000);
        cookieString = cookieString + ";expires=" + date.toUTCString();
    }
    document.cookie = cookieString;
}

//修改cookie的值
function editCookie(name, value, expiresHours) {
    var cookieString = name + "=" + escape(value);
    if (expiresHours > 0) {
        var date = new Date();
        date.setTime(date.getTime() + expiresHours * 1000);
        cookieString = cookieString + ";expires=" + date.toGMTString();
    }
    document.cookie = cookieString;
}

//根据名字获取cookie的值
function getCookieValue(name) {
    var strCookie = document.cookie;
    var arrCookie = strCookie.split("; ");
    for (var i = 0; i < arrCookie.length; i++) {
        var arr = arrCookie[i].split("=");
        if (arr[0] == name) {
            return unescape(arr[1]);
            break;
        } else {
            return "";
            break;
        }
    }
}

function nomore() {
    myTip("没有更多数据了");
}

//单选按钮
function radioClick(event) {
    $(event.target).toggleClass("selected").siblings().removeClass("selected");
}

//多选按钮
function checkClick(event) {
    $(event.target).toggleClass("selected");
}

(function ($) {
    $.fn.serializeJson = function () {
        var serializeObj = {};
        var array = this.serializeArray();
        $(array).each(function () {
            if (serializeObj[this.name]) {
                if ($.isArray(serializeObj[this.name])) {
                    serializeObj[this.name].push(this.value);
                } else {
                    serializeObj[this.name] = [serializeObj[this.name], this.value];
                }
            } else if (this.value) {
                serializeObj[this.name] = this.value;
            }
        });
        for (k in serializeObj) {
            if ($.isArray(serializeObj[k])) {
                serializeObj[k] = serializeObj[k].join(",");
            }
        }
        return serializeObj;
    };
})(jQuery);

/*!
 * jQuery Pagination Plugin
 * http://mricle.com/JqueryPagination
 * GitHub: https://github.com/mricle/pagination
 * Version: 1.2.7
 * Date: 2015-6-12
 * 
 * Copyright 2015 Mricle
 * Released under the MIT license
 */

!function ($) {
    "use strict";

    var pageEvent = {
        pageClicked: 'pageClicked',
        jumpClicked: 'jumpClicked',
        pageSizeChanged: 'pageSizeChanged'
    }

    var Page = function (element, options) {
        var defaultOption = {
            pageSize: 10,
            pageBtnCount: 11,
            showFirstLastBtn: true,
            firstBtnText: null,
            lastBtnText: null,
            prevBtnText: "&laquo;",
            nextBtnText: "&raquo;",
            loadFirstPage: true,
            isparent: false,
            remote: {
                url: null,
                params: null,
                callback: null,
                success: null,
                beforeSend: null,
                complete: null,
                pageIndexName: 'pageCur',
                pageSizeName: 'pageSize',
                totalName: 'totalNum'
            },
            showInfo: false,
            infoFormat: '{start} ~ {end} of {total} entires',
            showJump: false,
            jumpBtnText: 'Go',
            showPageSizes: false,
            pageSizeItems: null,
            debug: false
        }
        this.$element = $(element);
        this.$page = $('<ul class="pagination-page"></ul>');
        this.$size = $('<div class="pagination-size"></div>');
        this.$jump = $('<div class="pagination-jump"></div>');
        this.$info = $('<div class="pagination-info"></div>');
        this.options = $.extend(true, {}, defaultOption, $.fn.page.defaults, options);
        this.total = this.options.total || 0;
        this.options.pageSizeItems = this.options.pageSizeItems || [5, 10, 15, 20],
            this.currentPageIndex = 0;
        this.currentPageSize = this.options.pageSize;
        this.pageCount = getPageCount(this.total, this.currentPageSize);

        if (this.options.remote.success == null) {
            this.options.remote.success = this.options.remote.callback;
        }

        var init = function (obj) {
            var that = obj;

            //init size module
            var html = $('<select data-page-btn="size"></select>');
            for (var i = 0; i < that.options.pageSizeItems.length; i++) {
                html.append('<option value="' + that.options.pageSizeItems[i] + '">' + that.options.pageSizeItems[i] + '</option>')
            }
            html.val(that.currentPageSize);
            that.$size.append(html);

            //init jump module
            var jumpHtml = '<div class="m-pagination-group"><input type="text"><button data-page-btn="jump" type="button">' + that.options.jumpBtnText + '</button></div>';
            that.$jump.append(jumpHtml);
            that.$jump.find('input').change(function () {
                if (!checkIsPageNumber(this.value, that.pageCount))
                    this.value = null;
            });
            that.$element.append(that.$page.hide());
            that.$element.append(that.$size.hide());
            that.$element.append(that.$jump.hide());
            that.$element.append(that.$info.hide());
            that._remoteOrRedner(1);
            that.$element
                .on('click', {page: that}, function (event) {
                    eventHandler(event);
                })
                .on('change', {page: that}, function (event) {
                    eventHandler(event);
                });
        }

        var eventHandler = function (event) {
            var that = event.data.page;
            var $target = $(event.target);
            if (event.type === 'click' && $target.data('pageIndex') !== undefined && !$target.parent().hasClass('active')) {
                var pageIndex = $(event.target).data("pageIndex");
                that.$element.trigger(pageEvent.pageClicked, pageIndex);
                that.debug('event[ pageClicked ] : pageIndex = ' + (pageIndex));
                that._remoteOrRedner(pageIndex);
            } else if (event.type === 'click' && $target.data('pageBtn') === 'jump') {
                var pageIndexStr = that.$jump.find('input').val();
                if (checkIsPageNumber(pageIndexStr, that.pageCount)) {
                    var pageIndex = pageIndexStr - 1;
                    that.$element.trigger(pageEvent.jumpClicked, pageIndex);
                    that.debug('event[ jumpClicked ] : pageIndex = ' + (pageIndex));
                    that._remoteOrRedner(pageIndex);
                }
                that.$jump.find('input').val(null);
            } else if (event.type === 'change' && $target.data('pageBtn') === 'size') {
                var pageSize = that.$size.find('select').val();
                that.currentPageSize = pageSize;
                that.$element.trigger(pageEvent.pageSizeChanged, pageSize);
                that.debug('event[ pageSizeChanged ] : pageSize = ' + pageSize);
                that._remoteOrRedner(0);
            }
        }

        if (typeof this.options.total === 'undefined' && this.options.remote.url === null) {
            console && console.error("[init error] : the options must have the parameter of 'remote.url' or 'total'.");
        }
        else if (typeof this.options.total === 'undefined' && !this.options.loadFirstPage) {
            console && console.error("[init error] : if you don't remote the first page. you must set the options or 'total'.");
        }
        else {
            init(this);
        }
    }

    Page.prototype = {
        _remoteOrRedner: function (pageIndex) {
            if (this.options.remote.url != null && (this.options.loadFirstPage || pageIndex > 0))
                this.remote(pageIndex);
            else
                this.renderPagination(pageIndex);
        },
        remote: function (pageIndex, params) {
            var waitDialog;
            var _parent = this.options.isparent;
            if (_parent)
                waitDialog = myParentLoading();
            else
                waitDialog = myLoading();

            var that = this;
            if (isNaN(parseInt(pageIndex)) || typeof pageIndex === "object") {
                params = pageIndex;
                pageIndex = null;
            }
            if (isNaN(parseInt(pageIndex))) {
                pageIndex = that.currentPageIndex;
            }
            var pageParams = {};
            pageParams[this.options.remote.pageIndexName] = pageIndex;
            pageParams[this.options.remote.pageSizeName] = this.currentPageSize;
            this.options.remote.params = deserializeParams(this.options.remote.params);
            if (params) {
                params = deserializeParams(params);
                //this.options.remote.params = $.extend({}, this.options.remote.params, params);
                this.options.remote.params = params;
            }
            var requestParams = $.extend({}, this.options.remote.params, pageParams);

            $.ajax({
                type: "post",
                url: this.options.remote.url,
                dataType: 'json',
                data: requestParams,
                async: true,
                beforeSend: function (XMLHttpRequest) {
                    if (typeof that.options.remote.beforeSend === 'function') that.options.remote.beforeSend(XMLHttpRequest);
                },
                complete: function (XMLHttpRequest, textStatu) {
                    if (_parent)
                        parent.layer.close(waitDialog);
                    else
                        layer.close(waitDialog);

                    var message = XMLHttpRequest.responseText;
                    if (message && message.indexOf("AUTH_403_EXCEPTION") > 0) {
                        myAlert("微信授权登录已失效，重新授权");
                        window.location = "/mipwx/wxqy/auth";
                    }
                    if (typeof that.options.remote.complete === 'function') that.options.remote.complete(XMLHttpRequest, textStatu);
                },
                success: function (result) {
                    that.debug("ajax request : params = " + JSON.stringify(requestParams), result);
                    var total = GetCustomTotalName(result, that.options.remote.totalName);
                    if (total == null || total == undefined) {
                        console && console.error("the response of totalName :  '" + that.options.remote.totalName + "'  not found");
                    } else {
                        that._updateTotal(total);
                        if (typeof that.options.remote.success === 'function') that.options.remote.success(result, pageIndex);
                        that.renderPagination(pageIndex);
                    }
                }
            })
        },
        renderPagination: function (pageIndex) {
            this.currentPageIndex = pageIndex;
            var pages = renderPages(this.currentPageIndex, this.currentPageSize, this.total, this.options.pageBtnCount,
                this.options.firstBtnText, this.options.lastBtnText, this.options.prevBtnText, this.options.nextBtnText, this.options.showFirstLastBtn);
            this.$page.empty().append(pages);
            this.$info.text(renderInfo(this.currentPageIndex, this.currentPageSize, this.total, this.options.infoFormat));
            if (this.pageCount > 1) {
                this.$page.show();
                if (this.options.showPageSizes) this.$size.show();
                if (this.options.showJump) this.$jump.show();
                if (this.options.showInfo) this.$info.show();
            }
            else if (this.pageCount == 1) {
                if (this.options.showInfo) this.$info.show();
            }
            else {
                this.$page.hide();
                this.$size.hide();
                this.$jump.hide();
                this.$info.hide();
            }
        },
        _updateTotal: function (total) {
            this.total = total;
            this.pageCount = getPageCount(total, this.currentPageSize);
        },
        destroy: function () {
            this.$element.unbind().data("page", null).empty();
        },
        debug: function (message, data) {
            if (this.options.debug && console) {
                message && console.info(message);
                data && console.info(data);
            }
        }
    }

    var renderInfo = function (currentPageIndex, currentPageSize, total, infoFormat) {
        var startNum = (currentPageIndex * currentPageSize) + 1;
        var endNum = (currentPageIndex + 1) * currentPageSize;
        endNum = endNum >= total ? total : endNum;
        return infoFormat.replace('{start}', startNum).replace('{end}', endNum).replace('{total}', total);
    }
    var renderPages = function (pageIndex, pageSize, total, pageBtnCount, firstBtnText, lastBtnText, prevBtnText, nextBtnText, showFirstLastBtn) {
        pageIndex = pageIndex == undefined ? 1 : parseInt(pageIndex);      //set pageIndex from 1， convenient calculation page
        var pageCount = getPageCount(total, pageSize);
        var html = [];

        if (/*pageCount <= pageBtnCount*/false) {
            html = renderPage(1, pageCount, pageIndex);
        } else {
            var firstPage, prevPage, nextPage, lastPage;
            if (pageIndex <= 1) {
                firstPage = renderPerPageEnabled(firstBtnText || 1);
                prevPage = renderPerPageEnabled(prevBtnText);
            } else {
                firstPage = renderPerPage(firstBtnText || 1, 1);
                prevPage = renderPerPage(prevBtnText, pageIndex - 1);
            }
            if (pageIndex >= pageCount) {
                nextPage = renderPerPageEnabled(nextBtnText);
                lastPage = renderPerPageEnabled(lastBtnText || pageCount);
            } else {
                nextPage = renderPerPage(nextBtnText, pageIndex + 1);
                lastPage = renderPerPage(lastBtnText || pageCount, pageCount);
            }
            //button count of  both sides
            var symmetryBtnCount = (pageBtnCount - 1 - 4) / 2;
            if (!showFirstLastBtn)
                symmetryBtnCount = symmetryBtnCount + 1;
            var frontBtnNum = (pageBtnCount + 1) / 2;
            var behindBtnNum = pageCount - ((pageBtnCount + 1) / 2);

            symmetryBtnCount = symmetryBtnCount.toString().indexOf('.') == -1 ? symmetryBtnCount : symmetryBtnCount + 0.5;
            frontBtnNum = frontBtnNum.toString().indexOf('.') == -1 ? frontBtnNum : frontBtnNum + 0.5;
            behindBtnNum = behindBtnNum.toString().indexOf('.') == -1 ? behindBtnNum : behindBtnNum + 0.5;
            /* 手机端使用时，注释掉了该段逻辑
            if (pageIndex <= frontBtnNum) {
                if (showFirstLastBtn) {
                    html = renderPage(1, pageBtnCount - 2, pageIndex);
                    html.push(nextPage);
                    html.push(lastPage);
                } else {
                    html = renderPagenderPage(1, pageBtnCount - 1, pageIndex);
                    html.push(nextPage);
                }
            }else if (pageIndex > behindBtnNum) {
                if (showFirstLastBtn) {
                    html = renderPage(pageCount - pageBtnCount + 3, pageBtnCount - 2, pageIndex);
                    html.unshift(prevPage);
                    html.unshift(firstPage);
                } else {
                    html = renderPage(pageCount - pageBtnCount + 2, pageBtnCount - 1, pageIndex);
                    html.unshift(prevPage);
                }
            }else {
                if (showFirstLastBtn) {
                    html = renderPage(pageIndex - symmetryBtnCount, pageBtnCount - 4, pageIndex);
                    html.unshift(prevPage);
                    html.push(nextPage);
                    html.unshift(firstPage);
                    html.push(lastPage);
                } else {
                    html = renderPage(pageIndex - symmetryBtnCount, pageBtnCount - 2, pageIndex);
                    html.unshift(prevPage);
                    html.push(nextPage);
                }
            }*/
            ///手机端使用时，添加了该段逻辑
            if (showFirstLastBtn) {
                html = renderPage(pageIndex - symmetryBtnCount, pageBtnCount - 4, pageIndex);
                html.unshift(prevPage);
                html.push(nextPage);
                html.unshift(firstPage);
                html.push(lastPage);
            } else {
                html = renderPage(pageIndex - symmetryBtnCount, pageBtnCount - 2, pageIndex);
                html.unshift(prevPage);
                html.push(nextPage);
            }
        }
        return html;
    }
    var renderPage = function (beginPageNum, count, currentPage) {
        var html = [];
        for (var i = 0; i < count; i++) {
            var page = renderPerPage(beginPageNum, beginPageNum);
            if (beginPageNum == currentPage)
                page.addClass("active");
            html.push(page);
            beginPageNum++;
        }
        return html;
    }
    var renderPerPage = function (text, value) {
        return $("<li><a class='waves-effect waves-block waves-float' data-page-index='" + value + "'>" + text + "</a></li>");
    }
    var renderPerPageEnabled = function (text) {
        return $("<li><a class='waves-effect waves-block waves-float' href='javascript:nomore()'>" + text + "</a></li>");
    }
    var getPageCount = function (total, pageSize) {
        var pageCount = 0;
        var total = parseInt(total);
        var i = total / pageSize;
        pageCount = i.toString().indexOf('.') != -1 ? parseInt(i.toString().split('.')[0]) + 1 : i;
        return pageCount;
    }
    var deserializeParams = function (params) {
        var newParams = {};
        if (typeof params === 'string') {
            var arr = params.split('&');
            for (var i = 0; i < arr.length; i++) {
                var a = arr[i].split('=');
                newParams[a[0]] = decodeURIComponent(a[1]);
            }
        }
        else if (params instanceof Array) {
            for (var i = 0; i < params.length; i++) {
                newParams[params[i].name] = decodeURIComponent(params[i].value);
            }
        }
        else if (typeof params === 'object') {
            newParams = params;
        }
        return newParams;
    }
    var checkIsPageNumber = function (pageIndex, maxPage) {
        var reg = /^\+?[1-9][0-9]*$/;
        return reg.test(pageIndex) && parseInt(pageIndex) <= parseInt(maxPage);
    }
    var GetCustomTotalName = function (object, totalName) {
        var arr = totalName.split('.');
        var temp = object;
        var total = null;
        for (var i = 0; i < arr.length; i++) {
            temp = mapObjectName(temp, arr[i]);
            if (!isNaN(parseInt(temp))) {
                total = temp;
                break;
            }
            if (temp == null) {
                break;
            }
        }
        return total;
    }
    var mapObjectName = function (data, mapName) {
        for (var i in data) {
            if (i == mapName) {
                return data[i];
            }
        }
        return null;
    }

    $.fn.page = function (option) {
        var args = arguments;
        return this.each(function () {
            var $this = $(this);
            var data = $this.data('page');
            if (!data && (typeof option === 'object' || typeof option === 'undefined')) {
                var options = typeof option == 'object' && option;
                var data_api_options = $this.data();
                options = $.extend(options, data_api_options);
                $this.data('page', (data = new Page(this, options)));
            }
            else if (data && typeof option === 'string') {
                data[option].apply(data, Array.prototype.slice.call(args, 1));
            }
            else if (!data) {
                console && console.error("jQuery Pagination Plugin is uninitialized.");
            }
        });
    }
}(window.jQuery)

/*分页默认配置*/
$.fn.page.defaults = {
    pageSize: 10,
    pageBtnCount: 5,
    firstBtnText: '首页',
    prevBtnText: '上一页',
    nextBtnText: '下一页',
    lastBtnText: '尾页'
};

//获取当前日期：“yyyy-MM-dd HH:MM:SS”,字符串格式
function getNowFormatDate(m) {
    var date = new Date();
    var seperator1 = "-";
    var seperator2 = ":";
    var year = date.getFullYear();
    var month = date.getMonth() + 1;
    var strDate = date.getDate();
    var hours = date.getHours();
    var min = date.getMinutes();
    var ss = date.getSeconds();

    if (m) {
        if (month >= 1 && month <= m) {
            month = 12 + month - m;
            year = year - 1;
        } else {
            month = month - m;
        }
    }

    if (month >= 1 && month <= 9) {
        month = "0" + month;
    }
    if (strDate >= 0 && strDate <= 9) {
        strDate = "0" + strDate;
    }
    if (hours >= 0 && hours <= 9) {
        hours = "0" + hours;
    }
    if (min >= 0 && min <= 9) {
        min = "0" + min;
    }
    if (ss >= 0 && ss <= 9) {
        ss = "0" + ss;
    }
    var currentdate = year + seperator1 + month + seperator1 + strDate
        + " " + hours + seperator2 + min + seperator2 + ss;
    return currentdate;
}

//日期转时间戳
function dateToStamp(stringTime) {
    var timestamp = Date.parse(new Date(stringTime)) / 1000;
    return timestamp.toString();
}

/**
 * [Queue]
 * @param {[Int]} size [队列大小]
 */
function Queue(size) {
    var list = [];
    //向队列中添加数据
    this.push = function (data) {
        if (data == null) {
            return false;
        }
        //如果传递了size参数就设置了队列的大小
        if (size != null && !isNaN(size)) {
            if (list.length == size) {
                this.pop();
            }
        }
        list.unshift(data);//unshift() 方法可向数组的开头添加一个或更多元素，并返回新的长度。
        return true;
    }
    //从队列中取出数据
    this.pop = function () {
        return list.pop();
    }
    //返回队列的大小
    this.size = function () {
        return list.length;
    }
    //返回队列的内容
    this.quere = function () {
        return list;
    }
}
