package com.cjw.rhmanager.spring;

/**
 * Created by Administrator on 2018/1/4.
 */
public class ThreadTokenHolder {
    /**
     * 保存当前线程中的token
     */
    private static final ThreadLocal<String> THREAD_TOKEN = new ThreadLocal<String>();

    public static String getToken() {
        return THREAD_TOKEN.get();
    }

    public static void setToken(String token) {
        THREAD_TOKEN.set(token);
    }

    public static void clearToken() {
        THREAD_TOKEN.remove();
    }

}
