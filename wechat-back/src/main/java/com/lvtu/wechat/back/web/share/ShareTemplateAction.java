package com.lvtu.wechat.back.web.share;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.lvtu.wechat.back.base.BaseActionSupport;
import com.lvtu.wechat.back.utils.UploadImg;
import com.lvtu.wechat.common.enums.CommonType;
import com.lvtu.wechat.common.enums.StatusType;
import com.lvtu.wechat.common.model.share.ShareContent;
import com.lvtu.wechat.common.model.share.ShareImage;
import com.lvtu.wechat.common.model.share.ShareTemplate;
import com.lvtu.wechat.common.service.share.IShareTemplateService;
import com.lvtu.wechat.common.utils.MemcachedUtil;
import com.lvtu.wechat.common.vo.back.ShareTemplateVo;

/**
 * 分享模版Controller
 * 
 * @author qianqc
 */
@Controller
@RequestMapping("${adminPath}/share/template")
public class ShareTemplateAction extends BaseActionSupport {

    @Autowired
    private IShareTemplateService shareTemplateService;

    private JSONObject weixinJsonObject = null;

    private JSONObject qqJsonObject = null;

    private JSONObject weiboJsonObject = null;

    /**
     * 缓存前缀
     */
    private static final String SHARE_TEMPLATE_CODE = "wx_share_template_code_";
    
    /**
     * 分享模版首页
     * 
     * @param shareTemplateVo
     * @param model
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/query")
    public String index(ShareTemplateVo shareTemplateVo, Model model, HttpServletRequest request,
        HttpServletResponse response) {
        PageInfo<ShareTemplate> pageInfo = shareTemplateService.selectShareTemplateList(shareTemplateVo);
        List<StatusType> statusList = Arrays.asList(StatusType.values());
        model.addAttribute("shareTemplateList", pageInfo.getList());
        model.addAttribute("statusList", statusList);
        model.addAttribute("condition", shareTemplateVo);
        model.addAttribute("pageManage", pageInfo);
        return "/share/index";
    }

    /**
     * 进入新增分享模版页面
     * 
     * @param model
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/new")
    public String newShareTemplate(Model model, HttpServletRequest request, HttpServletResponse response) {
        String shareTemplateId = UUID.randomUUID().toString().replaceAll("-", "");
        List<StatusType> statusList = Arrays.asList(StatusType.getStatusType());
        model.addAttribute("statusList", statusList);
        model.addAttribute("shareTemplateId", shareTemplateId);
        return "/share/add";
    }

    
    /**
     * 修改活动
     * @param id
     * @return
     */
    @RequestMapping("/edit/{id}")
    public String changeState(@PathVariable String id, Model model) {
        ShareTemplate shareTemplate = new ShareTemplate();
        shareTemplate.setId(id);
        shareTemplate = shareTemplateService.queryShareTemplate(shareTemplate);
        model.addAttribute("shareTemplate", shareTemplate);
        model.addAttribute("statusList", Arrays.asList(StatusType.getStatusType()));
        if (shareTemplate.getWxShareContent() != null) {
            if (!shareTemplate.getWxShareContent().imageList.isEmpty()) {
                model.addAttribute("weixinImage", shareTemplate.getWxShareContent().imageList.get(0));
            }
        }
        
        if (shareTemplate.getQqShareContent() != null) {
            if (!shareTemplate.getQqShareContent().imageList.isEmpty()) {
                model.addAttribute("qqImage", shareTemplate.getQqShareContent().imageList.get(0));
            } 
        }
           
        if (shareTemplate.getWbShareContent() != null) {
            if (!shareTemplate.getWbShareContent().imageList.isEmpty()) {
                model.addAttribute("wbImageList", shareTemplate.getWbShareContent().imageList);
            }
        }
        return "/share/add";
    }
    
