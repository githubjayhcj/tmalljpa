package com.taobao.tmalljpa.service;

import com.taobao.tmalljpa.dao.OrderItemDao;
import com.taobao.tmalljpa.entity.Order;
import com.taobao.tmalljpa.entity.OrderItem;
import com.taobao.tmalljpa.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderItemService {
    @Autowired
    private OrderItemDao orderItemDao;

    public List<OrderItem> findAll(){
        return orderItemDao.findAll();
    }

    public List<OrderItem> findByOrderIn(List orders){
        return orderItemDao.findByOrderIn(orders);
    }

    public void save(OrderItem orderItem){
        orderItemDao.save(orderItem);
    }

    public void save(List<OrderItem> orderItems){
        orderItemDao.saveAll(orderItems);
    }

    public List<OrderItem> findByOrderIsNull(){
        return orderItemDao.findByOrderIsNull();
    }

    public List<OrderItem> findOrderItemsByUserAndOrderIsNull(User user){
        return orderItemDao.findOrderItemsByUserAndOrderIsNull(user);
    }

    public List<OrderItem> findOrderItemsByIdInAndOrderIsNull(List<Integer> oiids){
        return orderItemDao.findOrderItemsByIdInAndOrderIsNull(oiids);
    }

    public List<OrderItem> findOrderItemsByOrder(Order order){
        return orderItemDao.findOrderItemsByOrder(order);
    }

    public void deleteByOrder(Order order){
        orderItemDao.deleteByOrder(order);
    }


}
