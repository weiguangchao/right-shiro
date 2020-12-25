package com.github.rightshiro.util;

import java.util.regex.Pattern;

import org.owasp.esapi.ESAPI;

import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;

/**
 * @author weiguangchao
 * @date 2020/11/20
 */
public abstract class XssUtils {

    private static final String STR_EMPTY = "";
    private static final String STR_SCRIPT_1 = "<script>(.*?)</script>";
    private static final String STR_SCRIPT_2 = "</script>";
    private static final String STR_SCRIPT_3 = "<script(.*?)>";
    private static final String STR_SRC_1 = "src[\r\n]*=[\r\n]*\\\'(.*?)\\\'";
    private static final String STR_SRC_2 = "src[\r\n]*=[\r\n]*\\\"(.*?)\\\"";
    private static final String STR_EVAL = "eval\\((.*?)\\)";
    private static final String STR_EXP = "expression\\((.*?)\\)";
    private static final String STR_JS = "javascript:";
    private static final String STR_VB = "vbscript:";
    private static final String STR_ON = "onload(.*?)=";
    private static final String STR_SQL_INJECTION = "('.+--)|(--)|(%7C)";

    private static final Pattern PATTERN_SCRIPT_1 = Pattern.compile(STR_SCRIPT_1, Pattern.CASE_INSENSITIVE);
    private static final Pattern PATTERN_SCRIPT_2 = Pattern.compile(STR_SCRIPT_2, Pattern.CASE_INSENSITIVE);
    private static final Pattern PATTERN_SCRIPT_3 = Pattern.compile(
            STR_SCRIPT_3,
            Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL
    );
    private static final Pattern PATTERN_SRC_1 = Pattern.compile(
            STR_SRC_1,
            Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL
    );
    private static final Pattern PATTERN_SRC_2 = Pattern.compile(
            STR_SRC_2,
            Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL
    );
    private static final Pattern PATTERN_EVAL = Pattern.compile(
            STR_EVAL,
            Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL
    );
    private static final Pattern PATTERN_EXP = Pattern.compile(
            STR_EXP,
            Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL
    );
    private static final Pattern PATTERN_JS = Pattern.compile(STR_JS, Pattern.CASE_INSENSITIVE);
    private static final Pattern PATTERN_VB = Pattern.compile(STR_VB, Pattern.CASE_INSENSITIVE);
    private static final Pattern PATTERN_ON = Pattern.compile(
            STR_ON,
            Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL
    );

    /**
     * 过滤sql注入与xss
     */
    public static String stripXssSql(String value) {
        return stripXss(stripSql(value));
    }

    /**
     * 过滤sql注入
     */
    public static String stripSql(String value) {
        return ReUtil.replaceAll(value, STR_SQL_INJECTION, STR_EMPTY);
    }

    /**
     * 过滤xss脚本
     */
    public static String stripXss(String value) {
        String result = STR_EMPTY;

        if (StrUtil.isNotEmpty(value)) {
            result = ESAPI.encoder().canonicalize(value);
            // 去除空字符
            result = result.replaceAll(STR_EMPTY, STR_EMPTY);
            // 过滤脚本标签间(如:<script>..</script>)的内容
            result = PATTERN_SCRIPT_1.matcher(result).replaceAll(STR_EMPTY);
            // 过滤单个</script>标签
            result = PATTERN_SCRIPT_2.matcher(result).replaceAll(STR_EMPTY);
            // 过滤单个<script ...> 标签
            result = PATTERN_SCRIPT_3.matcher(result).replaceAll(STR_EMPTY);
            // 过滤由项目路径(src)直接访问项目资源的行为
            result = PATTERN_SRC_1.matcher(result).replaceAll(STR_EMPTY);
            result = PATTERN_SRC_2.matcher(result).replaceAll(STR_EMPTY);
            // 过滤 eval(...) 表达式
            result = PATTERN_EVAL.matcher(result).replaceAll(STR_EMPTY);
            // 过滤expression(...) 表达式
            result = PATTERN_EXP.matcher(result).replaceAll(STR_EMPTY);
            // 过滤javascript表达式攻击
            result = PATTERN_JS.matcher(result).replaceAll(STR_EMPTY);
            // 过滤vbscript表达式攻击
            result = PATTERN_VB.matcher(result).replaceAll(STR_EMPTY);
            // 过滤onload=事件
            result = PATTERN_ON.matcher(result).replaceAll(STR_EMPTY);
        }

        return result;
    }
}
