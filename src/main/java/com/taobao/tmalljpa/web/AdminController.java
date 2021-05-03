package com.taobao.tmalljpa.web;


import com.taobao.tmalljpa.entity.*;
import com.taobao.tmalljpa.util.ImageUtil;
import com.taobao.tmalljpa.util.NavigatorPage;
import com.taobao.tmalljpa.util.OrderStatus;
import com.taobao.tmalljpa.util.ToolClass;

import com.taobao.tmalljpa.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.data.domain.*;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.*;

@RestController
@RequestMapping()
public class AdminController {
    public static final BigDecimal TOP_PRICE = BigDecimal.valueOf(9999999999.99);

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
    private OrderService orderService;
    @Autowired
    private OrderItemService orderItemService;
    //order 状态类
//    @Autowired
//    private OrderStatus orderStatus;

    /*
    *  tip : javax.persistence.Transient;
    * */

    //add , update or delete propertyValue
    @PutMapping("propertyValues/{pid}")
    public Response setPropertyValue(@PathVariable(name = "pid")int pid, @RequestBody Property property){
        ToolClass.out("put propertyValues");
        ToolClass.out("property="+property.toString());
        ToolClass.out("pid="+pid);
        //获取product
        Product product = productService.findById(pid);
        if(product != null){
            //propertyValues 的value 为 空则删除，其余为更改或新增
            PropertyValue propertyValue = property.getPropertyValue();
            if(!propertyValue.getValue().trim().equals("")){//不为空，新增或修改
                propertyValue.setProduct(product);
                propertyValue.setProperty(property);
                propertyValueService.save(propertyValue);
                ToolClass.out("不为空");
                return new Response("set propertyValue successful (save)");
            }else {//为空
                propertyValueService.delete(propertyValue);
                ToolClass.out("为空");
                return new Response("set propertyValue successful (delete)");
            }
        }else {
            return new Response(Response.FAIL,"set propertyValue failed not find product by id = "+pid);
        }
    }

    //get all propertyValue
    @GetMapping("propertyValues")
    public Response getPropertyValues(int pid){
        ToolClass.out("get propertyValues");
        ToolClass.out("pid="+pid);
        //查询产品
        Product product = productService.findById(pid);
        if(product!=null){
            //查询分类
            Category category = categoryService.findById(product.getCategory().getId());
            if(category != null){
                //查询分类的属性
                List<Property> properties = propertyService.findByCategoryId(product.getCategory().getId());
                if(properties.size() > 0){
                    List<PropertyValue> propertyValues = propertyValueService.findByPid(product.getId());
                    ToolClass.out("propertyValues size="+propertyValues.size());
                    //填充property 的 propertyValue值
                    propertyService.setPropertiesvalue(properties,propertyValues);
                    /*List<PropertyValue> lastedPropertyValues = new ArrayList<>(propertyValues);
                    for (Property property : properties){
                        for (int i = 0;i<lastedPropertyValues.size();i++){
                            if (property.getId() == lastedPropertyValues.get(i).getProperty().getId()){
                                // 对象重叠，形成笛卡尔积，报错： Method threw 'java.lang.StackOverflowError' exception. Cannot evaluate Property.toString()
                                lastedPropertyValues.get(i).setProperty(new Property());//去掉内存轮回
                                property.setPropertyValue(lastedPropertyValues.get(i));//深度复制对象
                                //lastedPropertyValues.remove(i);//移除
                                break;
                            }
                        }
                        //遍历完成后，不存在property，手动创建(前端取值异常排除)
                        if(property.getPropertyValue() == null){
                            property.setPropertyValue(new PropertyValue());
                        }
                    }*/
                    ToolClass.out("propertyValues size="+properties.size());
                    return new Response<List<Property>>("get propertyValues successful",properties);
                }else {
                    return new Response(Response.FAIL,"get propertyValues failed , Property not find by category id ="+product.getCategory().getId());
                }
            }else {
                return new Response(Response.FAIL,"get propertyValues failed , category not find by category id ="+product.getCategory().getId());
            }
        }else {
            return new Response(Response.FAIL,"get propertyValues failed , product not find by id ="+pid);
        }
    }

