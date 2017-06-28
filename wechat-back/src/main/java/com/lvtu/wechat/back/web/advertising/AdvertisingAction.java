package com.lvtu.wechat.back.web.advertising;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.lvtu.wechat.common.enums.ClassificationType;
import com.lvtu.wechat.common.model.advertising.Advertising;
import com.lvtu.wechat.common.model.advertising.AdvertisingClicks;
import com.lvtu.wechat.common.service.advertising.IAdvertisingClicksService;
import com.lvtu.wechat.common.service.advertising.IAdvertisingService;
import com.lvtu.wechat.common.utils.MemcachedUtil;
import com.lvtu.wechat.common.vo.back.AdvertisingClicksConditionVo;
import com.lvtu.wechat.common.vo.back.AdvertisingConditionVo;

/**
 * 广告位
 * 
 * @author wxlizhi
 */
@Controller
@RequestMapping("${adminPath}/advertising")
public class AdvertisingAction extends BaseActionSupport {
    @Autowired
    private IAdvertisingService advertisingService;
    
    @Autowired
    private IAdvertisingClicksService advertisingClicksService;
    
    
    /**
     * 清除广告位缓存
     * @param redirectAttrs
     * @return
     */
    @RequestMapping("/clearCache")
    public String clearCache(RedirectAttributes redirectAttrs) {
        MemcachedUtil mem = MemcachedUtil.getInstance();
        for(ClassificationType primaryType : ClassificationType.values()) {
            for(ClassificationType secondaryType : ClassificationType.values()) {
                String key=primaryType.getClassification() + "_" + secondaryType.getClassification();
                mem.remove(key);
            }
        }
        addMessage(redirectAttrs, "清除广告位缓存成功!");

        return "redirect:" + adminPath + "/advertising/manager";
    }

    /**
     * 广告位管理页面
     * 
     * @return
     */
    @RequestMapping("/manager")
    public String advertisingManager(AdvertisingConditionVo advertisingConditionVo,AdvertisingClicksConditionVo advertisingClicksConditionVo, Model model) {
    	//广告位相关
    	model.addAttribute("conditionManage", advertisingConditionVo);
        PageInfo<Advertising> pageInfo = advertisingService.queryAdvertisingList(advertisingConditionVo);
        model.addAttribute("advertisingListManage", pageInfo.getList());
        model.addAttribute("primaryClassificationList", getClassificationList(false,null, "primary"));
        List<ClassificationType> secondaryClassificationList=getSecondaryClassificationList(false,advertisingConditionVo.getPrimaryClassification());
        model.addAttribute("secondaryClassificationList", secondaryClassificationList);
        model.addAttribute("page", pageInfo);
        //点击量统计相关
        model.addAttribute("clicksConditionManage", advertisingClicksConditionVo);
        PageInfo<AdvertisingClicks> clicksPageInfo = advertisingClicksService.queryAdsClicksByTime(advertisingClicksConditionVo);
        model.addAttribute("advertisingClicksListManage", clicksPageInfo.getList());
        model.addAttribute("clicksPage", clicksPageInfo);
        model.addAttribute("tab", "manages");
        if(advertisingClicksConditionVo.getAdvertisingId() != null){
            model.addAttribute("tab", "clicks");
        }
        return "advertising/advertisingManager";
    }

    /**
     * 添加广告位
     * 
     * @return
     */
    @RequestMapping("/new")
    public String newAdvertising(AdvertisingConditionVo advertisingConditionVo,Model model) {
        List<ClassificationType> primaryClassificationList=getClassificationList(true,null, "primary");
        model.addAttribute("primaryClassificationList", primaryClassificationList);
        List<ClassificationType> secondaryClassificationList=getSecondaryClassificationList(true,primaryClassificationList.get(0).getClassification());
        model.addAttribute("secondaryClassificationList", secondaryClassificationList);
        return "advertising/advertisingForm";
    }

