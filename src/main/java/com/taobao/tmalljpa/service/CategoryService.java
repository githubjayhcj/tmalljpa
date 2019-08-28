package com.taobao.tmalljpa.service;

import com.taobao.tmalljpa.dao.*;
import com.taobao.tmalljpa.entity.Category;
import com.taobao.tmalljpa.entity.Product;
import com.taobao.tmalljpa.entity.ProductImage;
import com.taobao.tmalljpa.entity.Property;
import com.taobao.tmalljpa.util.ImageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
//@Transactional(propagation = Propagation.REQUIRED,readOnly = false)
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

    public List<Category> findAll(){
        return categoryDao.findAll();
    }

    public Category findByName(String name){
        return categoryDao.findByName(name);
    }

    public Page findAll(Pageable pageable){
        Page<Category> page = categoryDao.findAll(pageable);
        return page;
    }

    @Transactional(propagation = Propagation.REQUIRED,readOnly = false)
    public Category save(Category category){
        return categoryDao.save(category);
    }

    public void delete(Category category){
        categoryDao.delete(category);
    }

    public Category findById(int id){
        return categoryDao.findById(id).get();
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
