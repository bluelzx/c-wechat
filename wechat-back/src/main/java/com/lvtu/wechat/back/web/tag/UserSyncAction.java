package com.lvtu.wechat.back.web.tag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lvmama.comm.pet.service.user.UserCooperationUserService;
import com.lvmama.crm.service.HbaseUserPortraitInfoDubboService;
import com.lvtu.wechat.back.base.BaseActionSupport;
import com.lvtu.wechat.back.utils.Constants;
import com.lvtu.wechat.common.model.weixin.WechatUser;
import com.lvtu.wechat.common.service.weixin.IWechatUserService;
import com.lvtu.wechat.common.spring.SpringBeanProxy;

@Controller
@RequestMapping("${adminPath}/userSync")
public class UserSyncAction extends BaseActionSupport  {
    
    /**
     * 用户信息同步初始页面
     * @param model
     * @return
     */
    @RequestMapping("/index")
    public String index() {
        return "/tag/userSync";
    }
    
    /**
     * 
    * @Title: userTagSync 
    * @Description: TODO(同步用户标签) 
    * @param @param model    设定文件 
    * @return void    返回类型 
    * @throws
     */
    @RequestMapping(value = "/userTagSync", method = RequestMethod.POST)
    @ResponseBody
    private void userTagSync(Model model) {
       //查询所有的模型和标签
       logger.info("同步用户对应的标签");
       IWechatUserService wechatUserService = SpringBeanProxy.getBean(IWechatUserService.class, "remoteWechatUserService");
       HbaseUserPortraitInfoDubboService hbaseUserPortraitInfoDubboService = SpringBeanProxy.getBean(HbaseUserPortraitInfoDubboService.class, "hbaseUserPortraitInfoDubboService");
       int startIndex = 0;
       int num = Integer.parseInt(Constants.getConfig("WXUSER_SYNC_LVUSER_NUM"));
       int endIndex = num;
       
       List<WechatUser> wechatUserList = getUserIdList(wechatUserService, startIndex, endIndex);
       List<Long> userIdList = new ArrayList<Long>();
       Map<Long, String> userIdTagMap = new HashMap<Long, String>();
       String tags = null;
       while (wechatUserList != null && wechatUserList.size() != 0) {
           parseUserIdTag(wechatUserList, userIdList, userIdTagMap);
           if (userIdList == null || userIdList.isEmpty()) {
               break;
           }
           Map<Long,List<String>> resultMap = hbaseUserPortraitInfoDubboService.bulkGetUserTagsByUserIds(userIdList);
           logger.info(resultMap.size());
           for (Long userId : userIdList) {
               tags = resultMap.get(userId).toString().replace("[", "").replace("]", "");
               if (tags.equals(userIdTagMap.get(userId))) {
                   continue;
               }
               wechatUserService.updateUserTags(userId, tags);
           }
           if (wechatUserList.size() != num) {
               break;   
           }
           startIndex = endIndex + 1;
           endIndex = endIndex + num;
           wechatUserList = getUserIdList(wechatUserService, startIndex, endIndex);
       }
    }
        
    /**
     * 
     * @Title: parseUserIdTag 
     * @Description: TODO(构造需要的list和map) 
     * @param @param wechatUserList
     * @param @param userIdList
     * @param @param userIdTagMap    设定文件 
     * @return void    返回类型 
     * @throws
     */
    private void parseUserIdTag(List<WechatUser> wechatUserList, List<Long> userIdList,
        Map<Long, String> userIdTagMap) {
        userIdList.clear();
        userIdTagMap.clear();
        Long userid = null;
        for (WechatUser wechatUser : wechatUserList) {
            userid = wechatUser.getUserId();
            if (userid == null) {
                continue;
            }
            userIdList.add(userid);
            userIdTagMap.put(userid, wechatUser.getTags());
        }
    }
    
    /**
     * 
     * @Title: getUserIdList 
     * @Description: TODO(获取wechat_user_bind表数据) 
     * @param @param wechatUserService
     * @param @param index
     * @param @param endIndex
     * @param @return    设定文件 
     * @return List<WechatUser>    返回类型 
     * @throws
     */
    private List<WechatUser> getUserIdList(IWechatUserService wechatUserService, int startIndex, int endIndex) {
        return wechatUserService.getUserIdList(startIndex, endIndex);
    }
    
