package com.mwj.cms.framework.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author Meng Wei Jin
 * @description
 * EnableGlobalMethodSecurity: 开启方法安全注解
 **/
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(jsr250Enabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Qualifier("defaultUserDetailsService")
    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * Spring Security能够防止主体对同一应用程序进行超过指定次数的并发身份验证。
     * 而网络管理员喜欢这个特性，因为它有助于防止人们共享登录名。
     * 例如，您可以阻止用户“Batman”从两个不同的会话登录到web应用程序。
     * 您可以终止他们以前的登录，也可以在他们试图再次登录时报告错误，从而阻止第二次登录。
     * 注意，如果使用第二种方法，没有显式注销的用户(例如，刚刚关闭浏览器的用户)将无法再次登录，直到其原始会话过期。
     * 所以，一般情况下，使用第一种方式即可。
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 授权请求
        http.authorizeRequests()
            // 匹配器 permitAll()表示允许所有相匹配的请求访问 /**表示可以跨文件夹
            .antMatchers("/kaptcha/renderCode").permitAll()
            // 只能管理员访问, hasRole("ADMIN")其实security判断的时候根据ROLE_ADMIN来判断的，所以sys_role表存储的时候需要存储为ROLE_ADMIN形式。
            .antMatchers("/druid/**").hasRole("druid")
            .antMatchers("/actuator/**").hasRole("actuator")
            .antMatchers("/swagger-ui.html").hasRole("swagger")
            // 其他任何请求,登录后可以访问
            .anyRequest().authenticated();

        // 对druid关闭csrf
        http.csrf().ignoringAntMatchers("/druid/**");

        http.headers()
            // iframe嵌入网页(如：使用layer弹层时)，security默认使用deny策略，浏览器报错x-frame-options deny
            // 修改为SAMEORIGIN，页面只能显示在与页面本身相同的原点的框架中
            .frameOptions().sameOrigin()
            // 开启xss过滤
            .xssProtection().xssProtectionEnabled(true);

        // 将我们自定义的过滤器，配置到UsernamePasswordAuthenticationFilter之前
        http.addFilterBefore(captchaFilter(), UsernamePasswordAuthenticationFilter.class);

        // 定义当需要用户登录时候，转到的登录页面。
        http.formLogin().loginPage("/login").permitAll();

        http.logout()
            .logoutUrl("/logout")
            .logoutSuccessUrl("/")
            .invalidateHttpSession(true)
            .deleteCookies("JSESSIONID")
            // 登出成功后处理，记录登出日志
            .logoutSuccessHandler(defaultLogoutSuccessHandler());
            // 添加CSRF将更新LogOutFutter仅使用HTTP POST。这确保注销需要CSRF令牌，并且恶意用户不能强制注销您的用户。
            //如果想使用注销的HTTP GET请求，添加如下代码，但是这样是不推荐的。
            //.logoutRequestMatcher(new AntPathRequestMatcher("/logout"));

        http.sessionManagement()
            // session失效时跳转的url
            //.invalidSessionUrl("/login?sessionInvalid")
            // 限制同一用户在应用中同时允许存在的已经通过认证的session数量。Security默认不限制，默认使先前的session失效。
            .maximumSessions(1000)
            // 如果当前用户已经在设备A登录，然后又在设备B登录时，是否阻止其登录。true为阻止其继续登录。
            // 如果设置为true, 没有显式注销的用户(例如，刚刚关闭浏览器的用户)将无法再次登录，直到其原始会话过期。
            //.maxSessionsPreventsLogin(false)
            // 用户试图使用已被并发会话控制器“过期”的会话(用户超出允许的会话数量，并在其他地方再次登录)，将重定向到的URL
            .expiredUrl("/login")
            .sessionRegistry(sessionRegistry());

        //启用rememberMe功能，将用户信息保存在cookie中
        http.rememberMe()
            // cookie的有效期为两个星期
            .tokenValiditySeconds(14*24*60*60);

    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        // 解决静态资源被拦截的问题 /**表示可以跨文件夹
        web.ignoring().antMatchers("/webjars/**", "/extend/**", "/image/**", "/css/**", "/js/**");
    }

    /**
     * 添加 UserDetailsService， 实现自定义登录校验
     */
    @Override
    protected void configure(AuthenticationManagerBuilder builder) throws Exception {
        builder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    /**
     * 密码加密
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /**
     * session注册器
     * @return
     */
    @Bean
    public SessionRegistry sessionRegistry(){
        return new SessionRegistryImpl();
    }

    /**
     * 自定义注销成功后，后处理器
     * @return
     */
    @Bean
    public DefaultLogoutSuccessHandler defaultLogoutSuccessHandler(){
        return new DefaultLogoutSuccessHandler();
    }

    /**
     * 自定义验证码过滤器
     * @return
     */
    @Bean
    public CaptchaFilter captchaFilter() {
        return new CaptchaFilter();
    }

}