    /**
     * 清除缓存活动
     * @param id
     * @return
     */
    @RequestMapping("/uncached/{templateId}")
    public String uncached(@PathVariable String templateId, Model model, RedirectAttributes redirectAttrs) {
        MemcachedUtil mem = MemcachedUtil.getInstance();
        mem.remove(SHARE_TEMPLATE_CODE + templateId);
        addMessage(redirectAttrs, "清除缓存成功");
        return "redirect:" + adminPath + "/share/template/query";
    }
    /**
     * 删除分享图片
     * @param redirectAttrs
     * @param key
     * @return
     */
    @RequestMapping("/delete")
    @ResponseBody
    public Object delete(RedirectAttributes redirectAttrs, String key) {
        ShareImage shareImage = new ShareImage();
        shareImage.setId(key);
        shareTemplateService.deleteImage(shareImage);
        return getResult();
    }
    /**
     * 更改状态
     */
    @RequestMapping("/changeState/{id}/{state}")
    public String changeState(@PathVariable String id, @PathVariable String state, RedirectAttributes redirectAttrs) {
        if (state.equals(String.valueOf(CommonType.ENABLE.getValue()))) {
            state = String.valueOf(CommonType.DISABLE.getValue());
            addMessage(redirectAttrs, "禁用状态成功");
        } else {
            state = String.valueOf(CommonType.ENABLE.getValue());
            addMessage(redirectAttrs, "启用状态成功");
        }
        ShareTemplate shareTemplate = new ShareTemplate();
        shareTemplate.setId(id);
        shareTemplate.setState(state);
        shareTemplateService.changeState(shareTemplate);
        return "redirect:" + adminPath + "/share/template/query";
    }
    
    /**
     * 添加分享模版
     * 
     * @param jsonRequest
     * @return
     */
    @RequestMapping("/addShareTemplate")
    @ResponseBody
    public Map<String, Object> addShareTemplate(HttpServletRequest request) {
        String data = request.getParameter("data");
        Map<String, Object> result = getResult();
        JSONObject jsonObject = JSONObject.parseObject(data);
        if (!validate(jsonObject, result)) {
            return result;
        }
        ShareTemplate shareTemplate = new ShareTemplate();
        if (StringUtils.isBlank(jsonObject.getString("shareTemplateId"))) {
            shareTemplate.preInsert();
            pareseShareTemplate(shareTemplate, jsonObject);
            ShareTemplate shareTemplateTemp = new ShareTemplate();
            shareTemplateTemp.setTemplateId(shareTemplate.getTemplateId());
            //根据templateId查询出是否存在相关的分享模版
            shareTemplateTemp = shareTemplateService.queryShareTemplateByTemplateId(shareTemplateTemp);
            if (shareTemplateTemp != null) {
                result.put("code", "-1");
                result.put("msg", "模版id已经存在");
                return result;
            }
            shareTemplate.setCreateDate(new Date());
            shareTemplateService.insertShareTemplate(shareTemplate);
        }
        else {
            shareTemplate.setId(jsonObject.getString("shareTemplateId"));
            pareseShareTemplate(shareTemplate, jsonObject);
            ShareTemplate shareTemplateTemp = new ShareTemplate();
            shareTemplateTemp.setTemplateId(shareTemplate.getTemplateId());
            //根据templateId查询出是否存在相关的分享模版
            shareTemplateTemp = shareTemplateService.queryShareTemplateByTemplateId(shareTemplateTemp);
            if (shareTemplateTemp != null && !shareTemplateTemp.getId().equals(shareTemplate.getId())) {
                result.put("code", "-1");
                result.put("msg", "模版id已经存在");
                return result;
            }
            shareTemplateService.updateShareTemplate(shareTemplate);
        }
        return result;
    }

