package com.taobao.tmalljpa.service;

import com.taobao.tmalljpa.dao.PropertyValueDao;
import com.taobao.tmalljpa.entity.PropertyValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PropertyValueService {
    @Autowired
    private PropertyValueDao propertyValueDao;

    public List<PropertyValue> findByPid(int pid){
        return propertyValueDao.findByProductId(pid);
    }

    public void save(PropertyValue propertyValue){
        propertyValueDao.save(propertyValue);
    }

    public void delete(PropertyValue propertyValue){
        propertyValueDao.delete(propertyValue);
    }

}
