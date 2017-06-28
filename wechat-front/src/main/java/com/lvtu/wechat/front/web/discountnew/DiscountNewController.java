package com.lvtu.wechat.front.web.discountnew;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lvmama.comm.pet.po.mark.MarkCoupon;
import com.lvmama.comm.pet.po.user.UserUser;
import com.lvtu.wechat.common.annotation.FreqRequestLimit;
import com.lvtu.wechat.common.annotation.NeedOauth;
import com.lvtu.wechat.common.enums.CommonType;
import com.lvtu.wechat.common.model.activity.discountnew.DiscountActivityNew;
import com.lvtu.wechat.common.model.activity.discountnew.DiscountCouponNew;
import com.lvtu.wechat.common.model.activity.discountnew.DiscountGetNew;
import com.lvtu.wechat.common.model.weixin.WechatUser;
import com.lvtu.wechat.common.service.activity.discountnew.IDiscountActivityNewService;
import com.lvtu.wechat.front.base.BaseController;
import com.lvtu.wechat.front.utils.CouponUtils;
import com.lvtu.wechat.front.utils.WebchatUtil;
import com.lvtu.wechat.front.vo.CouponVO;

/**
 * @author majun
 */
@Controller
@RequestMapping("/380Discount")
@NeedOauth
public class DiscountNewController extends BaseController {
    
    @Autowired
    private IDiscountActivityNewService discountActivityNewService;
    
    /**
	 * 380未绑定时，去进行绑定，绑定链接
	 */
	@SuppressWarnings("unused")
    private final static String DISCOUNT_BIND_URL = "https://m.lvmama.com/weixinAccountControl_Before.htm";
	
	/**
	 *  返回url:https://weixin.lvmama.com/380Discount/index的Base64编码
	 */
	private static String callbackUrl = "aHR0cHM6Ly93ZWl4aW4ubHZtYW1hLmNvbS8zODBEaXNjb3VudC9pbmRleA==";
	
    /**
     * 活动页面
     * @param model
     * @param resp
     * @return
     */
    @RequestMapping("/index")
    @FreqRequestLimit(setToken=true)
    public String index(Model model, HttpServletRequest request, HttpServletResponse resp, String isTest) {
        WechatUser wechatUser = getWechatUser(request);
        boolean isBindWx = WebchatUtil.isBind(wechatUser);
        //判断用户是否绑定，如果未绑定，则进入绑定页面
        logger.info("380优惠券是否绑定驴妈妈账户："+isBindWx);
        if(!isBindWx){
        	
        	   model.addAttribute("wechatUser", wechatUser);
               model.addAttribute("callbackUrl", callbackUrl);//绑定后回调链接
               return "/wechat/accountbing";
        }
        
        //判断是否是运营测试，运营测试isTest参数不为空，运营测试和线上用户使用查询参数不一致
        DiscountCouponNew discountCoupon = new DiscountCouponNew();
        if (StringUtils.isBlank(isTest)) {
        	discountCoupon.setState("'2','3'");
        	discountCoupon.setOrderBy("curr_order");
        }
        else {
        	discountCoupon.setState("'1','2'");
        	discountCoupon.setOrderBy("next_order");
        }
    	DiscountActivityNew discountActivity = discountActivityNewService.queryDiscountActivityNew();
        if(discountActivity != null) {
        	 
        	//获取当前所有可以领取的优惠券
        	List<DiscountCouponNew>  DiscountCouponList = discountActivityNewService.queryallDiscountCouponNew(discountCoupon);
        
            DiscountGetNew discountGet= new DiscountGetNew();
            discountGet.setOpenid(wechatUser.getOpenid());
            //获取用户已领取的所有的优惠券
            List<DiscountGetNew> discountGetList= discountActivityNewService.getAquireCoupons(discountGet);

            //判断当前优惠券用户是否已经领取过，若领取过isAquire参数则为1
            aquireCoupons(DiscountCouponList, discountGetList, discountActivity);
            model.addAttribute("discountActivity", discountActivity);
            model.addAttribute("DiscountCouponList", DiscountCouponList);
            return "/discountnew/index";
        }
        else{
            return renderString(resp, "活动不存在!", "text/html;charset=UTF-8");
        }
    }
    
    
    
	  /**
	   * 
	  * @Title: aquireCoupons 
	  * @Description: TODO(判断并设置优惠券已领取属性) 
	  * @param @param discountCouponList
	  * @param @param discountGetList
	  * @param @param discountActivity    设定文件 
	  * @return void    返回类型 
	  * @throws
	   */
    private void aquireCoupons(List<DiscountCouponNew> discountCouponList, List<DiscountGetNew> discountGetList, DiscountActivityNew discountActivity) {
    	
    	//取出活动周期，与优惠券的createDate进行对比，小于可以领取
   	 	String day = discountActivity.getPeriod();
   	 	int dayInt = Integer.parseInt(day);
   		Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -dayInt);
        Date offsetDate = cal.getTime();
        
