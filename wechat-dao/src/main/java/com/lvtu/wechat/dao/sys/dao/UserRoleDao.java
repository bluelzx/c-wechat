package com.lvtu.wechat.dao.sys.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.lvtu.wechat.common.base.BaseIbatisDAO;
import com.lvtu.wechat.common.model.sys.UserRole;

/**
 * 角色用户关联
* @ClassName: UserRoleDao 
* @Description: TODO
* @author majun
* @date 2016-11-4 下午2:58:28
 */
@Repository
public class UserRoleDao extends BaseIbatisDAO {
    
    public UserRoleDao() {
        super("sys_user_role");
    }
    
    /**
     * 
    * @Title: queryByid 
    * @Description: TODO(根据roleidz在sys_user_role表中查询) 
    * @param @param id
    * @param @return    设定文件 
    * @return String    返回类型 
    * @throws
     */
    public List<UserRole> queryByid(String id) {
        return super.getList("queryByid", id);
    }

    /**
     * 
    * @Title: deleteUserRole 
    * @Description: TODO(根据roleId删除表sys_user_role中的数据) 
    * @param @param id    设定文件 
    * @return void    返回类型 
    * @throws
     */
    public void deleteUserRole(String id) {
        super.delete("deleteUserRole", id);
    }
}