    /**
     * 编辑广告位
     * 
     * @param model
     * @param id
     * @return
     */
    @RequestMapping("/edit")
    public String editAdvertising(Model model, String id) {
        Advertising advertisingform = advertisingService.queryAdvertisingById(id);
        model.addAttribute("advertisingform", advertisingform);
        
        model.addAttribute("primaryClassificationList", getClassificationList(true,null, "primary"));
        List<ClassificationType> secondaryClassificationList=getSecondaryClassificationList(true,advertisingform.getPrimaryClassification());
        model.addAttribute("secondaryClassificationList", secondaryClassificationList);
        
        return "advertising/advertisingForm";
    }

    /**
     * 删除广告位
     * 
     * @param model
     * @param id
     * @return
     */
    @RequestMapping("/delete")
    public String deleteAdvertising(Model model, String id, AdvertisingConditionVo advertisingConditionVo,AdvertisingClicksConditionVo advertisingClicksConditionVo) {
        advertisingService.deleteAdvertisingById(id);
        return advertisingManager(advertisingConditionVo,advertisingClicksConditionVo,model);
    }

    /**
     * 上传图片
     * 
     * @param model
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/imageUpload")
    @ResponseBody
    public Map<String, Object> imageUpload(Model model, MultipartHttpServletRequest request,
        HttpServletResponse response) {
        String fileDiv = "_product";
        int width = 10000;// 宽度固定640
        int height = 10000;// 高度固定415
        boolean isFixed = false;// 尺寸不固定
        MultipartFile image = request.getFile("productUpload");
        UploadImg uploadImg = new UploadImg();
        return uploadImg.imageUpload(width, height, isFixed, fileDiv, image);
    }

    /**
     * 保存添加或者修改广告位
     * 
     * @param model
     * @param couponActivityform
     * @return
     */
    @RequestMapping("/save")
    public String saveAdvertising(Model model, Advertising advertisingform) {
        advertisingService.saveAdvertisingform(advertisingform);
        return "redirect:" + adminPath + "/advertising/manager";
    }

    /**
     * 根据父节点和分类级别查询的分类列表
     * 
     * @param parentId
     * @return
     */
    public List<ClassificationType> getClassificationList(boolean isNew,Integer parentId, String type) {
        ClassificationType[] classificationTypeArr = ClassificationType.values();
        List<ClassificationType> classificationList = Arrays.asList(classificationTypeArr);
        List<ClassificationType> newClassificationList = new ArrayList<ClassificationType>();
        if(!isNew)
            newClassificationList.add(ClassificationType.ALL);
        for (ClassificationType ct : classificationList) {
            if (type != null && ct.getType().equals(type)) {
                newClassificationList.add(ct);
            }else {
                if (ct.getParentId() == parentId) {
                    newClassificationList.add(ct);
                }
            }
        }
        return newClassificationList;
    }

    
    /**
     * 返回二级分类信息
     * @param model
     * @param classification
     * @return
     */
    @RequestMapping("/getClassification")
    @ResponseBody
    public Map<String, Object> getClassification(Model model, String classification,boolean isNew) {
        Map<String, Object> map = new HashMap<String, Object>();
        List<ClassificationType> newClassificationList=getSecondaryClassificationList(isNew,classification);
        List<String> classifications = new ArrayList<String>();
        for (ClassificationType nct : newClassificationList) {
            classifications.add(nct.getClassification() + "," + nct.getShowName());
        }
        map.put("classificationList", classifications);
        return map;
    }
    
    
    /**
     * 根据一级分类获得二级分类信息
     * @param classification
     * @return
     */
    public List<ClassificationType> getSecondaryClassificationList(boolean isNew,String classification){
        List<ClassificationType> newClassificationList = new ArrayList<ClassificationType>();
        if (classification == null || "".equals(classification)) {
            newClassificationList = getClassificationList(isNew,null, "secondary");
        }
        else {
            Integer id = null;
            ClassificationType[] classificationTypeArr = ClassificationType.values();
            List<ClassificationType> classificationList = Arrays.asList(classificationTypeArr);
            for (ClassificationType ct : classificationList) {
                if (ct.getClassification().equals(classification)) {
                    id = ct.getId();
                }
            }
            newClassificationList = getClassificationList(isNew,id, null);
        }
        return newClassificationList;
    }
}
