package com.xx.xiuxianserver.Mapper;

import com.xx.xiuxianserver.Entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author jiangzk
 * 2025/1/24  下午5:09
 */
@Mapper
public interface UserMapper {

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    List<User> listAll();

    User selectByUserName(String username);

    User selectByPhone(String phone);
}