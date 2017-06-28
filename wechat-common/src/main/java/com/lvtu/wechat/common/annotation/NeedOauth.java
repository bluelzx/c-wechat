package com.lvtu.wechat.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记需要微信授权才能访问
 * 
 * @author xuyao
 *
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface NeedOauth {
	
	/**
	 * 是否强制授权，
	 * 若为true，不管是微信浏览器还是其他浏览器都需要授权
	 * 若为false，只有在微信流量器打开时才进行授权
	 * @return
	 */
	boolean isForceOauth() default true;
	
	boolean isSnsapiOauth() default true;
}
