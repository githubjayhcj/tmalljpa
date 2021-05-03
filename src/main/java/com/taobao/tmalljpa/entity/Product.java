package com.taobao.tmalljpa.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "product_")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "subTitle")
    private String subTitle;

    // 最贵价格为10 亿级，数据库限定
    @Column(name = "originalPrice")
    private BigDecimal originalPrice;

    @Column(name = "promotePrice")
    private BigDecimal promotePrice;

    @Column(name = "stock")
    private int stock;

    @ManyToOne
    @JoinColumn(name = "cid")
    private Category category;

    @Column(name = "createDate")
    private Date createDate;

    //前端数据需求产品封面
    @Transient
    private ProductImage productImage;

    //前端数据需求产品封面
    @Transient
    private List<ProductImage> productImages;

    public Product() {
    }

    public Product(int id) {
        this.id = id;
    }

    public Product(String name, String subTitle, BigDecimal originalPrice, BigDecimal promotePrice, int stock, Category category, Date createDate) {
        this.name = name;
        this.subTitle = subTitle;
        this.originalPrice = originalPrice;
        this.promotePrice = promotePrice;
        this.stock = stock;
        this.category = category;
        this.createDate = createDate;
    }

    public Product(int id, String name, String subTitle, BigDecimal originalPrice, BigDecimal promotePrice, int stock, Category category, Date createDate) {
        this.id = id;
        this.name = name;
        this.subTitle = subTitle;
        this.originalPrice = originalPrice;
        this.promotePrice = promotePrice;
        this.stock = stock;
        this.category = category;
        this.createDate = createDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public BigDecimal getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(BigDecimal originalPrice) {
        this.originalPrice = originalPrice;
    }

    public BigDecimal getPromotePrice() {
        return promotePrice;
    }

    public void setPromotePrice(BigDecimal promotePrice) {
        this.promotePrice = promotePrice;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public ProductImage getProductImage() {
        return productImage;
    }

    public void setProductImage(ProductImage productImage) {
        this.productImage = productImage;
    }

    public List<ProductImage> getProductImages() {
        return productImages;
    }

    public void setProductImages(List<ProductImage> productImages) {
        this.productImages = productImages;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", subTitle='" + subTitle + '\'' +
                ", originalPrice=" + originalPrice +
                ", promotePrice=" + promotePrice +
                ", stock=" + stock +
                ", category=" + category +
                ", createDate=" + createDate +
                ", productImage=" + productImage +
                ", productImages=" + productImages +
                '}';
    }
}
