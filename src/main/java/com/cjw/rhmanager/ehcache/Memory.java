package com.cjw.rhmanager.ehcache;

import com.cjw.rhmanager.entity.LoginUser;
import com.cjw.rhmanager.spring.TokenProcessor;
import com.cjw.rhmanager.utils.MD5Util;
import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

/**
 * Created by Administrator on 2018/1/4.
 */
@Component
public class Memory {

    @Autowired
    private Cache ehcache;

    /**
     * 关闭缓存管理器
     */
    @PreDestroy
    protected void shutdown() {
        if (ehcache != null) {
            ehcache.getCacheManager().shutdown();
        }
    }

    /**
     * 保存当前登录用户信息
     * timeToLiveSeconds -->当对象自从被存放到缓存中后，如果处于缓存中的时间超过了 timeToLiveSeconds属性值,这个对象就会过期，
     * EHCache将把它从缓存中清除；即缓存自创建日期起能够存活的最长时间，单位为秒(s)
     * <p>
     * timeToIdleSeconds -->  当对象自从最近一次被访问后，如果处于空闲状态的时间超过了timeToIdleSeconds属性值，这个对象就会过期，
     * EHCache将把它从缓存中清空；即缓存被创建后，最后一次访问时间到缓存失效之时，两者之间的间隔，单位为秒(s)
     */
    public LoginUser saveLoginUser(LoginUser loginUser) {
        // 生成seed和token值
        String seed = TokenProcessor.getInstance().generateSeed(loginUser.getName());
        String token = TokenProcessor.getInstance().generateToken(loginUser.getName(), loginUser.getPassword());
        // 保存token到登录用户中
        loginUser.setToken(token);
        // 清空之前的登录信息
        clearLoginInfo(seed);
        // 保存新的token和登录信息
        ehcache.put(new Element(seed, token, 60 * 30, 60 * 60));
        ehcache.put(new Element(token, loginUser, 60 * 30, 60 * 60));
        return loginUser;
    }

    /**
     * 获取当前线程中的用户信息
     */
    private LoginUser currentLoginUser(String token) {
        Element element = ehcache.get(token);
        return element == null ? null : (LoginUser) element.getObjectValue();
    }

    /**
     * 根据token检查用户是否登录
     */
    public boolean checkTokenOutTime(String token) {
        Element element = ehcache.get(token);
        return element != null && (LoginUser) element.getObjectValue() != null;
    }

    /**
     * 清空登录信息
     */
    public void clearLoginInfoByToken(String token) {
        LoginUser loginUser = currentLoginUser(token);
        if (loginUser != null) {
            // 根据登录的用户名生成seed，然后清除登录信息
            String seed = MD5Util.getMD5Code(loginUser.getName());
            clearLoginInfo(seed);
        }
    }

    /**
     * 根据seed清空登录信息
     */
    private void clearLoginInfo(String seed) {
        // 根据seed找到对应的token
        Element element = ehcache.get(seed);
        if (element != null) {
            // 根据token清空之前的登录信息
            ehcache.remove(seed);
            ehcache.remove(element.getObjectValue());
        }
    }
}