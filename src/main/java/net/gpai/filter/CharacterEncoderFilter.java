package net.gpai.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Slf4j
//@WebFilter(filterName = "characterEncoder", urlPatterns = "**icbc**")
//@Order(0)//控制加载和执行顺序
//@Component
public class CharacterEncoderFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        request.setCharacterEncoding("GBK");
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        log.info("请求匹配过滤器：{}", httpServletRequest.getRequestURI());
        chain.doFilter(request, response);
    }
}
