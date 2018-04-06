package com.cjw.rhmanager.service.impl;

import com.cjw.rhmanager.dao.RentHouseDao;
import com.cjw.rhmanager.entity.LoginUser;
import com.cjw.rhmanager.entity.system.Page;
import com.cjw.rhmanager.service.RentHouseService;
import com.cjw.rhmanager.utils.CommConst;
import com.cjw.rhmanager.utils.ParamData;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;

/**
 * Created by Administrator on 2017/12/26.
 */
@Service
public class RentHouseServiceImpl implements RentHouseService {
    @Resource
    private RentHouseDao rentHouseDao;

    @Override
    public LoginUser getUserInfo(ParamData pd) {
        return rentHouseDao.selectUser(pd);
    }

    @Override
    public int register(ParamData pd) {
        LoginUser userInfo = rentHouseDao.selectUserByName(pd);
        if (userInfo != null) {
            return 0;//已存在
        } else {
            return rentHouseDao.insertUser(pd);
        }
    }

    @Override
    public List<String> getSchoolName(ParamData pd) {
        return rentHouseDao.selectSchoolName(pd);
    }

    @Override
    public ParamData getSchoolByName(ParamData pd) {
        return rentHouseDao.selectSchoolByName(pd);
    }

    @Override
    public List<ParamData> getCampusList(Page page) {
        return rentHouseDao.selectCampusListByPage(page);
    }

    @Override
    public void loadFile(HttpServletRequest request, HttpServletResponse response) {
        BufferedInputStream bis = null;
        BufferedOutputStream fos = null;
        try {
            String filePath = request.getParameter("filePath");
            String uploadType = request.getParameter("uploadType");
            File file = new File(filePath);

            bis = new BufferedInputStream(new FileInputStream(file));
            fos = new BufferedOutputStream(response.getOutputStream());

            String[] split1 = filePath.split("/");
            String fileName = split1[split1.length - 1];

            response.setContentType(uploadType);
            response.setHeader("Cache-Control", "no-cache,must-revalidate");//告诉浏览器当前页面不进行缓存，每次访问的时间必须从服务器上读取最新的数据
            response.setContentLength(bis.available());
            response.setHeader("Content-Disposition", "inline; filename=\"" + fileName + "\"");

            byte[] buffer = new byte[1024];
            int length;
            while ((length = bis.read(buffer)) > 0) {
                fos.write(buffer, 0, length);
            }
            fos.flush();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bis != null)
                    bis.close();
                if (fos != null)
                    fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String uploadFile(CommonsMultipartFile file) {
        BufferedInputStream bis = null;
        BufferedOutputStream fos = null;
        try {
            String realPath = getPathByUploadType(file.getContentType());
            String fileName = file.getOriginalFilename();
            String filePath = realPath + fileName;
            buildFolder(new File(realPath));//判断该路径存不存在，不存在新建一个
            bis = new BufferedInputStream(file.getInputStream());
            fos = new BufferedOutputStream(new FileOutputStream(filePath));
            byte[] buffer = new byte[1024 * 10];//每次读10k
            int length;
            while ((length = bis.read(buffer)) > 0) {
                fos.write(buffer, 0, length);
            }
            fos.flush();

            //判断文件是否上传成功
            File file1 = new File(filePath);
            if (file1.exists() && file1.length() == file.getSize())
                return filePath;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bis != null)
                    bis.close();
                if (fos != null)
                    fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public int publishRent(ParamData pd) {
        return rentHouseDao.insertRent(pd);
    }

    @Override
    public List<ParamData> getBBsList(ParamData pd) {
        return rentHouseDao.selectBBsList(pd);
    }

    @Override
    public int bbs(ParamData pd) {
        return rentHouseDao.insertBBs(pd);
    }

    /**
     * @return 根据上传的文件类型选择存放的路径
     */
    private String getPathByUploadType(String uploadType) {
        if (uploadType.contains("image"))
            return CommConst.UPLOAD_IMAGE;
        else if (uploadType.contains("video"))
            return CommConst.UPLOAD_VIDEO;
        else if (uploadType.contains("audio"))
            return CommConst.UPLOAD_AUDIO;
        return CommConst.UPLOAD_TEMP;
    }

    /**
     * @return 判断文件夹是否存在，不存在则创建
     */

    private File buildFolder(File file) {
        if (!file.exists())
            file.mkdirs();

        return file;
    }

    @Override
    public int deleteRent(ParamData pd) {
        return rentHouseDao.deleteRent(pd);
    }

    @Override
    public int updateStatusRent(ParamData pd) {
        return rentHouseDao.updateStatusRent(pd);
    }

    public ParamData selectDetailRent(ParamData pd) {
        return rentHouseDao.selectRentById(pd);
    }
}
