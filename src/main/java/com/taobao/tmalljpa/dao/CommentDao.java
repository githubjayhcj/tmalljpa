package com.taobao.tmalljpa.dao;

import com.taobao.tmalljpa.entity.Comment;
import com.taobao.tmalljpa.entity.Order;
import com.taobao.tmalljpa.entity.Product;
import com.taobao.tmalljpa.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentDao extends JpaRepository<Comment,Integer> {
    public List<Comment> findByProductInAndUser(List<Product> products, User user);

    public List<Comment> findByUserAndOrder(User user, Order order);

    public Comment findByOrderAndProduct(Order order,Product product);

}
