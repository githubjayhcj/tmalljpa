package com.taobao.tmalljpa.dao;

import com.taobao.tmalljpa.entity.Order;
import com.taobao.tmalljpa.entity.OrderItem;
import com.taobao.tmalljpa.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemDao extends JpaRepository<OrderItem,Integer> {

    public List<OrderItem> findByOrderIn(List<Order> orders);

    public List<OrderItem> findByOrderIsNull();

    public List<OrderItem> findOrderItemsByUserAndOrderIsNull(User user);

    public List<OrderItem> findOrderItemsByIdInAndOrderIsNull(List<Integer> oiids);

    public List<OrderItem> findOrderItemsByOrder(Order order);

    public void deleteByOrder(Order order);
}
