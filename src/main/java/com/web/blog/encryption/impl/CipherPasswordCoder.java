package com.web.blog.encryption.impl;

import com.web.blog.encryption.PasswordCoder;
import com.web.blog.exceptions.PasswordCodingException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class CipherPasswordCoder implements PasswordCoder {

    private static final String TRANSFORMATION = "AES";

    private static final String ALGORITHM = "AES/ECB/PKCS5Padding";

    private final String secretKey;

    public CipherPasswordCoder(@Value("${password.coder.secret-key}") String secretKey) {
        this.secretKey = secretKey;
    }

    @Override
    public String encrypt(String rawPassword) {
        try {

            return base64Encode(aesEncryptToBytes(rawPassword));

        } catch (Exception e) {
            throw new PasswordCodingException("Fail to encrypt given raw password: " + e.getMessage(), e);
        }
    }

    @Override
    public String decrypt(String encryptedPassword) {
        try {

            return StringUtils.isEmpty(encryptedPassword) ? null : aesDecryptByBytes(base64Decode(encryptedPassword));

        } catch (Exception e) {
            throw new PasswordCodingException("Fail to decrypt given encoded password: " + e.getMessage(), e);
        }
    }

    private SecretKeySpec getSecretKey() {
        return new SecretKeySpec(secretKey.getBytes(), TRANSFORMATION);
    }

    private String base64Encode(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    private byte[] aesEncryptToBytes(String content) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, getSecretKey());

        return cipher.doFinal(content.getBytes(StandardCharsets.UTF_8));
    }

    private String aesDecryptByBytes(byte[] encryptBytes) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, getSecretKey());
        byte[] decryptBytes = cipher.doFinal(encryptBytes);
        return new String(decryptBytes);
    }

    private byte[] base64Decode(String base64Code) {
        return StringUtils.isEmpty(base64Code) ? null : Base64.getDecoder().decode(base64Code);
    }
}
