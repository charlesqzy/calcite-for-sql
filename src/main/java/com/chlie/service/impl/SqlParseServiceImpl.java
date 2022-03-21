package com.chlie.service.impl;

import com.chlie.entity.FromPhrase;
import com.chlie.entity.SelectAndFromPhrase;
import com.chlie.entity.SelectPhrase;
import com.chlie.service.SqlParseService;
import org.apache.calcite.sql.*;
import org.apache.calcite.sql.parser.SqlParseException;
import org.apache.calcite.sql.parser.SqlParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author: Charlie Qzy
 * @date: 2022/3/21
 * @description:
 * @version: 1.0.0
 */

@Service
public class SqlParseServiceImpl implements SqlParseService {

    @Autowired
    private SqlParser.Config sqlParserConfig;

    private static final Logger LOGGER = LoggerFactory.getLogger(SqlParseServiceImpl.class);

    @Override
    public SelectAndFromPhrase parseSelectAndFromPhrase(String sql) {
        LOGGER.info(sql);
        SelectAndFromPhrase selectAndFromPhrase = new SelectAndFromPhrase();
        SqlParser sqlParser = SqlParser.create(sql, sqlParserConfig);

        SqlSelect sqlSelect = null;
        try {
            SqlNode sqlNode = sqlParser.parseStmt();
            if (SqlKind.SELECT.equals(sqlNode.getKind())) {
                sqlSelect = (SqlSelect) sqlNode;
            }
        } catch (SqlParseException e) {
            e.printStackTrace();
        }

        if (sqlSelect != null) {
            List<SelectPhrase> selectPhrases = handleSelectPart(sqlSelect.getSelectList());
            List<FromPhrase> fromPhrases = handleFromPart(sqlSelect.getFrom());

            selectAndFromPhrase.setSelectPhrases(selectPhrases);
            selectAndFromPhrase.setFromPhrases(fromPhrases);
        }

        return selectAndFromPhrase;
    }


    /**
     * 处理 from 部分，包含子查询嵌套
     *
     * @param from
     * @return
     */
    private List<FromPhrase> handleFromPart(SqlNode from) {
        List<FromPhrase> fromPhrases = new ArrayList<>();
        if (from == null) {
            LOGGER.info("handleFromPart : from is null.");
            return fromPhrases;
        }
        if (SqlKind.JOIN.equals(from.getKind())) {
            List<FromPhrase> joinPart = handleJoinPart(from);
            fromPhrases.addAll(joinPart);
        } else {
            FromPhrase fromPhrase = new FromPhrase(from, null);
            // 处理from中的子查询
            SelectAndFromPhrase subQuery = handleSubQuery(from);
            if (subQuery != null) {
                fromPhrase.setSubQuery(true);
                fromPhrase.setSubQueryPhrase(subQuery);
            }
            fromPhrases.add(fromPhrase);
        }
        return fromPhrases;
    }

    /**
     * 处理Join部分，包含子查询
     *
     * @param from
     * @return List<FromPhrase>
     */
    private List<FromPhrase> handleJoinPart(SqlNode from) {
        List<FromPhrase> fromPhrases = new ArrayList<>();
        SqlJoin sqlJoin = (SqlJoin) from;
        SqlCall left = (SqlCall) sqlJoin.getLeft();
        SqlCall right = (SqlCall) sqlJoin.getRight();
        // 从右至左解析
        List<FromPhrase> rightFromPhrases = parseFromPhraseInJoin(right, sqlJoin.getJoinType().name());
        List<FromPhrase> leftFromPhrases = parseFromPhraseInJoin(left, null);

        fromPhrases.addAll(leftFromPhrases);
        fromPhrases.addAll(rightFromPhrases);
        return fromPhrases;
    }

    /**
     * 处理Join中包含的FromPhrase
     *
     * @param joinPart
     * @param typeOfJoin
     * @return List<FromPhrase>
     */
    private List<FromPhrase> parseFromPhraseInJoin(SqlCall joinPart, String typeOfJoin) {
        List<FromPhrase> fromPhrases = new ArrayList<>();
        if (SqlKind.JOIN.equals(joinPart.getKind())) {
            List<FromPhrase> recurrenceJoin = handleJoinPart(joinPart); // 递归
            fromPhrases.addAll(recurrenceJoin);
        } else if (SqlKind.AS.equals(joinPart.getKind())) {
            FromPhrase fromPhrase = new FromPhrase(joinPart, typeOfJoin);
            // select 子查询
            SelectAndFromPhrase subQuery = handleSubQuery(joinPart);
            if (subQuery != null) {
                fromPhrase.setSubQuery(true);
                fromPhrase.setSubQueryPhrase(subQuery);
            }
            fromPhrases.add(fromPhrase);
            Collections.reverse(fromPhrases);
        }
        return fromPhrases;
    }

    /**
     * 处理 select 部分，包含子查询嵌套
     *
     * @param selectList
     * @return List<SelectPhrase>
     */
    private List<SelectPhrase> handleSelectPart(SqlNodeList selectList) {
        List<SelectPhrase> selectPhrases = new ArrayList<>();

        if (selectList == null) {
            LOGGER.info("handleSelectPart : selectList is null.");
            return selectPhrases;
        }

        selectList.getList().forEach(x -> {
            SelectPhrase selectPhrase = new SelectPhrase(x.getKind().toString(), x.toString());
            // 处理子查询
            SelectAndFromPhrase subSelectAndFromPhrase = handleSubQuery(x);
            if (subSelectAndFromPhrase != null) {
                selectPhrase.setSubQuery(true);
                selectPhrase.setSubQueryPhrase(subSelectAndFromPhrase);
            }
            selectPhrases.add(selectPhrase);
        });
        return selectPhrases;

    }

    /**
     * 处理select、from、join中的子查询
     *
     * @param sqlNode
     * @return
     */
    private SelectAndFromPhrase handleSubQuery(SqlNode sqlNode) {
        if (SqlKind.AS.equals(sqlNode.getKind())) {
            SqlNode[] subSelectNodes = ((SqlBasicCall) sqlNode).getOperands();
            if (subSelectNodes == null || subSelectNodes.length == 0) {
                return null;
            }
            if (SqlKind.SELECT.equals(subSelectNodes[0].getKind())) {
                // 递归处理
                SelectAndFromPhrase selectAndFromPhrase = parseSelectAndFromPhrase(subSelectNodes[0].toString());
                return selectAndFromPhrase;
            }
        }
        return null;
    }
}
