package com.lvtu.wechat.dao.weixin.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.lvtu.wechat.common.base.BaseIbatisDAO;
import com.lvtu.wechat.common.model.weixin.WechatUser;


@Repository
public class WechatUserDAO extends BaseIbatisDAO {

	public WechatUserDAO() {
		super("WX_USER");
	}

	/**
	 * 根据openid获取用户信息
	 * @param openid
	 * @return
	 */
	public List<WechatUser> selectByOpenid(String openid) {
		return getList("selectByOpenid", openid);
	}
	
	/**
	 * 根据unionid获取用户信息
	 * @param openid
	 * @return
	 */
	public List<WechatUser> selectByUnionid(String unionid) {
		return getList("selectByOpenid", unionid);
	}

	/**
	 * 插入一条流量数据
	 * @param flow
	 * @return
	 */
	public WechatUser insert(WechatUser wechatUser) {
		return (WechatUser) insert("insertSelective", wechatUser);
	}

	/**
	 * 更新流量信息
	 * @param flow
	 * @return
	 */
	public Integer updateByPrimaryKey(WechatUser wechatUser) {
		return update("updateByPrimaryKeySelective", wechatUser);
	}

	/**
	 * 查询num条wechatUser
	 * @param num
	 * @return
	 */
    public List<WechatUser> getWechatUserList(String num) {
        return getList("getWechatUserList", num);
    }

    /**
     * 批量修改wx_user的is_sync字段为已经同步
     * @param wechatUserIds
     */
    public void batchUpdateIsSync(String wechatUserIds) {
        update("batchUpdateIsSync", wechatUserIds);
    }

}