    //delete productImage by piid
    @DeleteMapping("productImages")
    public Response deleteProductImageByPiId(@RequestParam(name = "id") int id,@RequestParam(name = "type") String type){
        ToolClass.out("@DeleteMapping(\"productImages\")");
        ToolClass.out("id="+id);
        ToolClass.out("type="+type);
        if(type.equals(ProductImage.SINGLE_TYPE) || type.equals(ProductImage.DETAIL_TYPE)){//type 值是否正确
            productImageService.deleteById(id);
            //删除图片
            String fileRoot = "";
            try{
                fileRoot = ResourceUtils.getURL("classpath:static/image").getPath().replace("%20"," ").replace("/","\\");
            }catch (FileNotFoundException e){
                e.getMessage();
                e.printStackTrace();
                return new Response(Response.FAIL,"FileNotFoundException");
            }
            if(type.equals(ProductImage.SINGLE_TYPE)){//single , 删除productSingle，productSingle_middle，productSingle_small
                //删除productSingle
                File file = new File(fileRoot+"/productSingle",id+".jpg");
                if(file.exists()){
                    file.delete();
                }
                //productSingle_middle
                file = new File(fileRoot+"/productSingle_middle",id+".jpg");
                if(file.exists()){
                    file.delete();
                }
                //productSingle_small
                file = new File(fileRoot+"/productSingle_small",id+".jpg");
                if(file.exists()){
                    file.delete();
                }
                return new Response("delete single productImage successful",type);
            }else if(type.equals(ProductImage.DETAIL_TYPE)){//detail 删除productDetail
                //productSingle_small
                File file = new File(fileRoot+"/productDetail",id+".jpg");
                if(file.exists()){
                    file.delete();
                }
                return new Response("delete detail productImage successful",type);
            }
        }
        // type 参数异常
        return new Response(Response.FAIL,"delete failed type should be 'single' or 'detail'");
    }

    //get productImage by pid
    @GetMapping("productImages/{pid}")
    public Response findProductImageByPage(@PathVariable(name = "pid")int pid,@RequestParam(name = "type")String type){
        ToolClass.out("@GetMapping(\"productImages\")");
        ToolClass.out("pid="+pid);
        ToolClass.out("type="+type);

        List<ProductImage> productImages = productImageService.findByProductIdAndType(pid,type);
        ToolClass.out("productImages="+productImages.toString());
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("type",type);
        map.put("productImages",productImages);
        return new Response<Map<String,Object>>("find productImage successful",map);
    }

    //save productImage
    @PostMapping("productImages")
    public Response addProductImage(@RequestParam(name = "type")String type,@RequestParam(name = "pid")int pid,@RequestParam(name = "image") MultipartFile multipartFile){
        ToolClass.out("@PostMapping(\"productImages\")");
        ToolClass.out("type="+type);
        ToolClass.out("pid="+pid);
        ToolClass.out("multipartFile="+multipartFile);

        Product product = productService.findById(pid);
        if(product != null){//存在当前产品
            ProductImage productImage = new ProductImage(product,type);
            productImageService.save(productImage);
            if (productImage.getId() > 0){//成功
                //保存图片
                if (ProductImage.SINGLE_TYPE.equals(type)){//single : 需要存源文件（productSingle）， 中等文件（prodcutSingle_middle）,小文件（prodcutSingle_small）
                    //存源文件（productSingle）
                    boolean infer = ImageUtil.saveImage("static/image/productSingle",String.valueOf(productImage.getId()),multipartFile);
                    if(infer){
                        try{
                            String filePath = ResourceUtils.getURL("classpath:static/image").getPath().replace("$20"," ").replace("/","\\");
                            ToolClass.out("filePath="+filePath);
                            //中等文件（prodcutSingle_middle）
                            File targetFile = new File(filePath+"/productSingle",productImage.getId()+".jpg");
                            ToolClass.out("targetFile="+targetFile);
                            File productSingle_middle = new File(filePath+"/productSingle_middle",productImage.getId()+".jpg");
                            if (!productSingle_middle.exists()){
                                productSingle_middle.mkdir();
                            }
                            ImageUtil.resizeImage(targetFile,217,190,productSingle_middle);
                            //小文件（prodcutSingle_small）
                            File productSingle_small = new File(filePath+"/productSingle_small",productImage.getId()+".jpg");
                            if(!productSingle_small.exists()){
                                productSingle_small.mkdir();
                            }
                            ImageUtil.resizeImage(targetFile,56,56,productSingle_small);
                            return new Response("add prodcut single image successful");
                        }catch (FileNotFoundException f){
                            f.getMessage();
                            f.getStackTrace();
                            return new Response(Response.FAIL,"add prodcut single image failed");
                        }
                    }else {
                        return new Response(Response.FAIL,"add prodcut single image failed");
                    }
                }
                if(ProductImage.DETAIL_TYPE.equals(type)){//detail
                    boolean infer = ImageUtil.saveImage("static/image/productDetail",String.valueOf(productImage.getId()),multipartFile);
                    if (infer){
                        return new Response("add prodcutImage successful");
                    }else {
                        return new Response(Response.FAIL,"add prodcut detail image failed");
                    }
                }
                return new Response("add prodcutImage successful");
            }else {//失败
                return new Response(Response.FAIL,"add prodcutImage failed");
            }
        }else {
            new Response(Response.FAIL,"not find prodcut by id ="+pid);
        }
        return new Response();
    }

