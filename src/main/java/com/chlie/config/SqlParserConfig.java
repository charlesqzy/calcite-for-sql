package com.chlie.config;

import org.apache.calcite.avatica.util.Casing;
import org.apache.calcite.avatica.util.Quoting;
import org.apache.calcite.sql.parser.SqlParser;
import org.apache.calcite.sql.parser.SqlParserImplFactory;
import org.apache.calcite.sql.parser.impl.SqlParserImpl;
import org.apache.calcite.sql.validate.SqlConformance;
import org.apache.calcite.sql.validate.SqlConformanceEnum;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: Charlie Qzy
 * @date: 2022/3/21
 * @description:
 * @version: 1.0.0
 */

@Configuration
public class SqlParserConfig {
    @Bean
    public SqlParser.Config getSqlParserConfig() {
        return SqlParser.config()
                .withParserFactory(SqlParserImpl.FACTORY)
                .withCaseSensitive(false)
                .withQuoting(Quoting.BACK_TICK)
                .withQuotedCasing(Casing.TO_UPPER)
                .withUnquotedCasing(Casing.TO_UPPER)
                .withConformance(SqlConformanceEnum.MYSQL_5);
    }
}
