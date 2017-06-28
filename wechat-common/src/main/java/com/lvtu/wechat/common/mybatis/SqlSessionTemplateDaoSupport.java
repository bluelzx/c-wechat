package com.lvtu.wechat.common.mybatis;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * sqlSession基类
 * 
 * @author wenzhengtao
 * 
 */
public class SqlSessionTemplateDaoSupport {

	protected SqlSessionTemplate sqlSession;
	protected boolean externalSqlSession;

	protected SqlSessionTemplate sqlSessionR;
	protected boolean externalSqlSessionR;

	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSession = sqlSessionTemplate;
		this.externalSqlSession = true;
	}

	public void setSqlSessionTemplateR(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSessionR = sqlSessionTemplate;
		this.externalSqlSessionR = true;
	}

	public final SqlSession getBatchSqlSession() {
		return new SqlSessionTemplate(this.sqlSession.getSqlSessionFactory(),
				ExecutorType.BATCH);
	}

	public final SqlSession getBatchSqlSessionR() {
		return new SqlSessionTemplate(this.sqlSessionR.getSqlSessionFactory(),
				ExecutorType.BATCH);
	}

	public SqlSession getSqlSession() {
		return this.sqlSession;
	}

	public SqlSession getSqlSessionR() {
		return this.sqlSessionR;
	}

	@Autowired(required = false)
	public final void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
		if (!this.externalSqlSession) {
			this.sqlSession = new SqlSessionTemplate(sqlSessionFactory);
		}
	}

	@Autowired(required = false)
	public final void setSqlSessionFactoryR(SqlSessionFactory sqlSessionFactory) {
		if (!this.externalSqlSessionR) {
			this.sqlSessionR = new SqlSessionTemplate(sqlSessionFactory);
		}
	}
}
