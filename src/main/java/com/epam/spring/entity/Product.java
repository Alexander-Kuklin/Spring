package com.epam.spring.entity;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Product extends AbstractEntity<Integer> {

    private static final long serialVersionUID = 7124094868883072870L;

    int idCategory;
    String alias;
    String title;
    String textShort;
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
