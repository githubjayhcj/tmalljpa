package com.taobao.tmalljpa.service;

import com.taobao.tmalljpa.dao.UserDao;
import com.taobao.tmalljpa.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserDao userDao;

    public List<User> findAll(){
        return userDao.findAll();
    }

    public User findByName(String name){
        return userDao.findByName(name);
    }

    public void save(User user){
        userDao.save(user);
    }
}
