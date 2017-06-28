package com.lvtu.wechat.service.weixin;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lvtu.wechat.common.hessian.HessianService;
import com.lvtu.wechat.common.model.productorder.ExportOrders;
import com.lvtu.wechat.common.model.weixin.WechatUser;
import com.lvtu.wechat.common.service.weixin.IWechatUserService;
import com.lvtu.wechat.common.utils.Constants;
import com.lvtu.wechat.common.utils.Crypto;
import com.lvtu.wechat.common.utils.EmojiUtils;
import com.lvtu.wechat.common.utils.MemcachedUtil;
import com.lvtu.wechat.common.vo.back.BindCountConditionVo;
import com.lvtu.wechat.dao.weixin.dao.WechatUserBindDAO;
import com.lvtu.wechat.dao.weixin.dao.WechatUserDAO;

@HessianService("remoteWechatUserService")
@Service("wechatUserService")
@Transactional(readOnly = true)
public class WechatUserServiceImpl implements IWechatUserService {

	@Autowired
	private WechatUserDAO wechatUserDao;

	@Autowired
	private WechatUserBindDAO wechatUserBindDao;
	
	@Override
	public WechatUser getByOpenid(String openid) {
		if (StringUtils.isBlank(openid))
			return null;

		List<WechatUser> wechatUserList = wechatUserDao.selectByOpenid(openid);
		if (wechatUserList == null || wechatUserList.isEmpty()) {
		    return null;
		}
		return wechatUserList.get(0);
	}

	@Override
	public WechatUser getByUnionid(String unionid) {
		if (StringUtils.isBlank(unionid))
			return null;
		List<WechatUser> wechatUserList = wechatUserDao.selectByUnionid(unionid);
        if (wechatUserList == null || wechatUserList.isEmpty()) {
            return null;
        }
		return wechatUserList.get(0);
	}

	@Override
	@Transactional(readOnly = false)
	public WechatUser save(WechatUser wechatUser) {
		wechatUser.setNickname(EmojiUtils.filterName(wechatUser.getNickname()));
		if (StringUtils.isNotBlank(wechatUser.getOpenid())) {
			WechatUser existUser = this.getByOpenid(wechatUser.getOpenid());
			if (existUser == null) {
				wechatUser.preInsert();
				//判断微信新建用户，头像为空的情况
				if (wechatUser.getHeadimgurl()!= null) {
					 String img_url = wechatUser.getHeadimgurl().replace("http://", "https://");	
					 wechatUser.setHeadimgurl(img_url);
				}
			   
				wechatUser.setCreateDate(new Date());
				wechatUserDao.insert(wechatUser);
			}
		}
		return null;
	}

	@Override
	@Transactional(readOnly = false)
	public Integer update(WechatUser wechatUser) {
		if (StringUtils.isBlank(wechatUser.getId()))
			return 0;
		wechatUser.setNickname(EmojiUtils.filterName(wechatUser.getNickname()));
		//微信新建用户，头像为空的情况
		if (wechatUser.getHeadimgurl()!= null) {
			 String img_url = wechatUser.getHeadimgurl().replace("http://", "https://");	
			 wechatUser.setHeadimgurl(img_url);
		}
		
		wechatUser.setUpdateDate(new Date());
		return wechatUserDao.updateByPrimaryKey(wechatUser);
	}

	@Override
	public WechatUser getBySessionId(String sessionId) {
		if (StringUtils.isBlank(sessionId))
			return null;

		// 执行AES解密，由于为驴妈妈内部调用， 不考虑session_id过期的限制
		String randomOpenid = Crypto.decryptAES(sessionId, Constants.SECRET_KEY);
		if (StringUtils.isBlank(randomOpenid))
			return null;

		// 加密openid前会在openid后面加上时间戳yyyyMMddHHmmss,长度为14
		WechatUser wechatUser = null;
		int timestampLength = 14;
		String openid = randomOpenid.substring(0, randomOpenid.length() - timestampLength);
		if (StringUtils.isNotBlank(openid)) {
			Object userObject = MemcachedUtil.getInstance().get(openid);
			if (userObject != null) {
				wechatUser = JSONObject.parseObject(userObject.toString(), WechatUser.class);
			} else {
				wechatUser = getByOpenid(openid);
				if (wechatUser != null && StringUtils.isNotBlank(wechatUser.getOpenid())) {
					MemcachedUtil.getInstance().set(wechatUser.getOpenid(), 24 * 60 * 60,
							JSONObject.toJSONString(wechatUser));
				}
			}
		}
		return wechatUser;
	}