    /**
     * 根据jsonObject构造ShareTemplate对象
     * @param shareTemplate
     * @param jsonObject
     */
    private void pareseShareTemplate(ShareTemplate shareTemplate, JSONObject jsonObject) {
        shareTemplate.setName(jsonObject.getString("shareTemplateName"));
        shareTemplate.setState(jsonObject.getString("shareTemplateState"));
        shareTemplate.setTemplateId(jsonObject.getString("shareTemplateCode"));
        if (weixinJsonObject != null) {
            shareTemplate.setWeixin(CommonType.SHARE.getStringValue());
            ShareContent wxShareContent = new ShareContent();
            shareTemplate.setWxShareContent(wxShareContent);
            wxShareContent.setShareId(shareTemplate.getId());
            wxShareContent.setChannel(CommonType.SHARE_CHANNEL_WX.getStringValue());
            wxShareContent.setTitle(weixinJsonObject.getString("weixinShareTitle"));
            wxShareContent.setShareDesc(weixinJsonObject.getString("weixinShareDesc"));
            if (!StringUtils.isBlank(weixinJsonObject.getString("weixinShareUrl"))) {
                wxShareContent.setUrl(weixinJsonObject.getString("weixinShareUrl"));
            }
            wxShareContent.preInsert();
            if (!StringUtils.isBlank(weixinJsonObject.getString("weixinShareImg"))) {
                ShareImage weixinShareImage = new ShareImage();
                weixinShareImage = new ShareImage();
                weixinShareImage.setShareId(shareTemplate.getId());
                weixinShareImage.setChannel(CommonType.SHARE_CHANNEL_WX.getStringValue());
                weixinShareImage.setUrl(weixinJsonObject.getString("weixinShareImg"));
                weixinShareImage.preInsert();
                wxShareContent.imageList.add(weixinShareImage);
            }
        }
        else {
            shareTemplate.setWeixin(CommonType.NO_SHARE.getStringValue());
        }
        if (qqJsonObject != null) {
            shareTemplate.setQq(CommonType.SHARE.getStringValue());
            ShareContent qqShareContent = new ShareContent();
            shareTemplate.setQqShareContent(qqShareContent);
            qqShareContent.setShareId(shareTemplate.getId());
            qqShareContent.setChannel(CommonType.SHARE_CHANNEL_QQ.getStringValue());
            qqShareContent.setTitle(qqJsonObject.getString("qqShareTitle"));
            qqShareContent.setShareDesc(qqJsonObject.getString("qqShareDesc"));
            if (!StringUtils.isBlank(qqJsonObject.getString("qqShareUrl"))) {
                qqShareContent.setUrl(qqJsonObject.getString("qqShareUrl"));
            }
            qqShareContent.preInsert();
            if (!StringUtils.isBlank(qqJsonObject.getString("qqShareImg"))) {
                ShareImage qqShareImage = new ShareImage();
                qqShareImage = new ShareImage();
                qqShareImage.setShareId(shareTemplate.getId());
                qqShareImage.setChannel(CommonType.SHARE_CHANNEL_QQ.getStringValue());
                qqShareImage.setUrl(qqJsonObject.getString("qqShareImg"));
                qqShareImage.preInsert();
                qqShareContent.imageList.add(qqShareImage);
            }
        }
        else {
            shareTemplate.setQq(CommonType.NO_SHARE.getStringValue());
        }
        if (weiboJsonObject != null) {
            shareTemplate.setWeibo(CommonType.SHARE.getStringValue());
            ShareContent wbShareContent = new ShareContent();
            shareTemplate.setWbShareContent(wbShareContent);
            wbShareContent.setShareId(shareTemplate.getId());
            wbShareContent.setChannel(CommonType.SHARE_CHANNEL_WB.getStringValue());
            wbShareContent.setShareDesc(weiboJsonObject.getString("weiboShareContent"));
            wbShareContent.preInsert();
            if (!StringUtils.isBlank(weiboJsonObject.getString("weiboShareTips"))) {
                wbShareContent.setTips(weiboJsonObject.getString("weiboShareTips"));
            }
            if (!StringUtils.isBlank(weiboJsonObject.getString("weiboShareUrl"))) {
                wbShareContent.setUrl(weiboJsonObject.getString("weiboShareUrl"));
            }
            JSONArray jsonArray = null;
            ShareImage weiboShareImage = null;
            if ((jsonArray = weiboJsonObject.getJSONArray("weiboShareImgs")) != null) {
                for (Object url : jsonArray) {
                    weiboShareImage = new ShareImage();
                    weiboShareImage.setUrl((String) url);
                    weiboShareImage.setChannel(CommonType.SHARE_CHANNEL_WB.getStringValue());
                    weiboShareImage.setShareId(shareTemplate.getId());
                    weiboShareImage.preInsert();
                    wbShareContent.imageList.add(weiboShareImage);
                }
            }
        }
        else {
            shareTemplate.setWeibo(CommonType.NO_SHARE.getStringValue());
        }
    }

