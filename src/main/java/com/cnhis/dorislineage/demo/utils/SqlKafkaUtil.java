package com.cnhis.dorislineage.demo.utils;


import org.springframework.util.StringUtils;

import java.io.LineNumberReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Sql 拆分处理
 */
public class SqlKafkaUtil {

    public static List<String> sqlExtract2List(String text) {
        // 多条SQL拆分转换成一条
        LineNumberReader lineReader = new LineNumberReader(new StringReader(text));
        List<String> sqlList = new ArrayList<>();
        StringBuilder sqlBuilder = new StringBuilder();
        lineReader.lines().forEach(line -> {
            line = line.trim();
            // 注释符开头认为是注释
            if (line.startsWith(StringPool.TWO_MIDDLE_LINE)) {
                sqlBuilder.append(line).append(StringPool.NEWLINE);
                // ;认为是结尾
            } else if (line.endsWith(StringPool.SEMICOLON)) {
                sqlBuilder.append(line).append(StringPool.NEWLINE);
                sqlList.add(sqlBuilder.toString());
                sqlBuilder.delete(0, sqlBuilder.length());
            } else {
                sqlBuilder.append(line).append(StringPool.SPACE);
            }
        });
        // 如果最后一条语句没有;结尾，补充
        if (!StringUtils.isEmpty(sqlBuilder)) {
            String r = new LineNumberReader(new StringReader(sqlBuilder.toString().trim()))
                    .lines()
                    .filter(line -> !line.trim().startsWith(StringPool.TWO_MIDDLE_LINE))
                    .findFirst()
                    .orElse(StringPool.EMPTY);
            if (!StringUtils.isEmpty(r)) {
                // 全为注释 noting to do 否则补充一条SQL
                sqlList.add(sqlBuilder.toString());
            }
        }
        return sqlList;
    }
}
