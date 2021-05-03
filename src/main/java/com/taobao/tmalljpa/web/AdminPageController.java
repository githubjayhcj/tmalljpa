package com.taobao.tmalljpa.web;

import com.taobao.tmalljpa.dao.UserDao;
import com.taobao.tmalljpa.service.UserService;
import com.taobao.tmalljpa.util.ToolClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping()
public class AdminPageController {

    @Autowired
    private UserService userService;

    @RequestMapping("index")
    public String index(Model model) {
        ToolClass.out(" index start ......");

        model.addAttribute("user",userService.findById(1));

        ToolClass.out(" index end ......");

        return "index";
    }

    //全局异常处理页
    /*@RequestMapping("globalExcptionPage")
    public String globalExcptionPage(){
        return "excption/globalExcptionPage";
    }*/

    //后台主页
    @RequestMapping("admin")
    public String admin(){
        return "admin/listCategory";
    }

    //分类页
    @RequestMapping("listCategory")
    public String listCategory(Model model) {
        return "admin/listCategory";
    }

    //分类编辑页
    @RequestMapping("editCategory")
    public String editCategory(){
        return "admin/editCategory";
    }

    //分类属性页
    @RequestMapping("listProperty")
    public String listProperty(){
        //ToolClass.out("listProperty");
        return "admin/listProperty";
    }

    //分类属性编辑页
    @RequestMapping("editProperty")
    public String editProperty(){
        return "admin/editProperty";
    }

    //产品管理页
    @RequestMapping("listProduct")
    public String listProduct(){
        return "admin/listProduct";
    }

    //产品图片管理页
    @RequestMapping("listProductImage")
    public String listProductImage(){
        return "admin/listProductImage";
    }

    //产品属性值管理页
    @RequestMapping("editPropertyValue")
    public String editPropertyValue(){
        return "admin/editPropertyValue.html";
    }

    //产品管理页
    @RequestMapping("editProduct")
    public String editProduct(){
        return "admin/editProduct.html";
    }

    //用户管理
    @RequestMapping("listUser")
    public String listUser(){
        return "admin/listUser";
    }

    //订单管理
    @RequestMapping("listOrder")
    public String listOrder(){
        return "admin/listOrder";
    }
}
