package com.su29029.server.bean;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginHandlerInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        
        System.out.println(request.getRequestURL());
        System.out.println(request.getSession().getAttribute("user"));
        Object user = request.getSession().getAttribute("user");
        if (user == null) {
            System.out.println("intercept");
            // request.setAttribute("loginStatus",-1);
            return false;
        } else {
            System.out.println("pass");
            request.setAttribute("loginStatus", 1);
            return true;
        }
    }
    
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        System.out.println("postHandle" + response.toString());
        response.setHeader("Set-Cookie", "aa");
    }

    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler) {
        System.out.println("afterCompletion" + response.toString());
        response.setHeader("Set-Cookie", "aa");
    }
}
