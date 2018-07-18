package com.common.http.util;

import android.util.Base64;

import com.library.log.LogUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

/**
 * @author jzy
 * created at 2018/7/10
 */
public class RSAUtils {

    private static final String RSA_PUBLICE = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCggWurlNAeA/SbQW1qQpnW7E+u/iX483FmrExwpzA7x9PHCx4zFK31dOWJ43H6jdNB7+fcapAdwz3kNYdoiuHnF+3Ci0HKYMhBE6PnppdTxb8LTGejxzHGCMVjWKzO1Jcd9rG/YeEwdzUb0/km9FNjgtAu1TSxSDErRh4ZO778UwIDAQAB";
    private static final String ALGORITHM = "RSA";

    /**
     * 得到公钥
     *
     * @param algorithm
     * @param bysKey
     * @return
     */
    private static PublicKey getPublicKeyFromX509(String algorithm,
                                                  String bysKey) throws NoSuchAlgorithmException, Exception {
        byte[] decodedKey = Base64.decode(bysKey, Base64.NO_WRAP);
        X509EncodedKeySpec x509 = new X509EncodedKeySpec(decodedKey);

        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        return keyFactory.generatePublic(x509);
    }

    /**
     * 使用公钥加密
     *
     * @param content
     * @return
     */
    public static String encryptByPublic(String content) {
        try {
            PublicKey pubkey = getPublicKeyFromX509(ALGORITHM, RSA_PUBLICE);

            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, pubkey);

            byte plaintext[] = content.getBytes("UTF-8");
            byte[] output = cipher.doFinal(plaintext);
            String s = new String(Base64.encode(output, Base64.NO_WRAP));

            return s;

        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 使用公钥解密
     *
     * @param content 密文
     * @return 解密后的字符串
     */
    public static String decryptByPublic(String content) {
        try {
            PublicKey pubkey = getPublicKeyFromX509(ALGORITHM, RSA_PUBLICE);
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, pubkey);
            InputStream ins = new ByteArrayInputStream(Base64.decode(content, Base64.NO_WRAP));
            ByteArrayOutputStream writer = new ByteArrayOutputStream();
            byte[] buf = new byte[128];
            int bufl;
            while ((bufl = ins.read(buf)) != -1) {
                byte[] block = null;
                if (buf.length == bufl) {
                    block = buf;
                } else {
                    block = new byte[bufl];
                    for (int i = 0; i < bufl; i++) {
                        block[i] = buf[i];
                    }
                }
                writer.write(cipher.doFinal(block));
            }
            return new String(writer.toByteArray(), "utf-8");
        } catch (Exception e) {
            return null;
        }
    }

}
