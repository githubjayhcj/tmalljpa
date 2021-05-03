package com.taobao.tmalljpa.service;

import com.taobao.tmalljpa.dao.UserDao;
import com.taobao.tmalljpa.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserDao userDao;

    public List<User> findAll(){
        return userDao.findAll();
    }

    public Page<User> findAll(Pageable pageable){
        Page page = userDao.findAll(pageable);
        return page;
    }

    public User findByName(String name){
        return userDao.findByName(name);
    }

    public void save(User user){
        userDao.save(user);
    }

    public User findById(int id){
       return userDao.findById(id);
    }
}
