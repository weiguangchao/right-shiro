package com.github.rightshiro.util;

import static java.nio.charset.StandardCharsets.UTF_8;

import cn.hutool.core.codec.Base64;
import cn.hutool.crypto.symmetric.AES;

/**
 * @author weiguangchao
 * @date 2020/11/23
 */
public abstract class AesUtils {

    public static String encrypt(String content, String key) {
        byte[] keyBytes = key.getBytes(UTF_8);
        AES aes = new AES("CBC", "PKCS5Padding", keyBytes, keyBytes);
        byte[] encryptByteArr = aes
                .encrypt(content.getBytes(UTF_8));
        return Base64.encode(encryptByteArr);
    }

    public static String decrypt(String content, String key) {
        byte[] keyBytes = key.getBytes(UTF_8);
        AES aes = new AES("CBC", "PKCS5Padding", keyBytes, keyBytes);
        return new String(aes.decrypt(Base64.decode(content)), UTF_8);
    }
}
