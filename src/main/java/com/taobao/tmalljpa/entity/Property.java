package com.taobao.tmalljpa.entity;

import javax.persistence.*;

@Entity
@Table(name = "property_")
public class Property {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "cid")
    private Category category;

    @Column(name = "name")
    private String name;

    @Transient //（短暂的）忽略该行的持久化
    private PropertyValue propertyValue;

    public Property() {
    }

    public Property(int id) {
        this.id = id;
    }

    public Property(Category category) {
        this.category = category;
    }

    public Property(String name) {
        this.name = name;
    }

    public Property(int id, Category category) {
        this.id = id;
        this.category = category;
    }

    public Property(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Property(Category category, String name) {
        this.category = category;
        this.name = name;
    }

    public Property(int id, Category category, String name) {
        this.id = id;
        this.category = category;
        this.name = name;
    }

    public Property(Category category, String name, PropertyValue propertyValue) {
        this.category = category;
        this.name = name;
        this.propertyValue = propertyValue;
    }

    public Property(int id,Category category, String name, PropertyValue propertyValue) {
        this.id = id;
        this.category = category;
        this.name = name;
        this.propertyValue = propertyValue;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PropertyValue getPropertyValue() {
        return propertyValue;
    }

    public void setPropertyValue(PropertyValue propertyValue) {
        this.propertyValue = propertyValue;
    }

    @Override
    public String toString() {
        return "Property{" +
                "id=" + id +
                ", category=" + category +
                ", name='" + name + '\'' +
                ", propertyValue=" + propertyValue +
                '}';
    }
}
