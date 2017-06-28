package com.lvtu.wechat.back.web.activity.coupon;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.github.pagehelper.PageInfo;
import com.lvtu.wechat.back.base.BaseActionSupport;
import com.lvtu.wechat.back.utils.UploadImg;
import com.lvtu.wechat.common.enums.CommonType;
import com.lvtu.wechat.common.enums.StatusType;
import com.lvtu.wechat.common.model.activity.coupon.Coupon;
import com.lvtu.wechat.common.model.activity.coupon.CouponActivity;
import com.lvtu.wechat.common.service.activity.coupon.ICouponActivityService;
import com.lvtu.wechat.common.vo.back.CouponActConditionVo;

/**
 * 给驴粉发券码
 * 
 * @author qianqc & wxlizhi
 */
@Controller
@RequestMapping("${adminPath}/activity")
public class CouponAction extends BaseActionSupport {
    @Autowired
    private ICouponActivityService couponActivityService;

    /**
     * 驴粉发券码初始页面
     * 
     * @param model
     * @return
     */
    @RequestMapping("/coupon")
    public String coupon(CouponActConditionVo couponActConditionVo, Model model, HttpServletRequest request,
        HttpServletResponse response) {
        String tab = request.getParameter("tab");
        if (tab == null) {
            tabManage(couponActConditionVo, model);
            tabStatistical(couponActConditionVo, model);
            model.addAttribute("tab", "tabManage");
        }
        else if (tab.equals("tabManage")) {
            tabManage(couponActConditionVo, model);
            tabStatistical(cvNullVo(), model);
            model.addAttribute("tab", "tabManage");
        }
        else if (tab.equals("tabStatistical")) {
            tabStatistical(couponActConditionVo, model);
            tabManage(cvNullVo(), model);
            model.addAttribute("tab", "tabStatistical");
        }
        return "/activity/coupon/couponManager";
    }

    /**
     * 添加活动
     * 
     * @param model
     * @return
     */
    @RequestMapping("/coupon/new")
    public String newCouponAct(Model model) {
        return "/activity/coupon/couponForm";
    }

    /**
     * 修改活动
     * 
     * @param model
     * @param id
     * @return
     */
    @RequestMapping("/coupon/edit")
    public String editCouponAct(Model model, String id) {
        CouponActivity couponActivityform = couponActivityService.queryCouponActivityById(id);
        model.addAttribute("couponform", couponActivityform);
        return "/activity/coupon/couponForm";
    }

    /**
     * 保存添加或者修改活动
     * 
     * @param model
     * @param couponActivityform
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/coupon/save")
    public String saveCouponAct(Model model, CouponActivity couponActivityform, MultipartHttpServletRequest request,
        HttpServletResponse response) {
        MultipartFile file = request.getFile("fileTxt");
        List<Coupon> coupons = new ArrayList<Coupon>();
        try {
            String line = "";
            Coupon coupon = null;
            BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()));
            while ((line = br.readLine()) != null) {
                coupon = new Coupon();
                coupon.preInsert();
                coupon.setCouponCode(line);
                coupons.add(coupon);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        String tips = request.getParameter("tipsTxt");
        String instructions = request.getParameter("instructionsTxt");
        String imgUrl = request.getParameter("imgUrl");
        couponActivityform.setTips(tips);
        couponActivityform.setImgUrl(imgUrl);
        couponActivityform.setInstructions(instructions);

        couponActivityService.saveCouponActivityform(couponActivityform, coupons);
        return "redirect:" + adminPath + "/activity/coupon";
    }

    /**
     * 活动统计
     * 
     * @param couponActConditionVo
     * @param model
     */
    private void tabStatistical(CouponActConditionVo couponActConditionVo, Model model) {
        StatusType[] statusTypeArr = StatusType.values();
        List<StatusType> statusList = Arrays.asList(statusTypeArr);
        model.addAttribute("conditionStatistical", couponActConditionVo);
        PageInfo<CouponActivity> pageInfo = couponActivityService.queryCouponActivityList(couponActConditionVo);
        model.addAttribute("couponActListStatistical", pageInfo.getList());
        model.addAttribute("pageStatistical", pageInfo);
        model.addAttribute("statusList", statusList);
    }

    /**
     * 活动管理
     * 
     * @param couponActConditionVo
     * @param model
     */
    public void tabManage(CouponActConditionVo couponActConditionVo, Model model) {
        StatusType[] statusTypeArr = StatusType.values();
        List<StatusType> statusList = Arrays.asList(statusTypeArr);
        model.addAttribute("conditionManage", couponActConditionVo);
        PageInfo<CouponActivity> pageInfo = couponActivityService.queryCouponActivityList(couponActConditionVo);
        // PageInfo<CouponActivity> pageInfo = new PageInfo<CouponActivity>();
        model.addAttribute("couponActListManage", pageInfo.getList());
        model.addAttribute("pageManage", pageInfo);
        model.addAttribute("statusList", statusList);
    }

    /**
     * 生成空的查询条件
     * 
     * @return
     */
    public CouponActConditionVo cvNullVo() {
        CouponActConditionVo cvNullVo = new CouponActConditionVo();
        cvNullVo.setName(null);
        cvNullVo.setInviteCode(null);
        cvNullVo.setBeginDate(null);
        cvNullVo.setEndDate(null);
        return cvNullVo;
    }

    /**
     * 切换活动状态（启用或禁用）
     * 
     * @param couponActivity
     * @param redirectAttrs
     * @return
     */
    @RequestMapping("/coupon/usstate")
    public String state(CouponActivity couponActivity, RedirectAttributes redirectAttrs) {
        couponActivityService.updateState(couponActivity);
        if (couponActivity.getState().equals(CommonType.ENABLE.getStringValue())) {
            addMessage(redirectAttrs, "启用状态成功");
        }
        else {
            addMessage(redirectAttrs, "禁用状态成功");
        }

        return "redirect:" + adminPath + "/activity/coupon";
    }

    /**
     * 上传图片
     * 
     * @param model
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/coupon/imageUpload")
    @ResponseBody
    public Map<String, Object> imageUpload(Model model, MultipartHttpServletRequest request,
        HttpServletResponse response) {
        String fileDiv = "_product";
        int width = 640;// 宽度固定640
        int height = 400;// 高度固定415
        boolean isFixed = true;// 尺寸固定
        MultipartFile image = request.getFile("productUpload");
        UploadImg uploadImg = new UploadImg();
        return uploadImg.imageUpload(width, height, isFixed, fileDiv, image);
    }

}
