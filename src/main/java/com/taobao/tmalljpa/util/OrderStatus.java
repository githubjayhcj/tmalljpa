package com.taobao.tmalljpa.util;

import org.springframework.stereotype.Component;

@Component
public class OrderStatus {
    //order.status 状态信息
    public static final String WAIT_PAY = "waitPay";
    public static final String WAIT_DELIVERY = "waitDelivery";
    public static final String WAIT_CONFIRM = "waitConfirm";
    public static final String WAIT_COMMENT = "waitComment";
    public static final String FINISH = "finish";
    public static final String DELETE = "delete";

    public static String setStatusDesc(String status){
        String statusDesc = "未知";
        switch (status){
            case "waitPay":
                statusDesc = "等待付款";
                break;
            case "waitDelivery":
                statusDesc = "等待发货";
                break;
            case "waitConfirm":
                statusDesc = "等待收货";
                break;
            case "waitComment":
                statusDesc = "等待评价";
                break;
            case "finish":
                statusDesc = "订单完成";
                break;
            case "delete":
                statusDesc = "订单删除";
                break;
        }
        return statusDesc;
    }
}
