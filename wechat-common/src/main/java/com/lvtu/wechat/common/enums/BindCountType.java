package com.lvtu.wechat.common.enums;

/**
 * 绑定统计类型
 * @author majun
 *
 */
public enum BindCountType {
	
	    SUM( -1,"总绑定量"),
	    WEIXIN(0 ,"微信渠道绑定量"),
	    KEFU(1 ,"客服渠道绑定量");
	    
	    
	    private Integer value;
	    
	    private String showName;

	    private BindCountType(Integer value, String showName) {
	        this.value = value;
	        this.showName = showName;
	    }
	    public String getShowName() {
	        return showName;
	    }
		public Integer getValue() {
			return value;
		}
		
		
		/**
	     * 获取微信和客服状态
	     * @return
	     */
	    public static BindCountType[] getStatusType() {
	    	BindCountType[] statusTypeArr = new BindCountType[2];
	        statusTypeArr[0] = BindCountType.WEIXIN;
	        statusTypeArr[1] = BindCountType.KEFU;
	        return statusTypeArr;
	    }

}
