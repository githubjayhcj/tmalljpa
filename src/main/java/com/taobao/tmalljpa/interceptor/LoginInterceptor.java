package com.taobao.tmalljpa.interceptor;

import com.taobao.tmalljpa.entity.User;
import com.taobao.tmalljpa.util.ToolClass;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //需要登录url
        String[] loginUrl = new String[]{
                //请求页面
                "registerSuccessful",
                "shopCart",
                "orderDetail",
                "payPage",
                "myOrderList",
                "comment",
                // 页面请求
                "addOrderItem",
                "getOrderItem",
                "updateOrderItemCount",
                "sentOrderItems",
                "getSessionOrderItem",
                "addOrder",
                "getOrder",
                "payMoney",
                "getMyOrder",
                "confirmOrder",
                "getCurrentOrder",
                "postComment"

        };
        HttpSession session = request.getSession();
        if ((User)session.getAttribute("user") == null){
            String url = request.getRequestURI();
            url = url.substring(url.lastIndexOf("/")+1);
            ToolClass.out("url="+url);
            for (String signUrl : loginUrl){
                if (signUrl.equals(url)){
//                    request.getRequestDispatcher("login").forward(request,response);  // 转发
                    response.sendRedirect("login");  // 重定向
                    return false;
                }
            }
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
