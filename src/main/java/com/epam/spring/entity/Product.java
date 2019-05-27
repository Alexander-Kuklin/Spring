package com.epam.spring.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Setter
@Getter
@Entity
@Table(name = "product")
public class Product extends AbstractEntity<Integer> {

    private static final long serialVersionUID = 7124094868883072870L;

    @Column(name = "id_category")
    int idCategory;
    @Column(name = "alias", length = 20)
    String alias;
    @Column(name = "title", length = 30)
    String title;
    @Column(name = "text_short")
    String textShort;
    @Column(name = "text_full")
    String textFull;
    Double price;
    int qty;
    int discount;
    boolean active;

    @Override
    public String toString() {
        return "Product{" +
                "id=" + getId() +
                ", idCategory=" + idCategory +
                ", alias='" + alias + '\'' +
                ", title='" + title + '\'' +
                ", textShort='" + textShort + '\'' +
                ", textFull='" + textFull + '\'' +
                ", price=" + price +
                ", qty=" + qty +
                ", discount=" + discount +
                ", active=" + active +
                ", createDate = " + getCreateDate() +
                ", modifyDate = " + getModifyDate() +
                ", lastModifyUserId = " + getLastModifyUser() +
                "}\n";
    }
}
