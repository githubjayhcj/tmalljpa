package com.taobao.tmalljpa.web;


import com.taobao.tmalljpa.entity.*;
import com.taobao.tmalljpa.util.ImageUtil;
import com.taobao.tmalljpa.util.NavigatorPage;
import com.taobao.tmalljpa.util.OrderStatus;
import com.taobao.tmalljpa.util.ToolClass;

import com.taobao.tmalljpa.service.*;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

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
    @Autowired
    private CommentService commentService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderItemService orderItemService;


    // home data
    @GetMapping("homeData")
    public Response homeData(){
        ToolClass.out("homeData");

        //find all category
        List<Category> categories = categoryService.findAll();
        //find five product by category one by one
        Map<Integer,List<Product>> productsMap = new HashMap<>();
        Map<Integer, ProductImage> productImageMap = new HashMap<>();
        for (Category category : categories){
            List<Product> products = productService.findByCategoryId(category.getId(),0,5,3).getContent();
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
        ToolClass.err("signUp");
        ToolClass.out(user.toString());
        user.setName(user.getName().trim());
        // html 字符过滤，防止注入攻击
        user.setName(HtmlUtils.htmlEscape(user.getName()));
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
        String salt = new SecureRandomNumberGenerator().nextBytes().toString();
        //  密码验证的核心步骤
        String encodePassword = new SimpleHash("md5",user.getPassword(),salt,2).toString();//最后参数为加密迭代次数
        ToolClass.out("password="+user.getPassword()+",salt="+salt+",encodePassword="+encodePassword);
        user.setSalt(salt);
        user.setPassword(encodePassword);
        userService.save(user);
        return new Response<Boolean>("register successful",true);
    }

    // signIn
    @PostMapping("signIn")
    public Response signIn(@RequestBody User inferUser,HttpSession httpSession){
        ToolClass.err("signIn");
        ToolClass.out(inferUser.toString());

        inferUser.setName(inferUser.getName().trim());
        inferUser.setPassword(inferUser.getPassword().trim());
        if(inferUser.getName().equals("")){
            return new Response<Integer>(2,"userName is empty");
        }
        if(inferUser.getPassword().equals("")){
            return new Response<Integer>(3,"password is empty");
        }
        User user = userService.findByName(inferUser.getName());
        if (user == null){
            return new Response<Integer>(4,"username not exist");
        }
        //向shirorealm注入账号密码
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(inferUser.getName(),inferUser.getPassword());
        ToolClass.out("su=="+SecurityUtils.getSubject());
        Subject subject = SecurityUtils.getSubject();
        //如果已经登录过了，退出
        if (subject.isAuthenticated()){
            subject.logout();
        }
        try {
            subject.login(usernamePasswordToken);
            httpSession.setAttribute("user",user);
            return new Response<Integer>(Response.SUCCESS,"sign in successful");
        }catch (AuthenticationException ae){
            ToolClass.out("用户密码错误");
            return new Response<Integer>(5,"password is wrong");
        }

    }

    //sign out
    /*@GetMapping("signOut")
    public Response signOut(HttpSession httpSession){
        httpSession.removeAttribute("user");
        return new Response("loginOut successful");
    }*/

    // signIn
    @GetMapping("getItemData")
    public Response getItemData(@RequestParam(value = "pid")int pid,HttpSession httpSession){
        ToolClass.out("getItemData");
        ToolClass.out("pid");
        ToolClass.out(pid);
        if(pid <= 0){
            return new Response(Response.FAIL,"find product failed id ="+pid);
        }
        Product product = productService.findById(pid);
        ToolClass.out("product");
        ToolClass.out(product);
        if(product == null){
            return new Response(Response.FAIL,"ont find product by id ="+pid);
        }
        List<ProductImage> productImages = productImageService.findByProductId(pid);
        ToolClass.out("product image");
        ToolClass.out(productImages);
        List<ProductImage> productSingle = new ArrayList<>();
        List<ProductImage> productDetail = new ArrayList<>();
        for(ProductImage productImage : productImages){
            if(productImage.getType().equals(ProductImage.SINGLE_TYPE)){
                productSingle.add(productImage);
            }
            if(productImage.getType().equals(ProductImage.DETAIL_TYPE)){
                productDetail.add(productImage);
            }
        }
        ToolClass.out("productsingle");
        ToolClass.out(productSingle);
        ToolClass.out("productdetail");
        ToolClass.out(productDetail);
        List<Property> properties = propertyService.findByCategoryId(product.getCategory().getId());
        ToolClass.out("properties");
        ToolClass.out(properties);
        if(properties.size() <=0){
            return new Response(Response.FAIL,"ont find Properties by category id ="+product.getCategory().getId());
        }
        List<PropertyValue> propertyValues = propertyValueService.findByPid(product.getId());
        ToolClass.out("propertiesvalues");
        ToolClass.out(propertyValues);
        //填充property 的 propertyValue值
        propertyService.setPropertiesvalue(properties,propertyValues);
        ToolClass.out("properties");
        ToolClass.out(properties);
        //推荐产品列表，随机10个
//        Sort sort = new Sort(Sort.Direction.DESC,"id");
//        Pageable pageable = PageRequest.of(2,10,sort);
        List<Product> products = productService.findAll(2,10,3).getContent();
        Map<String,ProductImage> productImageMap = new HashMap();
        for(Product p : products){
            List<ProductImage> pis = productImageService.findByProductIdAndType(p.getId(),ProductImage.SINGLE_TYPE);
            if(pis.size()>0){
                productImageMap.put(String.valueOf(p.getId()),pis.get(0));
            }else {
                productImageMap.put(String.valueOf(p.getId()),new ProductImage());
            }
        }
        Map<String,Object> map = new HashMap<>();
        map.put("product",product);
        map.put("productSingle",productSingle);
        map.put("productDetail",productDetail);
        map.put("properties",properties);
        map.put("products",products);
        map.put("productImageMap",productImageMap);
        ToolClass.out("-----"+map.toString());
        httpSession.setAttribute("product",product);
        return new Response<Map<String,Object>>(map);
    }

    // signIn
    @GetMapping("productSortByZH")
    public Response productSortByZH(@RequestParam(name = "start",defaultValue = "0")int start,@RequestParam(name = "size",defaultValue = "5")int size,int cid){
        ToolClass.out("cid = "+ cid);
        NavigatorPage<Product> navigatorPage = productService.findByCategoryId(cid,start,size,3);
        // set image
        List<Product> products = navigatorPage.getContent();
        List<ProductImage> images = productImageService.findByProductInAndType(products,ProductImage.SINGLE_TYPE);
        productService.setProductImages(products,images);

        ToolClass.out(navigatorPage.getContent().get(0).toString());

        Response<NavigatorPage> response = new Response<>();
        response.setCode(Response.SUCCESS);
        response.setResult(navigatorPage);

        return response;
    }

    // 判断是否登录
    @GetMapping("inferSignIn")
    public Response inferSignIn(HttpServletRequest request){
        ToolClass.out("inferSigIn");
        Response<User> response = new Response();
        Subject subject = SecurityUtils.getSubject();
        if(subject.isAuthenticated()){//  login
            response.setCode(Response.SUCCESS);
            HttpSession httpSession = request.getSession();
            User user = (User) httpSession.getAttribute("user");
            response.setResult(user);
            response.setMessage("user login");
        }else {
            response.setCode(Response.FAIL);
            response.setMessage("user logout");
        }
        return response;
    }

    //  添加购物车
    @PostMapping("addOrderItem")
    public Response addOrderItem(HttpSession session,@RequestBody Map params){
        ToolClass.out("addOrderItem");
        ToolClass.out(params.toString());
        //ToolClass.out("uid="+uid+"pid="+pid+"count="+count);
        User user = (User) session.getAttribute("user");
        Response response = new Response();
        if (user != null){
            if (user.getId() == (Integer)params.get("uid")){
                List<OrderItem> orderItems = orderItemService.findOrderItemsByUserAndOrderIsNull(user);
                boolean inferCreate = true;
                List<OrderItem> orderItemsId = new ArrayList<>();
                if (orderItems.size() != 0){
                    for (OrderItem oi : orderItems){
                        if (oi.getProduct().getId() == Integer.parseInt(params.get("pid").toString())){
                            oi.setCount(Integer.parseInt(params.get("count").toString()));
                            orderItemService.save(oi);
                            orderItemsId.add(oi);
                            inferCreate = false;
                            break;
                        }
                    }
                }
                if(inferCreate) {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setUser(new User(Integer.parseInt(params.get("uid").toString())));
                    orderItem.setProduct(new Product(Integer.parseInt(params.get("pid").toString())));
                    orderItem.setCount((Integer.parseInt(params.get("count").toString())));
                    orderItemService.save(orderItem);
                    orderItemsId.add(orderItem);
                }
                ToolClass.out("addOrderItem2");
                //List<OrderItem> orderItems2 = orderItemService.findOrderItemsByUserAndOrderIsNull(user);
                //ToolClass.out(orderItems2.toString());
                response.setCode(Response.SUCCESS);
                Map<String,Object> map = new HashMap<>();
                map.put("orderItems",orderItemsId);
                response.setResult(map);
                response.setMessage("add order item success");
            }else {
                response.setCode(Response.FAIL);
                response.setMessage("用户数据错误，请重新提交购买物品");
            }
        }else {
            response.setCode(Response.FAIL);
            response.setMessage("getOrderItem fail please login");
        }
        return response;
    }

    //  购物车
    @GetMapping("getOrderItem")
    public Response getOrderItem(HttpSession session){
        ToolClass.out("getOrderItem");
        User user = (User) session.getAttribute("user");
        Response response = new Response();
        if (user != null){
            List<OrderItem> orderItems = orderItemService.findOrderItemsByUserAndOrderIsNull(user);
            if (orderItems.size() > 0){
                // set image
                List<Product> products = new ArrayList<>();
                for (OrderItem orderItem : orderItems){
                    products.add(orderItem.getProduct());
                }
                List<ProductImage> images = productImageService.findByProductInAndType(products,ProductImage.SINGLE_TYPE);
                ToolClass.err(" images ====");
                ToolClass.out(images.toString());
                productService.setProductImage(products,images);
                response.setCode(Response.SUCCESS);
//                Map<String,Object> result = new HashMap();
//                result.put("orderItems",orderItems);

                response.setResult(orderItems);
                response.setMessage("getOrderItem success");
                ToolClass.out(orderItems.toString());
            }else {
                response.setCode(Response.FAIL);
                response.setMessage(" current no OrderItem ");
            }
        }else {
            response.setCode(2);
            response.setMessage("getOrderItem fail please login");
        }
        return response;
    }

    // 底部推荐栏 5 条 ---------
    @GetMapping("getRecommendProduct")
    public Response getRecommendProduct(){
        NavigatorPage<Product> navigatorPage = productService.findByCategoryId(1,0,5,3);
        Map<String,Object> result = new HashMap();
        result.put("products",navigatorPage.getContent());
        ToolClass.out("products ===="+navigatorPage.getContent());
        //ToolClass.out(page.getContent().toString());
        List<Product> ps = new ArrayList<>(navigatorPage.getContent());
        List<ProductImage> productImages = productImageService.findByProductInAndType(ps,ProductImage.SINGLE_TYPE);
        ToolClass.out("products images ====");
        ToolClass.out(productImages.toString());
        Map<String,ProductImage> productImageMap = new HashMap<>();
        for (Product product : ps){
            for (ProductImage productImage : productImages){
                if (product.getId() == productImage.getProduct().getId()){
                    productImageMap.put(String.valueOf(product.getId()),productImage);
                    break;
                }
            }
        }
        result.put("productImageMap",productImageMap);
        Response response = new Response();
        response.setResult(result);
        return response;
    }

    //  更改购买数  orderitem count
    @PostMapping("updateOrderItemCount")
    public Response updateOrderItemCount(HttpSession session ,@RequestBody Map params){
        ToolClass.out("updateOrderItemCount");
        ToolClass.out(params.get("count"));
        Response response = new Response();
        User user = (User)session.getAttribute("user");
        if (user != null){
            List<OrderItem> orderItems = orderItemService.findOrderItemsByUserAndOrderIsNull(user);
            for (OrderItem orderItem : orderItems){
                if (orderItem.getId() == Integer.parseInt(params.get("id").toString())){
                    orderItem.setCount(Integer.parseInt(params.get("count").toString()));
                    orderItemService.save(orderItem);
                    response.setCode(Response.SUCCESS);
                    response.setMessage(" update ok ");
                }
            }
            if (orderItems.size() == 0){
                response.setCode(3);
                response.setMessage(" orderitem not exist ");
            }
        }else {
            response.setCode(2);
            response.setMessage(" update fail no login ");
        }
        return response;
    }

    //  session 存储 orderitems 被选中要购买的物品
    @PostMapping("sentOrderItems")
    public Response sentOrderItems(HttpSession session ,@RequestBody int[] oiids){
        ToolClass.out("sentOrderItems");
        ToolClass.out(oiids.toString());
        ToolClass.out(oiids.length);
        Response response = new Response();
        if (oiids.length > 0){
            User user = (User) session.getAttribute("user");
            if (user != null){
                List<Integer> ids = new ArrayList<>();
                for (int oiid : oiids){
                    ids.add(oiid);
                }
                session.setAttribute("oiids",ids);//方便下次查询
                List<OrderItem> orderItems = orderItemService.findOrderItemsByIdInAndOrderIsNull(ids);
                if (orderItems.size() == oiids.length){
                    response.setCode(1);
                    response.setMessage(" sentOrderItems success ");
                }else {
                    response.setCode(2);
                    response.setMessage(" orderitem is changed (请重新选择购物车中的商品)  ");
                }
            }else {
                response.setCode(2);
                response.setMessage(" account not login");
            }
        }else {
            response.setCode(2);
            response.setMessage(" orderitem id is empty ");
        }
        return response;
    }

    //  获取 session 存储 orderitems
    @PostMapping("getSessionOrderItem")
    public Response getSessionOrderItem(HttpSession session, HttpServletResponse httpServletResponse){
        ToolClass.out("getSessionOrderItem");
        Response response = new Response();
        User user =(User) session.getAttribute("user");
        if (user != null){
            List<Integer> oiids = (List<Integer>) session.getAttribute("oiids");
            ToolClass.out("getSessionOrderItem = "+oiids);
            if (oiids == null || oiids.size() == 0){
                ToolClass.out("oiids is empty");
                response.setCode(3);
                response.setMessage(" SessionOrderItem id is empty ");
            }else {
                List<OrderItem> orderItems = orderItemService.findOrderItemsByIdInAndOrderIsNull(oiids);
                if (orderItems.size() == oiids.size()){
                    //set single image
                    List<Product> products = new ArrayList<>();
                    BigDecimal totalPrice = new BigDecimal(0);
                    for (OrderItem orderItem : orderItems){
                        products.add(orderItem.getProduct());
                        totalPrice= totalPrice.add(new BigDecimal(orderItem.getCount()).multiply(orderItem.getProduct().getPromotePrice()));
                    }
                    List<ProductImage> productImages = productImageService.findByProductInAndType(products,ProductImage.SINGLE_TYPE);
                    productService.setProductImage(products,productImages);
                    Map<String,Object> map = new HashMap<>();
                    map.put("orderItems",orderItems);
                    map.put("totalPrice",totalPrice);
                    map.put("userName",user.getName());
                    response.setResult(map);
                    response.setCode(1);
                    response.setMessage(" getSessionOrderItem success ");
                }else {
                    response.setCode(2);
                    response.setMessage(" SessionOrderItem is changed (请重新选择购物车中的商品)  ");
                }
            }
        }else {
            response.setCode(2);
            response.setMessage(" account not login");
        }
        return response;
    }

    //   添加 order 存储 orderitems
    @PostMapping("addOrder")
    public Response addOrder(HttpSession session ,@RequestBody Order order) {
        ToolClass.out("addOrder");
        ToolClass.out(order.toString());
        Response response = new Response();
        User user =(User) session.getAttribute("user");
        if (user != null){
            List<Integer> oiids = (List<Integer>) session.getAttribute("oiids");
            session.removeAttribute("oiids");
            ToolClass.out("addorder = "+oiids);
            if (oiids.size() > 0){
                List<OrderItem> orderItems = orderItemService.findOrderItemsByIdInAndOrderIsNull(oiids);
                ToolClass.out("orderItems = "+orderItems.size());
                if (orderItems.size() == oiids.size()){
                    if (order.getAddress().trim() != ""){
                        if (order.getReceiver().trim() != ""){
                            if (String.valueOf(Long.parseLong(order.getMobile())).length() == 11){
                                order.setStatus(OrderStatus.WAIT_PAY);
                                String code = String.valueOf(System.currentTimeMillis());
                                code += user.getId();
                                order.setOrderCode(code);
                                order.setUser(user);
                                order.setCreateTime(new Date());
                                orderService.save(order);
                                for (OrderItem orderItem : orderItems){
                                    orderItem.setOrder(order);
                                }
                                orderItemService.save(orderItems);
                                ToolClass.out("order = "+order.toString());
                                response.setCode(1);
                                response.setResult(order.getId());
                                response.setMessage("add order success");
                            }else {
                                response.setCode(2);
                                response.setMessage(" order mobile is unvalide ");
                            }
                        }else {
                            response.setCode(2);
                            response.setMessage(" order Receiver is empty ");
                        }
                    }else {
                        response.setCode(2);
                        response.setMessage(" order address is empty ");
                    }
                }else {
                    response.setCode(2);
                    response.setMessage(" SessionOrderItem is changed (请重新选择购物车中的商品)  ");
                }
            }else {
                response.setCode(2);
                response.setMessage(" SessionOrderItem id is empty ");
            }
        }else {
            response.setCode(2);
            response.setMessage(" account not login");
        }
        return response;
    }

    //    获取 order  计算 orderitem 支付
    @GetMapping("getOrder")
    public Response getOrder(HttpSession session ,@RequestParam int oid) {
        ToolClass.out("getOrder");
        ToolClass.out(oid);
        Response response = new Response();
        User user =(User) session.getAttribute("user");
        if (user != null){
            Order order = orderService.findOrderById(oid);
            if (order != null && order.getStatus().equals(OrderStatus.WAIT_PAY)){
                List<OrderItem> orderItems = orderItemService.findOrderItemsByOrder(order);
                if (orderItems.size() > 0){
                    BigDecimal totalPrice = new BigDecimal(0);
                    for (OrderItem orderItem : orderItems){
                        totalPrice = totalPrice.add(new BigDecimal(orderItem.getCount()).multiply(orderItem.getProduct().getPromotePrice()));
                    }
                    Order newOrder = new Order();
                    newOrder.setId(order.getId());
                    newOrder.setTotalPrice(totalPrice);
                    response.setCode(1);
//                    Map<String,Order> map = new HashMap<>();
//                    map.put("order",newOrder);
                    response.setResult(newOrder);
                    response.setMessage("get order success");
                    ToolClass.out(newOrder);
                }else {
                    response.setCode(2);
                    response.setMessage(" order item not find");
                }
            }else {
                response.setCode(2);
                response.setMessage(" order not exist");
            }
        }else {
            response.setCode(2);
            response.setMessage(" account not login");
        }
        return response;
    }

    //   添加 order 存储 orderitems
    @PostMapping("payMoney")
    public Response payMoney(HttpSession session ,@RequestBody Order order) {
        ToolClass.out("payMoney");
        ToolClass.out(order.toString());
        Response response = new Response();
        User user = (User) session.getAttribute("user");
        if (user != null) {
            order = orderService.findOrderById(order.getId());
            if (order != null && order.getStatus().equals(OrderStatus.WAIT_PAY)){
                order.setStatus(OrderStatus.WAIT_DELIVERY);
                order.setPaymentTime(new Date());
                orderService.save(order);
                response.setCode(1);
                response.setMessage(" order is pay complet ");
            }else {
                response.setCode(2);
                response.setMessage(" order not exist");
            }
        }else {
            response.setCode(2);
            response.setMessage(" account not login");
        }
        return response;
    }

    //get order by page 进行分页查询用户数据
    @GetMapping("getMyOrder")
    public Response<NavigatorPage<Order>> getMyOrder (HttpSession session,@RequestParam(value = "start",defaultValue = "0")int start,@RequestParam(value = "size",defaultValue = "5")int size){
        ToolClass.out("getMyOrder ");
        Response response = new Response();
        User user =(User) session.getAttribute("user");
        if(user != null){
            //按id 排序 正序
            Sort sort = new Sort(Sort.Direction.ASC,"id");
            // 分页对象
            Pageable pageable = PageRequest.of(start,size,sort);
            Page<Order> page = orderService.findOrdersByUser(user,pageable);
            ToolClass.out(page.getContent().size());
            // 自定义分页对象(整理成自己需要的数据字段，方便取值显示)
            NavigatorPage<Order> navigatorPage = new NavigatorPage<Order>(page,5);

       ToolClass.out(navigatorPage.toString());
//        List<Order> orders = orderService.findAll();
//        ToolClass.out(orders.toString());

            //根据order  查询 orderitem
            List<OrderItem> orderItems = orderItemService.findByOrderIn(navigatorPage.getContent());
            //获取产品封面图
            List<Product> products = new ArrayList<>();
            for (OrderItem oi : orderItems){
                products.add(oi.getProduct());
            }
            List<ProductImage> productImages = productImageService.findByProductInAndType(products,ProductImage.SINGLE_TYPE);
            // 为 product 添加 productimage
            productService.setProductImage(products,productImages);
            // 为order 对象注入 orderitem   ,并计算参数值
            orderService.setOrderItems(navigatorPage.getContent(),orderItems);
            //去除笛卡尔积，对象相互绑定(内存溢出)
            orderService.setOrderNull(navigatorPage.getContent());

            response.setCode(1);
            response.setMessage("select orders success");
            response.setResult(navigatorPage);
            ToolClass.out(" --- --- - - - - - - - ");
            ToolClass.out(page.getContent().toString());
            ToolClass.out(orderItems.toString());
        }else {
            response.setCode(2);
            response.setMessage(" account not login");
        }
        return response;
    }

    //   添加 order 存储 orderitems
    @PostMapping("confirmOrder")
    public Response confirmOrder(HttpSession session ,@RequestBody Order order) {
        ToolClass.out("confirmOrder");
        ToolClass.out(order.toString());
        Response response = new Response();
        User user = (User) session.getAttribute("user");
        if (user != null) {
            order = orderService.findOrderById(order.getId());
            if (order != null && order.getStatus().equals(OrderStatus.WAIT_CONFIRM)){
                order.setStatus(OrderStatus.WAIT_COMMENT);
                order.setPaymentTime(new Date());
                orderService.save(order);
                response.setCode(1);
                response.setMessage(" order is pay complet ");
            }else {
                response.setCode(2);
                response.setMessage(" order not exist");
            }
        }else {
            response.setCode(2);
            response.setMessage(" account not login");
        }
        return response;
    }

    //get order by page 进行分页查询用户数据
    @PostMapping("getCurrentOrder")
    public Response getCurrentOrder (HttpSession session,@RequestBody Order order){
        ToolClass.out("getCurrentOrder ");
        ToolClass.out("Order =" +order);
        Response response = new Response();
        User user =(User) session.getAttribute("user");
        if(user != null){
            //根据order  查询 orderitem
            List<OrderItem> orderItems = orderItemService.findOrderItemsByOrder(order);
            ToolClass.out("orderItems ");
            ToolClass.out("orderItems =" +orderItems.toString());
            if (orderItems.size() != 0){
                // 获取对应评论,并剔除已评论的该订单商品
                List<Comment> comments = commentService.findByUserAndOrder(user,order);
                for (Comment comment : comments){
                    for (OrderItem orderItem : orderItems){
                        if(comment.getProduct().getId() == orderItem.getProduct().getId()){
                            orderItems.remove(orderItem);
                            break;
                        }
                    }
                }
                if (orderItems.size() != 0){
                    //获取产品封面图
                    List<Product> products = new ArrayList<>();
                    for (OrderItem oi : orderItems){
                        products.add(oi.getProduct());
                    }
                    List<ProductImage> productImages = productImageService.findByProductInAndType(products,ProductImage.SINGLE_TYPE);
                    // 为 product 添加 productimage
                    productService.setProductImage(products,productImages);
                    // 为order 对象注入 orderitem   ,并计算参数值
                    List<Order> orders = new ArrayList<>();
                    orders.add(order);
                    orderService.setOrderItems(orders,orderItems);
                    //去除笛卡尔积，对象相互绑定(内存溢出)
                    orderService.setOrderNull(orders);
                    response.setCode(1);
                    response.setMessage("get order success");
                    response.setResult(order);
                    ToolClass.out("order");
                    ToolClass.out(order.toString());
                }else {
                    response.setCode(2);
                    response.setMessage("没有需要评论的订单商品");
                }
            }else {
                response.setCode(2);
                response.setMessage(" orderitems not exist");
            }
        }else {
            response.setCode(2);
            response.setMessage(" account not login");
        }
        return response;
    }

    //get order by page 进行分页查询用户数据
    @PostMapping("postComment")
    public Response postComment (HttpSession session,@RequestBody Map params){
        ToolClass.out("postComment ");
        ToolClass.out("params =" +params.toString());
        Response response = new Response();
        User user =(User) session.getAttribute("user");
        if(user != null){
            int uid = Integer.parseInt(params.get("uid").toString());
            if (uid == user.getId()){
                String commentContent = params.get("commentContent").toString();
                if (commentContent.trim() != ""){
                    //根据order  product  查询  comment
                    int oid = Integer.parseInt(params.get("oid").toString());
                    Order order = orderService.findOrderById(oid);
                    int pid = Integer.parseInt(params.get("pid").toString());
                    Product product = productService.findById(pid);
                    Comment comment = commentService.findByOrderAndProduct(order,product);
                    if (comment == null){
                        ToolClass.out("order set =" +order.toString());
                        comment = new Comment();
                        comment.setOrder(order);
                        comment.setUser(user);
                        comment.setProduct(product);
                        comment.setContent(commentContent);
                        comment.setCreateDate(new Date());
                        commentService.save(comment);
                        ToolClass.out("comment =" +comment.toString());

                        // set order status  finish
                        //根据order  查询 orderitem
                        List<OrderItem> orderItems = orderItemService.findOrderItemsByOrder(order);
                        // 获取对应评论
                        List<Comment> comments = commentService.findByUserAndOrder(user,order);
                        if (orderItems.size() == comments.size()){//订单全部完成评论
                            user = userService.findById(user.getId());
                            order.setStatus(OrderStatus.FINISH);
                            orderService.save(order);
                        }
                        response.setCode(1);
                        response.setMessage("comment success ");
                    }else {
                        response.setCode(2);
                        response.setMessage("该订单产品评论已存在 ");
                    }
                }else {
                    response.setCode(2);
                    response.setMessage(" comment content is empty ");
                }
            }else {
                response.setCode(2);
                response.setMessage("account error ,please return order list ");
            }
        }else {
            response.setCode(2);
            response.setMessage(" account not login");
        }
        return response;
    }

    //@GetMapping("foreTest")  // 需要显示的定义请求方法为get ( 疑虑：浏览器地址栏请求服务端无法解析 )
    @RequestMapping("foreTest")
    public Response foreTest(){
        ToolClass.out("foreTest");

//        List<Comment> comments = commentService.findAll();
//        ToolClass.out(comments.size());
//        for (Comment comment : comments){
//            //commentService.delete(comment);
//        }

        Response response = new Response();
        return response;
    }
}
