package com.cjw.rhmanager.controller;

import com.cjw.rhmanager.controller.base.BaseController;
import com.cjw.rhmanager.entity.PageResult;
import com.cjw.rhmanager.entity.system.Page;
import com.cjw.rhmanager.service.RentHouseService;
import com.cjw.rhmanager.utils.CommUtil;
import com.cjw.rhmanager.utils.HandleEnum;
import com.cjw.rhmanager.utils.ParamData;
import com.cjw.rhmanager.utils.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by Administrator on 2017/12/26.
 */
@Controller
@RequestMapping("/rh")
@SuppressWarnings({"rawtypes", "unchecked"})
public class RentHouseController extends BaseController {
    @Autowired
    private RentHouseService rentHouseService;

    @ResponseBody
    @RequestMapping("/campusList")
    public PageResult<ParamData> campusList(HttpServletRequest request) {
        try {
            //获取参数
            ParamData pd = this.paramDataInit();
            //初始化分页参数
            int pageCur = CommUtil.paramConvert(pd.getString("pageCur"), 1);//当前页
            int pageSize = CommUtil.paramConvert(pd.getString("pageSize"), 10);//每一页10条数据
            Page page = new Page(pageCur, pageSize, pd);
            List<ParamData> result = rentHouseService.getCampusList(page);

            PageResult<ParamData> pr = new PageResult<ParamData>(page.getPageCur(), page.getPageSize(), page.getTotalNum(), result);

            return pr;
        } catch (Exception e) {
            e.printStackTrace();
            return new PageResult<ParamData>(false, e.getMessage());
        }
    }

    @ResponseBody
    @RequestMapping("/loadFile")
    public void loadFile(HttpServletRequest request, HttpServletResponse response) {
        rentHouseService.loadFile(request, response);
    }

    /**
     * 发布出租
     */
    @ResponseBody
    @RequestMapping("/publishRent")
    public ResultData<ParamData> publishRent
    (@RequestParam("files") CommonsMultipartFile files[], HttpServletRequest request) {
        try {
            boolean isAllUploadSuccess = false;
            ParamData pd = new ParamData();
            pd.put("title", request.getParameter("title"));
            pd.put("content", request.getParameter("content"));
            pd.put("type", request.getParameter("type"));
            pd.put("location", request.getParameter("location"));
            pd.put("longitude", request.getParameter("longitude"));
            pd.put("latitude", request.getParameter("latitude"));
            pd.put("amount", request.getParameter("amount"));
            pd.put("bed", request.getParameter("bed"));
            pd.put("area", request.getParameter("area"));
            pd.put("house_type", request.getParameter("house_type"));
            pd.put("user_id", request.getParameter("user_id"));
            pd.put("label", request.getParameter("label"));

            String otherImg = "";
            for (int i = 0; i < files.length; i++) {
                String filePath = rentHouseService.uploadFile(files[i]);
                if (filePath != null) {
                    isAllUploadSuccess = true;
                    if (i == 0) {
                        pd.put("title_img", filePath);
                    } else {
                        otherImg += filePath + ",";
                    }
                } else isAllUploadSuccess = false;
            }
            if (otherImg.length() > 0) otherImg = otherImg.substring(0, otherImg.length() - 1);
            pd.put("other_img", otherImg);
            if (isAllUploadSuccess) {
                if (rentHouseService.publishRent(pd) > 0)
                    return new ResultData<ParamData>(HandleEnum.SUCCESS);
            }
            return new ResultData<ParamData>(HandleEnum.FAIL, "上传失败");

        } catch (Exception e) {
            e.printStackTrace();
            return new ResultData<ParamData>(HandleEnum.FAIL, e.getMessage());
        }
    }

    @ResponseBody
    @RequestMapping("/bbs")
    public ResultData<List<ParamData>> bbs(HttpServletRequest request) {
        try {
            //获取参数
            ParamData pd = this.paramDataInit();
            int result = rentHouseService.bbs(pd);
            if (result > 0) {
                return new ResultData<List<ParamData>>(HandleEnum.SUCCESS, rentHouseService.getBBsList(pd));
            }

            return new ResultData<List<ParamData>>(HandleEnum.FAIL);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultData<List<ParamData>>(HandleEnum.FAIL, e.getMessage());
        }
    }

    @ResponseBody
    @RequestMapping("/bbsList")
    public ResultData<List<ParamData>> bbsList(HttpServletRequest request) {
        try {
            //获取参数
            ParamData pd = this.paramDataInit();
            List<ParamData> result = rentHouseService.getBBsList(pd);

            return new ResultData<List<ParamData>>(HandleEnum.SUCCESS, result);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultData<List<ParamData>>(HandleEnum.FAIL, e.getMessage());
        }
    }
}
