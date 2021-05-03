package com.taobao.tmalljpa.dao;

import com.taobao.tmalljpa.entity.Order;
import com.taobao.tmalljpa.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderDao extends JpaRepository<Order,Integer> {
    public Order findOrderById(int id);
    public Page<Order> findOrdersByUser(User user, Pageable pageable);
}
