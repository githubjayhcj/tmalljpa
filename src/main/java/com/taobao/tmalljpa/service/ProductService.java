package com.taobao.tmalljpa.service;

import com.taobao.tmalljpa.dao.ProductDao;
import com.taobao.tmalljpa.dao.ProductImageDao;
import com.taobao.tmalljpa.dao.PropertyValueDao;
import com.taobao.tmalljpa.entity.Category;
import com.taobao.tmalljpa.entity.Product;
import com.taobao.tmalljpa.entity.ProductImage;
import com.taobao.tmalljpa.entity.Response;
import com.taobao.tmalljpa.es.ProductES_Dao;
import com.taobao.tmalljpa.util.ImageUtil;
import com.taobao.tmalljpa.util.NavigatorPage;
import com.taobao.tmalljpa.util.ToolClass;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service()
@CacheConfig(cacheNames = {"product"})//指定默认缓存区
public class ProductService {

    @Autowired
    private ProductDao productDao;
    @Autowired
    private PropertyValueDao propertyValueDao;
    @Autowired
    private ProductImageDao productImageDao;
    //elasticsearch dao
    @Autowired
    ProductES_Dao productES_dao;


    //关键字搜索（elasticsearch）
//    public NavigatorPage<Product> searchProduct(String keyWord,int start ,int size,int pageNumb){
//        SearchQuery searchQuery =new Sear
//    }

    // 同步更新elasticsearch (查询)数据库
    public void save(Product product){
        //productDao.save(product);
        productES_dao.delete(product);
        productES_dao.save(product);
    }

    @Cacheable(key = "'id='+#id",unless = "#result != null")//(condition 主语不缓存 , unless : 主语缓存)
    public Product findById(int id){
        Optional<Product> optionalProduct = productDao.findById(id);
        if (optionalProduct.isPresent()){
            return optionalProduct.get();
        }
        return new Product();
    }

    @Cacheable(key = "'count='+#result")
    public long count(){
        return productDao.count();
    }

    @Cacheable(key = "'name='+#name",unless="#result != null")
    public Product findByName(String name){
        return productDao.findByName(name);
    }

    @Cacheable(key = "'all'",unless="#result != null")
    public List<Product> finAll(){
        return productDao.findAll();
    }

    @Cacheable(key = "'cid='+#cid+',start='+#start+',size='+#size")
    public NavigatorPage<Product> findByCategoryId(int cid, int start, int size, int pageNumb){
        Sort sort = new Sort(Sort.Direction.ASC,"id");
        Pageable pageable = PageRequest.of(start,size,sort);
        Page<Product> page = productDao.findByCategoryId(cid,pageable);
        NavigatorPage<Product> navigatorPage = new NavigatorPage<>(page,pageNumb);
        return navigatorPage;
    }

    //根据分页获取
//    public Page<Product> findAll(Pageable pageable){
//        return productDao.findAll(pageable);
//    };

    //根据分页获取
    @Cacheable(key = "'start-'+#start+',size-'+#size")
    public NavigatorPage<Product> findAll(int start,int size,int pageNumb){
        Sort sort = new Sort(Sort.Direction.ASC,"id");
        Pageable pageable = PageRequest.of(start,size,sort);//页码基0
        Page<Product> page = productDao.findAll(pageable);
        NavigatorPage<Product> navigatorPage = new NavigatorPage<Product>(page,pageNumb);
        return navigatorPage;
    };

    //删除  product 资源
    @Transactional(propagation = Propagation.REQUIRED,readOnly = false)
    public Response deleteProductResource(int id){//删除product ， productImage ， propertyValue
        //查询当前product
        Optional<Product> optionalProduct = productDao.findById(id);
        Product product = new Product();
        if (optionalProduct.isPresent()){
            optionalProduct.get();
        }
        if(product !=null){
            //删除productValue
            propertyValueDao.deleteAllByProduct(product);
            //获取productImage ，进行文件删除
            List<ProductImage> productImages = productImageDao.findByProductId(product.getId());
            String imageRoot = "";
            for (ProductImage productImage : productImages){//删除所有图片资源
                if(productImage.getType().equalsIgnoreCase("single")){//single
                    //productSingle
                    imageRoot = "static/image/productSingle";
                    ImageUtil.deleteImage(imageRoot,String.valueOf(productImage.getId()));
                    //productSingle_middle
                    imageRoot = "static/image/productSingle_middle";
                    ImageUtil.deleteImage(imageRoot,String.valueOf(productImage.getId()));
                    //productSingle_small
                    imageRoot = "static/image/productSingle_small";
                    ImageUtil.deleteImage(imageRoot,String.valueOf(productImage.getId()));
                }else {//detail
                    //productDetail
                    imageRoot = "static/image/productDetail";
                    ImageUtil.deleteImage(imageRoot,String.valueOf(productImage.getId()));
                }
            }
            //删除productImage
            productImageDao.deleteAllByProduct(product);
            //删除product
            productDao.deleteById(product.getId());
            // 同步更新elasticsearch (查询)数据库
            productES_dao.delete(product);

            return new Response("delete product successful");
        }else {
            return new Response(Response.FAIL,"delete product , not find product by id="+id);
        }
    }

    //为 product 注入 productimage 属性
    public void setProductImage (List<Product> products,List<ProductImage> productImages){
        ToolClass.out("-------setProductImage---------");
        ToolClass.out(products.toString());
        ToolClass.out("-------setProductImage---------");
        ToolClass.out(productImages.toString());
        for (Product product : products){
            for (ProductImage productImage : productImages){
                if (product.getId() == productImage.getProduct().getId()){
                    product.setProductImage(productImage);
                    break;
                }
            }
        }
        //去掉重复数据
        for (ProductImage productImage : productImages){
            productImage.setProduct(null);
        }

    }

    //为 product 注入 productimage 属性
    public void setProductImages (List<Product> products,List<ProductImage> productImages){
        ToolClass.out("-------setProductImage---------");
        ToolClass.out(products.toString());
        ToolClass.out("-------setProductImage---------");
        ToolClass.out(productImages.toString());
        for (Product product : products){
            List<ProductImage> pis = new ArrayList<>();
            for (ProductImage productImage : productImages){
                if (product.getId() == productImage.getProduct().getId()){
                    pis.add(productImage);
                }
            }
            product.setProductImages(pis);
        }
        //去掉重复数据
        for (ProductImage productImage : productImages){
            productImage.setProduct(null);
        }
    }

}
