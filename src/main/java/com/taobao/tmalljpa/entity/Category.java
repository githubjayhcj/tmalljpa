package com.taobao.tmalljpa.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;

@Table(name="category_")
@Entity
public class Category implements Serializable {//序列化对象
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //jpa 默认会使用 hibernate, 在 jpa 工作过程中，就会创造代理类来继承 Category ，并添加 handler 和 hibernateLazyInitializer 这两个无须 json 化的属性，所以这里需要用 JsonIgnoreProperties 把这两个属性忽略掉。
    // @JsonIgnoreProperties({ "handler","hibernateLazyInitializer" })
    private int id;

    @Column(name = "name")
    private String name;

    public Category() {
    }

    public Category(int id) {
        this.id = id;
    }

    public Category(String name) {
        this.name = name;
    }

    public Category(int id,String name) {
        this.id = id;
        this.name = name;
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

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
