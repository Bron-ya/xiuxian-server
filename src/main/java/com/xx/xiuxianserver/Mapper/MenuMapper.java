package com.xx.xiuxianserver.Mapper;

import com.xx.xiuxianserver.Entity.Menu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author jiangzk
 * 2025/1/24  下午2:59
 */
@Mapper
public interface MenuMapper {

    int insertSelective(Menu record);

    Menu selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Menu record);

    int updateByPrimaryKey(Menu record);

    List<String> selectMenuByUserId(Integer userId);
}