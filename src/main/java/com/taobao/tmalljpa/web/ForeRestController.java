package com.taobao.tmalljpa.web;


import com.taobao.tmalljpa.entity.*;
import com.taobao.tmalljpa.util.ToolClass;

import com.taobao.tmalljpa.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("")
public class ForeRestController {
    @Autowired
    private UserService userService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private PropertyService propertyService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductImageService productImageService;
    @Autowired
    private PropertyValueService propertyValueService;


    // home data
    @GetMapping("homeData")
    public Response homeData(){
        ToolClass.out("homeData");

        //find all category
        List<Category> categories = categoryService.findAll();
        //find five product by category one by one
        Map<Integer,List<Product>> productsMap = new HashMap<>();
        Sort sort = new Sort(Sort.Direction.DESC,"id");
        Pageable pageable = PageRequest.of(0,5,sort);
        Map<Integer, ProductImage> productImageMap = new HashMap<>();
        for (Category category : categories){
            List<Product> products = productService.findByCategoryId(category.getId(),pageable).getContent();
            if (products.size() > 0){// products size > 0
                productsMap.put(category.getId(),products);
                //get single Image by product one by one
                for(Product product : products){
                    List<ProductImage> productImages = productImageService.findByProductIdAndType(product.getId(),ProductImage.SINGLE_TYPE);
                    if(productImages.size() > 0){
                        productImageMap.put(product.getId(),productImages.get(0));
                    }else {
                        productImageMap.put(product.getId(),new ProductImage());
                    }
                }
            }
        }
        Map<String,Object> map = new HashMap<>();
        map.put("categories",categories);
        map.put("productsMap",productsMap);
        map.put("productImageMap",productImageMap);
        return new Response("get homeData successful",map);
    }

    /* register html */
    // validate userName
    @GetMapping("validateUserName")
    public Response validateUserName(@RequestParam String name){
        ToolClass.out("validateUserName");
        ToolClass.out("name="+name);
        User user = userService.findByName(name);
        if(user != null){
            return new Response("userName is exist",false);
        }
        return new Response("userName is usable",true);
    }

    // signUp
    @PostMapping("signUp")
    public Response signUp(@RequestBody User user){
        ToolClass.out("signUp");
        ToolClass.out(user.toString());
        user.setName(user.getName().trim());
        user.setPassword(user.getPassword().trim());
        if(user.getName().equals("")){
            return new Response<Boolean>("userName is empty",false);
        }
        if(user.getPassword().equals("")){
            return new Response<Boolean>("password is empty",false);
        }
        User inferUser = userService.findByName(user.getName());
        if(inferUser != null){
            return new Response<Boolean>("userName is exist",false);
        }
        userService.save(user);
        ToolClass.out("salt="+user.getPassword());
        return new Response<Boolean>("register successful",true);
    }

    // signIn
    @PostMapping("signIn")
    public Response signIn(@RequestBody User user,HttpSession httpSession){
        ToolClass.out("signIn");
        ToolClass.out(user.toString());

        user.setName(user.getName().trim());
        user.setPassword(user.getPassword().trim());
        if(user.getName().equals("")){
            return new Response<Integer>("userName is empty",1);
        }
        if(user.getPassword().equals("")){
            return new Response<Integer>("password is empty",2);
        }
        User inferUser = userService.findByName(user.getName());
        if(inferUser == null){
            return new Response<Integer>("userName is ont find",3);
        }
        if(!user.getPassword().equals(inferUser.getPassword())){
            return new Response<Integer>("password is wrong",4);
        }
        httpSession.setAttribute("user",inferUser);
        return new Response<Integer>("sign in successful",5);
    }

    //sign out
    /*@GetMapping("signOut")
    public Response signOut(HttpSession httpSession){
        httpSession.removeAttribute("user");
        return new Response("loginOut successful");
    }*/

}
