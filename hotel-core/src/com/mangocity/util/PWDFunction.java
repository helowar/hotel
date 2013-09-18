package com.mangocity.util;

import java.util.Random;

/**
 */
public class PWDFunction {
    public PWDFunction() {
    }

    public static String encode(String sMess) {
        String s = "";
        String sTmp = "";
        Random Rd = new Random();
        Rd.setSeed(0);
        int i = sMess.length();
        if ("".equals(sMess.trim())) {// 如果是空
            // 这里加提示信息
            return null;
        } else if (250 < i) {
            // 这里加提示信息：加密明文长度不能超过250个字
            return null;
        }
        while (0 < i) {
            sTmp = Integer.toString((i % 4 + 'a'));
            if (2 < sTmp.length()) {
                sTmp = sTmp.substring(0, 2);
            }
            s += sTmp;
            i -= 3;
        }
        s += (randomEnd() + sMess);
        String s1 = "";
        char cTmp;
        for (int k = 0; k < s.length(); k++) {
            cTmp = (char) (Rd.nextInt(25) + 'a');// parasoft-suppress PB.CLP "业务问题，暂不修改。" 
            // 一个范围在'a'~'z'之间的随机字符
            s1 += Character.toString(s.charAt(k)) + cTmp; // 随机加上一个小写字符
        }
        s1 = fz(s1);
        return s1;
    }

    public static String decrypt(String sMess) { // 解密方法
        int iLen = sMess.length();
        String s = "";
        String[] s1 = { "io", "kjl", "jhi", "m,", "uy", "rw", "qw", "zx", "jk", "ki" };
        if ("".equals(sMess.trim())) {
            // 这里做提示
            return null;
        } else if (250 < sMess.length()) {
            // 这里做提示
            return null;
        } else {
            sMess = fz(sMess);
            for (int i = 0; i < sMess.length() - 1; i += 2) { // step is 2
                s += sMess.substring(i, i + 1);
            }
            int j = 0, k;
            for (k = 0; 10 > k; k++) {
                j = s.indexOf(s1[k]);
                if (0 <= j)
                    break;
            }
            iLen = s1[k].length();
            s = s.substring(j + iLen, s.length());
            return s;
        }

    }

    private static String randomEnd() {
        int i = 0;
        String[] sRandom = { "io", "kjl", "jhi", "m,", "uy", "rw", "qw", "zx", "jk", "ki" };
        Random Rd = new Random();
        i = Rd.nextInt(10);// 生成0-10之间的随机数，包括0，不包括10
        return sRandom[i];
    }

    private static String fzChar(char cMess) {
        int i = 0;
        String s = "";
        if ('a' <= cMess && 'z' >= cMess) {
            i = cMess - 'a';
            i = 25 - i;
            s = Character.toString((char) (i + 'a'));// parasoft-suppress PB.CLP "业务问题，暂不修改。"
        } else if ('A' <= cMess && 'Z' >= cMess) {
            i = cMess - 'A';
            i = 25 - i;
            s = Character.toString((char) (i + 'A'));// parasoft-suppress PB.CLP "业务问题，暂不修改。"
        } else if ('0' <= cMess && '9' >= cMess) {
            i = cMess - '0';
            i = 9 - i;
            s = Character.toString((char) (i + '0'));// parasoft-suppress PB.CLP "业务问题，暂不修改。"
        } else {
            s = Character.toString(cMess);
        }
        return s;
    }

    private static String fz(String sMess) {
        String s = "";
        for (int i = 0; i < sMess.length(); i++) {
            s += fzChar(sMess.charAt(i));
        }
        return s;
    }
}
