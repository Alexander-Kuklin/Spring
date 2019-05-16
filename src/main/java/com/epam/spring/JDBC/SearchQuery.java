package com.epam.spring.JDBC;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SearchQuery {
    private StringBuilder sql;
    private List<Object> params;

    public SearchQuery() {
        super();
    }

    public SearchQuery(StringBuilder sql, List<Object> params) {
        super();
        this.sql = sql;
        this.params = params;
    }

}
