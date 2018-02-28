package com.cjw.rhmanager.controller;

import com.cjw.rhmanager.controller.base.BaseController;
import com.cjw.rhmanager.ehcache.Memory;
import com.cjw.rhmanager.entity.LoginUser;
import com.cjw.rhmanager.service.RentHouseService;
import com.cjw.rhmanager.utils.HandleEnum;
import com.cjw.rhmanager.utils.ParamData;
import com.cjw.rhmanager.utils.ResultData;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by Administrator on 2018/1/5.
 */
@Controller
@RequestMapping("/rh")
public class RhLoginController extends BaseController {
    @Autowired
    private RentHouseService rentHouseService;
    @Autowired
    private Memory memory;

    @ResponseBody
    @RequestMapping("/login")
    public ResultData<LoginUser> login(HttpServletRequest request) {
        try {
            //获取参数
            ParamData pd = this.paramDataInit();
            LoginUser user = rentHouseService.getUserInfo(pd);
            if (user != null) {
                LoginUser loginUser = memory.saveLoginUser(user);//保存用户信息到缓存
                return new ResultData<LoginUser>(HandleEnum.SUCCESS, loginUser);
            } else
                return new ResultData<LoginUser>(HandleEnum.FAIL, "登录失败");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultData<LoginUser>(HandleEnum.FAIL, e.getMessage());
        }

    }

    @ResponseBody
    @RequestMapping("/login/register")
    public ResultData<ParamData> register(HttpServletRequest request) {
        try {
            ParamData pd = this.paramDataInit();
            ParamData schoolPd = rentHouseService.getSchoolByName(pd);
            if (schoolPd != null) {
                pd.put("school_id", schoolPd.get("id"));
                if (rentHouseService.register(pd) > 0)

                    return new ResultData<ParamData>(HandleEnum.SUCCESS);
                else
                    return new ResultData<ParamData>(HandleEnum.FAIL, "用户名已被注册");
            } else {
                return new ResultData<ParamData>(HandleEnum.FAIL, "输入校园名有误");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultData<ParamData>(HandleEnum.FAIL, e.getMessage());
        }
    }

    @ResponseBody
    @RequestMapping("/login/out")
    public ResultData<ParamData> outLogin(HttpServletRequest request) {
        try {
            ParamData pd = this.paramDataInit();
            memory.clearLoginInfoByToken(pd.getString("token"));
            return new ResultData<ParamData>(HandleEnum.SUCCESS, "注销成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultData<ParamData>(HandleEnum.FAIL, e.getMessage());
        }
    }

    @ResponseBody
    @RequestMapping("/login/unLogin")
    public ResultData<ParamData> unLogin(HttpServletRequest request) {
        try {
            String message = (String) request.getAttribute("message");
            return new ResultData<ParamData>(HandleEnum.UNLOGIN, message);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultData<ParamData>(HandleEnum.FAIL, e.getMessage());
        }
    }

    @ResponseBody
    @RequestMapping("/login/schoolName")
    public ResultData<List<String>> school(HttpServletRequest request) {
        try {
            //获取参数
            ParamData pd = this.paramDataInit();
            List<String> result = rentHouseService.getSchoolName(pd);
            return new ResultData<List<String>>(HandleEnum.SUCCESS, result);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultData<List<String>>(HandleEnum.FAIL, e.getMessage());
        }
    }
}
