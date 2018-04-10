package com.kb.spring.security.service.utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.xml.bind.DatatypeConverter;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class TripleDes {

    private static final String KEY = "C868E4F3CCM3YX2ZHKMB69WT";
    private static final String IV = "4XYRBWBF";
    private static final String SECRET_KEY_INSTANCE = "DESede";
    private static final String CIPHER_INSTANCE = "DESede/CBC/PKCS5Padding";

    public static String encode(String dataStr) {
        try {
            Cipher cipher = Cipher.getInstance(CIPHER_INSTANCE);
            IvParameterSpec ips = new IvParameterSpec(IV.getBytes());

            cipher.init(Cipher.ENCRYPT_MODE, getDesKey(), ips);
            byte[] finalByte = cipher.doFinal(dataStr.getBytes());

            return DatatypeConverter.printBase64Binary(finalByte);
        } catch (Exception e) {
            throw new UnsupportedOperationException(e);
        }
    }

    private static Key getDesKey() throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] key = KEY.getBytes();
        DESedeKeySpec spec = new DESedeKeySpec(key);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(SECRET_KEY_INSTANCE);
        return keyFactory.generateSecret(spec);
    }
}