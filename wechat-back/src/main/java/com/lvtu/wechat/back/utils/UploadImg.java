package com.lvtu.wechat.back.utils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.lvtu.wechat.common.utils.Constants;

public class UploadImg {
    /**
     * @param width 宽度
     * @param height 高度
     * @param isFixed 是否是固定宽度和高度
     * @param checkWidth 是否需要校验宽高
     * @param checkSize 是否需要校验大小 不校验则默认为1024
     * @param fileDiv 文件路径
     * @param image
     * @param size 大小限定 单位为M
     * @return
     */
    public Map<String, Object> imageUpload(Integer width, Integer height, boolean isFixed, boolean checkWidth,
        boolean checkSize, Integer size, String fileDiv, MultipartFile image) {
        // 提示信息
        String msgInfo = "";
        // 判断是否图片
        boolean isPicture = false;
        // 图片尺寸是否满足
        boolean isWidthOk = false;
        // 图片大小是否满足
        boolean isSizeOk = false;
        // 上传文件返回URL
        String imgPath = "";
        String fileFullName = "";// 数据库存放图片地址
        String error = "";
        String urlFull = "";
        String fileName = "";// 时间文件名
        // 原本文件名
        String oldFileName = image.getOriginalFilename();
        if (image.isEmpty()) {
            msgInfo = "请上传文件";
        }
        else {

            // 得到后缀并转换为小写
            String ext = oldFileName.substring(oldFileName.lastIndexOf(".") + 1).toLowerCase();
            // 临时目录
            // String path = request.getSession().getServletContext().getRealPath("upload");
            String path = System.getProperty("java.io.tmpdir");
            // 时间文件名
            fileName = System.currentTimeMillis() + "." + ext;
            // 数据库存放图片地址
            fileFullName = "weixin/back/" + DateUtil.getCurrentYearAndMonth() + "/" + fileName;

            if (ext.equals("jpg") || ext.equals("bmp") || ext.equals("png") || ext.equals("gif")) {
                isPicture = true;
            }
            else {
                msgInfo = "请上传JPG、BMP、PNG、GIF格式图片";
            }

            if (isPicture) {
                File file = new File(path, fileName);
                if (!file.exists()) {
                    file.mkdirs();
                }
                // MultipartFile转File
                try {
                    image.transferTo(file);
                }
                catch (IllegalStateException e) {
                    e.printStackTrace();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                // 判断图片尺寸
                try {
                    if (checkWidth) {
                        if (isFixed) {
                            if (UploadCtrl.fixedImgWidthAndHeight(file, width, height)) {
                                msgInfo = "请上传固定尺寸为" + width + "*" + height + "的图片。";
                            }
                            else {
                                isWidthOk = true;
                            }
                        }
                        else {
                            if (UploadCtrl.checedImgWidthAndHeight(file, width, height)) {
                                msgInfo = "请上传尺寸小于" + width + "*" + height + "的图片。";
                            }
                            else {
                                isWidthOk = true;
                            }
                        }
                    }
                    else {
                        isWidthOk = true;
                    }
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                if (checkSize) {
                    if (size == null) {
                        size = 1;
                    }
                    // 判断图片大小
                    if (UploadCtrl.checkImgSize(file, size * 1024)) {
                        msgInfo = "上传的文件不能大于" + size + "M";
                    }
                    else {
                        isSizeOk = true;
                    }
                }
                // 图片类型、大小、尺寸都满足
                if (isPicture && isSizeOk && isWidthOk) {
                    // 上传文件返回URL
                    try {
                        imgPath = UploadCtrl.postToRemote(file, fileFullName);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            // 删除临时文件
            File f = new File(path, fileName);
            if (f.exists()) {
                f.delete();
            }
        }

        if ((imgPath.length()) == 0) {
            // //提示信息
            // fileName=msgInfo;
            msgInfo = oldFileName + "上传失败。" + msgInfo;
        }
        else {
            // 服务器URL+数据库存放地址（完整路径）
            urlFull = Constants.getConfig("prefix_pic") + imgPath;
            // page.setAttribute("fileName", fileName);
        }
        Map<String, Object> map = new HashMap<String, Object>(1);
        map.put("url", urlFull);
        map.put("name", fileName);
        map.put("msgInfo", msgInfo);
        map.put("originalName", imgPath);
        map.put("size", imgPath);
        map.put("error", error);
        map.put("fileDiv", fileDiv);
        map.put("delete_type", imgPath);
        map.put("delete_url", imgPath);
        map.put("oldFileName", oldFileName);
        return map;
    }

    public Map<String, Object> imageUpload(Integer width, Integer height, boolean isFixed, String fileDiv,
        MultipartFile image) {
        // 提示信息
        String msgInfo = "";
        // 判断是否图片
        boolean isPicture = false;
        // 图片尺寸是否满足
        boolean isWidthOk = false;
        // 图片大小是否满足
        boolean isSizeOk = false;
        // 上传文件返回URL
        String imgPath = "";
        String fileFullName = "";// 数据库存放图片地址
        String error = "";
        String urlFull = "";
        String fileName = "";// 时间文件名
        // 原本文件名
        String oldFileName = image.getOriginalFilename();
        if (image.isEmpty()) {
            msgInfo = "请上传文件";
        }
        else {

            // 得到后缀并转换为小写
            String ext = oldFileName.substring(oldFileName.lastIndexOf(".") + 1).toLowerCase();
            // 临时目录
            // String path = request.getSession().getServletContext().getRealPath("upload");
            String path = System.getProperty("java.io.tmpdir");
            // 时间文件名
            fileName = System.currentTimeMillis() + "." + ext;
            // 数据库存放图片地址
            fileFullName = "weixin/back/" + DateUtil.getCurrentYearAndMonth() + "/" + fileName;

            if (ext.equals("jpg") || ext.equals("bmp") || ext.equals("png") || ext.equals("gif")) {
                isPicture = true;
            }
            else {
                msgInfo = "请上传JPG、BMP、PNG、GIF格式图片";
            }

            if (isPicture) {
                File file = new File(path, fileName);
                if (!file.exists()) {
                    file.mkdirs();
                }
                // MultipartFile转File
                try {
                    image.transferTo(file);
                }
                catch (IllegalStateException e) {
                    e.printStackTrace();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                // 判断图片尺寸
                try {
                    if (isFixed) {
                        if (UploadCtrl.fixedImgWidthAndHeight(file, width, height)) {
                            msgInfo = "请上传固定尺寸为" + width + "*" + height + "的图片。";
                        }
                        else {
                            isWidthOk = true;
                        }
                    }
                    else {
                        if (UploadCtrl.checedImgWidthAndHeight(file, width, height)) {
                            msgInfo = "请上传尺寸小于" + width + "*" + height + "的图片。";
                        }
                        else {
                            isWidthOk = true;
                        }
                    }

                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                // 判断图片大小
                if (UploadCtrl.checkImgSize(file, 1024)) {
                    msgInfo = "上传的文件不能大于1MB";
                }
                else {
                    isSizeOk = true;
                }
                // 图片类型、大小、尺寸都满足
                if (isPicture && isSizeOk && isWidthOk) {
                    // 上传文件返回URL
                    try {
                        imgPath = UploadCtrl.postToRemote(file, fileFullName);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            // 删除临时文件
            File f = new File(path, fileName);
            if (f.exists()) {
                f.delete();
            }

        }

        if ((imgPath.length()) == 0) {
            // //提示信息
            // fileName=msgInfo;
            msgInfo = oldFileName + "上传失败。" + msgInfo;
        }
        else {
            // 服务器URL+数据库存放地址（完整路径）
            urlFull = Constants.getConfig("prefix_pic") + imgPath;
            // page.setAttribute("fileName", fileName);
        }
        Map<String, Object> map = new HashMap<String, Object>(1);
        map.put("url", urlFull);
        map.put("name", fileName);
        map.put("msgInfo", msgInfo);
        map.put("originalName", imgPath);
        map.put("size", imgPath);
        map.put("error", error);
        map.put("fileDiv", fileDiv);
        map.put("delete_type", imgPath);
        map.put("delete_url", imgPath);
        map.put("oldFileName", oldFileName);

        return map;

    }
}
