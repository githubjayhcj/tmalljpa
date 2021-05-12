package com.taobao.tmalljpa.exceptionController;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
@Controller
public class GlobalExcptionHandler {// 全局异常处理 controller

    @ExceptionHandler(value = Exception.class)
    public String globalExcption(HttpServletRequest request, HttpServletResponse response, Exception e){
        request.getSession().setAttribute("e",e);
        System.err.println("全局异常处理....");
        System.err.println("GlobalExcptionHandler --> Exception");
        System.err.println("excption message:"+e.getMessage());
        e.printStackTrace();

        return "excption/globalExcptionPage";
    }

}

//或者
//@ControllerAdvice
//public class GlobalExceptionHandler {
//    @ExceptionHandler(value = Exception.class)
//    public ModelAndView defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {
//        ModelAndView mav = new ModelAndView();
//        mav.addObject("exception", e);
//        mav.addObject("url", req.getRequestURL());
//        mav.setViewName("errorPage");
//        return mav;
//    }
//
//}
