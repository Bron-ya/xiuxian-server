package com.xx.xiuxianserver.Service.Impl;

import cn.hutool.core.date.DateUtil;
import com.xx.xiuxianserver.Entity.UserRole;
import com.xx.xiuxianserver.Mapper.UserRoleMapper;
import com.xx.xiuxianserver.Service.UserRoleService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
/**
 * @author jiangzk
 * 2025/1/20  下午4:29
 */
@Service
public class UserRoleServiceImpl implements UserRoleService {

    @Resource
    private UserRoleMapper userRoleMapper;

    @Override
    public int insertSelective(UserRole record) {
        return userRoleMapper.insertSelective(record);
    }

    @Override
    public UserRole selectByPrimaryKey(Integer userId,Integer roleId) {
        return userRoleMapper.selectByPrimaryKey(userId,roleId);
    }

    @Override
    public int updateByPrimaryKeySelective(UserRole record) {
        record.setEditTime(null);
        return userRoleMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(UserRole record) {
        record.setEditTime(DateUtil.date());
        record.setCreateTime(record.getCreateTime());
        record.setDeleted(false);
        return userRoleMapper.updateByPrimaryKey(record);
    }
}