    //delete product
    @DeleteMapping("products")
    public Response deleteProduct(@RequestParam("id")int id){
        ToolClass.out(" @DeleteMapping(\"products\")");
        ToolClass.out("id="+id);
        //删除product ， productImage ， propertyValue
        return productService.deleteProductResource(id);
    }

    //get one product
    @GetMapping("products")
    public Response getProduct(@RequestParam(value = "pid",required = true)int id){
        ToolClass.out("getProduct");
        ToolClass.out("id="+id);
        Product product = productService.findById(id);
        //ToolClass.out(product.toString());
        return new Response<Product>("find product by id="+id+" successful",product);
    }

    //update product
    @PutMapping("products")
    public Response updateProduct(@RequestBody Product product){
        ToolClass.out("updateProduct");
        //product.setOriginalPrice(1100124.99f);
        ToolClass.out(product.toString());
        //价格是否合理（大于0 小于最大价格）
        if((product.getOriginalPrice().compareTo(TOP_PRICE) == -1 && product.getOriginalPrice().compareTo(BigDecimal.valueOf(0)) == 1)
                || (product.getPromotePrice().compareTo(TOP_PRICE) == -1 && product.getPromotePrice().compareTo(BigDecimal.valueOf(0)) == 1)){
            productService.save(product);
            return new Response("save product successful");
        }
        //价格超过上限
        return new Response(Response.FAIL,"add product failed max price should < "+TOP_PRICE);
    }

    //get product by page
    @GetMapping("products/{cid}")
    public Response getProductByPage(@PathVariable(name = "cid")int cid,@RequestParam(name = "start",defaultValue = "0")int start,@RequestParam(name = "size",defaultValue = "3")int size){
        ToolClass.out("cid="+cid);
        ToolClass.out("start="+start);
        try {
            Sort sort = new Sort(Sort.Direction.ASC,"id");
            Pageable pageable = PageRequest.of(start,size,sort);
            Page<Product> page = productService.findByCategoryId(cid,pageable);
            ToolClass.out(page.getContent().toString());
            if(page.getContent() != null){
                //获取product 图片做封面（默认第一张）
                List<Product> products = page.getContent();
                Map<Integer,ProductImage> productFirstImages = new HashMap<>();
                List<ProductImage> productImages = new ArrayList<>();
                for (Product product : products){
                    productImages = productImageService.findByProductIdAndType(product.getId(),"single");
                    if (productImages.size()>0){
                        productFirstImages.put(product.getId(),productImages.get(0));
                    }else {//前端为空异常处理
                        productFirstImages.put(product.getId(),new ProductImage());
                    }
                }
                //获取分页页码
                NavigatorPage<Product> navigatorPage = new NavigatorPage<Product>(page,3);
                Map<String,Object> map = new HashMap<>();
                map.put("navigatorPage",navigatorPage);
                map.put("productFirstImages",productFirstImages);
                ToolClass.out(navigatorPage.getContent().size());
                ToolClass.out(navigatorPage.toString());
                return new Response<Map>("find product by page successful",map);
            }
        }catch (InvalidDataAccessResourceUsageException e){
            System.err.println("查询失败，该category 下无 product");
        }
        return new Response(Response.FAIL,"not find product by page");
    }

