package com.cjw.rhmanager.service;

import com.cjw.rhmanager.entity.LoginUser;
import com.cjw.rhmanager.entity.system.Page;
import com.cjw.rhmanager.utils.ParamData;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by Administrator on 2017/12/26.
 */
public interface RentHouseService {
    LoginUser getUserInfo(ParamData pd);

    int register(ParamData pd);

    List<String> getSchoolName(ParamData pd);

    ParamData getSchoolByName(ParamData pd);

    List<ParamData> getCampusList(Page page);

    void loadFile(HttpServletRequest request, HttpServletResponse response);

    String uploadFile(CommonsMultipartFile file);

    int publishRent(ParamData pd);

    List<ParamData> getBBsList(ParamData pd);

    int bbs(ParamData pd);
}