    /**
     * 微信用户信息与驴妈妈用户同步
     */
    @RequestMapping(value = "/start", method = RequestMethod.POST)
    @ResponseBody
    private void userSync(Model model) {
    	 UserCooperationUserService userCooperationUserService = SpringBeanProxy.getBean(UserCooperationUserService.class, "userCooperationUserService");
        IWechatUserService wechatUserService = SpringBeanProxy.getBean(IWechatUserService.class, "remoteWechatUserService");
        List<WechatUser> wechatUserList = new ArrayList<WechatUser>();
        List<String> wechatUserUnionIdList = new ArrayList<String>();
        Map<String, WechatUser> map = new HashMap<String, WechatUser>();
        //获取所有的微信用户
        wechatUserList = getNotBindWechatUser(wechatUserService);
        while(wechatUserList != null && !wechatUserList.isEmpty()) {
            logger.info("================需要同步的数据有：" +  wechatUserList.size() + "条===========");
            list2Map(wechatUserService, map, wechatUserList, wechatUserUnionIdList);
            //批量调用接
            List<String> tempWechatUserList = new ArrayList<String>();
            Map<String,Long> resultMap;
            while(!wechatUserUnionIdList.isEmpty()) {
                logger.info("================batchQueryUserIdByUnionId  start===========");
                resultMap = batchQueryUserIdByUnionId(userCooperationUserService, wechatUserUnionIdList, map, tempWechatUserList);
                logger.info("================end  start===========");
                if (resultMap != null && !resultMap.isEmpty()) {
                    insertIntoWechatUserBind(resultMap, map, wechatUserService);
                }
            }
            wechatUserList = getNotBindWechatUser(wechatUserService);
        }
        logger.info("================没有数据需要同步===========");
        return;
    }
    
    /**
     * 将查询的结果根据unionid和wechatuser对应上，将userid赋值给对应的wechatUser,将结果插入到wechatUserBind表
     * @param resultMap
     * @param map
     * @param wechatUserService 
     */
    private void insertIntoWechatUserBind(Map<String, Long> resultMap, Map<String, WechatUser> map, IWechatUserService wechatUserService) {
        List<WechatUser> wechatUserList = new ArrayList<WechatUser>();
        WechatUser wechatUser = new WechatUser();
        Set<String> keySet = resultMap.keySet();
        Iterator<String> it = keySet.iterator();
        String unionid = null;
        while (it.hasNext()) {
            unionid = it.next();
            wechatUser = map.get(unionid);
            wechatUser.setUserId(resultMap.get(unionid));
            wechatUser.setId(UUID.randomUUID().toString().replaceAll("-", ""));
            wechatUser.setUnionid(unionid);
            wechatUser.setChannel("0");
            wechatUserList.add(wechatUser);
        }
        if (wechatUserList != null && !wechatUserList.isEmpty()) {
            wechatUserService.insertIntoWechatUserBind(wechatUserList);
        }
    }
    
    /**
     * 分批次去查  userId
     * @param userCooperationUserService
     * @param wechatUserUnionIdList
     * @param map
     * @param tempWechatUserList 
     */
    private Map<String,Long> batchQueryUserIdByUnionId(UserCooperationUserService userCooperationUserService,
        List<String> wechatUserUnionIdList, Map<String, WechatUser> map, List<String> tempWechatUserList) {
        tempWechatUserList.clear();
        int size = wechatUserUnionIdList.size(); 
        if (size >= 1000) {
            tempWechatUserList.addAll(wechatUserUnionIdList.subList(0, 1000));
            wechatUserUnionIdList.subList(0, 1000).clear();
        }
        else {
            tempWechatUserList.addAll(wechatUserUnionIdList.subList(0, size));
            wechatUserUnionIdList.subList(0, size).clear();
        }
        logger.info("=============查询UserId==============");
        //调用会员接口获取UnionId对应的UserId
        Map<String,Long> resultMap = userCooperationUserService.bulkGetUserIdsByWechatUnionIds(tempWechatUserList);
        logger.info("=============剩余未查数据==============" + wechatUserUnionIdList.size());
        return resultMap;
    }

    /**
     * 一次性取N条数据, N配置在配置文件里
     * @param wechatUserList 
     * @param wechatUseService 
     * @return
     */
    private List<WechatUser> getNotBindWechatUser(IWechatUserService wechatUserService) {
        String num = Constants.getConfig("WXUSER_SYNC_LVUSER_NUM");
        return wechatUserService.getWechatUserList(num);
    }
    
    /**
     * 将List的内容变成Map<String,WechatUser> map key为unionid
     * 并且每5000条做一下批量更新wx_user
     * @param map 
     * @param wechatUserList 
     * @param wechatUserUnionIdList 
     */
    private void list2Map(IWechatUserService wechatUserService, Map<String, WechatUser> map, List<WechatUser> wechatUserList, List<String> wechatUserUnionIdList) {
        logger.info("=============list2Map    start==============");
        StringBuilder wechatUserIds;
        wechatUserIds = new StringBuilder();
        int i = 1;
        for (WechatUser wechatUser : wechatUserList) {
            wechatUserIds.append("'").append(wechatUser.getId()).append("'").append(",");
            i++;
            if (!StringUtils.isBlank(wechatUser.getUnionid())) {
                map.put(wechatUser.getUnionid(), wechatUser);
                wechatUserUnionIdList.add(wechatUser.getUnionid());
            }
            if (i == 5000) {
                wechatUserService.batchUpdateIsSync(wechatUserIds.substring(0, wechatUserIds.length() - 1));
                wechatUserIds = new StringBuilder();
                i = 1;
            }
        }
        //如果最后还有没更新的，就更新
        if (wechatUserIds.length() != 0) {
            wechatUserService.batchUpdateIsSync(wechatUserIds.substring(0, wechatUserIds.length() - 1));
        }
        wechatUserList.clear();
        logger.info("=============list2Map    end==============");
    }
}
