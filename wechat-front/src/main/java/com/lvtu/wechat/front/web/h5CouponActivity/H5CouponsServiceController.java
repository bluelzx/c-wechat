package com.lvtu.wechat.front.web.h5CouponActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lvmama.com.lvtu.common.utils.MemcachedUtil;
import com.lvmama.comm.pet.po.mark.MarkCoupon;
import com.lvmama.comm.pet.po.user.UserUser;
import com.lvmama.comm.pet.service.user.UserUserProxy;
import com.lvmama.comm.pet.service.user.UserUserProxy.USER_IDENTITY_TYPE;
import com.lvtu.wechat.common.annotation.NeedOauth;
import com.lvtu.wechat.common.enums.ProductType;
import com.lvtu.wechat.common.enums.StatusType;
import com.lvtu.wechat.common.model.activity.h5coupon.H5Coupon;
import com.lvtu.wechat.common.model.activity.h5coupon.H5CouponRcmdPrd;
import com.lvtu.wechat.common.model.activity.h5coupon.H5CouponRecord;
import com.lvtu.wechat.common.model.activity.h5coupon.ProductAdvertising;
import com.lvtu.wechat.common.model.weixin.WechatUser;
import com.lvtu.wechat.common.service.activity.h5coupon.IH5CouponService;
import com.lvtu.wechat.common.utils.AuthSmsCodeUtils;
import com.lvtu.wechat.common.utils.Constants;
import com.lvtu.wechat.common.utils.CookieUtils;
import com.lvtu.wechat.common.utils.HttpsUtil;
import com.lvtu.wechat.common.utils.JSONUtil;
import com.lvtu.wechat.front.base.BaseController;
import com.lvtu.wechat.front.utils.CouponUtils;
import com.lvtu.wechat.front.utils.PhoneUtil;
import com.lvtu.wechat.front.utils.WebchatUtil;
import com.lvtu.wechat.front.vo.CouponVO;

/**
 * @author wxlizhi
 */
@Controller
@RequestMapping("/h5CouponActivityService")
@NeedOauth
public class H5CouponsServiceController extends BaseController {

	  /**
     * 短信验证错误计数前缀,后跟openid
     */
    public static final String COUNT_WRONG_SMS_CODE_INPUT_CACHE_KEY_PREF = "wx_h5coupon_sms_code_wrong_count_";

    /**
     * 频繁操作后静默时间
     */
    public static final Integer WRONG_SMS_CODE_QUIET_SECONDS = 600;

    /**
     * 最大允许输入验证码错误次数
     */
    public static final Integer MAX_SMS_CODE_WRONG_VERIFY = 3;
    /**
     * h5couponActId的正则校验，防止注入
     */
     public static final String regex = "^([0-9]|[a-z]|[A-Z]){32}$";
    
    /**
     * 匹配losc参数的正则表达式
     */
    public static final String regexLosc="losc=[0-9]*";

    @Autowired
    private IH5CouponService h5CouponService;
    
    @Autowired
    private UserUserProxy userProxy;


    /**
     * 活动页面
     * @param h5couponActId
     * @param model
     * @param resp
     * @return
     */
    @RequestMapping("/{h5couponActId}")
    public String index(@PathVariable String h5couponActId, Model model, HttpServletRequest request, HttpServletResponse resp) {
        if (StringUtils.isNotBlank(h5couponActId) && h5couponActId.matches(regex)) {
            //活动信息
            H5Coupon h5CouponActivity = h5CouponService.queryH5CouponActivityById(h5couponActId);
            if (h5CouponActivity != null) {
                Date now = new Date();
                if (!StatusType.OPEN.getValue().equals(h5CouponActivity.getState())) {
                    model.addAttribute("activity", h5CouponActivity);
                    model.addAttribute("state", "close");
                    model.addAttribute("isNew", true);
                    return "/h5CouponActivity/indexService";
                }else if(h5CouponActivity.getStartDate() != null && now.before(h5CouponActivity.getStartDate())){
                    model.addAttribute("notOpen", true);
                    model.addAttribute("activity", h5CouponActivity);
                    model.addAttribute("isNew", true);
                    return "/h5CouponActivity/indexService";
                }else if(h5CouponActivity.getEndDate() != null && now.after(h5CouponActivity.getEndDate())){
                    model.addAttribute("activity", h5CouponActivity);
                    model.addAttribute("state", "close");
                    model.addAttribute("isNew", true);
                    return "/h5CouponActivity/indexService";
                }
                model.addAttribute("activity", h5CouponActivity);
                
                if (isGetH5couponRecord(h5couponActId, model,request)) {
                	model.addAttribute("isNew", false);
                }
                else {
                	model.addAttribute("isNew", true);
                }
                return "/h5CouponActivity/indexService";
            }
            else {
                return renderString(resp, "活动不存在!", "text/html;charset=UTF-8");
            }
        }
        else {
            return renderString(resp, "活动不存在!", "text/html;charset=UTF-8");
        }
    }
    
