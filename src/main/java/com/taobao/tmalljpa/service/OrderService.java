package com.taobao.tmalljpa.service;

import com.taobao.tmalljpa.dao.OrderDao;
import com.taobao.tmalljpa.entity.Order;
import com.taobao.tmalljpa.entity.OrderItem;
import com.taobao.tmalljpa.entity.User;
import com.taobao.tmalljpa.util.ToolClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderDao orderDao;

    public List<Order> findAll(){
        return orderDao.findAll();
    }

    public Page<Order> findAll(Pageable pageable){
        return orderDao.findAll(pageable);
    }

    public List<Order> saveAll(List<Order> orders){
        return orderDao.saveAll(orders);
    }

    public void save(Order order){
        orderDao.save(order);
    }

    public void delete(Order order){
        orderDao.delete(order);
    }

    public Order findOrderById(int id){
        return orderDao.findOrderById(id);
    }

    public Page<Order> findOrdersByUser(User user, Pageable pageable){
        return orderDao.findOrdersByUser(user,pageable);
    }

    //为orders 注入 orderItems ,并计算相应参数值
    public void setOrderItems(List<Order> orders,List<OrderItem> orderItems){
        for (Order order : orders){
            List<OrderItem> beOrderItems = new ArrayList();
            BigDecimal orderTotalPrice = new BigDecimal(0);
            int totalCount = 0;
            for (OrderItem orderItem : orderItems){
                if (order.getId() == orderItem.getOrder().getId()){
                    beOrderItems.add(orderItem);
                    BigDecimal allPrice = new BigDecimal(orderItem.getCount()).multiply(orderItem.getProduct().getPromotePrice());
                    //注入 orderitem 的总金额
                    orderItem.setAllPrice(allPrice);
                    totalCount += orderItem.getCount();
                    ToolClass.out("allprice="+allPrice+",orderprice="+orderTotalPrice);
                    orderTotalPrice = orderTotalPrice.add(allPrice);
//                    ToolClass.out(orderTotalPrice);
//                    ToolClass.out(new BigDecimal(0.1).add(new BigDecimal(1)));
                }
            }
            order.setOrderItems(beOrderItems);
            //注入order 的总金额
            order.setTotalPrice(orderTotalPrice);
            //注入order 的商品总数量
            order.setTotalCount(totalCount);
        }
    };

    //  去除对象间相互绑定数据的问题
    public void setOrderNull(List<Order> orders){
        for (Order order : orders){
            for (OrderItem orderItem : order.getOrderItems()){
                orderItem.setOrder(null);
            }
        }
    }

}
