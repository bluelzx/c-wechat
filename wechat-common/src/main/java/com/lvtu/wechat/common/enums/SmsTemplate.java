package com.lvtu.wechat.common.enums;

/**
 * 短信模板的标识
 */
public enum SmsTemplate {
	/**
	 * 正常注册
	 */
	SMS_NORMAL_REGIST_OK, /**
							 * 静默注册
							 */
	SMS_PHONE_USER_SILENT_REGIST_OK, /**
										 * 批量注册
										 */
	BATCH_REGISTER, /**
					 * 百事活动的新用户注册
					 */
	PEPSI_REGISTER, /**
					 * 百事活动用户的提醒
					 */
	PEPSI_REMIND, /**
					 * 手机密码找回
					 */
	REGISTER_MOBILE_GET_PASSWORD, /**
									 * 手机注册时发送的验证码
									 */
	SMS_REGIST_AUTHENTICATION_CODE, /**
									 * 手机验证时发送的验证码
									 */
	SMS_MOBILE_AUTHENTICATION_CODE, /**
									 * 手机找回密码验证时发送的验证码
									 */
	SMS_MOBILE_RESET_PASSWORD, /**
								 * 存款账户动态支付密码
								 */
	SMS_MONEY_ACCOUNT_DYNAMIC_PAYMENT_PASSWORD_CODE, /**
														 * 存款账户找回支付密码的校验码.
														 */
	SMS_MONEY_ACCOUNT_FIND_PAYMENT_PASSWORD_CODE, /**
													 * 用户等级升级
													 */
	SMS_MEMBER_GRADE_UPGRADE_REMIND, /**
										 * 用户等级降级提醒
										 */
	SMS_MEMBER_GRADE_DEGRADE_REMIND, /**
										 * 客户端2014世界杯活动-注册成功后发送幸运码
										 */
	SMS_MOBILE_FIFA_LUCKYCODE, /**
								 * 客户端2014世界杯活动-注册发送验证码
								 */
	SMS_MOBILE_FIFA_AUTHENTICATION_CODE, /**
											 * 客户端正常注册短信校验码
											 */
	SMS_NORMAL_AUTHENTICATION_CODE_CLIENT, /**
											 * 客户端正常注册成功
											 */
	SMS_NORMAL_REGIST_CLIENT_OK, /**
									 * 正常注册 4 客户端
									 */
	SMS_NORMAL_REGIST_OK_MOBILE, /**
									 * 客户端手机号查询订单功能
									 */
	SMS_MOBILE_FIND_ORDER_CODE, /**
								 * 客户端足彩网活动-注册发送兑换码
								 */
	SMS_MOBILE_ZUCAI_LOTTERY_CODE
};