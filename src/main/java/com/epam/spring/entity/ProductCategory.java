package com.epam.spring.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class ProductCategory extends AbstractEntity<Integer> {

    private static final long serialVersionUID = 6364052264115559151L;

    int idParent;
    String alias;
    String title;
    String text;
    boolean active;

    public ProductCategory() {
    }

    public ProductCategory(int id, int idParent, String alias, String title, String text, boolean active) {
        super(id);
        this.idParent = idParent;
        this.alias = alias;
        this.title = title;
        this.text = text;
        this.active = active;
    }
}
