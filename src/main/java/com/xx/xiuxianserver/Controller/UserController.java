package com.xx.xiuxianserver.Controller;


import com.xx.xiuxianserver.Common.Enums.AppHttpCodeEnum;
import com.xx.xiuxianserver.Common.Exception.MyException;
import com.xx.xiuxianserver.Common.Utils.SecurityUtils;
import com.xx.xiuxianserver.Entity.User;
import com.xx.xiuxianserver.Service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author jiangzk
 * 2025/1/17  上午10:09
 */
@Tag(name = "用户管理")
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @PreAuthorize("@ps.hasPerm('USER:ADD')")
    @Operation(summary = "添加用户信息")
    @PostMapping("/add")
    public int add(@RequestBody User user) {
        return userService.add(user);
    }

    @PreAuthorize("@ps.hasPerm('USER:UPDATE')")
    @Operation(summary = "修改用户信息")
    @PostMapping("/update")
    public int update(@RequestBody User user) {
        return userService.updateUser(user);
    }

    @PreAuthorize("@ps.hasPerm('USER:DELETE')")
    @Operation(summary = "删除用户信息")
    @GetMapping("/delete/{id}")
    public int delete(@PathVariable("id") int id) {
        return userService.deleteUser(id);
    }

    @Operation(summary = "查询当前用户信息")
    @GetMapping("/info")
    public User getUserInfo() {
        Integer userId = SecurityUtils.getUserId();
        if (userId == null) {
            throw new MyException(AppHttpCodeEnum.USER_NOT_FOUND.getCode(),
                    AppHttpCodeEnum.USER_NOT_FOUND.getMsg());
        }
        return userService.getUserInfo(userId);
    }

    @PreAuthorize("@ps.hasPerm('USER:LIST')")
    @Operation(summary = "查询用户列表")
    @GetMapping("/list")
    public List<User> list() {
        return userService.listAll();
    }
}
