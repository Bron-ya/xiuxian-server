package com.xx.xiuxianserver.Service;


import com.xx.xiuxianserver.Entity.Menu;

/**
 * @author jiangzk
 * 2025/1/20  下午4:17
 */
public interface MenuService{

    int insertSelective(Menu record);

    Menu selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Menu record);

    int updateByPrimaryKey(Menu record);
}
