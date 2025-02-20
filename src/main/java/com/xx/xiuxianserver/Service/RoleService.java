package com.xx.xiuxianserver.Service;


import com.xx.xiuxianserver.Entity.Role;

/**
 * @author jiangzk
 * 2025/1/20  下午4:23
 */
public interface RoleService{

    int insertSelective(Role record);

    Role selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Role record);

    int updateByPrimaryKey(Role record);
}
