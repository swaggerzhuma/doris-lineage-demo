package com.cnhis.dorislineage.demo.utils;

/**
 * @author liuqiang
 * @date 2025/4/1 08:42
 */
public class StringUtil {

    public static String escapeSingleQuotes(String input) {
        if (input == null) {
            return null;
        }
        return input.replace("'", "\\'");
    }
}
