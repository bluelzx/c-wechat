package com.lvtu.wechat.common.service.qrcode;

import java.util.Map;

import com.lvtu.wechat.common.hessian.RemoteService;
import com.lvtu.wechat.common.model.activity.flowActivity.FlowActivity;
import com.lvtu.wechat.common.model.activity.group.GroupActivity;
import com.lvtu.wechat.common.model.qrcode.QRCode;
import com.lvtu.wechat.common.model.qrcode.UseQRCode;

/**
 * 二维码使用
 * @author wxlizhi
 *
 */
@RemoteService("qRCodeService")
public interface IQRCodeService {
    
    /**
     * 根据活动id查询二维码信息
     * @param id
     * @return
     */
    UseQRCode queryUseQRCode(UseQRCode useQRCode);
    
    /**
     * 相应活动添加二维码
     * @param groupActivity
     * @return
     */
    Map<String, Object> aquireQRCode(GroupActivity groupActivity,Map<String, Object> result);
    
    /**
     * 相应活动添加二维码(flow)
     * @param groupActivity
     * @return
     */
    Map<String, Object> aquireQRCode(FlowActivity flowActivity,Map<String, Object> result);

    /**
     * 根据二维码id查询二维码的信息
     * @param wxQRCodeId
     * @return
     */
    QRCode queryQRCode(QRCode qrcode );
    
    
    /** 
    * @Title: queryQRCodeByActId 
    * @Description: 根据活动id查询二维码信息 
    * @param @param actId
    * @param @return    设定文件 
    * @return QRCode    返回类型 
    * @throws 
    */
    QRCode queryQRCodeByActId(String actId);
    
    /**
     * 查询二维码对应的推送消息
     * @param qrcode
     * @return
     */
    UseQRCode queryUsedQrCode(QRCode qrcode);

}
