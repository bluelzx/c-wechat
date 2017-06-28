package com.lvtu.wechat.service.share;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lvtu.wechat.common.enums.CommonType;
import com.lvtu.wechat.common.hessian.HessianService;
import com.lvtu.wechat.common.model.share.ShareContent;
import com.lvtu.wechat.common.model.share.ShareImage;
import com.lvtu.wechat.common.model.share.ShareTemplate;
import com.lvtu.wechat.common.service.share.IShareTemplateService;
import com.lvtu.wechat.common.utils.MemcachedUtil;
import com.lvtu.wechat.common.vo.back.ShareTemplateVo;
import com.lvtu.wechat.dao.share.dao.ShareContentDAO;
import com.lvtu.wechat.dao.share.dao.ShareImageDAO;
import com.lvtu.wechat.dao.share.dao.ShareTemplateDAO;

@HessianService("shareTemplateService")
@Service("shareTemplateService")
@Transactional(readOnly = true)
public class ShareTemplateServiceImpl implements IShareTemplateService {

    @Autowired
    private ShareTemplateDAO shareTemplateDao;

    @Autowired
    private ShareImageDAO shareImageDao;

    @Autowired
    private ShareContentDAO shareContentDao;
    
    /**
     * 缓存前缀
     */
    private static final String SHARE_TEMPLATE_CODE = "wx_share_template_code_";
    
    /**
     * 缓存失效时间
     */
    private static final int seconds = 86400;

    /**
     * 分页查询分享模版
     */
    @Override
    public PageInfo<ShareTemplate> selectShareTemplateList(ShareTemplateVo shareTemplateVo) {
        PageHelper.startPage(shareTemplateVo.getPage(), shareTemplateVo.getPageSize());
        List<ShareTemplate> shareTemplateList = shareTemplateDao.selectShareTemplateList(shareTemplateVo);
        PageInfo<ShareTemplate> pageInfo = new PageInfo<ShareTemplate>(shareTemplateList);
        return pageInfo;
    }

    /**
     * 更新分享模版
     */
    @Override
    @Transactional(readOnly = false)
    public void updateShareTemplate(ShareTemplate shareTemplate) {
        shareTemplateDao.update(shareTemplate);
        ShareContent shareContentParam = new ShareContent();
        shareContentParam.setShareId(shareTemplate.getId());
        ShareImage shareImageParam = new ShareImage();
        shareImageParam.setShareId(shareTemplate.getId());
        shareContentDao.delete(shareContentParam);

        if (CommonType.SHARE.getStringValue().equals(shareTemplate.getWeibo())) {
            shareImageParam.setChannel("'" + CommonType.SHARE_CHANNEL_QQ.getStringValue() + "','"
                + CommonType.SHARE_CHANNEL_WX.getStringValue() + "'");
        }
        else {
            shareImageParam.setChannel("'" + CommonType.SHARE_CHANNEL_QQ.getStringValue() + "','"
                + CommonType.SHARE_CHANNEL_WX.getStringValue() + "','" + CommonType.SHARE_CHANNEL_WB.getStringValue()
                + "'");
        }
        shareImageDao.delete(shareImageParam);
        if (CommonType.SHARE.getStringValue().equals(shareTemplate.getWeixin())) {
            shareContentDao.insert(shareTemplate.getWxShareContent());
            if (!shareTemplate.getWxShareContent().imageList.isEmpty()) {
                shareImageDao.insert(shareTemplate.getWxShareContent().imageList);
            }
        }
        if (CommonType.SHARE.getStringValue().equals(shareTemplate.getQq())) {
            shareContentDao.insert(shareTemplate.getQqShareContent());
            if (!shareTemplate.getQqShareContent().imageList.isEmpty()) {
                shareImageDao.insert(shareTemplate.getQqShareContent().imageList);
            }
        }
        if (CommonType.SHARE.getStringValue().equals(shareTemplate.getWeibo())) {
            shareContentDao.insert(shareTemplate.getWbShareContent());
            if (!shareTemplate.getWbShareContent().imageList.isEmpty()) {
                shareImageDao.insert(shareTemplate.getWbShareContent().imageList);
            }
        }
    }

    /**
     * 插入分享模版
     */
    @Override
    @Transactional(readOnly = false)
    public void insertShareTemplate(ShareTemplate shareTemplate) {
        shareTemplateDao.insetShareTemplate(shareTemplate);
        if (CommonType.SHARE.getStringValue().equals(shareTemplate.getWeixin())) {
            shareContentDao.insert(shareTemplate.getWxShareContent());
            if (!shareTemplate.getWxShareContent().imageList.isEmpty()) {
                shareImageDao.insert(shareTemplate.getWxShareContent().imageList);
            }
        }
        if (CommonType.SHARE.getStringValue().equals(shareTemplate.getQq())) {
            shareContentDao.insert(shareTemplate.getQqShareContent());
            if (!shareTemplate.getQqShareContent().imageList.isEmpty()) {
                shareImageDao.insert(shareTemplate.getQqShareContent().imageList);
            }
        }
        if (CommonType.SHARE.getStringValue().equals(shareTemplate.getWeibo())) {
            shareContentDao.insert(shareTemplate.getWbShareContent());
            if (!shareTemplate.getWbShareContent().imageList.isEmpty()) {
                shareImageDao.insert(shareTemplate.getWbShareContent().imageList);
            }
        }
    }

