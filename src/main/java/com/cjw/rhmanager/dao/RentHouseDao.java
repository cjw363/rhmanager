package com.cjw.rhmanager.dao;

import com.cjw.rhmanager.entity.LoginUser;
import com.cjw.rhmanager.entity.system.Page;
import com.cjw.rhmanager.utils.ParamData;

import java.util.List;

/**
 * Created by Administrator on 2017/12/26.
 */
public interface RentHouseDao {
    LoginUser selectUser(ParamData pd);

    LoginUser selectUserByName(ParamData pd);

    int insertUser(ParamData pd);

    List<String> selectSchoolName(ParamData pd);

    ParamData selectSchoolByName(ParamData pd);

    List<ParamData> selectCampusListByPage(Page page);

    int insertRent(ParamData pd);

    List<ParamData> selectCampusListByTime(Page page);

    List<ParamData> selectCampusListByAmountUp(Page page);

    List<ParamData> selectCampusListByAmountDown(Page page);

    List<ParamData> selectBBsList(ParamData pd);

    int insertBBs(ParamData pd);
}
