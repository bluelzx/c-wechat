package com.lvtu.wechat.dao.share.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.lvtu.wechat.common.base.BaseIbatisDAO;
import com.lvtu.wechat.common.model.share.ShareContent;

/**
 * 分享模版相关内容表dao层
 * @author qianqc
 *
 */
@Repository
public class ShareContentDAO extends BaseIbatisDAO {
    public ShareContentDAO() {
        super("SHARE_CONTENT");
    }
    
    
    /**
     * 查询分享内容
     */
    public List<ShareContent> queryShareContent(ShareContent shareContent) {
        return super.queryForList("queryShareContent", shareContent);
    }
    
    
   /**
    * 更新分享内容
    */
    public void update(ShareContent shareContent) {
        super.update("updateShareContent", shareContent);
    }
    
    /**
     * 插入分享内容
     * @param shareContent
     */
    public void insert(ShareContent shareContent) {
        super.insert("insertShareContent", shareContent);
    }


    public void delete(ShareContent shareContentParam) {
        super.delete("delete", shareContentParam);
    }
}
