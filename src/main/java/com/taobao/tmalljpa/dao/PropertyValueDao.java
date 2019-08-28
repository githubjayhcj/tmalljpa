package com.taobao.tmalljpa.dao;

import com.taobao.tmalljpa.entity.Product;
import com.taobao.tmalljpa.entity.Property;
import com.taobao.tmalljpa.entity.PropertyValue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PropertyValueDao extends JpaRepository<PropertyValue,Integer> {

    List<PropertyValue> findByProductId(int pid);

    void deleteAllByProperty(Property property);

    void deleteAllByProduct(Product product);
}
