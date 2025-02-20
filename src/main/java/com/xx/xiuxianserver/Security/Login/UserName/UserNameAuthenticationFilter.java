package com.xx.xiuxianserver.Security.Login.UserName;

import com.xx.xiuxianserver.Common.Enums.AppHttpCodeEnum;
import com.xx.xiuxianserver.Common.Exception.MyException;
import com.xx.xiuxianserver.Common.Utils.MyJsonUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 认证过滤器 【这里还没开始认证，只是进行封装对象进行传递】
 * <p>
 * AbstractAuthenticationProcessingFilter 的实现类要做的工作：
 * 1. 从 HttpServletRequest 提取授权凭证（username 和 password相关信息），封装到 Authentication 对象中
 * 2. 将 Authentication 对象传给 AuthenticationManager（委托Provider处理） 进行实际的认证授权操作
 * @author jiangzk
 * 2025/1/23  下午4:50
 */
public class UserNameAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private static final Logger logger = LoggerFactory.getLogger(UserNameAuthenticationFilter.class);

    public UserNameAuthenticationFilter(AntPathRequestMatcher pathRequestMatcher,
                                        AuthenticationManager authenticationManager,
                                        AuthenticationSuccessHandler authenticationSuccessHandler,
                                        AuthenticationFailureHandler authenticationFailureHandler) {
        super(pathRequestMatcher);
        setAuthenticationManager(authenticationManager);
        setAuthenticationSuccessHandler(authenticationSuccessHandler);
        setAuthenticationFailureHandler(authenticationFailureHandler);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException, IOException{
        logger.debug("use UsernameAuthenticationFilter");

        // 1.提取待认证用户的凭证信息
        String requestJsonData = request.getReader().lines()
                .collect(Collectors.joining(System.lineSeparator()));
        Map<String, Object> requestMapData = MyJsonUtils.parseToMap(requestJsonData);
        String username = requestMapData.get("username").toString();
        String password = requestMapData.get("password").toString();
        // 1.1 判断传入的值是否合理
        if (username == null || password == null) {
            throw new MyException(AppHttpCodeEnum.INVALID_USERNAME_OR_PASSWORD.getCode(),
                    AppHttpCodeEnum.INVALID_USERNAME_OR_PASSWORD.getMsg());
        }

        // 2.将凭证信息封装到 Authentication 对象中
        UserNameAuthentication userNameAuthentication = new UserNameAuthentication();
        userNameAuthentication.setUsername(username);
        userNameAuthentication.setPassword(password);
        userNameAuthentication.setAuthenticated(false);

        // 3.将 userNameAuthentication 对象传给 AuthenticationManager 进行实际的认证授权操作（寻找 AuthenticationProvider进行登录认证）
        return getAuthenticationManager().authenticate(userNameAuthentication);
    }
}
