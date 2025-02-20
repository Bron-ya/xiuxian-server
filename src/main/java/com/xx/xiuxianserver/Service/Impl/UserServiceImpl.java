package com.xx.xiuxianserver.Service.Impl;

import cn.hutool.core.date.DateUtil;

import com.xx.xiuxianserver.Common.Enums.AppHttpCodeEnum;
import com.xx.xiuxianserver.Common.Exception.MyException;
import com.xx.xiuxianserver.Common.Utils.SecurityUtils;
import com.xx.xiuxianserver.Entity.User;
import com.xx.xiuxianserver.Mapper.UserMapper;
import com.xx.xiuxianserver.Service.UserService;
import jakarta.annotation.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author jiangzk
 * 2025/1/17  上午9:53
 */
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Override
    public int insertSelective(User record) {
        return userMapper.insertSelective(record);
    }

    @Override
    public User selectByPrimaryKey(Integer id) {
        return userMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(User record) {
        record.setEditTime(null);
        return userMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(User record) {
        User user = userMapper.selectByPrimaryKey(record.getId());
        record.setEditTime(DateUtil.date());
        record.setCreateTime(user.getCreateTime());
        record.setDeleted(false);
        return userMapper.updateByPrimaryKey(record);
    }

    @Override
    public List<User> listAll() {
        return userMapper.listAll();
    }

    @Override
    public int updateUser(User user) {
        if (user.getId() == null || userMapper.selectByPrimaryKey(user.getId()) == null) {
             throw new MyException(HttpStatus.NOT_FOUND.value(), "当前修改的用户不存在");
        }
        Integer userId = SecurityUtils.getUserId();
        user.setOperatorId(userId);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return this.updateByPrimaryKeySelective(user);
    }

    @Override
    public int add(User user) {
       if (user.getUsername() == null || user.getPassword() == null) {
            throw new MyException(HttpStatus.BAD_REQUEST.value(),"用户名或密码不能为空");
       }
       Integer userId = SecurityUtils.getUserId();
       user.setOperatorId(userId);
       user.setPassword(passwordEncoder.encode(user.getPassword()));
       return insertSelective(user);
    }


    @Override
    public int deleteUser(int id) {
        User user = userMapper.selectByPrimaryKey(id);
        if (user == null) {
            throw new MyException(HttpStatus.NOT_FOUND.value(), "当前用户不存在，删除失败");
        }
        Integer userId = SecurityUtils.getUserId();
        user.setOperatorId(userId);
        user.setDeleted(true);
        return updateByPrimaryKeySelective(user);
    }

    @Override
    public User getUserInfo(Integer id) {
        if (id == null || userMapper.selectByPrimaryKey(id) == null) {
            throw new MyException(AppHttpCodeEnum.USER_NOT_FOUND.getCode(),
                    AppHttpCodeEnum.USER_NOT_FOUND.getMsg());
        }
        return userMapper.selectByPrimaryKey(id);
    }
}
