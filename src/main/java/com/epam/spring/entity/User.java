package com.epam.spring.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
@Getter
public class User extends AbstractEntity<Integer> {

    private static final long serialVersionUID = -3641867784705451175L;

    String email;
    String password;
    String name;
    boolean active;


}
