package com.epam.spring.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Setter
@Getter
@EqualsAndHashCode
@MappedSuperclass
public class AbstractEntity<T> implements Serializable {

    private static final long serialVersionUID = 6909063828490721137L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "last_modify_user", nullable = false)
    private int lastModifyUser;

    @Column(name = "create_date", nullable = false)
    private LocalDateTime createDate;

    @Column(name = "modify_date", nullable = false)
    private LocalDateTime modifyDate;


//    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }
//
//    public AbstractEntity(int id) {
//        this.id = id;
//    }
//
//    public AbstractEntity() {
//    }
//
//    @Override
//    public String toString() {
//        return String.format("%s [id=%s]", getClass().getSimpleName(), id);
//    }
}
