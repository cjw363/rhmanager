package com.cjw.rhmanager.spring;

import com.cjw.rhmanager.ehcache.Memory;
import com.cjw.rhmanager.utils.CommConst;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Administrator on 2017/12/27.
 */
public class RhLoginInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private Memory memory;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //如果是登录页面则放行
        if (request.getRequestURI().contains("/login")) {
            return true;
        }
        // 检查请求的token值是否为空
        String token = getTokenFromRequest(request);
        if (StringUtils.isEmpty(token)) {
            request.setAttribute("message", "Token不能为空");
        } else if (!memory.checkTokenOutTime(token)) {
            request.setAttribute("message", "Session已过期，请重新登录");
        } else {
//            ThreadTokenHolder.setToken(token); // 保存当前token，用于Controller层获取登录用户信息
            return true;//放行
        }
        request.getRequestDispatcher("/rh/login/unLogin").forward(request, response);
        return false;
    }

    /**
     * 从请求信息中获取token值
     */
    private String getTokenFromRequest(HttpServletRequest request) {
        // 默认从header里获取token值
        String token = request.getHeader(CommConst.TOKEN);
        if (StringUtils.isEmpty(token)) {
            // 从请求信息中获取token值
            token = request.getParameter(CommConst.TOKEN);
        }
        return token;
    }
}
