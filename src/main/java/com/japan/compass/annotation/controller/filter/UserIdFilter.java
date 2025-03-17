package com.japan.compass.annotation.controller.filter;

import com.japan.compass.annotation.domain.entity.User;
import org.slf4j.MDC;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

public class UserIdFilter implements Filter {

    private static final String USER_ID_KEY = "USER_ID";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            if (SecurityContextHolder.getContext().getAuthentication() != null
                    && SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof User) {
                MDC.put(USER_ID_KEY, String.valueOf(((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId()));
            }

            chain.doFilter(request, response);
        } finally {
            MDC.remove(USER_ID_KEY);
        }
    }
}
