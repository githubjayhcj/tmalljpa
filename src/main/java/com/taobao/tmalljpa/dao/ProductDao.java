package com.taobao.tmalljpa.dao;

import com.taobao.tmalljpa.entity.Category;
import com.taobao.tmalljpa.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductDao extends JpaRepository<Product,Integer> {

    //根据category id 分页查询 product(list)
    Page<Product> findByCategoryId(int cid, Pageable pageable);

    List<Product> findAllByCategory(Category category);

    void deleteAllByCategory(Category category);

    Product findByName(String name);
}