    /**
     * 修改状态
     */
    @Override
    @Transactional(readOnly = false)
    public void changeState(ShareTemplate shareTemplate) {
        shareTemplateDao.update(shareTemplate);
    }

    /**
     * 查询分享模版
     */
    @Override
    public ShareTemplate queryShareTemplate(ShareTemplate shareTemplate) {
        shareTemplate = shareTemplateDao.selectShareTemplate(shareTemplate);
        if (shareTemplate == null) {
            return null;
        }
        ShareContent shareContentParam = new ShareContent();
        shareContentParam.setShareId(shareTemplate.getId());
        ShareImage shareImageParam = new ShareImage();
        shareImageParam.setShareId(shareTemplate.getId());
        List<ShareContent> shareContentList = shareContentDao.queryShareContent(shareContentParam);
        List<ShareImage> shareImageList = shareImageDao.queryShareImage(shareImageParam);
        if (shareImageList == null || shareImageList.isEmpty()) {
            shareImageList = new ArrayList<ShareImage>();
        }
        if (shareContentList == null || shareContentList.isEmpty()) {
            return null;
        }
        for (ShareContent shareContent : shareContentList) {
            if (CommonType.SHARE_CHANNEL_QQ.getStringValue().equals(shareContent.getChannel())) {
                shareTemplate.setQqShareContent(shareContent);
                for (ShareImage shareImage : shareImageList) {
                    if (CommonType.SHARE_CHANNEL_QQ.getStringValue().equals(shareImage.getChannel())) {
                        shareContent.imageList.add(shareImage);
                    }
                }
            }
            if (CommonType.SHARE_CHANNEL_WX.getStringValue().equals(shareContent.getChannel())) {
                shareTemplate.setWxShareContent(shareContent);
                for (ShareImage shareImage : shareImageList) {
                    if (CommonType.SHARE_CHANNEL_WX.getStringValue().equals(shareImage.getChannel())) {
                        shareContent.imageList.add(shareImage);
                    }
                }
            }
            if (CommonType.SHARE_CHANNEL_WB.getStringValue().equals(shareContent.getChannel())) {
                shareTemplate.setWbShareContent(shareContent);
                for (ShareImage shareImage : shareImageList) {
                    if (CommonType.SHARE_CHANNEL_WB.getStringValue().equals(shareImage.getChannel())) {
                        shareContent.imageList.add(shareImage);
                    }
                }
            }
        }
        return shareTemplate;
    }

    /**
     * 删除图片
     */
    @Override
    @Transactional(readOnly = false)
    public void deleteImage(ShareImage shareImage) {
        shareImageDao.deleteShareImage(shareImage);
    }

    /**
     * 根据TemplateId查询分享模版
     */
    @Override
    public ShareTemplate queryShareTemplateByTemplateId(ShareTemplate shareTemplate) {
        return shareTemplateDao.selectShareTemplate(shareTemplate);
    }
    
    /**
     * 根据分享模版id，渠道获取分享模版内容
     * @param templateId
     * @param channel
     * @return
     */
    @Override
    public ShareTemplate getShareTemplate(String templateId, int channel) {
        if (StringUtils.isBlank(templateId)) {
            return null;
        }
        MemcachedUtil memcachedUtil = MemcachedUtil.getInstance();
        ShareTemplate shareTemplate = (ShareTemplate) memcachedUtil.get(SHARE_TEMPLATE_CODE + templateId); 
        if (shareTemplate == null) {
            shareTemplate = new ShareTemplate();
            shareTemplate.setTemplateId(templateId);
            shareTemplate = queryShareTemplate(shareTemplate);
            if (shareTemplate != null) {
                memcachedUtil.set(SHARE_TEMPLATE_CODE + templateId, seconds , shareTemplate);
            }
            else {
                return null;
            }
        }
        if (CommonType.NO_SHARE.getStringValue().equals(shareTemplate.getState())) {
            return null;
        }
        if (CommonType.SHARE_CHANNEL_QQ.getStringValue().equals(channel)) {
            shareTemplate.setWbShareContent(null);
            shareTemplate.setWxShareContent(null);
        }
        if (CommonType.SHARE_CHANNEL_WX.getStringValue().equals(channel)) {
            shareTemplate.setWbShareContent(null);
            shareTemplate.setQqShareContent(null);
        }
        if (CommonType.SHARE_CHANNEL_WB.getStringValue().equals(channel)) {
            shareTemplate.setWxShareContent(null);
            shareTemplate.setQqShareContent(null);
        }
        return shareTemplate;
    }
}