    //add product
    @PostMapping("products/{cid}")
    public Response addProduct(@PathVariable(name = "cid")int cid,@RequestBody Product product){
        ToolClass.out("cid="+cid);
        ToolClass.out("@PostMapping(\"products\")");
        ToolClass.out(product.toString());
        //价格是否合理（大于0 小于最大价格）
        if((product.getOriginalPrice().compareTo(TOP_PRICE) == -1 && product.getOriginalPrice().compareTo(BigDecimal.valueOf(0)) == 1)
                || (product.getPromotePrice().compareTo(TOP_PRICE) == -1 && product.getPromotePrice().compareTo(BigDecimal.valueOf(0)) == 1)){
            Category category = categoryService.findById(cid);
            if(category != null){
                product.setCategory(category);
                product.setCreateDate(new Date());
                productService.save(product);
                ToolClass.out(product.toString());
                return new Response("add product successfal current category not find");
            }
            return new Response(Response.FAIL,"add product failed");
        }else {//价格超过上限
            return new Response(Response.FAIL,"add product failed max price should < "+TOP_PRICE);
        }
    }

    //update property
    @PutMapping("properties")
    public Response updateProperty(@RequestBody(required = true) Property property){
        ToolClass.out(property.toString());
        propertyService.save(property);
        return new Response("update property successfal");
    }

    // delete peroperty
    @DeleteMapping("properties/{id}")
    public Response deleteProperty(@PathVariable(value = "id")int id){
        ToolClass.out("id111111="+id);
        //删除当前属性的所有属性值，然后删除当前属性(需要事务管理)
        propertyService.deletePropertyAndPropertyValues(new Property(id));
        return new Response("delete property successful");
    }

    //  add property
    @PostMapping("properties/{cid}")
    public Response saveProperty(@PathVariable(value = "cid")int cid, @RequestParam(value = "name") String name){
        try {
            Category category = categoryService.findById(cid);//无查询结果会抛出异常:NoSuchElementException
            Property property = new Property(category,name);
            propertyService.save(property);
            return new Response("Property save successful");
        }catch (NoSuchElementException e){
            e.printStackTrace();
            e.getMessage();
            return new Response("category not find");
        }
    }

    //get property by id
    @GetMapping("properties")
    public Response getPropertyById(@RequestParam(value = "id")int id){
        ToolClass.out("id="+id);
        Property property = propertyService.findById(id);
        return new Response<Property>("get property successful",property);
    }

    //  get property by page
    @GetMapping("properties/{cid}")
    public Response listProperty(@PathVariable(value = "cid")int cid,@RequestParam(value = "start",defaultValue = "0") int start, @RequestParam(value = "size",defaultValue = "3")int size,HttpServletRequest request){
        ToolClass.out("properties GetMapping");
        ToolClass.out("cid="+cid);
        ToolClass.out("start=="+start);
        ToolClass.out("size=="+size);
        //find category
        Category category = categoryService.findById(cid);
        if(category != null){//不为空
            Sort sort = new Sort(Sort.Direction.ASC,"id");
            Pageable pageable = PageRequest.of(start,size,sort);
            Page<Property> page = propertyService.findByCategoryId(cid,pageable);
            NavigatorPage<Property> navigatorPage = new NavigatorPage<Property>(page,3);
            ToolClass.out(navigatorPage.toString());
            Map<String,Object> map = new HashMap<>();
            map.put("category",category);
            map.put("navigatorPage",navigatorPage);
            return new Response<Map>("categoryPageNavigator",map);
        }else {
            return new Response(Response.FAIL,"category not find by id ="+cid);
        }

    }

    //getByPage
    @GetMapping("categories")
    public Response listCategory(@RequestParam(value = "start" ,defaultValue = "0") int start ,@RequestParam(value = "size" ,defaultValue = "3") int size) {
        ToolClass.out("start=="+start);

        Sort sort = new Sort(Sort.Direction.ASC,"id");
        Pageable pageable = PageRequest.of(start,size,sort);//页码基0
        Page<Category> page = categoryService.findAll(pageable);
        NavigatorPage<Category> categoryPage = new NavigatorPage<Category>(page,5);//显示页码数量
        Response<NavigatorPage<Category>> response = new Response<>("categoryPageNavigator",categoryPage);
        //response.getResult().
        return response;
    }

    //get one
    @GetMapping("categories/{id}")
    public Response findById(@PathVariable(value = "id")Category category){//当用类型接收属性参数时，无需手动执行查询，分装过程中自动自从数据库获取(前提需要定义service查询方法)
        //Category category = categoryService.findById(id);
        ToolClass.out("auto:"+category.toString());
        return new Response<Category>(category);
    }

