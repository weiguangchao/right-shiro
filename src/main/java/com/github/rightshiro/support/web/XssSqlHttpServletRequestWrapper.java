package com.github.rightshiro.support.web;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.rightshiro.util.WebUtils;
import com.github.rightshiro.util.XssUtils;

import static java.nio.charset.StandardCharsets.UTF_8;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;

/**
 * @author weiguangchao
 * @date 2020/11/20
 */
public class XssSqlHttpServletRequestWrapper extends HttpServletRequestWrapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(XssSqlHttpServletRequestWrapper.class);

    private byte[] body;

    public XssSqlHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);

        this.init(request);
    }

    /**
     * {@code @RequestBody} xss、sql注入过滤.
     */
    @Override
    public ServletInputStream getInputStream() throws IOException {
        String content = XssUtils.stripXssSql(new String(this.body, UTF_8));

        if (StrUtil.isEmpty(content)) {
            content = "";
        }

        return WebUtils.createServletInputStream(new ByteArrayInputStream(content.getBytes(UTF_8)));
    }

    /**
     * 数组参数过滤.
     */
    @Override
    public String[] getParameterValues(String parameter) {
        String[] parameterValueArr = super.getParameterValues(parameter);
        if (ArrayUtil.isNotEmpty(parameterValueArr)) {
            return Arrays.stream(parameterValueArr)
                    .map(this::filterParamString)
                    .toArray(String[]::new);
        }
        return parameterValueArr;
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        Map<String, String[]> parameterMap = super.getParameterMap();
        if (MapUtil.isNotEmpty(parameterMap)) {
            return parameterMap.entrySet()
                    .stream()
                    .collect(Collectors.toMap(
                            Entry::getKey,
                            entry -> filterEntryString(entry.getValue()))
                    );
        }
        return parameterMap;
    }

    @Override
    public String getParameter(String parameter) {
        return filterParamString(super.getParameter(parameter));
    }

    @Override
    public String getHeader(String name) {
        return filterParamString(super.getHeader(name));
    }

    @Override
    public Cookie[] getCookies() {
        Cookie[] cookieArr = super.getCookies();
        if (ArrayUtil.isNotEmpty(cookieArr)) {
            Arrays.stream(cookieArr).forEach(cookie -> {
                cookie.setValue(filterParamString(cookie.getValue()));
            });
        }
        return cookieArr;
    }

    /**
     * 过滤字符串数组不安全内容.
     */
    private String[] filterEntryString(String[] arr) {
        return Arrays.stream(arr)
                .map(this::filterParamString)
                .toArray(String[]::new);
    }

    /**
     * 过滤字符串不安全内容.
     */
    private String filterParamString(String value) {
        if (StrUtil.isNotEmpty(value)) {
            return XssUtils.stripXssSql(value);
        }
        return value;
    }

    private void init(HttpServletRequest request) {
        try {
            body = IoUtil.readBytes(request.getInputStream(), true);
        }
        catch (IOException ex) {
            LOGGER.error("读取request InputStream失败::" + ex.getMessage(), ex);
        }
    }
}
