package com.lvtu.wechat.common.service.weixin;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.lvtu.wechat.common.hessian.RemoteService;
import com.lvtu.wechat.common.model.productorder.ExportOrders;
import com.lvtu.wechat.common.model.weixin.WechatUser;
import com.lvtu.wechat.common.vo.back.BindCountConditionVo;

/**
 * 微信用户服务类
 * @author xuyao
 *
 */
@RemoteService("remoteWechatUserService")
public interface IWechatUserService {
	
	/**
	 * 根据openid获取用户信息
	 * @param openid
	 * @return
	 */
	public WechatUser getByOpenid(String openid);
	
	/**
	 * 根据openid获取用户信息
	 * @param unionid
	 * @return
	 */
	public WechatUser getByUnionid(String unionid);
	
	/**
	 * 保存用户信息
	 * @param wechatUser
	 * @return
	 */
	public WechatUser save(WechatUser wechatUser);
	
	/**
	 * 更新用户信息
	 * @param wechatUser
	 * @return
	 */
	public Integer update(WechatUser wechatUser);
	
	/**
	 * 根据授权后cookie中的session_id获取用户信息
	 * @param sessionId
	 * @return
	 */
	public WechatUser getBySessionId(String sessionId);
	
	
	/**
	 * 批量查询一批wechat用户
	 */
	public List<WechatUser> getWechatUserList(String num);

	/**
	 * 批量修改wx_user的is_sync字段为已经同步
	 * @param string
	 */
    public void batchUpdateIsSync(String string);

    /**
     * 批量插入wechatUserBind
     * @param wechatUserList
     */
    public void insertIntoWechatUserBind(List<WechatUser> wechatUserList);

    /**
     * 查询用户绑定情况
     * @param wechatUser
     * @return
     */
    public WechatUser queryUserBind(WechatUser wechatUser);
    
    /**
     * 单个插入wechatUserBind
    * @Title: insertIntoWechatUserBind 
    * @Description: TODO(这里用一句话描述这个方法的作用) 
    * @param @param tempWechatUser    设定文件 
    * @return void    返回类型 
    * @throws
     */
    public void insertIntoWechatUserBind(WechatUser tempWechatUser);
    
    /**
     * 
     * @Title: getUserIdList 
     * @Description: TODO(查询所有绑定用户的userId) 
     * @param @return    设定文件 
     * @return List<String>    返回类型 
     * @throws
     */
    public List<WechatUser> getUserIdList(int start, int end);

    /**
     * 
     * @Title: updateUserTags 
     * @Description: TODO(更新用户标签) 
     * @param @param userId
     * @param @param tags    设定文件 
     * @return void    返回类型 
     * @throws
     */
    public void updateUserTags(Long userId, String tags);

    
    /**
     * 统计绑定的初始主页面
     * @param bindCountConditionVo
     * @return
     */
	public PageInfo<WechatUser> queryBindUserList(
			BindCountConditionVo bindCountConditionVo);

	/**
	 * 统计绑定导出数据
	 * @return
	 */
	public List<ExportOrders> selectByExport(
			BindCountConditionVo bindCountConditionVo);


}
