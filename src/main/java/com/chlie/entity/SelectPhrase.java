package com.chlie.entity;

import lombok.Data;

/**
 * @author: Charlie Qzy
 * @date: 2022/3/21
 * @description:
 * @version: 1.0.0
 */
@Data
public class SelectPhrase {
    private String sqlKind;
    private String expression;
    private boolean isSubQuery;  //是否子查询
    private SelectAndFromPhrase subSelectPhrase;   //子查询

    public SelectPhrase(String sqlKind, String expression) {
        this.sqlKind = sqlKind;
        this.expression = expression;
        this.isSubQuery = false;
        this.subSelectPhrase = null;
    }
}
