package com.xx.xiuxianserver.Mapper;

import com.xx.xiuxianserver.Entity.RoleMenu;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author jiangzk
 * 2025/1/24  下午3:00
 */
@Mapper
public interface RoleMenuMapper {

    int insertSelective(RoleMenu record);

    RoleMenu selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RoleMenu record);

    int updateByPrimaryKey(RoleMenu record);
}