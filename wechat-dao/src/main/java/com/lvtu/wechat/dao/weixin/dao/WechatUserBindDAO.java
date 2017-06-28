package com.lvtu.wechat.dao.weixin.dao;

import java.util.List;

import org.apache.ibatis.binding.MapperMethod.ParamMap;
import org.springframework.stereotype.Repository;

import com.lvtu.wechat.common.base.BaseIbatisDAO;
import com.lvtu.wechat.common.model.productorder.ExportOrders;
import com.lvtu.wechat.common.model.weixin.WechatUser;
import com.lvtu.wechat.common.vo.back.BindCountConditionVo;


@Repository
public class WechatUserBindDAO extends BaseIbatisDAO {

    public WechatUserBindDAO() {
        super("WX_USER_BIND");
    }
    
//    /**
//     * 批量修改wx_user的is_sync字段为已经同步
//     * @param wechatUserIds
//     */
//    public void batchInsertWechatUserBind(List<WechatUser> wechatUserList) {
//        super.insert("batchInsertWechatUserBind", wechatUserList);
//    }
    
    /**
     * 查询用户绑定情况
     * @param wechatUser
     */
    public WechatUser queryUserBind(WechatUser wechatUser) {
        return super.get("queryUserBind", wechatUser);
    }
    
    /**
     * 更新wx_user_bind，针对 解绑用户，避免出现冗余数据
     * @param wechatUser
     */
    public int updateUserId(WechatUser wechatUser) {
        return super.update("updateUserId", wechatUser);
    }

    /**
     * @param end 
     * @param start 
     * 
     * @Title: getUserIdList 
     * @Description: TODO(查询所有绑定用户的userId) 
     * @param @return    设定文件 
     * @return List<String>    返回类型 
     * @throws
     */
    public List<WechatUser> getUserIdList(int start, int end) {
        ParamMap<String> map = new ParamMap<String>();
        map.put("start", start + "");
        map.put("end", end + "");
        return super.getList("queryUserBindList", map);
    }

    /**
     * 
     * @Title: updateUserTags 
     * @Description: TODO(更新用户标签) 
     * @param @param userId
     * @param @param tags    设定文件 
     * @return void    返回类型 
     * @throws
     */
    public void updateUserTags(Long userId, String tags) {
        ParamMap<String> map = new ParamMap<String>();
        map.put("userid", userId + "");
        map.put("tags", tags);
        super.update("updateUserTag", map);
    }

    /**
     * 绑定统计的主页面
     * @param bindCountConditionVo
     * @return
     */
	public List<WechatUser> querybindUserList(BindCountConditionVo bindCountConditionVo) {
		return super.getList("querybindUserList", bindCountConditionVo);
	}

	/**
	 * 根据条件绑定统计导出数据
	 * @return
	 */

	public List<ExportOrders> selectByExport(BindCountConditionVo bindCountConditionVo) {
		return super.getList("selectByExport", bindCountConditionVo);
	}
	
	/**
	 * 导出所有的数据
	 * @param bindCountConditionVo
	 * @return
	 */
	public List<ExportOrders> selectAllByExport(BindCountConditionVo bindCountConditionVo) {
		return super.getList("selectAllByExport", bindCountConditionVo);
	}

	/**
	 * 总的绑定量统计
	 * @param bindCountConditionVo
	 * @return
	 */
	public List<WechatUser> queryAllbindUserList(
			BindCountConditionVo bindCountConditionVo) {
		return super.getList("queryAllbindUserList", bindCountConditionVo);
	}

	 /**
     * 批量修改wx_user的is_sync字段为已经同步
     * @param wechatUserIds
     */
	public void batchInsertWechatUserBind(WechatUser wechatUsertemp) {
		 super.insert("batchInsertWechatUserBind", wechatUsertemp);
	}

}