    /**
     * 进行参数校验
     * 
     * @param jsonObject
     * @param result
     * @return
     */
    private boolean validate(JSONObject jsonObject, Map<String, Object> result) {
        result.put("code", "-1");
        if (StringUtils.isBlank(jsonObject.getString("shareTemplateName"))) {
            result.put("msg", "分享模板名称不能为空");
            return false;
        }
        if (StringUtils.isBlank(jsonObject.getString("shareTemplateCode"))) {
            result.put("msg", "分享模版ID不能为空");
            return false;
        }
        if (StringUtils.isBlank(jsonObject.getString("shareTemplateState"))) {
            result.put("msg", "分享模版状态不能为空");
            return false;
        }
        if (!CommonType.ENABLE.getStringValue().equals(jsonObject.getString("shareTemplateState"))
            && !CommonType.DISABLE.getStringValue().equals(jsonObject.getString("shareTemplateState"))) {
            result.put("msg", "分享模版状态错误");
            return false;
        }
        if ((jsonObject.getJSONObject("weixin") == null) && (jsonObject.getJSONObject("qq") == null)
            && (jsonObject.getJSONObject("weibo") == null)) {
            result.put("msg", "请选择分享渠道");
            return false;
        }

        if ((weixinJsonObject = jsonObject.getJSONObject("weixin")) != null) {
            if (StringUtils.isBlank(weixinJsonObject.getString("weixinShareTitle"))) {
                result.put("msg", "微信分享标题不能为空");
                return false;
            }
            if (StringUtils.isBlank(weixinJsonObject.getString("weixinShareDesc"))) {
                result.put("msg", "微信分享描述不能为空");
                return false;
            }
        }
        if ((weiboJsonObject = jsonObject.getJSONObject("weibo")) != null) {
            if (StringUtils.isBlank(weiboJsonObject.getString("weiboShareContent"))) {
                result.put("msg", "微博分享内容不能为空");
                return false;
            }
        }
        if ((qqJsonObject = jsonObject.getJSONObject("qq")) != null) {
            if (StringUtils.isBlank(qqJsonObject.getString("qqShareTitle"))) {
                result.put("msg", "QQ分享标题不能为空");
                return false;
            }
            if (StringUtils.isBlank(qqJsonObject.getString("qqShareDesc"))) {
                result.put("msg", "QQ分享描述不能为空");
                return false;
            }
        }
        result.put("code", "1");
        return true;
    }

    /**
     * 文件上传
     * 
     * @param model
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/fileupload")
    public String fileupload(Model model, HttpServletRequest request, HttpServletResponse response) {
        return "/share/fileupload";
    }

    /**
     * 上传图片
     * 
     * @param model
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/imageUpload")
    @ResponseBody
    public Map<String, Object> imageUpload(Model model, MultipartHttpServletRequest request,
        HttpServletResponse response) {
        String fileDiv = "_shareTemplate";
        Map<String, Object> result = getResult();
        int width = 300;// 宽度固定300
        int height = 300;// 高度固定300
        boolean isFixed = true;// 尺寸固定
        String type = request.getParameter("type");
        MultipartFile image = null;
        UploadImg uploadImg = new UploadImg();
        if (CommonType.SHARE_CHANNEL_WX.getStringValue().equals(type)) {
            fileDiv = "_shareTemplateWX";
            image = request.getFile("weixinShareUpload");
            result = uploadImg.imageUpload(width, height, isFixed, fileDiv, image);
        }
        else if (CommonType.SHARE_CHANNEL_QQ.getStringValue().equals(type)) {
            fileDiv = "_shareTemplateQQ";
            image = request.getFile("qqShareUpload");
            result = uploadImg.imageUpload(width, height, isFixed, fileDiv, image);
        }
        else if (CommonType.SHARE_CHANNEL_WB.getStringValue().equals(type)) {
            fileDiv = "_shareTemplateWB";
            image = request.getFile("file_data");
            if (image == null) {
                result.put("code", "-1");
                result.put("msg", "图片上传失败");
                return result;
            }
            result = uploadImg.imageUpload(null, null, false, false, true, 20, fileDiv, image);
        }
        else {
            result.put("code", "-1");
            result.put("msg", "图片上传失败");
            return result;
        }
        return result;
    }

    public Map<String, Object> getResult() {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", "1");
        result.put("msg", "操作成功");
        return result;
    }
}
