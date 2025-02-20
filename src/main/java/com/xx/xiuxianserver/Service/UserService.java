package com.xx.xiuxianserver.Service;


import com.xx.xiuxianserver.Entity.User;

import java.util.List;

/**
 * @author jiangzk
 * 2025/1/17  上午9:53
 */
public interface UserService{

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    List<User> listAll();

    int add(User user);

    int updateUser(User user);

    int deleteUser(int id);

    User getUserInfo(Integer id);
}
