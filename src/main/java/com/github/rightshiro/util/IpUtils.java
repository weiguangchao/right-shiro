package com.github.rightshiro.util;

import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.text.StrTokenizer;

/**
 * @author weiguangchao
 * @date 2020/11/21
 */
public abstract class IpUtils {

    private static final String X_FORWARDED_FOR = "x-forwarded-for";
    private static final String N255 = "(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)";
    private static final Pattern PATTERN = Pattern.compile("^(?:" + N255 + "\\.){3}" + N255 + "$");

    public static String getIpFromRequest(HttpServletRequest request) {
        String ip;
        boolean found = false;
        if ((ip = request.getHeader(X_FORWARDED_FOR)) != null) {
            StrTokenizer tokenizer = new StrTokenizer(ip, ",");
            while (tokenizer.hasNext()) {
                ip = tokenizer.nextToken().trim();
                if (isIpv4Valid(ip) && !isIpv4Private(ip)) {
                    found = true;
                    break;
                }
            }
        }
        if (!found) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    private static boolean isIpv4Private(String ip) {
        long longIp = ipv4ToLong(ip);
        return (longIp >= ipv4ToLong("10.0.0.0") && longIp <= ipv4ToLong("10.255.255.255"))
                || (longIp >= ipv4ToLong("172.16.0.0") && longIp <= ipv4ToLong("172.31.255.255"))
                || longIp >= ipv4ToLong("192.168.0.0") && longIp <= ipv4ToLong("192.168.255.255");
    }

    private static long ipv4ToLong(String ip) {
        String[] octets = ip.split("\\.");
        return (Long.parseLong(octets[0]) << 24) + (Integer.parseInt(octets[1]) << 16)
                + (Integer.parseInt(octets[2]) << 8) + Integer.parseInt(octets[3]);
    }

    private static boolean isIpv4Valid(String ip) {
        return PATTERN.matcher(ip).matches();
    }
}
