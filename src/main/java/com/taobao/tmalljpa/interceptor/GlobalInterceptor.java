package com.taobao.tmalljpa.interceptor;

import com.taobao.tmalljpa.service.ProductService;
import com.taobao.tmalljpa.util.ToolClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GlobalInterceptor implements HandlerInterceptor {

    @Autowired
    private ProductService productService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String url = request.getRequestURL().toString();
        url = url.substring(url.lastIndexOf("/")+1);
        ToolClass.out("url g ="+url);
        if (url.equals("item")){
            String params = request.getQueryString();
            params = params.substring(params.lastIndexOf("pid")+4);
            if (params.indexOf("$") != -1){
                params = params.substring(0,params.indexOf("$"));
            }
            ToolClass.out("param g ="+params);
            request.getSession().setAttribute("currentProduct",productService.findById(Integer.parseInt(params)));
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
