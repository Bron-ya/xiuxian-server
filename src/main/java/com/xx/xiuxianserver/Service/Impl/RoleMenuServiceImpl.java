package com.xx.xiuxianserver.Service.Impl;

import cn.hutool.core.date.DateUtil;
import com.xx.xiuxianserver.Entity.RoleMenu;
import com.xx.xiuxianserver.Mapper.RoleMenuMapper;
import com.xx.xiuxianserver.Service.RoleMenuService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * @author jiangzk
 * 2025/1/20  下午4:24
 */
@Service
public class RoleMenuServiceImpl implements RoleMenuService {

    @Resource
    private RoleMenuMapper roleMenuMapper;

    @Override
    public int insertSelective(RoleMenu record) {
        return roleMenuMapper.insertSelective(record);
    }

    @Override
    public RoleMenu selectByPrimaryKey(Integer id) {
        return roleMenuMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(RoleMenu record) {
        record.setEditTime(null);
        return roleMenuMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(RoleMenu record) {
        RoleMenu roleMenu = roleMenuMapper.selectByPrimaryKey(record.getId());
        record.setEditTime(DateUtil.date());
        record.setCreateTime(roleMenu.getCreateTime());
        record.setDeleted(false);
        return roleMenuMapper.updateByPrimaryKey(record);
    }
}
