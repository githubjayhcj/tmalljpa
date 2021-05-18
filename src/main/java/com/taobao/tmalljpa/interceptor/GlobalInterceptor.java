package com.taobao.tmalljpa.interceptor;

import com.taobao.tmalljpa.entity.Order;
import com.taobao.tmalljpa.entity.OrderItem;
import com.taobao.tmalljpa.entity.User;
import com.taobao.tmalljpa.service.OrderItemService;
import com.taobao.tmalljpa.service.ProductService;
import com.taobao.tmalljpa.util.ToolClass;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

public class GlobalInterceptor implements HandlerInterceptor {

    @Autowired
    private ProductService productService;
    @Autowired
    private OrderItemService orderItemService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String url = request.getRequestURL().toString();
        ToolClass.out("url g ="+url);
        url = url.substring(url.lastIndexOf("/")+1);
        ToolClass.out("url g sub="+url);
        //product item title
        if (url.equals("item")){
            String params = request.getQueryString();
            params = params.substring(params.lastIndexOf("pid")+4);
            if (params.indexOf("$") != -1){
                params = params.substring(0,params.indexOf("$"));
            }
            ToolClass.out("param g ="+params);
            request.getSession().setAttribute("currentProduct",productService.findById(Integer.parseInt(params)));
        }
        //cart shop count
        ToolClass.out("subject ="+SecurityUtils.getSubject());
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()){
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");
            List<OrderItem> orderItems = orderItemService.findOrderItemsByUserAndOrderIsNull(user);
            session.setAttribute("orderItems",orderItems);//用户购物车商品数量
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
