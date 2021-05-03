package com.taobao.tmalljpa.dao;

import com.taobao.tmalljpa.entity.Product;
import com.taobao.tmalljpa.entity.ProductImage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductImageDao extends JpaRepository<ProductImage,Integer> {

    List<ProductImage> findByProductId(int pid);

    Page<ProductImage> findByProductId(int pid, Pageable pageable);

    List<ProductImage> findByProductIdAndType(int pid,String type);

    void deleteAllByProduct(Product product);

    public List<ProductImage> findByProductInAndType(List<Product> products,String type);

}
