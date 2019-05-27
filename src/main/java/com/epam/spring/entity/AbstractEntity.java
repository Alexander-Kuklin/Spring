package com.epam.spring.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Setter
@Getter
@MappedSuperclass
public class AbstractEntity<T> implements Serializable {

    private static final long serialVersionUID = 6909063828490721137L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private T id;
    @Column(name = "last_modify_user", nullable = false)
    private T lastModifyUser;
    @Column(name = "create_date", nullable = false)
    private LocalDateTime createDate;
    @Column(name = "modify_date", nullable = false)
    private LocalDateTime modifyDate;


    public T getId() {
        return id;
    }

    public void setId(T id) {
        this.id = id;
    }

    public AbstractEntity(T id) {
        this.id = id;
    }

    public AbstractEntity() {
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AbstractEntity other = (AbstractEntity) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;

    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return String.format("%s [id=%s]", getClass().getSimpleName(), id);
    }
}
