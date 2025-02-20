package com.xx.xiuxianserver.Service.Impl;

import cn.hutool.core.date.DateUtil;
import com.xx.xiuxianserver.Entity.Role;
import com.xx.xiuxianserver.Mapper.RoleMapper;
import com.xx.xiuxianserver.Service.RoleService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
/**
 * @author jiangzk
 * 2025/1/20  下午4:23
 */
@Service
public class RoleServiceImpl implements RoleService {

    @Resource
    private RoleMapper roleMapper;

    @Override
    public int insertSelective(Role record) {
        return roleMapper.insertSelective(record);
    }

    @Override
    public Role selectByPrimaryKey(Integer id) {
        return roleMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(Role record) {
        record.setEditTime(null);
        return roleMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(Role record) {
        Role role = roleMapper.selectByPrimaryKey(record.getId());
        record.setEditTime(DateUtil.date());
        record.setCreateTime(role.getCreateTime());
        record.setDeleted(false);
        return roleMapper.updateByPrimaryKey(record);
    }
}
