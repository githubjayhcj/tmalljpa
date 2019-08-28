package com.taobao.tmalljpa.web;

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
        return "redirect:home";
    }

}
