package com.xx.xiuxianserver.Controller;


import com.xx.xiuxianserver.Common.Enums.AppHttpCodeEnum;
import com.xx.xiuxianserver.Common.Exception.MyException;
import com.xx.xiuxianserver.Common.Result.ResponseResult;
import com.xx.xiuxianserver.Common.Utils.MyJwtUtilsService;
import com.xx.xiuxianserver.Common.Utils.SecurityUtils;
import com.xx.xiuxianserver.Entity.GlobalJWTPayload;
import com.xx.xiuxianserver.Entity.User;
import com.xx.xiuxianserver.Security.Login.Gitee.GiteeAuthentication;
import com.xx.xiuxianserver.Service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Resource
    private MyJwtUtilsService myJwtUtilsService;
    
    @Resource
    private AuthenticationManager authenticationManager;

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

    @Operation(summary = "gitee 用户登录")
    @PostMapping("/login/gitee1")
    public ResponseResult logingitee(@RequestBody Map<String, String> request) {
        String code = request.get("code");
        // 检查当前用户是否已登录
        Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();
        if (currentAuth != null && currentAuth.isAuthenticated()) {
            throw new MyException(AppHttpCodeEnum.NEED_LOGIN.getCode(), "用户已登录");
        }

        // 创建一个未认证的GiteeAuthentication对象
        GiteeAuthentication giteeAuthentication = new GiteeAuthentication();
        giteeAuthentication.setCode(code);
        giteeAuthentication.setAuthenticated(false);

        try {
            // 使用认证管理器进行认证
            Authentication result = authenticationManager.authenticate(giteeAuthentication);
            SecurityContextHolder.getContext().setAuthentication(result);

            // 获取认证结果中的用户信息和详细信息
            GiteeAuthentication authenticatedResult = (GiteeAuthentication) result;
            GlobalJWTPayload payload = authenticatedResult.getGlobalJwtPayload();
            Map<String, Object> details = (Map<String, Object>) authenticatedResult.getDetails();

            // 生成JWT token并构建响应
            Map<String, Object> response = new HashMap<>();
            response.put("token", myJwtUtilsService.createJwt(payload, 15000));
            response.put("refreshToken", details.get("refreshToken"));
            response.put("needInitUserInfo", details.get("needInitUserInfo"));
            response.put("nickname", details.get("nickname"));
            return ResponseResult.okResult(response,"登录成功");
        } catch (BadCredentialsException e) {
            throw new MyException(AppHttpCodeEnum.LOGIN_ERROR.getCode(), e.getMessage());
        }
    }
}
