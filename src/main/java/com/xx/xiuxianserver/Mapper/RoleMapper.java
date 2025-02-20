package com.xx.xiuxianserver.Mapper;

import com.xx.xiuxianserver.Entity.Role;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author jiangzk
 * 2025/1/24  下午3:00
 */
@Mapper
public interface RoleMapper {

    int insertSelective(Role record);

    Role selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Role record);

    int updateByPrimaryKey(Role record);
}