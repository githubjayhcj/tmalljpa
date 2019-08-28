package com.taobao.tmalljpa.exceptionController;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Controller
public class GlobalExcptionHandler {

    @ExceptionHandler(value = Exception.class)
    public Exception globalExcption(Exception e){
        System.err.println("GlobalExcptionHandler --> Exception");
        System.err.println("excption message:"+e.getMessage());
        e.printStackTrace();
        return e;
    }

}
