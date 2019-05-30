package com.epam.spring.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Setter
@Getter
@ToString
@Entity
@Table(name = "product_category")
public class ProductCategory extends AbstractEntity {

    private static final long serialVersionUID = 6364052264115559151L;

    @Column(name = "id_parent")
    int idParent;

    @Column(name = "alias", length = 20)
    String alias;

    @Column(name = "title", length = 30)
    String title;

    String text;

    boolean active;

}
