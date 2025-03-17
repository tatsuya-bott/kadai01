package com.japan.compass.annotation.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthenticationRedirectHandler implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        String requestUri = String.valueOf(request.getRequestURI());

        if (requestUri.contains("/user/") && !requestUri.contains("/admin/")) {
            response.sendRedirect("/user/answerer");
            return;
        }

        if (requestUri.contains("/admin/")) {
            response.sendRedirect("/admin/login");
            return;
        }

        response.sendRedirect("/user/answerer");
    }
}
