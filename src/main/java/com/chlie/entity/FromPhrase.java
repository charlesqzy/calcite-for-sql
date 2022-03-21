package com.chlie.entity;

import lombok.Data;
import org.apache.calcite.sql.SqlJoin;
import org.apache.calcite.sql.SqlKind;
import org.apache.calcite.sql.SqlNode;

/**
 * @author: Charlie Qzy
 * @date: 2022/3/21
 * @description:
 * @version: 1.0.0
 */

@Data
public class FromPhrase {
    private String sqlKind;
    private String joinType;
    private String expression;
    private boolean isSubQuery;  // 是否子查询
    private SelectAndFromPhrase subSelectPhrase;  //子查询语句

    public FromPhrase(SqlNode sqlNode, String joinType) {
        this.sqlKind = sqlNode.getKind().name();
        if (SqlKind.JOIN.equals(sqlNode.getKind())) {
            SqlJoin sqlJoin = (SqlJoin) sqlNode;
            this.expression = sqlJoin.getRight().toString();
        } else {
            this.expression = sqlNode.toString();
        }
        this.joinType = joinType;
        this.isSubQuery = false;
        this.subSelectPhrase = null;
    }
}
