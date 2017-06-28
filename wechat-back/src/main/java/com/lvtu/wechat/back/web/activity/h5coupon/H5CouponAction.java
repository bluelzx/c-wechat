package com.lvtu.wechat.back.web.activity.h5coupon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.github.pagehelper.PageInfo;
import com.lvtu.wechat.back.base.BaseActionSupport;
import com.lvtu.wechat.back.utils.UploadImg;
import com.lvtu.wechat.common.enums.CommonType;
import com.lvtu.wechat.common.enums.ProductType;
import com.lvtu.wechat.common.enums.PublicPlatformType;
import com.lvtu.wechat.common.enums.StatusType;
import com.lvtu.wechat.common.model.activity.h5coupon.H5Coupon;
import com.lvtu.wechat.common.model.activity.h5coupon.H5CouponRcmdPrd;
import com.lvtu.wechat.common.service.activity.h5coupon.IH5CouponService;
import com.lvtu.wechat.common.vo.back.H5CouponConditionVo;

/**
 * 优惠券H5模板
 * @author qianqc
 *
 */
@Controller
@RequestMapping("${adminPath}/activity")
public class H5CouponAction extends BaseActionSupport {
    
    @Autowired
    private IH5CouponService h5CouponService;
    
    /**
     * 首页 管理页面
     * @param h5CouponConditionVo
     * @param model
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/h5coupon")
    public String index(H5CouponConditionVo h5CouponConditionVo, Model model,HttpServletRequest request,HttpServletResponse response) {
        StatusType[] statusTypeArr = StatusType.values();
        List<StatusType> statusList = Arrays.asList(statusTypeArr);
        if (StatusType.ALL.getValue().equals(h5CouponConditionVo.getState())) {
            h5CouponConditionVo.setState(null);
        }
        model.addAttribute("conditionManage", h5CouponConditionVo);
        PageInfo<H5Coupon> pageInfo = h5CouponService.queryH5CouponList(h5CouponConditionVo);
        model.addAttribute("H5CouponList", pageInfo.getList());
        model.addAttribute("pageManage", pageInfo);
        model.addAttribute("statusList", statusList);
        return "/activity/h5coupon/manage";
    }
    
    /**
     * 修改状态
     * @param id
     * @param state
     * @return
     */
    @RequestMapping("/h5coupon/changeState/{id}/{state}")
    public String changeState(@PathVariable String id, @PathVariable String state, RedirectAttributes redirectAttrs) {
        if (state.equals(String.valueOf(CommonType.ENABLE.getValue()))) {
            state = String.valueOf(CommonType.DISABLE.getValue());
            addMessage(redirectAttrs, "禁用状态成功");
        } else {
            state = String.valueOf(CommonType.ENABLE.getValue());
            addMessage(redirectAttrs, "启用状态成功");
        }
        H5Coupon h5Coupon = new H5Coupon();
        h5Coupon.setId(id);
        h5Coupon.setState(state);
        h5CouponService.changeState(h5Coupon);
        return "redirect:" + adminPath + "/activity/h5coupon";
    }
    
    /**
     * 跳转页面到添加活动
     * @param model
     * @return
     */
    @RequestMapping("/h5coupon/new")
    public String newH5Coupon(Model model) {
        PublicPlatformType[] publicPlatformTypes=PublicPlatformType.values();
        List<PublicPlatformType> publicPlatformTypeList=Arrays.asList(publicPlatformTypes);
        model.addAttribute("publicPlatformTypeList", publicPlatformTypeList);
        model.addAttribute("productList", ProductType.h5CouponProductType());
        return "/activity/h5coupon/couponAdd";
    }
    
    /**
     * 修改活动
     * @param id
     * @return
     */
    @RequestMapping("/h5coupon/edit/{id}")
    public String changeState(@PathVariable String id, Model model) {
        H5Coupon h5Coupon = new H5Coupon();
        h5Coupon.setId(id);
        h5Coupon = h5CouponService.queryH5Coupon(h5Coupon);
        PublicPlatformType[] publicPlatformTypes=PublicPlatformType.values();
        List<PublicPlatformType> publicPlatformTypeList=Arrays.asList(publicPlatformTypes);
        model.addAttribute("publicPlatformTypeList", publicPlatformTypeList);
        model.addAttribute("h5coupon", h5Coupon);
        model.addAttribute("productList", ProductType.h5CouponProductType());
        return "/activity/h5coupon/couponAdd";
    }
    
    /**
     * 活动添加
     * @param model
     * @return
     */
    @RequestMapping("/h5coupon/add")
    public String addH5Coupon(H5Coupon h5Coupon,Model model, MultipartHttpServletRequest request, HttpServletResponse response) {
        List<H5CouponRcmdPrd> h5CouponRcmdPrdList = new ArrayList<H5CouponRcmdPrd>();
        h5Coupon.setH5CouponRcmdPrdList(h5CouponRcmdPrdList);
        String prdType1 = (String) request.getParameter("prdType1");
        String prdType2 = (String) request.getParameter("prdType2");
        String rcmdId1 = (String) request.getParameter("rcmdId1");
        String rcmdId2 = (String) request.getParameter("rcmdId2");
        if (!StringUtils.isBlank(prdType1) && !StringUtils.isBlank(prdType2) && !StringUtils.isBlank(rcmdId1) && !StringUtils.isBlank(rcmdId2)) {
            H5CouponRcmdPrd h5CouponRcmdPrd1 = new H5CouponRcmdPrd();
            h5CouponRcmdPrd1.setRcmdPrdId(rcmdId1);
            h5CouponRcmdPrd1.setRcmdPrdType(prdType1);
            H5CouponRcmdPrd h5CouponRcmdPrd2 = new H5CouponRcmdPrd();
            h5CouponRcmdPrd2.setRcmdPrdId(rcmdId2);
            h5CouponRcmdPrd2.setRcmdPrdType(prdType2);
            h5CouponRcmdPrdList.add(h5CouponRcmdPrd1);
            h5CouponRcmdPrdList.add(h5CouponRcmdPrd2);
        }
        if (StringUtils.isBlank(h5Coupon.getFirstExplain())) {
            h5Coupon.setFirstExplain("恭喜您获得优惠券");   
        }
        if (StringUtils.isBlank(h5Coupon.getMultyExplain())) {
            h5Coupon.setMultyExplain("您已领取过优惠券");
        }
        h5CouponService.saveOrUpdate(h5Coupon);
        return "redirect:" + adminPath + "/activity/h5coupon";
    }
    
    /**
     * 上传图片
     * @param model
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/h5coupon/imageUpload")
    @ResponseBody
    public Map<String, Object> imageUpload(Model model,MultipartHttpServletRequest request, HttpServletResponse response) {
        String fileDiv = "_banner";
        int width = 640;//宽度固定640
        int height = 400;//高度固定400
        boolean isFixed=true;//尺寸固定
        MultipartFile image = request.getFile("productUpload");
        UploadImg uploadImg=new UploadImg();
        return uploadImg.imageUpload(width, height, isFixed, fileDiv, image);
    }
    
}
