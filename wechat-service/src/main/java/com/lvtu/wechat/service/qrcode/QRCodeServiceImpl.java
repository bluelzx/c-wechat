package com.lvtu.wechat.service.qrcode;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lvtu.wechat.common.enums.CommonType;
import com.lvtu.wechat.common.hessian.HessianService;
import com.lvtu.wechat.common.model.activity.flowActivity.FlowActivity;
import com.lvtu.wechat.common.model.activity.group.GroupActivity;
import com.lvtu.wechat.common.model.qrcode.QRCode;
import com.lvtu.wechat.common.model.qrcode.UseQRCode;
import com.lvtu.wechat.common.service.qrcode.IQRCodeService;
import com.lvtu.wechat.dao.qrcode.dao.QRCodeDAO;
import com.lvtu.wechat.dao.qrcode.dao.UseQRCodeDAO;

/**
 * @author wxlizhi
 */
@HessianService("qRCodeService")
@Service("qRCodeService")
@Transactional(readOnly = true)
public class QRCodeServiceImpl implements IQRCodeService{
    
    @Autowired
    private QRCodeDAO qRCodeDAO;
    
    @Autowired
    private UseQRCodeDAO useQRCodeDAO;
    
    /**
     * 根据活动id获取二维码
     * @param id
     * @return 
     */
    @Override
    public UseQRCode queryUseQRCode(UseQRCode useQRCode) {
        List<UseQRCode> list =  useQRCodeDAO.queryUseQRCode(useQRCode);
        if(null != list && list.size()>0){
        	return list.get(0);
        }
        return null;
    }
    
    
    /**
     * 相应活动添加二维码
     * @param groupActivity
     * @return 
     */
    @Override
    @Transactional(readOnly = false)
    public Map<String, Object> aquireQRCode(GroupActivity groupActivity ,Map<String, Object> result) {
        QRCode qRCode=new QRCode();
        qRCode.setState(CommonType.UNUSED.getValue().toString());
        qRCode=qRCodeDAO.selectQRcode(qRCode);
        if(qRCode != null){
            qRCode.setState(CommonType.USED.getValue().toString());
        }else{
            result.put("code", "-1");
            result.put("msg", "二维码已用完");
            return result;
        }
        UseQRCode useQRCode = new UseQRCode();
        useQRCode.preInsert();
        useQRCode.setWxQRCodeId(qRCode.getId());
        useQRCode.setTitle(groupActivity.getName());
        useQRCode.setUrl("https://weixin.lvmama.com/groupActivity/"+groupActivity.getId());
        useQRCode.setPicUrl(groupActivity.getPicUrl());
        useQRCode.setState(CommonType.ENABLE.getValue().toString());
        useQRCode.setActId(groupActivity.getId());
        
        Integer i=qRCodeDAO.updateQRCode(qRCode);
        if(i == 1){
            useQRCodeDAO.insertUseQRCode(useQRCode);
            result.put("qRCode", qRCode);
        }
        return result;
    }

    
    /**
     * 相应活动添加二维码(flow)
     * @param groupActivity
     * @return 
     */
    @Override
    @Transactional(readOnly = false)
    public Map<String, Object> aquireQRCode(FlowActivity flowActivity ,Map<String, Object> result) {
    	UseQRCode a= new UseQRCode();
    	a.setActId(flowActivity.getId());   	
    	useQRCodeDAO.queryUseQRCode(a);
        QRCode qRCode=new QRCode();
        qRCode.setState(CommonType.UNUSED.getValue().toString());
        qRCode=qRCodeDAO.selectQRcode(qRCode);
        if(qRCode != null){
            qRCode.setState(CommonType.USED.getValue().toString());
        }else{
            result.put("code", "-1");
            result.put("msg", "二维码已用完");
            return result;
        }
        UseQRCode useQRCode = new UseQRCode();
        useQRCode.preInsert();
        useQRCode.setWxQRCodeId(qRCode.getId());
        useQRCode.setTitle(flowActivity.getName());
        useQRCode.setUrl("https://weixin.lvmama.com/flowActivity/index/"+flowActivity.getId());
        useQRCode.setPicUrl(flowActivity.getPicUrl());
        useQRCode.setState(CommonType.ENABLE.getValue().toString());
        useQRCode.setActId(flowActivity.getId());
        
        Integer i=qRCodeDAO.updateQRCode(qRCode);
        if(i == 1){
            useQRCodeDAO.insertUseQRCode(useQRCode);
            result.put("qRCode", qRCode);
        }
        return result;
    }

    /**
     * 查询二维码的信息
     * @param wxQRCodeId
     * @return 
     */
    @Override
    public QRCode queryQRCode(QRCode qrcode) {
        return qRCodeDAO.queryQRCode(qrcode);
    }
    
    /**
     * 查询二维码对应的推送消息
     * @param qrcode
     * @return
     */
    @Override
    public UseQRCode queryUsedQrCode(QRCode qrcode) {
        qrcode = queryQRCode(qrcode);
        UseQRCode useQRCode = new UseQRCode();
        useQRCode.setWxQRCodeId(qrcode.getId());
        return queryUseQRCode(useQRCode);
    }


	@Override
	public QRCode queryQRCodeByActId(String actId) {
		UseQRCode useQRCode = new UseQRCode();
		useQRCode.setActId(actId);
		List<UseQRCode> list = useQRCodeDAO.queryUseQRCode(useQRCode);
		if(null != list && list.size()>0){
			useQRCode = list.get(0);
			if(null != useQRCode.getId()){
				QRCode qRCode = new QRCode();
				qRCode.setId(useQRCode.getWxQRCodeId());
				qRCode = qRCodeDAO.queryQRCode(qRCode);
				return qRCode;
			}
		}				
		return null;
	}
}
