package com.lvtu.wechat.common.service.share;

import com.github.pagehelper.PageInfo;
import com.lvtu.wechat.common.hessian.RemoteService;
import com.lvtu.wechat.common.model.share.ShareImage;
import com.lvtu.wechat.common.model.share.ShareTemplate;
import com.lvtu.wechat.common.vo.back.ShareTemplateVo;

/**
 * 分享模版
 * @author qianqc
 *
 */
@RemoteService("shareTemplateService")
public interface IShareTemplateService {

    /**
     * 查询分享模版表
     * @param shareTemplateVo
     * @return 
     */
    PageInfo<ShareTemplate> selectShareTemplateList(ShareTemplateVo shareTemplateVo);

    
    /**
     * 插入分享模版
     */
    void insertShareTemplate(ShareTemplate shareTemplate);


    /**
     * 修改状态
     * @param shareTemplate
     */
    void changeState(ShareTemplate shareTemplate);

    /**
     * 查询分享模版
     * @param shareTemplate
     * @return
     */
    ShareTemplate queryShareTemplate(ShareTemplate shareTemplate);


    /**
     * 修改分享模版
     * @param shareTemplate
     */
    void updateShareTemplate(ShareTemplate shareTemplate);

    /**
     * 删除图片
     * @param shareImage
     */
    void deleteImage(ShareImage shareImage);

    
    /**
     * 根据TemplateId查询分享模版
     * @param shareTemplateTemp
     * @return
     */
    ShareTemplate queryShareTemplateByTemplateId(ShareTemplate shareTemplateTemp);
    
    /**
     * 根据分享模版id，渠道获取分享模版内容
     * @param templateId
     * @param channel
     * @return
     */
    ShareTemplate getShareTemplate(String templateId, int channel);
}
