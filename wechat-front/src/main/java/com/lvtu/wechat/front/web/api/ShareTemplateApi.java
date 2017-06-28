package com.lvtu.wechat.front.web.api;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lvtu.wechat.common.enums.CommonType;
import com.lvtu.wechat.common.model.comm.ApiResponse;
import com.lvtu.wechat.common.model.share.ShareTemplate;
import com.lvtu.wechat.common.service.share.IShareTemplateService;
import com.lvtu.wechat.common.utils.MemcachedUtil;
import com.lvtu.wechat.front.base.BaseController;

/**
 * 分享模版api封装
 * 
 * @author xuyao
 *
 */
@Controller
@RequestMapping("/api/shareTemplate")
public class ShareTemplateApi extends BaseController {
    
    @Autowired
    private IShareTemplateService shareTemplateService;
    
    /**
     * 缓存前缀
     */
    private static final String SHARE_TEMPLATE_CODE = "wx_share_template_code_";
    
    /**
     * 缓存失效时间
     */
    private static final int seconds = 86400;
    
    /**
     * 查询分享模版
     */
    @RequestMapping(value = "/{templateId}/{channel}", method=RequestMethod.POST)
    @ResponseBody
    public Object changeState(@PathVariable String templateId, @PathVariable String channel) {
        if (StringUtils.isBlank(templateId)) {
            return new ApiResponse<String>("-1", "分享模版ID不能为空！");
        }
        MemcachedUtil memcachedUtil = MemcachedUtil.getInstance();
        ShareTemplate shareTemplate = (ShareTemplate) memcachedUtil.get(SHARE_TEMPLATE_CODE + templateId); 
        if (shareTemplate == null) {
            shareTemplate = new ShareTemplate();
            shareTemplate.setTemplateId(templateId);
            shareTemplate = shareTemplateService.queryShareTemplate(shareTemplate);
            if (shareTemplate == null) {
                return new ApiResponse<String>("-1", "分享模版不存在！");
            }
            else {
                memcachedUtil.set(SHARE_TEMPLATE_CODE + templateId, seconds , shareTemplate);
            }
        }
        if (CommonType.NO_SHARE.getStringValue().equals(shareTemplate.getState())) {
            return new ApiResponse<String>("-1", "分享模版已关闭！");
        }
        if (CommonType.SHARE_CHANNEL_QQ.getStringValue().equals(channel)) {
            shareTemplate.setWbShareContent(null);
            shareTemplate.setWxShareContent(null);
            if (shareTemplate.getQqShareContent() == null) {
                return new ApiResponse<String>("-1", "分享模版对应渠道内容不存在！");
            }
        }
        else if (CommonType.SHARE_CHANNEL_WX.getStringValue().equals(channel)) {
            shareTemplate.setWbShareContent(null);
            shareTemplate.setQqShareContent(null);
            if (shareTemplate.getWxShareContent() == null) {
                return new ApiResponse<String>("-1", "分享模版对应渠道内容不存在！");
            }
        }
        else if (CommonType.SHARE_CHANNEL_WB.getStringValue().equals(channel)) {
            shareTemplate.setWxShareContent(null);
            shareTemplate.setQqShareContent(null);
            if (shareTemplate.getWbShareContent() == null) {
                return new ApiResponse<String>("-1", "分享模版对应渠道内容不存在！");
            }
        }
        else {
            return new ApiResponse<String>("-1", "渠道不存在！");
        }
        
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("shareTemplate", shareTemplate);
        ApiResponse<Map<String, Object>> resp = new ApiResponse<Map<String, Object>>();
        resp.setData(result);
        return resp;
    }
}
