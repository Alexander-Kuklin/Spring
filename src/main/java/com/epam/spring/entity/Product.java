package com.epam.spring.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Setter
@Getter
@Entity
@Table(name = "product")
public class Product extends AbstractEntity {

    private static final long serialVersionUID = 7124094868883072870L;

    @OneToOne
    private ProductCategory category;

    @Column(name = "alias", length = 20)
    private String alias;

    @Column(name = "title", length = 30)
    private String title;

    @Column(name = "text_short")
    private String textShort;

    @Column(name = "text_full")
    private String textFull;

    private Double price;

    private int qty;

    private int discount;

    private boolean active;
}
