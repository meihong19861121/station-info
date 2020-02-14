package com.example.stationinfo.config;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class BackInterceptor implements HandlerInterceptor {
    //重写preHandle方法
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ip = request.getParameter("ip");
        HttpSession session = request.getSession();
        if (session.getAttribute("user") != null) {
            return true;
        }
        response.sendRedirect("/index.html");
        return false;
    }
}
