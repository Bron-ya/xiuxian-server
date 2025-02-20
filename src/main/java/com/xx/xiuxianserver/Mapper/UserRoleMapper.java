package com.xx.xiuxianserver.Mapper;

import com.xx.xiuxianserver.Entity.UserRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author jiangzk
 * 2025/1/24  下午3:00
 */
@Mapper
public interface UserRoleMapper {

    int insertSelective(UserRole record);

    UserRole selectByPrimaryKey(@Param("userId") Integer userId, @Param("roleId") Integer roleId);

    int updateByPrimaryKeySelective(UserRole record);

    int updateByPrimaryKey(UserRole record);

    int selectRoleIdByUserId(@Param("userId") Integer userId);
}