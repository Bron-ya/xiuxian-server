package com.xx.xiuxianserver.Security.Filter;

import com.xx.xiuxianserver.Common.Utils.MyJwtUtilsService;
import com.xx.xiuxianserver.Common.Utils.MyTimeUtils;
import com.xx.xiuxianserver.Entity.GlobalJWTPayload;
import com.xx.xiuxianserver.Mapper.MenuMapper;
import com.xx.xiuxianserver.Security.Login.UserName.UserNameAuthentication;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.List;


/**
 * JWT 认证过滤器
 * 【1】 当一个请求到达时，首先由 jwtAuthenticationFilter 处理，经过校验后，如果请求中包含有效的 JWT，则用户会被直接认证，并保存到 security 上下文中
 *      而不会触发 AuthenticationProvider 等等一系列认证流程，从而减少了代码的运行量以及访问数据库的频率
 * 【2】 如果 jwtAuthenticationFilter 没有找到有效的或未携带的 JWT(Token)，才会走 Provider -> successHandler 一系列认证流程
 * 【3】 其中，jwtAuthenticationFilter 中所获取的 request 值，是从 successHandler 中进行获取的
 * @author jiangzk
 * 2025/1/25 00:19
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Resource
    private MenuMapper menuMapper;

    @Resource
    private MyJwtUtilsService myJwtUtilsService;

    @Override
    protected void doFilterInternal(@Nonnull HttpServletRequest request,@Nonnull HttpServletResponse response,
                                    @Nonnull FilterChain filterChain) throws ServletException, IOException {

        String token = resolveToken(request);
        System.out.println("token信息: " + token);
        try {
            if (token != null && validateToken(token)) {
                Authentication auth = getAuthentication(token);
                // 户认证成功，将认证成功的信息传入到上下文中进行保存
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        } catch (Exception ex) {
            System.out.println("设置用户身份验证失败: " + ex);
            throw ex;
        }
        filterChain.doFilter(request, response);
    }

    /**
     * 进行请求头信息处理，获取 token 值
     */
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        System.out.println("bearerToken: " +bearerToken);
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    /**
     * 校验当前 token 是否存在或者过期
     */
    private boolean validateToken(String token) {
        try {
            // 将 token 信息转换为 JWTPayload 类
            GlobalJWTPayload globalJwtPayload = myJwtUtilsService.verifyJwt(token, GlobalJWTPayload.class);
            if (globalJwtPayload == null) {
                System.out.println("JWTPayload 是空值");
                return false;
            }
            long expiredTime = globalJwtPayload.getExpiredTime();
            if (expiredTime <= MyTimeUtils.nowMilli()) {
                System.out.println("JWT token 已经过期");
                return false;
            }
            System.out.println("JWT token 是有效的");
            return true;
        } catch (Exception e) {
            System.out.println("无效的 JWT token: " + e.getMessage());
            return false;
        }
    }

    /**
     * 使用 Authentication 认证对象进行存入当前认证用户信息，并返回给上下文进行保存
     */
    private Authentication getAuthentication(String token) {
        try {
            // 1.将 token 信息转换为 JWTPayload 类
            GlobalJWTPayload globalJwtPayload = myJwtUtilsService.verifyJwt(token, GlobalJWTPayload.class);
            if (globalJwtPayload == null) {
                return null;
            }

            // 2.查询当前用户的权限信息，并传入
            List<String> authorities = menuMapper.selectMenuByUserId(globalJwtPayload.getUserId());
            Collection<? extends GrantedAuthority> grantedAuthorities = authorities.stream()
                    .map(SimpleGrantedAuthority::new)
                    .toList();
            System.out.println("当前用户权限grantedAuthorities: " + grantedAuthorities);

        /*
         todo 需要注意：之后可以根据实际情况拓展 JWTPayload 认证对象以及 jwtFilter 过滤器
         这里使用 UserNameAuthentication 或 SmsAuthentication 来封装认证信息都可以
         【原因】：
         一般来说，不同的 Authentication 认证对象，存储 token 认证对象(globalJwtPayload)是不一样的，也对应不同的 jwtFilter 过滤器
         但是在这个项目中，我统一了 username 和 sms 的 token 认证对象(globalJwtPayload)，所以这一个 jwtFilter 就够了
         */
            UserNameAuthentication userNameAuthentication = new UserNameAuthentication(grantedAuthorities);
            userNameAuthentication.setAuthenticated(true);
            userNameAuthentication.setGlobalJwtPayload(globalJwtPayload);
            return userNameAuthentication;
        } catch (Exception e) {
            System.out.println("设置用户身份验证失败: " + e);
            throw e;
        }
    }
}
