package com.taobao.tmalljpa.web;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("")
public class ForePageController {

    @RequestMapping("home")
    public String home(){
        return "fore/home";
    }

    @RequestMapping("register")
    public String register(){
        return "fore/register";
    }

    @RequestMapping("registerSuccessful")
    public String registerSuccessful(){
        return "fore/registerSuccessful";
    }

    @RequestMapping("login")
    public String login(){
        return "fore/login";
    }

    //sign out
    @RequestMapping("signOut")
    public String signOut(HttpSession httpSession){
        httpSession.removeAttribute("user");
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return "redirect:home";
    }

    @RequestMapping("item")
    public String item(){
        return "fore/item";
    }

    @RequestMapping("productSort")
    public String producSort(){
        return "fore/productSort";
    }

    @RequestMapping("shopCart")
    public String shopCart(){
        return "fore/shopCart";
    }

    @RequestMapping("orderDetail")
    public String orderDetail(){
        return "fore/orderDetail";
    }

    @RequestMapping("payPage")
    public String payPage(){
        return "fore/payPage";
    }

    @RequestMapping("myOrderList")
    public String myOrderList(){
        return "fore/myOrderList";
    }

    @RequestMapping("comment")
    public String comment(){
        return "fore/comment";
    }

    //test
    @RequestMapping("test")
    public String test(){
        return "fore/test";
    }

}
