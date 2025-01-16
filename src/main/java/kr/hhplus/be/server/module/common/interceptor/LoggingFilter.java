package kr.hhplus.be.server.module.common.interceptor;

import io.micrometer.common.util.StringUtils;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
@WebFilter(urlPatterns = "/*")
public class LoggingFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        log.info("Request Method: {}, Request URI: {}, Access IP: {}", httpRequest.getMethod(), httpRequest.getRequestURI(), request.getRemoteAddr());

        String userId = (String) httpRequest.getHeader("USER_ID");
        if(StringUtils.isNotEmpty(userId)) {
            request.setAttribute("loginId", userId);
        }

        chain.doFilter(request, response);

    }
}