    /**
     * 根据Act_id和openid查询是否有领取记录，用于用于第二次进入或者多次进入
     * @param model
     * @return
     */
    private boolean isGetH5couponRecord(String h5couponActId, Model model, HttpServletRequest request) {
    	
    	  H5Coupon h5CouponActivity = h5CouponService.queryH5CouponActivityById(h5couponActId);
    	
    	  WechatUser wechatUser = getWechatUser(request);
    	  UserUser lvUser = WebchatUtil.getWechatCoopUser(wechatUser.getUnionid());
          if (wechatUser != null && wechatUser.getOpenid() != null) {
              H5CouponRecord queryParam = new H5CouponRecord();
              queryParam.setH5CouponActId(h5couponActId);
              queryParam.setOpenid(wechatUser.getOpenid());
              List<H5CouponRecord> result = h5CouponService.isAquiredH5Coupon(queryParam);
              if (result == null || result.isEmpty()) {
            	  return false;
              }
              String couponIds="";
              for(H5CouponRecord h5CouponRecord : result){
                  couponIds+=h5CouponRecord.getH5CouponCode()+",";
              }
              couponIds=couponIds.substring(0, couponIds.length()-1);
              //获取优惠券信息
              String[] coupons = couponIds.trim().split(",");
              List<CouponVO> couponVOList = CouponUtils.getCouponsByCode(coupons);
              MarkCoupon markCoupon = null;
              for (CouponVO couponVo : couponVOList) {
                  markCoupon = couponVo.getCoupon();
                  markCoupon.setDescription(markCoupon.getFavorTypeDescription());
                  logger.info("H5服务号访问页面送优惠券的详情：" + markCoupon.getFavorTypeDescription());
              }
              logger.info("H5送优惠券服务号访问页面时,获得优惠券的信息" + couponVOList);
              //获取产品信息
              List<H5CouponRcmdPrd> h5CouponRcmdPrdList = h5CouponService.queryH5CouponRcmdPrdById(h5couponActId);
              List<ProductAdvertising> productAdvertisingList = productAdvertisingInfo(h5CouponRcmdPrdList);
              logger.info("H5送优惠券服务号访问页面时，获得产品的信息" + productAdvertisingList);
             // model.addAttribute("isFollow", isFollow);
              model.addAttribute("productAdvertisingList", productAdvertisingList);
             // model.addAttribute("activity", h5CouponActivity);
              model.addAttribute("couponList", couponVOList);
              model.addAttribute("userName", lvUser.getUserName());
              model.addAttribute("title", h5CouponActivity.getMultyExplain());
              model.addAttribute("isNew", false);
              return true;
            
          }

          return false;
	}

