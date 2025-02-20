package com.xx.xiuxianserver.Service.Impl;

import cn.hutool.core.date.DateUtil;
import com.xx.xiuxianserver.Entity.Menu;
import com.xx.xiuxianserver.Mapper.MenuMapper;
import com.xx.xiuxianserver.Service.MenuService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
/**
 * @author jiangzk
 * 2025/1/20  下午4:17
 */
@Service
public class MenuServiceImpl implements MenuService {

    @Resource
    private MenuMapper menuMapper;

    @Override
    public int insertSelective(Menu record) {
        return menuMapper.insertSelective(record);
    }

    @Override
    public Menu selectByPrimaryKey(Integer id) {
        return menuMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(Menu record) {
        record.setEditTime(null);
        return menuMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(Menu record) {
        Menu menu = menuMapper.selectByPrimaryKey(record.getId());
        record.setEditTime(DateUtil.date());
        record.setCreateTime(menu.getCreateTime());
        record.setDeleted(false);
        return menuMapper.updateByPrimaryKey(record);
    }
}
