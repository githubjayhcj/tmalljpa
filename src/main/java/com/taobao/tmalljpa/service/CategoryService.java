package com.taobao.tmalljpa.service;

import com.taobao.tmalljpa.dao.*;
import com.taobao.tmalljpa.entity.Category;
import com.taobao.tmalljpa.entity.Product;
import com.taobao.tmalljpa.entity.ProductImage;
import com.taobao.tmalljpa.entity.Property;
import com.taobao.tmalljpa.util.ImageUtil;
import com.taobao.tmalljpa.util.NavigatorPage;
import com.taobao.tmalljpa.util.ToolClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
//@Transactional(propagation = Propagation.REQUIRED,readOnly = false)
@CacheConfig(cacheNames = {"category"})//指定默认缓存区
public class CategoryService{
    @Autowired
    private CategoryDao categoryDao;
    @Autowired
    private PropertyDao propertyDao;
    @Autowired
    private PropertyValueDao propertyValueDao;
    @Autowired
    private ProductDao productDao;
    @Autowired
    private ProductImageDao productImageDao;

    @Cacheable(key = "'all'",unless = "#result != null")//@Cacheable 缓存有数据时，从缓存获取；没有数据时，执行方法，并将返回值保存到缓存中
    public List<Category> findAll(){
        return categoryDao.findAll();
    }

    @Cacheable(key = "'name='+#name" ,unless = "#result != null")//(condition 主语不缓存 , unless : 主语缓存)
    public Category findByName(String name){
        return categoryDao.findByName(name);
    }

    @Cacheable(key = "'start-'+#start+',size-'+#size",unless = "#result != null")// 根据key查询redis库，有缓存，则不执行方法，直接返回缓存中的json字符串/json对象，返回对象需要具备空构造器
    public NavigatorPage<Category> findByPage(int start,int size,int pageNumb){
        Sort sort = new Sort(Sort.Direction.ASC,"id");
        Pageable pageable = PageRequest.of(start,size,sort);//页码基0
        Page<Category> page = categoryDao.findAll(pageable);
        return new NavigatorPage<>(page,pageNumb);
    }

    @Transactional(propagation = Propagation.REQUIRED,readOnly = false)
    //@CacheEvict(allEntries = true,cacheNames = "category")//每次调用时，就会根据指定的key触发删除操作。当值为allEntries = true ：删除整个缓存区的所有值，此时指定的key无效
    public void save(Category category){
        ToolClass.out("category save");
        categoryDao.save(category);
    }

    @Transactional(propagation = Propagation.REQUIRED,readOnly = false)
    //@CacheEvict(allEntries = true,cacheNames = "category")
    public void delete(Category category){
        categoryDao.delete(category);
    }

    @Cacheable(key = "'id='+#id",unless = "#result != null")//(condition 主语不缓存 , unless : 主语缓存)
    public Category findById(int id){
        ToolClass.out("category find by id");
        Optional<Category> optionalCategory = categoryDao.findById(id);
        if (optionalCategory.isPresent()){
            return optionalCategory.get();
        }
        return new Category();
    }

    //删除分类，需要删除分类下关联的所有数据，即资源文件(图片)
    @Transactional(propagation = Propagation.REQUIRED,readOnly = false)
    public void deleteCategoryResource(Category category){
        // 删除category ，image(category-folder)， property ， product ， productImage ，propertyValue
        //find property
        List<Property> properties = propertyDao.findByCategoryId(category.getId());
        String fileRoot = "";
        //删除属性
        if (properties.size() > 0){//存在分类属性
            //foreach  delete propertyValue
            for (Property property : properties){
                propertyValueDao.deleteAllByProperty(property);
            }
            //delete property
            propertyDao.deleteAllByCategory(category);
        }
        //find product
        List<Product> products = productDao.findAllByCategory(category);
        //删除产品
        if(products.size() > 0){//存在产品数据
            //delete productImage
            for(Product product : products){
                //find productImage
                List<ProductImage> productImages = productImageDao.findByProductId(product.getId());
                // foreach delete productImage-folder(productDetail or productSingle,productSingle_middle,productSingle_small)
                for (ProductImage productImage : productImages){
                    if(productImage.getType().equalsIgnoreCase(ProductImage.SINGLE_TYPE)){//single
                        //productSingle
                        fileRoot = "static/image/productSingle";
                        ImageUtil.deleteImage(fileRoot,String.valueOf(productImage.getId()));
                        //productSingle_middle
                        fileRoot = "static/image/productSingle_middle";
                        ImageUtil.deleteImage(fileRoot,String.valueOf(productImage.getId()));
                        //productSingle_small
                        fileRoot = "static/image/productSingle_small";
                        ImageUtil.deleteImage(fileRoot,String.valueOf(productImage.getId()));
                    }else {//detail
                        //productDetail
                        fileRoot = "static/image/productDetail";
                        ImageUtil.deleteImage(fileRoot,String.valueOf(productImage.getId()));
                    }
                    //删除 product 下的 productImage
                    productImageDao.delete(productImage);
                }
            }
            //删除 category下的 product
            productDao.deleteAllByCategory(category);
        }
        //删除category 图片
        fileRoot = "static/image/category";
        ImageUtil.deleteImage(fileRoot,String.valueOf(category.getId()));
        //删除category
        categoryDao.delete(category);
    }


}
