package com.taobao.tmalljpa.service;

import com.taobao.tmalljpa.dao.ProductImageDao;
import com.taobao.tmalljpa.entity.Product;
import com.taobao.tmalljpa.entity.ProductImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductImageService {

    @Autowired
    private ProductImageDao productImageDao;

    public void save(ProductImage productImage){
        productImageDao.save(productImage);
    }

    public Page<ProductImage> findByProductId(int pid, Pageable pageable){
        return productImageDao.findByProductId(pid,pageable);
    }

    public List<ProductImage> findByProductId(int pid){
        return productImageDao.findByProductId(pid);
    }

    public List<ProductImage> findByProductIdAndType(int pid, String type){
        return productImageDao.findByProductIdAndType(pid,type);
    }

    public void deleteById(int id){
        productImageDao.deleteById(id);
    }

    public List<ProductImage> findByProductInAndType(List<Product> products,String type){
        return productImageDao.findByProductInAndType( products, type);
    }


}
