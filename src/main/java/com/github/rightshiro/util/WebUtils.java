package com.github.rightshiro.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.fasterxml.jackson.databind.ObjectMapper;

import static java.nio.charset.StandardCharsets.UTF_8;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;

/**
 * @author weiguangchao
 * @date 2020/11/21
 */
public abstract class WebUtils {

    private static final String STR_BODY = "body";
    private static final String STR_GET = "GET";
    private static final String STR_POST = "POST";
    private static final String DEFAULT_PATH_SEPARATOR = "/";
    private static final Logger LOGGER = LoggerFactory.getLogger(WebUtils.class);

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();


    /**
     * GET、POST、PUT、DELETE(大写).
     */
    public static String getRequestMethod(ServletRequest request) {
        HttpServletRequest req = toHttp(request);
        return req.getMethod().toUpperCase();
    }

    /**
     * (去掉末尾/).
     * /aaa -> /aaa;
     * /aaa/ -> /aaa
     */
    public static String getRequestUri(ServletRequest request) {
        String requestUri = toHttp(request).getRequestURI();
        return splitUri(requestUri);
    }

    /**
     * (去掉末尾/).
     * /aaa -> /aaa;
     * /aaa/ -> /aaa
     */
    public static String splitUri(String path) {
        String newPath = path;
        if (StrUtil.isNotEmpty(newPath)) {
            if (StrUtil.endWith(newPath, DEFAULT_PATH_SEPARATOR)) {
                newPath = StrUtil.sub(newPath, 0, newPath.length() - 1);
            }
        }
        return newPath;
    }

    public static HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest();
    }

    public static ServletInputStream createServletInputStream(ByteArrayInputStream bais) {
        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return bais.available() == 0;
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setReadListener(ReadListener listener) {
            }

            @Override
            public int read() throws IOException {
                return bais.read();
            }
        };
    }

    public static HttpServletRequest toHttp(ServletRequest request) {
        return (HttpServletRequest) request;
    }

    /**
     * 将request body以map的形式转化到request attribute[body]中
     */
    @SuppressWarnings("unchecked")
    public static Map<String, String> getRequestBodyMap(ServletRequest request) {
        if (Objects.nonNull(request.getAttribute(STR_BODY))) {
            return (Map<String, String>) request.getAttribute(STR_BODY);
        }
        else {
            try {
                InputStream in = toHttp(request).getInputStream();
                String content = IoUtil.read(in, UTF_8);

                if (StrUtil.isEmpty(content)) {
                    return Collections.emptyMap();
                }

                Map<String, Object> requestMap = OBJECT_MAPPER.readValue(content, Map.class);
                Map<String, String> resultMap = requestMap.entrySet()
                        .stream()
                        .collect(Collectors.toMap(
                                Entry::getKey,
                                entry -> String.valueOf(entry.getValue()))
                        );
                request.setAttribute(STR_BODY, resultMap);
                return resultMap;
            }
            catch (IOException ex) {
                LOGGER.error("转化request body to json失败::" + ex.getMessage(), ex);
                return Collections.emptyMap();
            }
        }
    }

    public static boolean isPostRequest(ServletRequest request) {
        return StrUtil.equals(
                STR_POST,
                getRequestMethod(request)
        );
    }

    public static boolean isGetRequest(ServletRequest request) {
        return StrUtil.equals(
                STR_GET,
                getRequestMethod(request)
        );
    }

    /**
     * 统一json返回.
     */
    public static void responseWriteJson(Object outObj, ServletResponse response) {
        HttpServletResponse resp = (HttpServletResponse) response;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=utf-8");
        PrintWriter writer;
        try {
            writer = resp.getWriter();
            writer.write(OBJECT_MAPPER.writeValueAsString(outObj));
        }
        catch (IOException ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
    }

    public static Map<String, String> getRequestHeaders(ServletRequest request) {
        Map<String, String> headerMap = new HashMap<>();
        HttpServletRequest req = toHttp(request);
        Enumeration<String> enums = req.getHeaderNames();
        while (enums.hasMoreElements()) {
            String name = enums.nextElement();
            String value = req.getHeader(name);
            if (StrUtil.isNotEmpty(value)) {
                headerMap.put(name, value);
            }
        }
        return headerMap;
    }
}