	/**
     * 发送短信验证码
     * 
     * @param mobile
     * @return
     */
    @RequestMapping("/sendAuthSms")
    @ResponseBody
    public Object sendAuthSMS(String mobile, String imgCode, HttpServletRequest request) {
    	Map<String, Object> result = getResultMap();
        //校验入参
        if(StringUtils.isBlank(mobile)) {
        	result.put("code", "-1");
        	result.put("msg", "手机号不能为空！");
            return result;
        }
        if(!PhoneUtil.isValide(mobile)){
        	result.put("code", "-1");
        	result.put("msg", "请输入正确的手机号");
            return result;
        }
        if(StringUtils.isBlank(imgCode)) {
        	result.put("code", "-1");
        	result.put("msg", "图形验证码不能为空！");
            return result;
        }
        
      //校验图形验证码
        String lvsessionid = CookieUtils.getCookie(request, "lvsessionid");
        String storedCode = (String) MemcachedUtil.getInstance().get(Constants.WX_IMG_AUTH_CODE_CACHE_KEY_PREF + lvsessionid);
        logger.info("获取图形验证码的lvsessionid：" + lvsessionid);
        logger.info("获取图形验证码值：" + storedCode);
        if(StringUtils.isBlank(storedCode) || !imgCode.equalsIgnoreCase(storedCode)) {
        	result.put("code", "-1");
        	result.put("msg", "图形验证码输入有误，或已经过期");
        	result.put("error", true);
            return result;
        }        
        try {
			// 发送短信
			String response = AuthSmsCodeUtils.sendAuthSMS(request, mobile);
			JSONObject jo = JSONUtil.getObject(response);
			if (jo != null && jo.get("code").equals("1")) {
				result.put("code", "1");
				result.put("msg", "验证码已发送到手机");
			} else {
				result.put("code", "-1");
				result.put("msg",jo.get("message") != null ? jo.get("message") : "验证码发送失败");
			}
		} catch (Exception e) {
			result.put("code", "-1");
			result.put("msg", "验证码发送失败");
		}                                               
        return result;
        
        
        
    }

    
    /**
     * 领取优惠券
     * 
     * @return
     */
    @RequestMapping("/getCoupons")
    public Object getCoupons(RedirectAttributes redirectAttributes, Model model, String activityId, String mobile,
            String smsCode, String imgCode, HttpServletRequest request, HttpServletResponse response) {
        String jsonType = "application/json;charset=UTF-8";
        H5Coupon h5CouponActivity = h5CouponService.queryH5CouponActivityById(activityId);
        // 校验数据
        if (h5CouponActivity == null) {
            return renderString(response, "{\"code\":\"-1\", \"msg\":\"不存在的活动！\"}", jsonType);
        }
        else if (!StatusType.OPEN.getValue().equals(h5CouponActivity.getState())) {
            return renderString(response, "{\"code\":\"-1\", \"msg\":\"活动已经结束！\"}", jsonType);
        }
     // 日期校验，如果单纯的是yyyy-mm-dd格式，结束时间得做offset处理
        Date now = new Date();
        if (h5CouponActivity.getStartDate() != null && now.before(h5CouponActivity.getStartDate())) {
            return renderString(response, "{\"code\":\"-1\", \"msg\":\"活动尚未开始！\"}", jsonType);
        }
        if (h5CouponActivity.getEndDate() != null && now.after(h5CouponActivity.getEndDate())) {
            return renderString(response, "{\"code\":\"-1\", \"msg\":\"活动已经结束！\"}", jsonType);
        }
        else if (StringUtils.isBlank(mobile)) {
            return renderString(response, "{\"code\":\"-1\", \"msg\":\"手机号码不能为空！\"}", jsonType);
        }
        else if (StringUtils.isBlank(imgCode)) {
            return renderString(response, "{\"code\":\"-1\", \"msg\":\"请输入图片验证码！\"}", jsonType);
        }
        else if (StringUtils.isBlank(smsCode)) {
            return renderString(response, "{\"code\":\"-1\", \"msg\":\"请输入短信验证码！\"}", jsonType);
        }
        else if (isBusyVisit(request)) {
            return renderString(response, "{\"code\":\"-1\", \"msg\":\"您操作的太频繁了,请稍后再试。\"}", jsonType);
        }                

        WechatUser nowUser = getWechatUser(request);
        MemcachedUtil mem = MemcachedUtil.getInstance();
        if (AuthSmsCodeUtils.checkSMSCodeRedis(mobile, smsCode)) {
            // 验证成功后清除错误计数缓存
            if (nowUser != null && nowUser.getOpenid() != null) {
                mem.remove(COUNT_WRONG_SMS_CODE_INPUT_CACHE_KEY_PREF + nowUser.getOpenid());
            }
            // 检查当前手机号是否已经注册过。没有注册就通过手机号自动注册
            UserUser lvUser = userProxy.getUsersByIdentity(mobile, USER_IDENTITY_TYPE.MOBILE);
            if (lvUser == null) { // 如果没有注册进行注册
            	lvUser = WebchatUtil.regUserByMobile(mobile);
                if (lvUser == null) {
                    return renderString(response, "{\"code\":\"-1\", \"msg\":\"系统异常，请稍后再试！\"}", jsonType);
                }
            }
            
            // 获取微信用户，如果wechatUser不为空则是微信授权的用户，如果wechatUser为空则是其他浏览器打开的 // 微信绑定操作
            WechatUser wechatUser = getWechatUser(request);
            if (wechatUser != null && wechatUser.getOpenid() != null && !WebchatUtil.isBind(wechatUser)) {
                boolean isBinded = WebchatUtil.bindWxLvmama(wechatUser, lvUser);
                logger.info("给驴粉送券码绑定驴妈妈账户结果：" + isBinded);
            }
            
            //判断是否已经领取过，领取过就直接显示领取的优惠券码
            List<H5CouponRecord> result = h5CouponService.queryH5CouponReocrd(mobile, activityId);
            if (result!= null && result.size()!= 0) {
                String couponIds="";
                for(H5CouponRecord h5CouponRecord : result){
                    couponIds+=h5CouponRecord.getH5CouponCode()+",";
                }
                couponIds=couponIds.substring(0, couponIds.length()-1);
                //获取优惠券信息
                String[] coupons = couponIds.trim().split(",");
                List<CouponVO> couponVOList=CouponUtils.getCouponsByCode(coupons);
                MarkCoupon markCoupon = null;
                for (CouponVO couponVo : couponVOList) {
                    markCoupon = couponVo.getCoupon();
                    markCoupon.setDescription(markCoupon.getFavorTypeDescription());
                    logger.info("H5已领取过送优惠券的详情："+markCoupon.getFavorTypeDescription());
                }
                logger.info("H5送优惠券已经领取过,获得优惠券的信息"+couponVOList);
                //获取产品信息
                List<H5CouponRcmdPrd> h5CouponRcmdPrdList = h5CouponService.queryH5CouponRcmdPrdById(h5CouponActivity.getId());
                List<ProductAdvertising> productAdvertisingList = productAdvertisingInfo(h5CouponRcmdPrdList);
                logger.info("H5送优惠券已经领取过获得优惠券时，获得产品的信息"+productAdvertisingList);
                model.addAttribute("productAdvertisingList", productAdvertisingList);
                model.addAttribute("activity", h5CouponActivity);
                model.addAttribute("couponList", couponVOList);
                model.addAttribute("mobile", mobile);
                model.addAttribute("title", h5CouponActivity.getMultyExplain());
                  return "/h5CouponActivity/successSubscribe";
            }
            
            
            
            
        // 送优惠券给用户
        String couponIds = h5CouponActivity.getCouponCodes();
        logger.info("[H5送优惠券活动服务号]赠送优惠券开始：" + couponIds);
        String[] coupons = couponIds.trim().split(",");
        List<CouponVO> couponVOList=CouponUtils.genAndBindCoupon(coupons, lvUser);
        if(couponVOList == null || couponVOList.isEmpty()) {
        	 return renderString(response, "{\"code\":\"-1\", \"msg\":\"该活动优惠券批次号不存在!\"}", jsonType);
        }
        MarkCoupon markCoupon = null;
        for (CouponVO couponVo : couponVOList) {
            markCoupon = couponVo.getCoupon();
            markCoupon.setDescription(markCoupon.getFavorTypeDescription());
            logger.info("H5服务号首次领取送优惠券的详情："+markCoupon.getFavorTypeDescription());
        }
        logger.info("H5送优惠券服务号首次领取,获得优惠券的信息"+couponVOList);
        for(CouponVO couponVO:couponVOList){
            H5CouponRecord h5CouponRecord = new H5CouponRecord();
            h5CouponRecord.setH5CouponActId(h5CouponActivity.getId());
            h5CouponRecord.setH5CouponCode(couponVO.getCouponCode().getCouponCode());
            h5CouponRecord.setOpenid(nowUser.getOpenid());
            // 做插入操作得做id生成
            h5CouponRecord.preInsert();
            h5CouponRecord.setCreateDate(new Date());
            h5CouponRecord.setMobile(lvUser.getMobileNumber());
            h5CouponRecord.setLvmamaUserNo(lvUser.getUserNo());
            // 记录H5优惠券领取情况
            h5CouponService.insertH5CouponRecord(h5CouponRecord);
        }
        
        List<H5CouponRcmdPrd> h5CouponRcmdPrdList = h5CouponService.queryH5CouponRcmdPrdById(h5CouponActivity.getId());
        List<ProductAdvertising> productAdvertisingList = productAdvertisingInfo(h5CouponRcmdPrdList);
        logger.info("H5送优惠券服务号已经领取过获得优惠券时，获得产品的信息"+productAdvertisingList);
        model.addAttribute("productAdvertisingList", productAdvertisingList);
        logger.info("[H5送优惠券服务号活动]赠送优惠券完成！");
        model.addAttribute("couponList", couponVOList);
//      model.addAttribute("isFollow", isFollow);
        model.addAttribute("activity", h5CouponActivity);
        model.addAttribute("userName", lvUser.getUserName());
        model.addAttribute("title", h5CouponActivity.getFirstExplain());
        return "/h5CouponActivity/successService";
        }
        else {
            // 验证码校验失败处理逻辑
            if (nowUser != null && nowUser.getOpenid() != null) {
                Object countObj = mem.get(COUNT_WRONG_SMS_CODE_INPUT_CACHE_KEY_PREF + nowUser.getOpenid());
                if (null == countObj) {
                    mem.set(COUNT_WRONG_SMS_CODE_INPUT_CACHE_KEY_PREF + nowUser.getOpenid(),
                        WRONG_SMS_CODE_QUIET_SECONDS, 1);
                }
                else {
                    int count = (Integer) countObj;
                    mem.set(COUNT_WRONG_SMS_CODE_INPUT_CACHE_KEY_PREF + nowUser.getOpenid(),
                        WRONG_SMS_CODE_QUIET_SECONDS, ++count);
                }
            }
            return renderString(response, "{\"code\":\"-1\", \"msg\":\"请输入正确的短信验证码\"}", jsonType);
        }
      }


