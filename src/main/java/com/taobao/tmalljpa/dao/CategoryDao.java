package com.taobao.tmalljpa.dao;

import com.taobao.tmalljpa.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryDao extends JpaRepository<Category,Integer>{
    Category findByName(String name);

}
