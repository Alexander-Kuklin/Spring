package com.epam.spring.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.ColumnTransformer;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@ToString
@Setter
@Getter
@Entity
@Table(name = "\"user\"")
public class User extends AbstractEntity<Integer> {

    private static final long serialVersionUID = -3641867784705451175L;

    private String email;
    @Column(name = "password", nullable = false)
    @ColumnTransformer(
            read = "password = crypt(?, password)",        //encrypt.key
            write = "crypt(?, gen_salt('bf', 8))"                //encrypt.key
    )
    private String password;
    private String name;
    boolean active;


}
