package com.github.rightshiro.util;

import java.io.IOException;
import java.security.Key;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.rightshiro.support.SpringContextHolder;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.CompressionCodec;
import io.jsonwebtoken.CompressionCodecResolver;
import io.jsonwebtoken.CompressionCodecs;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultHeader;
import io.jsonwebtoken.impl.DefaultJwsHeader;
import io.jsonwebtoken.impl.compression.DefaultCompressionCodecResolver;
import io.jsonwebtoken.io.Decoder;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.lang.Assert;
import io.jsonwebtoken.lang.Strings;
import io.jsonwebtoken.security.Keys;

/**
 * jwt工具类
 * @author weiguangchao
 * @date 2020/11/13
 */
public abstract class JwtUtils {

    public static final char SEPARATOR_CHAR = '.';
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final Decoder<String, byte[]> BASE64_URL_DECODER = Decoders.BASE64URL;
    private static final CompressionCodecResolver COMPRESSION_CODEC_RESOLVER = new DefaultCompressionCodecResolver();
    private static final String STR_JWT_SECRET_KEY = SpringContextHolder.getProperty("right.jwt.secret-key");
    private static final SignatureAlgorithm JWT_ALGORITHM = SignatureAlgorithm.HS512;
    private static final Key JWT_SIGN_KEY = Keys.hmacShaKeyFor(Decoders.BASE64.decode(STR_JWT_SECRET_KEY));

    /**
     * 签发jwt
     */
    public static String issueJwt(String id, String subject, String issuer, Long period, String roles,
            String permissions) {

        long currentTimeMillis = System.currentTimeMillis();
        JwtBuilder builder = Jwts.builder();
        if (StrUtil.isNotEmpty(id)) {
            builder.setId(id);
        }
        if (StrUtil.isNotEmpty(subject)) {
            builder.setSubject(subject);
        }
        if (StrUtil.isNotEmpty(issuer)) {
            builder.setIssuer(issuer);
        }
        // 设置签发时间
        builder.setIssuedAt(new Date(currentTimeMillis));
        // 设置到期时间
        if (Objects.nonNull(period)) {
            builder.setExpiration(new Date(currentTimeMillis + period * 1000));
        }
        if (StrUtil.isNotEmpty(roles)) {
            builder.claim("roles", roles);
        }
        if (StrUtil.isNotEmpty(permissions)) {
            builder.claim("perms", permissions);
        }
        // 压缩，可选GZIP
        builder.compressWith(CompressionCodecs.DEFLATE);
        // 加密设置
        builder.signWith(
                JWT_SIGN_KEY,
                JWT_ALGORITHM
        );
        return builder.compact();
    }

    /**
     * 验签jwt
     */
    public static Jws<Claims> verifyJwt(String jwt) {
        return Jwts.parserBuilder()
                .setSigningKey(JWT_SIGN_KEY)
                .build()
                .parseClaimsJws(jwt);
    }

    /**
     * 获取jwt payload(未进行signature校验)
     * @see io.jsonwebtoken.impl.DefaultJwtParser#parse(java.lang.String)
     */
    public static String parseJwtPayload(String jwt) {
        Assert.hasText(jwt, "JWT String argument cannot be null or empty.");

        if ("..".equals(jwt)) {
            String msg = "JWT string '..' is missing a header.";
            throw new MalformedJwtException(msg);
        }

        String base64UrlEncodedHeader = null;
        String base64UrlEncodedPayload = null;
        String base64UrlEncodedDigest = null;

        int delimiterCount = 0;

        StringBuilder sb = new StringBuilder(128);

        for (char c : jwt.toCharArray()) {
            if (c == SEPARATOR_CHAR) {
                CharSequence tokenSeq = Strings.clean(sb);
                String token = tokenSeq != null ? tokenSeq.toString() : null;

                if (delimiterCount == 0) {
                    base64UrlEncodedHeader = token;
                }
                else if (delimiterCount == 1) {
                    base64UrlEncodedPayload = token;
                }

                delimiterCount++;
                sb.setLength(0);
            }
            else {
                sb.append(c);
            }
        }

        if (delimiterCount != 2) {
            String msg = "JWT strings must contain exactly 2 period characters. Found: " + delimiterCount;
            throw new MalformedJwtException(msg);
        }
        if (sb.length() > 0) {
            base64UrlEncodedDigest = sb.toString();
        }

        // =============== Header =================
        Header header = null;
        CompressionCodec compressionCodec = null;

        if (base64UrlEncodedHeader != null) {
            byte[] bytes = BASE64_URL_DECODER.decode(base64UrlEncodedHeader);
            String origValue = new String(bytes, Strings.UTF_8);
            Map<String, Object> m = readValue(origValue);

            if (base64UrlEncodedDigest != null) {
                header = new DefaultJwsHeader(m);
            }
            else {
                header = new DefaultHeader(m);
            }

            compressionCodec = COMPRESSION_CODEC_RESOLVER.resolveCompressionCodec(header);
        }

        // =============== Body =================
        String payload = ""; // https://github.com/jwtk/jjwt/pull/540
        if (base64UrlEncodedPayload != null) {
            byte[] bytes = BASE64_URL_DECODER.decode(base64UrlEncodedPayload);
            if (compressionCodec != null) {
                bytes = compressionCodec.decompress(bytes);
            }
            payload = new String(bytes, Strings.UTF_8);
        }

        return payload;
    }

    public static Map<String, Object> readValue(String value) {
        try {
            byte[] byteArr = value.getBytes(Strings.UTF_8);
            return OBJECT_MAPPER.readValue(byteArr, Map.class);
        }
        catch (IOException e) {
            throw new MalformedJwtException("Unable to read JSON value: " + value, e);
        }
    }

    /**
     * 分割(,)字符串进SET
     * a,b,c -> [a,b,c]
     */
    public static Set<String> split(String str) {
        Set<String> set = new HashSet<>();
        if (StrUtil.isEmpty(str)) {
            return set;
        }
        set.addAll(Arrays.asList(str.split(",")));
        return set;
    }

    /**
     * 通过(,)进行合并
     * [a,b,c] -> "a,b,c"
     */
    public static String merge(String[] arr) {
        if (ArrayUtil.isNotEmpty(arr)) {
            StringBuilder builder = new StringBuilder();
            for (int i = 0, l = arr.length; i < l; i++) {
                if (i == 0) {
                    builder.append(arr[i]);
                }
                else {
                    builder.append(",").append(arr[i]);
                }
            }
            return builder.toString();
        }
        return null;
    }
}
