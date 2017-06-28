package com.lvtu.wechat.front.web.discount;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lvmama.comm.pet.po.mark.MarkCoupon;
import com.lvmama.comm.pet.po.user.UserUser;
import com.lvtu.wechat.common.annotation.NeedOauth;
import com.lvtu.wechat.common.model.activity.discount.DiscountActivity;
import com.lvtu.wechat.common.model.activity.discount.DiscountCoupon;
import com.lvtu.wechat.common.model.activity.discount.DiscountGet;
import com.lvtu.wechat.common.model.weixin.WechatUser;
import com.lvtu.wechat.common.service.activity.discount.IDiscountActivityService;
import com.lvtu.wechat.common.utils.CookieUtils;
import com.lvtu.wechat.front.base.BaseController;
import com.lvtu.wechat.front.utils.CouponUtils;
import com.lvtu.wechat.front.utils.WebchatUtil;
import com.lvtu.wechat.front.vo.CouponVO;

/**
 * @author wxlizhi
 */
@Controller
@RequestMapping("/380Discount/old")
@NeedOauth
public class DiscountController extends BaseController {
    
    @Autowired
    private IDiscountActivityService discountActivityService;

    
    
    /**
     * 活动页面
     * @param model
     * @param resp
     * @return
     */
    @RequestMapping("/index/old")
    public String index(Model model, HttpServletRequest request, HttpServletResponse resp) {
        DiscountActivity discountActivity = discountActivityService.queryDiscountActivity();
        if(discountActivity != null){
            WechatUser wechatUser = getWechatUser(request);
            if(wechatUser != null && wechatUser.getOpenid() != null){
                boolean isBindWx = WebchatUtil.isBind(wechatUser);
                logger.info("380优惠券是否绑定驴妈妈账户："+isBindWx);
                if(!isBindWx){
                    String lvsessionid=CookieUtils.getCookie(request, "lvsessionid");
                    String result = coopLogin(lvsessionid, wechatUser, resp);
                    logger.info("380优惠券绑定驴妈妈账户的结果："+result);
                }
                UserUser lvUser = WebchatUtil.getWechatCoopUser(wechatUser.getUnionid());
                if(lvUser != null && lvUser.getUserNo() != null){
                    DiscountGet discountGet= new DiscountGet();
                    discountGet.setLvmamaUserno(lvUser.getUserNo());
                    List<String> result=discountActivityService.isAquiredDiscount(discountGet);
                    logger.info("380优惠券用户已经领取的批次号："+result);
                    judgeIsAcquired(model, result);
                    model.addAttribute("discountActivity", discountActivity);
                    return "/discount/index";
                }
            }
            model.addAttribute("discountActivity", discountActivity);
            return "/discount/index";
        }else{
            return renderString(resp, "活动不存在!", "text/html;charset=UTF-8");
        }
    }

    /**
     * 判断用户是否已经领取过
     * @param model
     * @param result
     */
    private void judgeIsAcquired(Model model, List<String> result) {
        List<String> couponCodesList= discountActivityService.queryDiscountCouponCodes();
        String[] couponCodes = new String[couponCodesList.size()];
        couponCodesList.toArray(couponCodes);
        logger.info("380优惠券数据库解析的批次号："+couponCodes);
        boolean coupon1 = false,coupon2 = false,coupon3 = false,coupon4 = false,coupon5 = false,coupon6 = false,coupon7 =false;
        for(String couponCode : result){
            if(couponCode.equals(couponCodes[0])){
                coupon1=true;
            }else if(couponCode.equals(couponCodes[1])){
                coupon2=true;
            }else if(couponCode.equals(couponCodes[2])){
                coupon3=true;
            }else if(couponCode.equals(couponCodes[3])){
                coupon4=true;
            }else if(couponCode.equals(couponCodes[4])){
                coupon5=true;
            }else if(couponCode.equals(couponCodes[5])){
                coupon6=true;
            }else if(couponCode.equals(couponCodes[6])){
                coupon7=true;
            }
        }
        model.addAttribute("coupon1", coupon1);
        model.addAttribute("coupon2", coupon2);
        model.addAttribute("coupon3", coupon3);
        model.addAttribute("coupon4", coupon4);
        model.addAttribute("coupon5", coupon5);
        model.addAttribute("coupon6", coupon6);
        model.addAttribute("coupon7", coupon7);
        model.addAttribute("couponCodes", couponCodes);
    }
    
    /**
     * 领取优惠券
     * 
     * @return
     */
    @RequestMapping("/getCoupons/old")
    @ResponseBody
    public Object getCoupons(String couponCode ,HttpServletRequest request){
        logger.info("380优惠券点击领取的优惠券批次号："+couponCode);
        Map<String, Object> result = getResult();
        String[] couponCodes=new String[]{couponCode};
        DiscountCoupon discountCoupon = new DiscountCoupon();
        discountCoupon.setCouponCodes(couponCode);
        discountCoupon = discountActivityService.queryDiscountCoupon(discountCoupon);
        WechatUser nowUser = getWechatUser(request);
        UserUser lvUser = WebchatUtil.getWechatCoopUser(nowUser.getUnionid());
        List<CouponVO> couponVOList=CouponUtils.genAndBindCoupon(couponCodes, lvUser);
        logger.info("380优惠券领取优惠券结果："+couponVOList);
        MarkCoupon markCoupon = null;
        for (CouponVO couponVo : couponVOList) {
            markCoupon = couponVo.getCoupon();
            markCoupon.setDescription(markCoupon.getFavorTypeDescription());
            logger.info("380优惠券首次领取送优惠券的详情："+markCoupon.getFavorTypeDescription());
        }
        logger.info("380优惠券服务号首次领取,获得优惠券的信息"+couponVOList);
        for(CouponVO couponVO:couponVOList){
            DiscountGet discountGet = new DiscountGet();
            discountGet.setDiscountActivityid(null);
            discountGet.setDiscountCouponcode(couponVO.getCouponCode().getCouponCode());
            discountGet.setOpenid(nowUser.getOpenid());
            // 做插入操作得做id生成
            discountGet.preInsert();
            discountGet.setCreateDate(new Date());
            discountGet.setLvmamaUserno(lvUser.getUserNo());
            discountGet.setDiscountCouponid(discountCoupon.getId());
            // 记录H5优惠券领取情况
            discountActivityService.insertDiscountGet(discountGet);
            result.put("code", "1");
            result.put("msg", "操作成功");
        }
        return result;
    } 

    public Map<String, Object> getResult() {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", "-1");
        result.put("msg", "操作失败");
        return result;
    }
    
}
