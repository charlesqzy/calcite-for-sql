package com.chlie.controller;

import com.chlie.entity.SelectAndFromPhrase;
import com.chlie.entity.SqlQuery;
import com.chlie.service.SqlParseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: Charlie Qzy
 * @date: 2022/3/21
 * @description:
 * @version: 1.0.0
 */

@RequestMapping("/sql")
@RestController
public class CalciteController {

    @Autowired
    private SqlParseService sqlParseService;

    @PostMapping("/parse")
    public SelectAndFromPhrase parseSelectAndFrom(@RequestBody SqlQuery sqlQuery) {
        return sqlParseService.parseSelectAndFromPhrase(sqlQuery.getSql());
    }

}