    /**
     * 查询num条wechatUser
     * @param num
     * @return
     */
    @Override
    public List<WechatUser> getWechatUserList(String num) {
        return wechatUserDao.getWechatUserList(num);
    }

    /**
     * 批量修改wx_user的is_sync字段为已经同步
     * @param wechatUserIds
     */
    @Transactional(readOnly = false)
    public void batchUpdateIsSync(String wechatUserIds) {
        wechatUserDao.batchUpdateIsSync(wechatUserIds);
    }

    @Override
    @Transactional(readOnly = false)
    public void insertIntoWechatUserBind(List<WechatUser> wechatUserList) {
    	  for (WechatUser WechatUsertemp : wechatUserList) {
    		  WechatUsertemp.preInsert();
        	  Date now = new Date();
        	  WechatUsertemp.setCreateDate(now);
    		  wechatUserBindDao.batchInsertWechatUserBind(WechatUsertemp);
    	  }
        
    }

    /**
     * 查询用户绑定情况
     */
    @Override
    public WechatUser queryUserBind(WechatUser wechatUser) {
        WechatUser wechatUserTemp = wechatUserBindDao.queryUserBind(wechatUser);
        if(wechatUserTemp != null) {
            wechatUser.setUserId(wechatUserTemp.getUserId());
        }
       
        return wechatUser;
      
    }
    

    /**
     * 单个插入wx_user_bind
     */
    @Override
    @Transactional(readOnly = false)
    public void insertIntoWechatUserBind(WechatUser tempWechatUser) {
        //查询数据库是否已经存在记录
        WechatUser wechatUser = wechatUserBindDao.queryUserBind(tempWechatUser);
        if (wechatUser != null) {
            if (tempWechatUser.getUserId().equals(wechatUser.getUserId())) {
            	wechatUserBindDao.updateUserId(tempWechatUser);
            }
            else {
            	return;
            }
        }
        else {
            List<WechatUser> list = new ArrayList<WechatUser>();
            list.add(tempWechatUser);
            for (WechatUser WechatUsertemp : list) {
            	  WechatUsertemp.preInsert();
            	  Date now = new Date();
            	  WechatUsertemp.setCreateDate(now);
            	  wechatUserBindDao.batchInsertWechatUserBind(WechatUsertemp);
            }
           
        }
    }
       
    /**
     * 查询所有绑定用户的userId
     */
    @Override
    public List<WechatUser> getUserIdList(int start, int end) {
        List<WechatUser> list = wechatUserBindDao.getUserIdList(start, end);
        return list;
    }
      
    /**
     * 更新用户标签
     */
    @Override
    @Transactional(readOnly = false)
    public void updateUserTags(Long userId, String tags) {
        wechatUserBindDao.updateUserTags(userId, tags);
    }

    /**
     * 绑定统计的初始主页面
     */
	@Override
	public PageInfo<WechatUser> queryBindUserList(BindCountConditionVo bindCountConditionVo) {
		
		PageHelper.startPage(bindCountConditionVo.getPage(), bindCountConditionVo.getPageSize());
		
		if (!"-1".equals(bindCountConditionVo.getChannel())) {
			//根据条件查询微信或者客服渠道的绑定
			 List<WechatUser> bindUserList = wechatUserBindDao.querybindUserList(bindCountConditionVo);
			 PageInfo<WechatUser> pageInfo = new PageInfo<WechatUser>(bindUserList);
			 return pageInfo;
		} else {
			//查询总的绑定量
			List<WechatUser> bindUserList = wechatUserBindDao.queryAllbindUserList(bindCountConditionVo);
			PageInfo<WechatUser> pageInfo = new PageInfo<WechatUser>(bindUserList);
			return pageInfo;
		}
		
	}
	

	/**
	 * 统计绑定导出数据
	 */
	@Override
	public List<ExportOrders> selectByExport(BindCountConditionVo bindCountConditionVo) {
		if (!"-1".equals(bindCountConditionVo.getChannel())) {
			List<ExportOrders> list = wechatUserBindDao.selectByExport(bindCountConditionVo);
			return list;
		} else {
			List<ExportOrders> list = wechatUserBindDao.selectAllByExport(bindCountConditionVo);
			return list;
		}
		
	}
}
