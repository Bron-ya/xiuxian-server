package com.xx.xiuxianserver.Config;

import com.xx.xiuxianserver.Security.Exception.CustomAuthenticationExceptionHandler;
import com.xx.xiuxianserver.Security.Exception.CustomAuthorizationExceptionHandler;
import com.xx.xiuxianserver.Security.Exception.CustomSecurityExceptionHandler;
import com.xx.xiuxianserver.Security.Filter.JwtAuthenticationFilter;
import com.xx.xiuxianserver.Security.Login.Gitee.GiteeAuthenticationFilter;
import com.xx.xiuxianserver.Security.Login.Gitee.GiteeAuthenticationProvider;
import com.xx.xiuxianserver.Security.Login.LoginFailHandler;
import com.xx.xiuxianserver.Security.Login.LoginSuccessHandler;
import com.xx.xiuxianserver.Security.Login.Sms.SmsAuthenticationFilter;
import com.xx.xiuxianserver.Security.Login.Sms.SmsAuthenticationProvider;
import com.xx.xiuxianserver.Security.Login.UserName.UserNameAuthenticationFilter;
import com.xx.xiuxianserver.Security.Login.UserName.UserNameAuthenticationProvider;
import jakarta.annotation.Resource;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.SecurityContextHolderFilter;
import org.springframework.security.web.savedrequest.NullRequestCache;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.List;

/**
 * @Description: 自定义过滤连配置
 * @Author jiangzk
 * @Date 2025/1/22 22:28
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true) // 不加这个 PermissionService 不起作用，注意，注意，注意!!!
public class WebSecurityConfig {

    @Resource
    private ApplicationContext applicationContext;

    @Resource
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * 用户登录过滤链
     */
    @Bean
    public SecurityFilterChain loginFilterChain(HttpSecurity http) throws Exception {
        // 注入自定义配置
        commonHttpSetting(http);

        // 只有以下的路径会应用这个过滤链中的安全规则，其他路径不会受到影响
        http.securityMatcher("/user/login/*");
        // 对以上路径的设定
        http.authorizeHttpRequests(authorize -> authorize
                // 都需要经过认证
                .anyRequest().authenticated()
        );

        // 获取通用的成功和失败处理器
        LoginSuccessHandler loginSuccessHandler = applicationContext.getBean(LoginSuccessHandler.class);
        LoginFailHandler loginFailHandler = applicationContext.getBean(LoginFailHandler.class);

        // 登录方式：【用户名和密码登录】（自定义url与登录方式）
        UserNameAuthenticationFilter usernameLoginFilter = new UserNameAuthenticationFilter(
                new AntPathRequestMatcher("/user/login/username", HttpMethod.POST.name()),
                new ProviderManager(
                        List.of(applicationContext.getBean(UserNameAuthenticationProvider.class))),
                loginSuccessHandler,
                loginFailHandler);
        http.addFilterBefore(usernameLoginFilter, UsernamePasswordAuthenticationFilter.class);

        // 登录方式：【手机号和验证码登录】（自定义url与登录方式）
        SmsAuthenticationFilter smsLoginFilter = new SmsAuthenticationFilter(
                new AntPathRequestMatcher("/user/login/sms", HttpMethod.POST.name()),
                new ProviderManager(
                        List.of(applicationContext.getBean(SmsAuthenticationProvider.class))),
                loginSuccessHandler,
                loginFailHandler);
        http.addFilterBefore(smsLoginFilter, UsernamePasswordAuthenticationFilter.class);

        // 登录方式：【以 Gitee 第三方进行授权登录】（自定义url与登录方式）
        GiteeAuthenticationFilter giteeLoginFilter = new GiteeAuthenticationFilter(
                new AntPathRequestMatcher("/user/login/gitee", HttpMethod.POST.name()),
                new ProviderManager(
                        List.of(applicationContext.getBean(GiteeAuthenticationProvider.class))),
                loginSuccessHandler,
                loginFailHandler);
        http.addFilterBefore(giteeLoginFilter, UsernamePasswordAuthenticationFilter.class);

        // 允许跨域
        http.cors(Customizer.withDefaults());
        return http.build();
    }

    /**
     * Swagger 安全过滤链
     */
    @Bean
    public SecurityFilterChain swaggerFilterChain(HttpSecurity http) throws Exception {
        // 只有以下的路径会应用这个过滤链中的安全规则，其他路径不会受到影响
        http.securityMatcher("/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**", "/webjars/**");
        // 对以上路径的设定
        http.authorizeHttpRequests(authorize -> authorize
                // 都不需要认证
                .anyRequest().permitAll()
        );
        // 允许跨域
        http.cors(Customizer.withDefaults());
        return http.build();
    }

    /**
     * 自定义配置
     */
    private void commonHttpSetting(HttpSecurity http) throws Exception {
        /*
            禁用不必要的默认filter（这些filter都是非前后端分离项目的产物）
         */
        http.formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .sessionManagement(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .requestCache(cache -> cache
                        .requestCache(new NullRequestCache())
                )
                .anonymous(AbstractHttpConfigurer::disable);
        /*
            处理异常响应内容
         */
        http.exceptionHandling(exception -> {exception
                // 设置请求未认证接口的响应类
                .authenticationEntryPoint(new CustomAuthenticationExceptionHandler())
                // 设置未授权访问接口的响应类
                .accessDeniedHandler(new CustomAuthorizationExceptionHandler());
        });
        // 提前加载其他未知响应异常
        http.addFilterBefore(new CustomSecurityExceptionHandler(), SecurityContextHolderFilter.class);
        // 注入 JWT 认证过滤器，并进行在用户认证之前提前加载
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }

    /**
     * 密码加密算法
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
