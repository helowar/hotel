package com.mangocity.hotel.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.mangocity.util.log.MyLog;

/**
 * 
 * TODO liting: 该类用类MD5加密。
 * @version   Revision History
 * <pre>
 * Author     Version       Date        Changes
 * liting    1.0           Mar 26, 2012     Created
 *
 * </pre>
 * @since 1.
 */
public class Md5Encrypt {
	 /**
     * Used building output as Hex
     */
    private static final char[] DIGITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a',
            'b', 'c', 'd', 'e', 'f' };

    private static final MyLog log = MyLog.getLogger(Md5Encrypt.class);
    /**
     * 对字符串进行MD5加密
     * 
     * @param text
     *            明文
     * 
     * @return 密文
     */
    public static String md5(String text) {
        MessageDigest msgDigest = null;

        try {
            msgDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            log.error("System doesn't support MD5 algorithm.",e);
        }

        try {
            msgDigest.update(text.getBytes("utf-8"));

        } catch (UnsupportedEncodingException e) {

        	log.error("System doesn't support your  EncodingException.",e);

        }

        byte[] bytes = msgDigest.digest();

        String md5Str = new String(encodeHex(bytes));

        return md5Str;
    }

    private static char[] encodeHex(byte[] data) {

        int l = data.length;

        char[] out = new char[l << 1];

        // two characters form the hex value.
        for (int i = 0, j = 0; i < l; i++) {
            out[j++] = DIGITS[(0xF0 & data[i]) >>> 4];
            out[j++] = DIGITS[0x0F & data[i]];
        }
        return out;
    }
}
