package com.taobao.tmalljpa.entity;

import javax.persistence.*;

@Entity
@Table(name = "product_image_")
public class ProductImage {
    public static final String SINGLE_TYPE = "single";
    public static final String DETAIL_TYPE = "detail";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "pid")
    private Product product;

    @Column(name = "type")
    private String type;

    public ProductImage(){
        //默认构造器jpa 需要使用
    }
    public ProductImage(int id) {
        this.id = id;
    }

    public ProductImage(Product product) {
        this.product = product;
    }

    public ProductImage(String type) {
        this.type = type;
    }

    public ProductImage(Product product, String type) {
        this.product = product;
        this.type = type;
    }

    public ProductImage(int id,Product product, String type) {
        this.id = id;
        this.product = product;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "ProductImage{" +
                "id=" + id +
                ", product=" + product +
                ", type='" + type + '\'' +
                '}';
    }
}