    @DeleteMapping("categories/{id}")
    public Response deleteCategory(@PathVariable(value = "id") int id){
        // 删除category ，image， property ， product ， productImage ，propertyValue
        ToolClass.out("id="+id);
        categoryService.deleteCategoryResource(new Category(id));
        return new Response("category remove successful");
    }

    //  save/update category
    @PostMapping("categories")
    public Response upLoadCategoryImage(Category category , MultipartFile image){
        ToolClass.out("categories");
        ToolClass.out("name="+category.getName());
        ToolClass.out("image="+image);
        //ToolClass.out(category.getId());
        //存储图片
        //因为新增和更改共用当前处理类，新增必须有图片，更改不一定有图片，通过是否存在id 判断两者。
        if(category.getId() > 0){//修改
            //  执行修改
            category = categoryService.save(category);
            if(image != null){//如果图片不为空
                ImageUtil.saveImage("static/image/category",String.valueOf(category.getId()),image);
            }
            return new Response("update category successful");
        }else {//新增
            //是否重复 name
            Category inferCategory = categoryService.findByName(category.getName());
            if(inferCategory == null){//不重复
                //  执行新增
                category = categoryService.save(category);
                if(image != null){//图片不为空
                    ImageUtil.saveImage("static/image/category",String.valueOf(category.getId()),image);
                    return new Response("addition category successful");
                }else {//图片为空，无效，删除已存储的数据库数据，返回错误信息
                    categoryService.delete(category);
                    return new Response(Response.FAIL,"image is null : addition category failed");
                }
            }else {
                return new Response(Response.FAIL,"addition category failed current category name:"+category.getName()+",exist");
            }

        }
    }

    //get user list by page 进行分页查询用户数据
    @GetMapping("users")
    public Response<NavigatorPage<User>> users(@RequestParam(value = "start",defaultValue = "0")int start, @RequestParam(value = "size",defaultValue = "3")int size){
        ToolClass.out(" user list by page");
        Sort sort = new Sort(Sort.Direction.ASC,"id");
        Pageable pageable = PageRequest.of(start,size,sort);//页码基0
        Page<User> page = userService.findAll(pageable);
        NavigatorPage<User> userNavigatorPage = new NavigatorPage<User>(page,5);//显示页码数量
        ToolClass.out(userNavigatorPage.toString());
        Response<NavigatorPage<User>> response = new Response<>("userPageNavigator",userNavigatorPage);
        //response.getResult().
        return response;
    }

    //get order by page 进行分页查询用户数据
    @GetMapping("orders")
    public Response<NavigatorPage<Order>> orders (@RequestParam(value = "start",defaultValue = "0")int start,@RequestParam(value = "size",defaultValue = "3")int size){
        ToolClass.out("orders ");
        //按id 排序 正序
        Sort sort = new Sort(Sort.Direction.ASC,"id");
        // 分页对象
        Pageable pageable = PageRequest.of(start,size,sort);
        Page<Order> page = orderService.findAll(pageable);
        // 自定义分页对象(整理成自己需要的数据字段，方便取值显示)
        NavigatorPage<Order> navigatorPage = new NavigatorPage<Order>(page,5);

//        ToolClass.out(navigatorPage.toString());
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
        ToolClass.out(" --- --- - - - - - - - ");
        ToolClass.out(page.getContent().toString());
        ToolClass.out(orderItems.toString());
        return new Response<NavigatorPage<Order>>(navigatorPage);
    }

    //发货
    @PostMapping("postGoods")
    public Response<String> orders(@RequestBody Order order){
        ToolClass.out(order.toString());
        order.setStatus(OrderStatus.WAIT_CONFIRM);
        order.setPostTime(new Date());
        ToolClass.out(order.toString());
        orderService.save(order);
        Response response = new Response(Response.SUCCESS);
        response.setResult(order);
        return response;
    }

    // 数据测试路径
    //@GetMapping("test")
    public void test(){
        ToolClass.out("test---------------------");
        //ToolClass.out(OrderStatus.WAIT_PAY);
        List<Order> orders = orderService.findAll();
        for (Order o : orders){
            o.setStatus(OrderStatus.WAIT_DELIVERY);
        }
        orders.get(0).setStatus(OrderStatus.WAIT_DELIVERY);
        ToolClass.out(orders.toString());
        ToolClass.out(orders.get(0).getStatus());
        orderService.saveAll(orders);

    }
}
