package com.taobao.tmalljpa.service;

import com.taobao.tmalljpa.dao.ProductDao;
import com.taobao.tmalljpa.dao.ProductImageDao;
import com.taobao.tmalljpa.dao.PropertyValueDao;
import com.taobao.tmalljpa.entity.Category;
import com.taobao.tmalljpa.entity.Product;
import com.taobao.tmalljpa.entity.ProductImage;
import com.taobao.tmalljpa.entity.Response;
import com.taobao.tmalljpa.util.ImageUtil;
import com.taobao.tmalljpa.util.ToolClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service()
public class ProductService {

    @Autowired
    private ProductDao productDao;
    @Autowired
    private PropertyValueDao propertyValueDao;
    @Autowired
    private ProductImageDao productImageDao;

    public void save(Product product){
        productDao.save(product);
    }

    public Product findById(int id){
        return productDao.findById(id).get();
    }

    public long count(){
        return productDao.count();
    }

    public Product findByName(String name){
        return productDao.findByName(name);
    }

    public List<Product> finAll(){
        return productDao.findAll();
    }

    public Page<Product> findByCategoryId(int cid, Pageable pageable){
        return productDao.findByCategoryId(cid,pageable);
    }

    //根据分页获取
    public Page<Product> findAll(Pageable pageable){
        return productDao.findAll(pageable);
    };

    //删除  product 资源
    @Transactional(propagation = Propagation.REQUIRED,readOnly = false)
    public Response deleteProductResource(int id){//删除product ， productImage ， propertyValue
        //查询当前product
        Product product = productDao.findById(id).get();
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
