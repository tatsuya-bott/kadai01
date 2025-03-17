package com.japan.compass.annotation.config;

import com.japan.compass.annotation.controller.filter.RequestIdFilter;
import com.japan.compass.annotation.controller.filter.SessionValidationFilter;
import com.japan.compass.annotation.controller.filter.UserIdFilter;
import com.japan.compass.annotation.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@AllArgsConstructor
@Configuration(proxyBeanMethods = false)
public class FilterConfig {

    private final UserRepository userRepository;

    @Bean
    public FilterRegistrationBean<RequestIdFilter> requestIdFilter(){
        FilterRegistrationBean<RequestIdFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new RequestIdFilter());
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<UserIdFilter> userIdFilter(){
        FilterRegistrationBean<UserIdFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new UserIdFilter());
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<SessionValidationFilter> sessionValidationFilter(){
        FilterRegistrationBean<SessionValidationFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new SessionValidationFilter(userRepository));
        return registrationBean;
    }
}
