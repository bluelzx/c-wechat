package com.lvtu.wechat.dao.third.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.lvtu.wechat.common.base.BaseIbatisDAO;
import com.lvtu.wechat.common.model.third.ThirdPlatform;

@Repository
public class ThirdPlatformDao extends BaseIbatisDAO {

	public ThirdPlatformDao() {
		super("3rd_platform");
	}

	public ThirdPlatform getByPrimaryKey(String id) {
		return super.get("getByPrimaryKey", id);
	}

	public List<ThirdPlatform> findAllList() {
		return super.getList("findAllList");
	}

	public List<ThirdPlatform> findList(Map<String, Object> params) {
		return super.getList("findList", params);
	}

	public void deleteById(String id) {
		super.delete("delete", id);
	}

	public void insert(ThirdPlatform platform) {
		super.insert("insert", platform);
	}

	public void update(ThirdPlatform platform) {
		super.update("update", platform);
	}
}
