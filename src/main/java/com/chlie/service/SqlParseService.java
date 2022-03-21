package com.chlie.service;

import com.chlie.entity.SelectAndFromPhrase;
import com.chlie.entity.SqlQuery;

/**
 * @author: Charlie Qzy
 * @date: 2022/3/21
 * @description:
 * @version: 1.0.0
 */
public interface SqlParseService {
    SelectAndFromPhrase parseSelectAndFromPhrase(String sql);
}