    /**
     * 查看活动规则
     * 
     * @return
     */
    @RequestMapping("/rule")
    public String rule() {
        return "/h5CouponActivity/rule";
    }

    /**
     * 是否频繁操作。 超过3次输入错误的验证码视为频繁操作
     * 
     * @return
     */
    private boolean isBusyVisit(HttpServletRequest request) {
        WechatUser nowUser = getWechatUser(request);
        if (nowUser != null && nowUser.getOpenid() != null) {
            MemcachedUtil mem = MemcachedUtil.getInstance();
            Object countObj = mem.get(COUNT_WRONG_SMS_CODE_INPUT_CACHE_KEY_PREF + nowUser.getOpenid());
            if (countObj != null && ((Integer) countObj) >= MAX_SMS_CODE_WRONG_VERIFY) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获得广告产品的信息
     * 
     * @param h5CouponRcmdPrdList
     * @return
     */
    public List<ProductAdvertising> productAdvertisingInfo(List<H5CouponRcmdPrd> h5CouponRcmdPrdList) {
        List<ProductAdvertising> productAdvertisingList = new ArrayList<ProductAdvertising>();
        ProductAdvertising productAdvertising = null;
        logger.info("[H5送优惠券服务号活动]获取产品信息开始：");
        for (int i = 0; i < h5CouponRcmdPrdList.size(); i++) {
            String rcmdPrdType = h5CouponRcmdPrdList.get(i).getRcmdPrdType();
            String rcmdPrdId = h5CouponRcmdPrdList.get(i).getRcmdPrdId();
            productAdvertising = new ProductAdvertising();
            if (ProductType.ROUTE.getValue().toString().equals(rcmdPrdType)) {
                // 线路
                logger.info("[H5送优惠券活动]获取线路产品信息开始：");                
                Map<String,Object> paramMap = new HashMap<String,Object>();
                paramMap.put("lvversion", "7.8.5");
                paramMap.put("version", "2.0.0");
                paramMap.put("firstChannel", Constants.FIRSTCHANNEL);
                paramMap.put("secondChannel", Constants.SECONDCHANNEL);
                paramMap.put("productId", rcmdPrdId);
                paramMap.put("method", "api.com.route.common.product.getRouteDetails");        		        
                String url = AuthSmsCodeUtils.genLoginUrl("http://api3g2.lvmama.com/route/router/rest.do?", paramMap);
                logger.info("线路请求url="+url);
        		try {
					String result = HttpsUtil.requestGet(url);
					logger.info("线路请求result="+result);
					JSONObject jo = JSONUtil.getObject(result);
					if(null != jo && jo.get("code").equals("1")){
						JSONObject jsonObject = jo.getJSONObject("data");
						String productName =  (String) jsonObject.get("productName");	
					    Float productPrice = null;
					    if(jsonObject.get("sellPrice") != null){
					        productPrice = Float.parseFloat(jsonObject.getString("sellPrice"));
					    }
						JSONObject shareInfoVos = jsonObject.getJSONArray("shareInfoVos").getJSONObject(0);
					    String productImgURL =(String) shareInfoVos.get("shareImageUrl");
					    String oldProductWapURL = (String) shareInfoVos.get("wapUrl");
					    String productWapURL=newWapURL(oldProductWapURL, regexLosc, Constants.H5LOSC);
					    productAdvertising = createProductAdvertising(rcmdPrdId, rcmdPrdType, productName, productPrice,
					        productImgURL, productWapURL);
					}else{
						productAdvertising=null;
					}
				} catch (Exception e) {
					e.printStackTrace();
					productAdvertising=null;
					logger.error("[H5送优惠券服务号活动]读取线路产品异常", e);
				}      
                logger.info("[H5送优惠券活动]获取线路产品信息结束：");                                            
            }
            else if (ProductType.TICKET.getValue().toString().equals(rcmdPrdType)) {    
                // 门票
                logger.info("[H5送优惠券服务号活动]获取门票产品信息开始：");
                Map<String,Object> paramMap = new HashMap<String,Object>();
                paramMap.put("lvversion", "7.8.5");
                paramMap.put("version", Constants.TOUCHVERSION);
                paramMap.put("firstChannel", Constants.FIRSTCHANNEL);
                paramMap.put("secondChannel", Constants.SECONDCHANNEL);
                paramMap.put("productId", rcmdPrdId);
                paramMap.put("method", "api.com.csa.ticket.product.getDetails");        		        
                String url = AuthSmsCodeUtils.genLoginUrl("http://api3g2.lvmama.com/nticket/router/rest.do?", paramMap);
                logger.info("门票请求url="+url);
        		try {
					String result = HttpsUtil.requestGet(url);
					logger.info("门票请求result="+result);
					JSONObject jo = JSONUtil.getObject(result);
					if(null != jo && jo.get("code").equals("1")){
						JSONObject jsonObject = jo.getJSONObject("data");						
						String productName =  (String) jsonObject.get("productName");						
					    Float productPrice = null;
					    if(jsonObject.get("sellPrice") != null){
					        productPrice = Float.parseFloat(jsonObject.getString("sellPrice"));
					    }
					    String productImgURL =(String) jsonObject.get("shareImageUrl");
					    String oldProductWapURL = (String) jsonObject.get("wapUrl");
					    String productWapURL=newWapURL(oldProductWapURL, regexLosc, Constants.H5LOSC);
					    productAdvertising = createProductAdvertising(rcmdPrdId, rcmdPrdType, productName, productPrice,
					        productImgURL, productWapURL);
					    System.out.println(productAdvertising);
					}else{
						productAdvertising=null;
					}
				} catch (Exception e) {
					e.printStackTrace();
					productAdvertising=null;
					logger.error("[H5送优惠券服务号活动]读取门票产品异常", e);
				}           		        		       		
        		logger.info("[H5送优惠券服务号活动]获取门票产品信息结束：");                 	            	                       	
            }
            else if (ProductType.VISA.getValue().toString().equals(rcmdPrdType)) {
                // 签证
                logger.info("[H5送优惠券服务号活动]获取签证产品信息开始：");
                Map<String,Object> paramMap = new HashMap<String,Object>();
                paramMap.put("lvversion", "7.8.5");
                paramMap.put("version", Constants.TOUCHVERSION);
                paramMap.put("firstChannel", Constants.FIRSTCHANNEL);
                paramMap.put("secondChannel", Constants.SECONDCHANNEL);
                paramMap.put("goodsId", rcmdPrdId);
                paramMap.put("h5Flag", "N");
                paramMap.put("method", "api.com.visa.product.getVisaDetails");        		        
                String url = AuthSmsCodeUtils.genLoginUrl("http://api3g2.lvmama.com/api/router/rest.do?", paramMap);
                logger.info("签证请求url="+url);             
                try {
					String result = HttpsUtil.requestGet(url);
					logger.info("签证请求result="+result);
					JSONObject jo = JSONUtil.getObject(result);
					if(null != jo && jo.get("code").equals("1")){
						JSONObject jsonObject = jo.getJSONObject("data");						
						String productName =  (String) jsonObject.get("productName");						
					    Float productPrice = null;
					    if(jsonObject.get("price") != null){
					        productPrice = Float.parseFloat(jsonObject.getString("price"));
					    }
					    String productImgURL =(String) jsonObject.get("imageUrl");
					    String oldProductWapURL = (String) jsonObject.get("wapUrl");
					    String productWapURL=newWapURL(oldProductWapURL, regexLosc, Constants.H5LOSC);
					    productAdvertising = createProductAdvertising(rcmdPrdId, rcmdPrdType, productName, productPrice,
					        productImgURL, productWapURL);
					}else{
						productAdvertising=null;
					}
				} catch (Exception e) {
					e.printStackTrace();
					productAdvertising=null;
					logger.error("[H5送优惠券服务号活动]读取签证产品异常", e);
				}           		        		       		
        		logger.info("[H5送优惠券服务号活动]获取签证产品信息结束：");            	
            }
            else if (ProductType.CRUISE.getValue().toString().equals(rcmdPrdType)) {
                // 游轮
                logger.info("[H5送优惠券服务号活动]获取游轮产品信息开始：");
                Map<String,Object> paramMap = new HashMap<String,Object>();
                paramMap.put("lvversion", "7.8.5");
                paramMap.put("version", Constants.TOUCHVERSION);
                paramMap.put("firstChannel", Constants.FIRSTCHANNEL);
                paramMap.put("secondChannel", Constants.SECONDCHANNEL);
                paramMap.put("productId", rcmdPrdId);
                paramMap.put("method", "api.com.ship.getProductDetail");        		        
                String url = AuthSmsCodeUtils.genLoginUrl("http://api3g2.lvmama.com/api/router/rest.do?", paramMap);
                logger.info("游轮请求url="+url);                
                try {
					String result = HttpsUtil.requestGet(url);
					logger.info("游轮请求result="+result);
					JSONObject jo = JSONUtil.getObject(result);
					if(null != jo && jo.get("code").equals("1")){
						JSONObject jsonObject = jo.getJSONObject("data");						
						String productName =  (String) jsonObject.get("productName");						
					    Float productPrice = null;
					    if(jsonObject.get("priceYuan") != null){
					        productPrice = Float.parseFloat(jsonObject.getString("priceYuan"));
					    }
					    String productImgURL =(String) jsonObject.get("shareImageUrl");
					    String oldProductWapURL = (String) jsonObject.get("wapUrl");
					    String productWapURL=newWapURL(oldProductWapURL, regexLosc, Constants.H5LOSC);
					    productAdvertising = createProductAdvertising(rcmdPrdId, rcmdPrdType, productName, productPrice,
					        productImgURL, productWapURL);
					    System.out.println(productAdvertising);
					}else{
						productAdvertising=null;
					}
				} catch (Exception e) {
					e.printStackTrace();
					productAdvertising=null;
					logger.error("[H5送优惠券服务号活动]读取游轮产品异常", e);
				}           		        		       		
        		logger.info("[H5送优惠券服务号活动]获取游轮产品信息结束：");              	            	            	            	
            }
            if(productAdvertising!=null){
                productAdvertisingList.add(productAdvertising);
            }
        }
        logger.info("[H5送优惠券服务号活动]获取产品信息结束：");
        return productAdvertisingList;
    }

    /**
     * 创建产品信息的对象
     * 
     * @param productId
     * @param productType
     * @param productName
     * @param productPrice
     * @param productImgURL
     * @param productWapURL
     * @return
     */
    public ProductAdvertising createProductAdvertising(String productId, String productType, String productName,
        Float productPrice, String productImgURL, String productWapURL) {
        ProductAdvertising productAdvertising = new ProductAdvertising();
        productAdvertising.setProductId(productId);
        productAdvertising.setProductType(productType);
        productAdvertising.setProductName(productName);
        productAdvertising.setProductPrice(productPrice);
        productAdvertising.setProductImgURL(productImgURL);
        productAdvertising.setProductWapURL(productWapURL);
        return productAdvertising;
    }
    
    
    /**
     * 添加微信h5优惠券活动的losc
     * @param wapURL
     * @param regex
     * @param newLosc
     * @return
     */
    public String newWapURL(String wapURL,String regex,String newLosc){
        if(wapURL.indexOf("losc")==-1){
            if(wapURL.indexOf("?")==-1){
                wapURL=wapURL+"?"+newLosc;
            }else{
                wapURL=wapURL+"&"+newLosc;
            }
            return wapURL;
        }else{
            Pattern pattern=Pattern.compile(regex);
            Matcher matcher=pattern.matcher(wapURL);
            return matcher.replaceAll(newLosc);
        }
    }
    
}
