package com.xx.xiuxianserver.Service;


import com.xx.xiuxianserver.Entity.UserRole;

/**
 * @author jiangzk
 * 2025/1/20  下午4:29
 */
public interface UserRoleService{

    int insertSelective(UserRole record);

    UserRole selectByPrimaryKey(Integer userId, Integer roleId);

    int updateByPrimaryKeySelective(UserRole record);

    int updateByPrimaryKey(UserRole record);
}
