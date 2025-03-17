package com.japan.compass.annotation.controller.filter;

import com.japan.compass.annotation.domain.entity.User;
import com.japan.compass.annotation.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

@AllArgsConstructor
public class SessionValidationFilter implements Filter {

    private final UserRepository userRepository;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        if (SecurityContextHolder.getContext().getAuthentication() != null
                && SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof User) {
            User user = userRepository.findById(((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId());
            if (user == null || !user.isEnabled()) {
                SecurityContextHolder.clearContext();
            }
        }

        chain.doFilter(request, response);
    }
}
