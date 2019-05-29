package com.epam.spring.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.ColumnTransformer;

import javax.persistence.*;
import java.util.List;

@ToString
@Setter
@Getter
@Entity
@Table(name = "\"user\"")
public class User extends AbstractEntity {

    private static final long serialVersionUID = -3641867784705451175L;

    private String email;

    @Column(name = "password", nullable = false)
    @ColumnTransformer(
            read = "password = crypt(?, password)",
            write = "crypt(?, gen_salt('bf', 8))")
    private String password;

    private String name;

    boolean active;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<Order> orders;
}
