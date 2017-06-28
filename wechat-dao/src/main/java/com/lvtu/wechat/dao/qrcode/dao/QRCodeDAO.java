package com.lvtu.wechat.dao.qrcode.dao;

import org.springframework.stereotype.Repository;

import com.lvtu.wechat.common.base.BaseIbatisDAO;
import com.lvtu.wechat.common.model.qrcode.QRCode;

/**
 * wx_qr_code表dao
 * @author wxlizhi
 *
 */
@Repository
public class QRCodeDAO extends BaseIbatisDAO{
    public QRCodeDAO(){
        super("QRCODE");
    }

    /**
     * 查询二维码信息
     * @param qRCode
     * @return
     */
    public QRCode selectQRcode(QRCode qRCode) {
        return super.get("selectQRcode", qRCode);
    }

    /**
     * 更新二维码信息
     * @param qRCode
     * @return
     */
    public Integer updateQRCode(QRCode qRCode) {
        return super.update("updateQRCode", qRCode);
    }

    /**
     * 根据二维码id查询二维码的信息
     * @param wxQRCodeId
     * @return
     */
    public QRCode queryQRCode(QRCode qrCode) {
        return super.get("queryQRCode", qrCode);
    }
}
