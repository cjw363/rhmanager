package com.cjw.rhmanager.utils;

import org.springframework.context.ApplicationContext;

public class CommConst {
    public static final String ACCESS_DENIED_403 = "SPRING_SECURITY_403_EXCEPTION";
    public static final String AUTHENTICATION_EXCEPTION = "SPRING_SECURITY_LAST_EXCEPTION";
    // 验证码
    public static final String SESSION_SECURITY_CODE = "sessionSecCode";
    // 分页条数配置路径
    public static final String PAGE = "resource/config/page.txt";
    // 不对匹配该值的访问路径拦截（正则）
    public static final String NO_INTERCEPTOR_PATH = ".*/((login)|(logout)|(code)|(static)|(websocket)).*";
    public static final String LOGIN_PATH = "/user/login";
    // 该值会在web容器启动时由WebAppContextListener初始化
    public static ApplicationContext WEB_APP_CONTEXT = null;

    // 接口组装时返回的分页和结果属性
    public static String RESULT_PAGE = "page";
    public static String RESULT_DATA = "data";

    public static final int RESULT_CODE_SUCCESS = 1;
    public static final int RESULT_CODE_FAIL = 0;
    //上传文件的地址
    public static final String UPLOAD_IMAGE = "E:/Proj/graduation/upload/uploadImage/";
    public static final String UPLOAD_VIDEO = "E:/Proj/graduation/upload/uploadVideo/";
    public static final String UPLOAD_AUDIO = "E:/Proj/graduation/upload/uploadAudio/";
    //上传临时存放地址，文件过大时
    public static final String UPLOAD_TEMP = "F:/upload/Temp/";
    //状态评价短信接口
    public static final String URL_SMS = "http://10.124.140.244/biz/kit/public_.do?action=sendSMS";

    public static final String TOKEN = "token";
}