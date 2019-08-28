package com.taobao.tmalljpa.dao;

import com.taobao.tmalljpa.entity.Category;
import com.taobao.tmalljpa.entity.Property;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PropertyDao extends JpaRepository<Property,Integer> {

    Page<Property> findByCategoryId(int cid, Pageable pageable);

    List<Property> findByCategoryId(int cid);

    void deleteAllByCategory(Category category);

}
