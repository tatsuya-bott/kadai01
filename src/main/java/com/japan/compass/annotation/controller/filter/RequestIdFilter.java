package com.japan.compass.annotation.controller.filter;

import org.slf4j.MDC;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.util.UUID;

public class RequestIdFilter implements Filter {

    private static final String REQUEST_ID_KEY = "REQUEST_ID";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            MDC.put(REQUEST_ID_KEY, UUID.randomUUID().toString());
            chain.doFilter(request, response);
        } finally {
            MDC.remove(REQUEST_ID_KEY);
        }
    }
}
