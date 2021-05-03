package com.taobao.tmalljpa.dao;

import com.taobao.tmalljpa.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDao extends JpaRepository<User,Integer> {
    User findByName(String name);

    User findById(int id);
}
