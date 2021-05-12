package com.taobao.tmalljpa.service;

import antlr.collections.impl.LList;
import com.taobao.tmalljpa.dao.CommentDao;
import com.taobao.tmalljpa.entity.Comment;
import com.taobao.tmalljpa.entity.Order;
import com.taobao.tmalljpa.entity.Product;
import com.taobao.tmalljpa.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentDao commentDao;

    public List<Comment> findAll (){
        return commentDao.findAll();
    }

    public void saveAll(List<Comment> comments){
        commentDao.saveAll(comments);
    }

    public Comment save(Comment comment){
        return commentDao.save(comment);
    }

    public void delete(Comment comment){
        commentDao.delete(comment);
    }

    public List<Comment> findByProductInAndUser(List<Product> products, User user){
        return commentDao.findByProductInAndUser(products,user);
    }

    public List<Comment> findByUserAndOrder(User user, Order order){
        return commentDao.findByUserAndOrder(user,order);
    }

    public Comment findByOrderAndProduct(Order order,Product product){
        return commentDao.findByOrderAndProduct(order,product);
    }
}
