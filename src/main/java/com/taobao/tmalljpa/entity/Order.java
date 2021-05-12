package com.taobao.tmalljpa.entity;


import com.taobao.tmalljpa.util.OrderStatus;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "order_")
public class Order {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "status")
    private String status;

    @Column(name = "orderCode")
    private String orderCode;

    @ManyToOne
    @JoinColumn(name = "uid")
    private User user;

    @Transient//（短暂的）忽略该行的持久化
    private BigDecimal totalPrice;

    @Transient//（短暂的）忽略该行的持久化
    private int totalCount;

    @Column(name = "createTime")
    private Date createTime;

    @Column(name = "paymentTime")
    private Date paymentTime;

    @Column(name = "postTime")
    private Date postTime;

    @Column(name = "checkOutTime")
    private Date checkOutTime;

    @Column(name = "address")
    private String address;

    @Column(name = "receiver")
    private String receiver;

    @Column(name = "mobile")
    private String mobile;

    @Column(name = "user_message")
    private String userMessage;

    //前端数据遍历需求，带上所属 orderItem
    @Transient
    private List<OrderItem> orderItems;

    //order.status 中文状态转换
    // 满足实体类放回时json 化 的字段生成要求 get set (字符化时没有该字段存放数据会报错)
    //json 化只调用简单get 方法， 复杂get 逻辑 报错
    @Transient
    private String statusDesc;

    // jpa 需要提供显示的 无参构造方法
    public Order(){};

    public Order(User user, OrderItem orderItem) {
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    // 该字段无法 通过json 反序列化( 反序列化时，调用该方法时参数可能为null )
    public void setStatus(String status) {
        this.status = status;
        if (status != null){ //反序列化时，调用该方法时参数可能为null
            this.setStatusDesc(OrderStatus.setStatusDesc(status));
        }
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(Date paymentTime) {
        this.paymentTime = paymentTime;
    }

    public Date getPostTime() {
        return postTime;
    }

    public void setPostTime(Date postTime) {
        this.postTime = postTime;
    }

    public Date getCheckOutTime() {
        return checkOutTime;
    }

    public void setCheckOutTime(Date checkOutTime) {
        this.checkOutTime = checkOutTime;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUserMessage() {
        return userMessage;
    }

    public void setUserMessage(String userMessage) {
        this.userMessage = userMessage;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    //order.status 中文状态转换
    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", status='" + status + '\'' +
                ", orderCode='" + orderCode + '\'' +
                ", user=" + user +
                ", totalPrice=" + totalPrice +
                ", totalCount=" + totalCount +
                ", createTime=" + createTime +
                ", paymentTime=" + paymentTime +
                ", postTime=" + postTime +
                ", checkOutTime=" + checkOutTime +
                ", address='" + address + '\'' +
                ", receiver='" + receiver + '\'' +
                ", mobile='" + mobile + '\'' +
                ", userMessage='" + userMessage + '\'' +
                ", orderItems=" + orderItems +
                ", statusDesc='" + statusDesc + '\'' +
                '}';
    }
}
