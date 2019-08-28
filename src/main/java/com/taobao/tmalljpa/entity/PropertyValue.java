package com.taobao.tmalljpa.entity;

import javax.persistence.*;

@Entity
@Table(name = "property_value_")
public class PropertyValue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "pid")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "ptid")
    private Property property;

    @Column(name = "value")
    private String value;

    public PropertyValue() {
    }

    public PropertyValue(int id) {
        this.id = id;
    }

    public PropertyValue(Product product) {
        this.product = product;
    }

    public PropertyValue(int id ,Product product) {
        this.id = id;
        this.product = product;
    }

    public PropertyValue(Property property) {
        this.property = property;
    }

    public PropertyValue(int id,Property property) {
        this.id = id;
        this.property = property;
    }

    public PropertyValue(String value) {
        this.value = value;
    }

    public PropertyValue(Product product, Property property) {
        this.product = product;
        this.property = property;
    }

    public PropertyValue(int id,Product product, Property property) {
        this.id = id;
        this.product = product;
        this.property = property;
    }

    public PropertyValue(Product product, Property property, String value) {
        this.product = product;
        this.property = property;
        this.value = value;
    }

    public PropertyValue(int id,Product product, Property property, String value) {
        this.id = id;
        this.product = product;
        this.property = property;
        this.value = value;
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

    public Property getProperty() {
        return property;
    }

    public void setProperty(Property property) {
        this.property = property;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "PropertyValue{" +
                "id=" + id +
                ", product=" + product +
                ", property=" + property +
                ", value='" + value + '\'' +
                '}';
    }
}
