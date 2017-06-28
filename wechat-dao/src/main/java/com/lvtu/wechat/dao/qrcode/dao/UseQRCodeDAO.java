package com.lvtu.wechat.dao.qrcode.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.lvtu.wechat.common.base.BaseIbatisDAO;
import com.lvtu.wechat.common.model.qrcode.UseQRCode;

/**
 * wx_qr_code_use表DAO
 * @author wxlizhi
 *
 */
@Repository
public class UseQRCodeDAO extends BaseIbatisDAO{
    public UseQRCodeDAO(){
        super("USE_QRCODE");
    }

    /**
     * 插入使用二维码信息
     * @param useQRCode
     */
    public void insertUseQRCode(UseQRCode useQRCode) {
        super.insert("insertUseQRCode", useQRCode);
    }

    /**
     * 查询已使用的二维码信息
     * @param id
     */
    public List<UseQRCode> queryUseQRCode(UseQRCode useQRCode) {
        return super.getList("queryUseQRCode",useQRCode);
    }

    /**
     * 更新领取二维码信息
     * @param useQRCode
     */
    public Integer update(UseQRCode useQRCode) {
        return super.update("update", useQRCode);
    }
}
