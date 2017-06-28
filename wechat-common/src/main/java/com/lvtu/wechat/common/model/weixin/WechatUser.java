package com.lvtu.wechat.common.model.weixin;


import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.lvtu.wechat.common.base.BaseModel;
import com.lvtu.wechat.common.utils.EmojiFilter;

/**
 * 微信用户信息
 * @author xuyao
 *
 */
public class WechatUser extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4665708080124635499L;
	
	/**
	 * 关注状态
	 */
	private String subscribe;
	
	/**
	 * 用户openid
	 */
	private String openid;
	
	/**
	 * 用户昵称
	 */
	private String nickname;
	
	/**
	 * 用户性别
	 */
	private Integer sex;
	
	/**
	 * 省份
	 */
	private String province;
	
	/**
	 * 城市
	 */
	private String city;
	
	/**
	 * 街道
	 */
	private String country;
	
	/**
	 * 语言
	 */
	private String language;
	
	/**
	 * 头像Url
	 */
	private String headimgurl;
	
	/**
	 * unionid
	 */
	private String unionid;
	
	/**
	 * 如果微信公众号用户与驴妈妈账户绑定的话    userid 对应 lvmama_user.user_user表下的user_id字段
	 */
	private Long userId;
	
    /**
     * 创建时间
     */
    private Date createDate;
    
    /**
     * 更新时间
     */
    private Date updateDate;
    
    /**
     *  关注渠道
     */
    private String channel;
    
    /**
     * 用户标签
     */
    private String tags;
    
	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		//过滤昵称中的emoji表情
		nickname = EmojiFilter.filterEmoji(nickname);
		if(StringUtils.isBlank(nickname))
			nickname="微信用户";
		this.nickname = nickname;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getHeadimgurl() {
		return headimgurl;
	}

	public void setHeadimgurl(String headimgurl) {
		this.headimgurl = headimgurl;
	}

	public String getUnionid() {
		return unionid;
	}

	public void setUnionid(String unionid) {
		this.unionid = unionid;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getSubscribe() {
		return subscribe;
	}

	public void setSubscribe(String subscribe) {
		this.subscribe = subscribe;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }
    
	public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }
}
