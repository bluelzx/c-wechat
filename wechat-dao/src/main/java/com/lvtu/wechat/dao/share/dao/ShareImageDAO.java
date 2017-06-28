package com.lvtu.wechat.dao.share.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.lvtu.wechat.common.base.BaseIbatisDAO;
import com.lvtu.wechat.common.model.share.ShareImage;

/**
 * 分享模版对应图片表DAO层
 * @author qianqc
 *
 */
@Repository
public class ShareImageDAO extends BaseIbatisDAO {
    public ShareImageDAO() {
        super("SHARE_IMAGE");
    }
    
    public List<ShareImage> queryShareImage(ShareImage shareImageParam) {
        return super.queryForList("queryShareImage", shareImageParam);
    }
    
    public void deleteShareImage(ShareImage shareImage) {
        super.delete("deleteShareImage", shareImage);
    }
    
    public void insert(List<ShareImage> shareImageList) {
        super.insert("insertShareImage", shareImageList);
    }

    public void delete(ShareImage shareImageParam) {
        super.delete("delete", shareImageParam);
    }
}
