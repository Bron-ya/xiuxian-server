package com.xx.xiuxianserver.Service;


import com.xx.xiuxianserver.Entity.RoleMenu;

/**
 * @author jiangzk
 * 2025/1/20  下午4:24
 */
public interface RoleMenuService{

    int insertSelective(RoleMenu record);

    RoleMenu selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RoleMenu record);

    int updateByPrimaryKey(RoleMenu record);
}
