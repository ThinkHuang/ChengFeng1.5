package com.beautifulsoup.chengfeng.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.web.cors.CorsUtils;

import com.beautifulsoup.chengfeng.handler.TokenClearLogoutHandler;
import com.beautifulsoup.chengfeng.handler.TokenRefreshSuccessHandler;
import com.beautifulsoup.chengfeng.handler.UserLoginSuccessHandler;
import com.beautifulsoup.chengfeng.security.ChengfengAccessDeniedHandler;
import com.beautifulsoup.chengfeng.security.UserInfoService;
import com.beautifulsoup.chengfeng.security.configurer.TokenLoginConfigurer;
import com.beautifulsoup.chengfeng.security.configurer.UserLoginConfigurer;
import com.beautifulsoup.chengfeng.security.provider.TokenAuthenticationProvider;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/images/**").permitAll()//设置静态资源无权限限制
                .antMatchers("/swagger-ui.html").permitAll()
                .antMatchers("/swagger-resources/**").permitAll()
                .antMatchers("/webjars/**").permitAll()
                .antMatchers("/v2/api-docs").permitAll()
                .antMatchers("/configuration/ui").permitAll()
                .antMatchers("/configuration/security").permitAll()
                .antMatchers("/community/listall","/user/registry").permitAll()//指定可以直接访问的url
                .antMatchers("/file/upload","/file/uploads","/user/login").permitAll()//指定可以直接访问的url
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                .requestMatchers(CorsUtils::isCorsRequest).permitAll()
                .anyRequest().authenticated()
                .and()
                .csrf().disable()
                .formLogin().disable()
                .sessionManagement().disable()
//                .headers().addHeaderWriter(new StaticHeadersWriter(Arrays.asList(
//                new Header("Access-control-Allow-Origin","*"),
//                new Header("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE"),
//                new Header("Access-Control-Max-Age", "3600"),
//                new Header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept"),
//                new Header("Access-Control-Expose-Headers","Authorization"))))
//                .and()
                //登录请求的过滤
                .apply(new UserLoginConfigurer<>()).loginSuccessHandler(userLoginSuccessHandler())
                .and()
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler())
                .and()
                //token请求的过滤
                .apply(new TokenLoginConfigurer<>())
                .tokenValidSuccessHandler(tokenRefreshSuccessHandler())
                .permissiveRequestUrls("/logout","/community/listall","/images/**","/user/registry","/swagger-resources/**","/swagger-ui.html")
                .permissiveRequestUrls("/webjars/**","/v2/api-docs","/configuration/ui","/configuration/security","/file/upload","/file/uploads","/user/login")
                .and()
                //登出的过滤器
                .logout()
                .addLogoutHandler(tokenClearLogoutHandler())
                .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler())
                .and()
                .sessionManagement().disable()
                .cors().and().exceptionHandling().accessDeniedHandler(accessDeniedHandler());
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider()).authenticationProvider(tokenAuthenticationProvider());
    }

    @Override
    protected UserDetailsService userDetailsService() {
        return new UserInfoService();
    }



    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean("daoAuthenticationProvider")
    protected AuthenticationProvider daoAuthenticationProvider() throws Exception{
        DaoAuthenticationProvider daoProvider = new DaoAuthenticationProvider();
        daoProvider.setPasswordEncoder(passwordEncoder());
        daoProvider.setUserDetailsService(userInfoService());
        return daoProvider;
    }
    @Bean("tokenAuthenticationProvider")
    protected AuthenticationProvider tokenAuthenticationProvider() {
        return new TokenAuthenticationProvider(userInfoService());
    }

    @Bean("userInfoService")
    protected UserInfoService userInfoService() {
        return new UserInfoService();
    }

    @Bean("passwordEncoder")
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    protected UserLoginSuccessHandler userLoginSuccessHandler() {
        return new UserLoginSuccessHandler(userInfoService());
    }

    @Bean
    protected TokenRefreshSuccessHandler tokenRefreshSuccessHandler() {
        return new TokenRefreshSuccessHandler();
    }

    @Bean
    protected TokenClearLogoutHandler tokenClearLogoutHandler() {
        return new TokenClearLogoutHandler(userInfoService());
    }

    @Bean
    protected ChengfengAccessDeniedHandler accessDeniedHandler(){
        return new ChengfengAccessDeniedHandler();
    }


//    @Bean
//    public WebMvcConfigurer corsConfigurer() {
//        return new WebMvcConfigurerAdapter() {
//            @Override
//            public void addCorsMappings(CorsRegistry registry) {
//                registry.addMapping("/**").allowedOrigins("*").allowedHeaders("*").allowedMethods("*");
//            }
//        };
//    }
//
//    @Bean
//    public FilterRegistrationBean corsFilter(){
//        FilterRegistrationBean bean=new FilterRegistrationBean(new CorsFilter(corsConfigurationSource()));
//        bean.setOrder(0);
//        return bean;
//    }
//
//
//    @Bean
//    protected CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.addAllowedOrigin("*");
//        configuration.addAllowedMethod("*");
//        configuration.addAllowedHeader("*");
//        configuration.addExposedHeader("Authorization");
//        configuration.setAllowCredentials(true);
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//        return source;
//    }

}

//.cors()
//.and()
//.headers().addHeaderWriter(new StaticHeadersWriter(Arrays.asList(
//new Header("Access-Control-Allow-Origin","*"),
//new Header("Access-Control-Expose-Headers","Authorization"))))//设置支持跨域请求
//.and()
//.addFilterAfter(new OptionsRequestFilter(), CorsFilter.class)