        List<String> couponIdList = new ArrayList<String>();
        
    	for (DiscountGetNew aquiredCoupon : discountGetList) {
    		if (aquiredCoupon.getCreateDate().before(offsetDate)) {
    			continue;
    		}
    		couponIdList.add(aquiredCoupon.getDiscountCouponid());
    	}
    	
    	for (DiscountCouponNew coupon : discountCouponList) {
    		if (couponIdList.contains(coupon.getId())) {
    			coupon.setIsAquire(CommonType.USED.getStringValue());
    			logger.info("优惠券领取的状态"+CommonType.USED.getStringValue());
    		}
    		else {
    			coupon.setIsAquire(CommonType.UNUSED.getStringValue());
    			logger.info("优惠券领取的状态"+CommonType.UNUSED.getStringValue());
    		}
    	}
	}
    
    

    /**
    * 
    * @Title: getCoupons 
    * @Description: TODO(点击领取优惠券) 
    * @param @param id
    * @param @param request
    * @param @return    设定文件 
    * @return Object    返回类型 
    * @throws
     */
    @RequestMapping("/getCoupons")
    @ResponseBody
    @FreqRequestLimit
    public Object getCoupons(String id, String couponCode , HttpServletRequest request){
    	logger.info("380优惠券点击领取的优惠券批次号："+couponCode);
    	//获取微信用户
        WechatUser nowUser = getWechatUser(request);
        String openid = nowUser.getOpenid();
        Map<String, Object> result = getResult();
        try {
        	  getCoupons(id, couponCode, request, result, nowUser);
        } catch (Exception e) {
            logger.error("380优惠券领取异常", e);
        }
        finally {
        	 resetToken(request, openid);
        }
        return result;
    }



	private void getCoupons(String id, String couponCode, HttpServletRequest request, Map<String, Object> result, WechatUser nowUser) {
          //获取对应的驴妈妈账户
          UserUser lvUser = WebchatUtil.getWechatCoopUser(nowUser.getUnionid());
          String openid = nowUser.getOpenid();
          logger.info("380优惠券点击领取的用户openid：" + openid);
          String[] couponCodes=new String[]{couponCode};
          DiscountGetNew discountGet= new DiscountGetNew();
          discountGet.setOpenid(openid);
          discountGet.setDiscountCouponcode(couponCode);
          List<DiscountGetNew> discountGetList= discountActivityNewService.getAquireCoupons(discountGet);
          List<String> couponIdList = new ArrayList<String>();
          for (DiscountGetNew aquiredCoupon : discountGetList ) {
          	if (discountGetList.contains(aquiredCoupon.getDiscountCouponid()))
          		couponIdList.add(aquiredCoupon.getDiscountCouponid());
          }
  		  if (couponIdList != null && !couponIdList.isEmpty()) {
		  	    	result.put("code", "-1");
		  	        result.put("msg", "您已领取过该优惠券");
  		  } else {
  		        List<CouponVO> couponVOList=CouponUtils.genAndBindCoupon(couponCodes, lvUser);
  		        if (couponVOList == null || couponVOList.isEmpty()) {
  		          result.put("code", "-2");
  		          result.put("msg", "优惠券批次号不存在！");
  		          return;
  		        }
  		        logger.info("380优惠券领取优惠券结果："+couponVOList);
  		        MarkCoupon markCoupon = null;
  		        for (CouponVO couponVo : couponVOList) {
  		            markCoupon = couponVo.getCoupon();
  		            markCoupon.setDescription(markCoupon.getFavorTypeDescription());
  		            logger.info("380优惠券首次领取送优惠券的详情："+markCoupon.getFavorTypeDescription());
  		        }
  		        logger.info("380优惠券服务号首次领取,获得优惠券的信息"+couponVOList);
  		        
  		       for(CouponVO couponVO:couponVOList){
  	                    discountGet.setDiscountCouponcode(couponVO.getCouponCode().getCouponCode());
  			            
  	                    // discountGet.setDiscountCouponcode(couponCode);用于在本地进行测试
  			            discountGet.setOpenid(nowUser.getOpenid());
  			            discountGet.preInsert();
  			            discountGet.setCreateDate(new Date());
  			            discountGet.setLvmamaUserno(lvUser.getUserNo());
  			            discountGet.setDiscountCouponid(id);
  			    	    discountActivityNewService.insertDiscountGetNew(discountGet);
  			            result.put("code", "1");
  			            result.put("msg", "操作成功");
          	 }
         }
	} 

    public Map<String, Object> getResult() {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", "-1");
        result.put("msg", "操作失败");
        return result;
    }
    
}
