package com.lvtu.wechat.dao.share.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.lvtu.wechat.common.base.BaseIbatisDAO;
import com.lvtu.wechat.common.model.share.ShareTemplate;
import com.lvtu.wechat.common.vo.back.ShareTemplateVo;

/**
 * 分享模版表dao层
 * @author qianqc
 *
 */
@Repository
public class ShareTemplateDAO extends BaseIbatisDAO {
    public ShareTemplateDAO() {
        super("SHARE_TEMPLATE");
    }
    
    /**
     * 查询分享模版集合
     * @param shareTemplateVo
     * @return
     */
    public List<ShareTemplate> selectShareTemplateList(ShareTemplateVo shareTemplateVo) {
        return super.queryForList("selectShareTemplateList", shareTemplateVo);
    }   
    
    /**
     * 查询单个分享模版
     * @param share
     * @return
     */
    public ShareTemplate selectShareTemplate(ShareTemplate shareTemplate) {
        return super.get("selectShareTemplate", shareTemplate);
    }
    
    /**
     * 插入分享模版
     * @param shareTemplate
     */
    public void insetShareTemplate(ShareTemplate shareTemplate) {
        super.insert("insertShareTemplate", shareTemplate);
    }
    
    /**
     * 修改状态
     */
    public void update(ShareTemplate shareTemplate) {
        super.update("update", shareTemplate);
    }
}
