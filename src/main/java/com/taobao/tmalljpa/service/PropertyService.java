package com.taobao.tmalljpa.service;

import com.taobao.tmalljpa.dao.PropertyDao;
import com.taobao.tmalljpa.dao.PropertyValueDao;
import com.taobao.tmalljpa.entity.Property;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PropertyService {

    @Autowired
    private PropertyDao propertyDao;
    @Autowired
    private PropertyValueDao propertyValueDao;

    public Property save(Property property){
        return propertyDao.save(property);
    }

    public List<Property> findAll(){
        return propertyDao.findAll();
    }

    public Page<Property> findAll(Pageable pageable){
        return propertyDao.findAll(pageable);
    }

    public Page<Property> findByCategoryId(int cid,Pageable pageable){
        return propertyDao.findByCategoryId(cid,pageable);
    }

    public void delete(Property property){
        propertyDao.delete(property);
    }

    public List<Property> findByCategoryId(int cid){
        return propertyDao.findByCategoryId(cid);
    }

    public Property findById(int id){
       return propertyDao.findById(id).get();
    }

    //删除当前属性(property)的所有属性值(propertyValue)，然后删除当前属性(需要事务管理)
    @Transactional(propagation = Propagation.REQUIRED,readOnly = false)
    public boolean deletePropertyAndPropertyValues(Property property){
        boolean infer = false;
        //删除当前属性的所有属性值(propertyValue)
        propertyValueDao.deleteAllByProperty(property);
        //然后删除当前属性(property)
        propertyDao.delete(property);
        infer = true;
        return infer;
    }

}
