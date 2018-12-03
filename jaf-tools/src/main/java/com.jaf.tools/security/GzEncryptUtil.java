package com.jaf.tools.security;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.*;
import javax.crypto.spec.DESedeKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.UUID;

/**
 * 基础加密算法类。当前支持des,md5。
 */
public class GzEncryptUtil {

    /**
     * MD5值计算<p>
     * MD5的算法在RFC1321 中定义:
     * 在RFC 1321中，给出了Test suite用来检验你的实现是否正确：
     * MD5 ("") = d41d8cd98f00b204e9800998ecf8427e
     * MD5 ("a") = 0cc175b9c0f1b6a831c399e269772661
     * MD5 ("abc") = 900150983cd24fb0d6963f7d28e17f72
     * MD5 ("message digest") = f96b697d7cb7938d525a2f31aaf161d0
     * MD5 ("abcdefghijklmnopqrstuvwxyz") = c3fcd3d76192e4007dfb496cca67e13b
     */
    public static byte[] MD5(String str) {
        try {
            byte[] res = str.getBytes("UTF-8");
            MessageDigest mdTemp = MessageDigest.getInstance("MD5".toUpperCase());
            mdTemp.update(res);
            return mdTemp.digest();
        } catch (Exception e) {
            return null;
        }
    }


    // 加密后解密
    public static String JM(byte[] inStr) {
        String newStr = new String(inStr);
        char[] a = newStr.toCharArray();
        for (int i = 0; i < a.length; i++) {
            a[i] = (char) (a[i] ^ 't');
        }
        String k = new String(a);
        return k;
    }


    /**
     * BASE64加密
     */
    public static String BASE64Encrypt(byte[] key) {
        String edata = null;
        try {
            edata = (new BASE64Encoder()).encodeBuffer(key).trim();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return edata;
    }


    /**
     * BASE64解密
     */
    public static byte[] BASE64Decrypt(String data) {
        if (data == null) return null;
        byte[] edata = null;
        try {
            edata = (new BASE64Decoder()).decodeBuffer(data);
            return edata;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param key 24位密钥
     * @param str 源字符串
     */
    public static byte[] DES3Encrypt(String key, String str) {
        try {
            byte[] newkey = key.getBytes();
            SecureRandom sr = new SecureRandom();
            DESedeKeySpec dks = new DESedeKeySpec(newkey);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
            SecretKey securekey = keyFactory.generateSecret(dks);
            Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);
            return cipher.doFinal(str.getBytes("utf-8"));
        } catch (NoSuchPaddingException | BadPaddingException | NoSuchAlgorithmException | IllegalBlockSizeException | UnsupportedEncodingException | InvalidKeyException | InvalidKeySpecException e) {
            throw new IllegalArgumentException(e);
        }

    }


    /**
     * 解密
     */
    public static String DES3Decrypt(byte[] edata, String key) {
        String data = "";
        try {
            if (edata != null) {
                byte[] newkey = key.getBytes();
                DESedeKeySpec dks = new DESedeKeySpec(newkey);
                SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
                SecretKey securekey = keyFactory.generateSecret(dks);
                Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
                cipher.init(Cipher.DECRYPT_MODE, securekey, new SecureRandom());
                byte[] bb = cipher.doFinal(edata);
                data = new String(bb, "UTF-8");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public static String build(String Operate, String Body) {
        String SequenceId = UUID.randomUUID().toString();
        String PartnerId = "68";
        String Key = "MjkyZmJjNWUtOTAwNC00Njc2";

        String EBody = BASE64Encrypt(DES3Encrypt(Key, Body));
        String Sign = BASE64Encrypt(MD5(SequenceId + PartnerId + Operate + EBody + Key));

        JSONObject object = new JSONObject();
        object.put("SequenceId", SequenceId);
        object.put("PartnerId", PartnerId);
        object.put("Operate", Operate);
        object.put("Sign", Sign);
        object.put("Body", EBody);
        return object.toJSONString();
    }

    private static String getBody() {
        JSONObject object = new JSONObject();
        return object.toJSONString();
    }

    private static void send() {
        String Operate = "Make";
        String Body = getBody();
        System.out.println(build(Operate, Body));
    }

    private static void decode(String data) {
        String Key = "MjkyZmJjNWUtOTAwNC00Njc2";
        JSONObject object = JSON.parseObject(data);
        String MsgCode = object.getString("MsgCode");
        String Msg = object.getString("Msg");
        String EBody = object.getString("Body");

        String DeSign = BASE64Encrypt(MD5(MsgCode + Msg + EBody + Key));

        String Sign = object.getString("Sign");
        System.out.println(DeSign.equals(Sign));
        String Body = DES3Decrypt(BASE64Decrypt(EBody), Key);
        System.out.println(Body);
    }

    public static void main(String[] args) {
        // send();
        // String data = "{\"Body\":\"kgfRR1JkyKPWBxT\\/TAJ165fTbDrsmeDWlRMRlVIQ9LGgNCKeR8g+VMjz+fHD3eLxc4J+oyFW9MW5VAcMaSdCnsRtviylr4X2Vh4GxlBZE4iptHH80Na3L3vRFXZ5Cd5ArSP\\/3IVR5Z1TIcIoO1kd9dttxTZkK9ps5NCjL6TTfWPYvIyhLtCZ3TsLGl8b+W5HqVtiJ8RudbM8ruDJOpB2jMq0+3fYvuLqqf35kJlLW\\/ZFsdHrkCyaey\\/Wl8OPvEvbkNoSD+CDSWt8BNhqhP6co\\/f8ABYdbEAY2Wdb20\\/SqWCYW07t2IT1aWIZlPHP1FfHZHuIUeDtPh+bJFJB4nFNZODdxoG45NaCYWvWqpbgLTZqx5lf8bJIy5nAMj9BUdCNki5YLKjgmunlX25wAE61Byrn7h9eQVPJCCPHl+mCyMA4TUtN\\/6lbkL1M1cHg2lMyjDZxPNxlTHKz\\/f5zfH5lOX8Jz6BgPkqxaXpHelXradLvzY8Ar0HXh2gvmDwwg0nC1gcU\\/0wCdeuX02w67Jng1pUTEZVSEPSxoDQinkfIPlQuDuSMncUlO9ttxTZkK9psb+b4GFLACs3YvIyhLtCZ3TsLGl8b+W5HqVtiJ8RudbM8ruDJOpB2jMq0+3fYvuLqj+e6eez9LmopYPy3WQIZvgrapYYd\\/0dJkNoSD+CDSWvKtPt32L7i6tp4Pzb4AsdI2Wdb20\\/SqWB7LRMxebZd\\/k4iDC7iPkDOPWJQJ6sVRwlxbUbAAQXbve60vi\\/5ovXLbwbebq9YYbA7Ihd0Ra12MxYlqLwe\\/ssc6uHMCNCV2ujFde5wFkRtm0pu2TaURtGs5V9ucABOtQfHoZJi5CUFgAgjx5fpgsjAOE1LTf+pW5D4ASLmKSVSLN07uepDt7+Ts\\/3+c3x+ZTmoteaINQpRuZ2xg\\/qKAAXcc4J+oyFW9MUavT76fXcr1sRtviylr4X2jbzAIGhA\\/50Y9RTYbaZgD1PRWFHEwFvIrSP\\/3IVR5Z3GgLpYk38ZVfiMMqoyA6TIFw6t9KTeuGSTxNkgPZ4udEnjb5ZHfoR9f2uqDQVyZttgqHMj971HRUKxeiJD0UceOyIXdEWtdjP88LZROroezD1iUCerFUcJEuwpqnCP9RDutL4v+aL1y28G3m6vWGGwOyIXdEWtdjMWJai8Hv7LHOrhzAjQldro632KQFCq9\\/JKbtk2lEbRrOVfbnAATrUHrWbG+lD1\\/Y0II8eX6YLIwDhNS03\\/qVuQ+AEi5iklUizdO7nqQ7e\\/k7P9\\/nN8fmU5U8NDej\\/cz9\\/DU6YjmrJlVp2xg\\/qKAAXcc4J+oyFW9MVqXBMqgmVWTMRtviylr4X2jbzAIGhA\\/50Y9RTYbaZgD1PRWFHEwFvIrSP\\/3IVR5Z2WOYpeuoJpm6enLvrn175chP2OOYYwZxnPIjVWhMSn60njb5ZHfoR9f2uqDQVyZttgqHMj971HRUKxeiJD0UceOyIXdEWtdjPI428sq41kMT1iUCerFUcJq+SOnluzR+bvGjl+z6WloG8G3m6vWGGwOyIXdEWtdjMWJai8Hv7LHOrhzAjQldrozOlsf3ewrH0oJLZjB2QozKQtYIG5rUkvCmGknTuXN6GbJFJB4nFNZODdxoG45NaCHqPmrC2i3C4i0o2it36AZJnAMj9BUdCN\\/D5i88h3PVmRjb0A6HUw63AvZwK4p7tYp6cu+ufXvlyE\\/Y45hjBnGQC83lZm1PJMSeNvlkd+hH1\\/a6oNBXJm22CocyP3vUdFQrF6IkPRRx47Ihd0Ra12MwTMQ6xw+tCXPWJQJ6sVRwkgkQ2fuvav\\/O8aOX7PpaWgbwbebq9YYbA7Ihd0Ra12MxYlqLwe\\/ssc6uHMCNCV2ui5ZLnNyPgsTkpu2TaURtGs5V9ucABOtQe\\/HCUuDZUnlwgjx5fpgsjAOE1LTf+pW5D4ASLmKSVSLN07uepDt7+Ts\\/3+c3x+ZTkHMNdpmshu8J2xg\\/qKAAXcc4J+oyFW9MXITxeUPOWqQMRtviylr4X2jbzAIGhA\\/50Y9RTYbaZgD1PRWFHEwFvIrSP\\/3IVR5Z1b9OJGDhCNC6enLvrn175chP2OOYYwZxnnmclCc0RcUknjb5ZHfoR9f2uqDQVyZttgqHMj971HRUKxeiJD0UceOyIXdEWtdjNTmQ1V5y8KlD1iUCerFUcJGXR3wS8Eyqb\\/IfauRVTdpW8G3m6vWGGwOyIXdEWtdjMWJai8Hv7LHOrhzAjQldroAV9abLMxeBBKbtk2lEbRrOVfbnAATrUHXSfKsEvE6ScII8eX6YLIwDhNS03\\/qVuQ+AEi5iklUizdO7nqQ7e\\/k7P9\\/nN8fmU5\\/ra9LyGrmjWdsYP6igAF3HOCfqMhVvTFTEca40\\/d3uzEbb4spa+F9o28wCBoQP+dGPUU2G2mYA9T0VhRxMBbyK0j\\/9yFUeWdPXdwXGnvJnunpy7659e+XPeG4B4+l2SEzndsFkhBHLhJ42+WR36EfX9rqg0FcmbbYKhzI\\/e9R0VCsXoiQ9FHHjsiF3RFrXYzE6Jba566oZ09YlAnqxVHCVDUze+TxnTj\\/yH2rkVU3aVvBt5ur1hhsDsiF3RFrXYzFiWovB7+yxzq4cwI0JXa6FYGOY7pb4eud9STqm0KToikLWCBua1JL5jxeu5LIf5qmyRSQeJxTWTg3caBuOTWgh6j5qwtotwuItKNord+gGSZwDI\\/QVHQjZ+K9IoiXP4rlfM5XEGH+6Zpekd6Vetp0j2dwEpcRN7baC+YPDCDScIMEmFaASuKmiWY7lk1b8s42Wdb20\\/SqWAShMv9+cuRb1xkgejFQGWTPWJQJ6sVRwnY3lxpUh6kqO8aOX7PpaWgbwbebq9YYbA7Ihd0Ra12M8ScLELU1MporSP\\/3IVR5Z1KnOV17kJej+vbvCk+x3Of223FNmQr2mxlFhptSwzZTNi8jKEu0JndkdDCl3YsgOUwMOQn6WyGP5nAMj9BUdCNAXXY6puRl61Kbtk2lEbRrOVfbnAATrUHrQN4649O7zRKZvFQu+QIzQ==\",\"Msg\":\"成功\",\"MsgCode\":\"1\",\"Sign\":\"90vEiiYb74ubCT2kPqwyGg==\"}";
        long b = System.currentTimeMillis();
        String data = "{\"Body\":\"kgfRR1JkyKOeTtkdVPK047UJqjByOjs9DaPBvDtwgAD\\/RGxwaD0BQ5n7GpjsEkAlmvf2PRrT3WYq3Lky5UJuCdi8jKEu0JndoQ6M1njXFP5rGeLUJTzleTAW+Y0cypLnmvf2PRrT3Wa6oWWiS3Gyn0njb5ZHfoR9E73OP0zflJEMEmFaASuKmiIbmS4\\/m1w1BhfEqT+s+YO6spN+LT\\/WE\\/8h9q5FVN2lbwbebq9YYbAhRwh41XCXLfoLAuB8vs4X9Wp0Jg+Qc\\/kOeUdf55CawZr39j0a091moHan6dyNuU7Ebb4spa+F9rg0OpRHSS\\/rD8c6YvY09cseOcKCTdgP\\/ggLMiwoQbDoNOMiDe2TIxAKatZD3P7tp5skUkHicU1k4N3Ggbjk1oIK6Bdb1\\/DFHE1j10IWCaO8fLbxDKeJio5ezX\\/ncFs0wc8B1\\/AJvnF5aC+YPDCDScKtwsC99edySbUJqjByOjs9u\\/b4C9jF3\\/zJGo6ROqRsn9DwXFBXgjBg7rS+L\\/mi9ctvBt5ur1hhsHy5kT+CvnCVTUKKlUrY73E2dTvZe4e2VypeHt4RTohhEOAeyqMWhnObJFJB4nFNZODdxoG45NaC4koav9uNp4vzj1ISVAlWOdOrWLJg6xWU9ZyssQ5I5zoII8eX6YLIwDhNS03\\/qVuQCXaheTjU5ObtaOB2pzv9VpkmNIcfu8L0p+n\\/VofYgOaw40EBtNEqYknjb5ZHfoR9xkGYXOYCXmF\\/1wi8NGGPl5Ajt7lhvqkBBhfEqT+s+YOfTk8u9xUv0knjb5ZHfoR9xkGYXOYCXmEMEmFaASuKmuQYYD+b3fmeBhfEqT+s+YMoOKPLVRNdpti8jKEu0JndK0Y2BbF1K1RrGeLUJTzlecrnP2KEsTZWYnkjH6OiIl0mZMP+a8PDkpskUkHicU1k4N3Ggbjk1oLiShq\\/242ni5KH6EzZd4s478T\\/gamchX+sNyrOxgmzru8aOX7PpaWgbwbebq9YYbB8uZE\\/gr5wlU1CipVK2O9xuxySRHw80a\\/oSLbH9TY43Kfp\\/1aH2IDmPVjzLn1wMBhJ42+WR36EfcZBmFzmAl5hDBJhWgEriprjT0OKICmmEB+WmA+q6EQ0wSe5Gp0yyENF20ap0XqKvZDaEg\\/gg0lrcSAUqxanbwpNuFwWjIGewcVRoY2+Btqvp+n\\/VofYgOYIQ4YePtpDJJDaEg\\/gg0lrcSAUqxanbwp+ByJTQ4IOFZH7QiAfkLdFp+n\\/VofYgObutL4v+aL1y28G3m6vWGGwfLmRP4K+cJVNQoqVStjvcRoL6126R4QrPEoPdTXeEmv6hOz0cRDKopskUkHicU1k4N3Ggbjk1oLiShq\\/242ni1imy2XHHsBdvX3ra9BqAfH3xN5eVqzRCJskUkHicU1k4N3Ggbjk1oLiShq\\/242nizjiofIMk+SNJ8wIJ1GjSssGF8SpP6z5gycoIw0ysx4PSeNvlkd+hH3GQZhc5gJeYQwSYVoBK4qa9\\/C1NVqUvroGF8SpP6z5g1plW9aupNcgkNoSD+CDSWtxIBSrFqdvCtj46siS2Dy7iF6k\\/rLS7DODbzEcmJNfQhhTekE67KWskvk6gJrGr8ZvBt5ur1hhsHy5kT+CvnCVTUKKlUrY73FAHNH9e+zyBtDwXFBXgjBgkOdzuUBZr2qQ2hIP4INJa3EgFKsWp28KTbhcFoyBnsEP08zriFyzF6fp\\/1aH2IDmVANg73pr6KRJ42+WR36EfcZBmFzmAl5hDBJhWgEripoHLn7yuWLEzE0uWJdUUqAP0PBcUFeCMGB78JXXear6UW8G3m6vWGGwvBGSFFfhbQ0gzLY5r15I9XVRzVu2zispwSe5Gp0yyEOnQ7otsTPoUZDaEg\\/gg0lreWg8roHjkKxnEJnLxPs8qV7iPHrjzqX1p+n\\/VofYgOaYKLuWoUriqknjb5ZHfoR9nVJNsH6vncYckTx6uKCsUdDioX8ioMRifjIvEm8cK17BJ7kanTLIQzythspp011VkNoSD+CDSWt5aDyugeOQrFRYZm3YjbWKEhAjRY4krAmn6f9Wh9iA5qdDui2xM+hRkNoSD+CDSWt5aDyugeOQrFRYZm3YjbWKFgzm\\/Cs\\/aMGDbzEcmJNfQne+bE9nPAQD2FCCAfGPLCRvBt5ur1hhsLwRkhRX4W0N3lzR8tChyvwZqUcyhS3FZAtrnXx\\/pymjcBQI3qVzZ7ybJFJB4nFNZODdxoG45NaC2xMcEJgEBNMtxnjbDXJ7sAw9dI075xRXU\\/QDP+2mp+gAoaAbd46PgAgjx5fpgsjAOE1LTf+pW5C0qQhWWyyyXjf22wIFKaut7NH+BJQLNwY3tqpeFJ0\\/AG75vmwXXcNWaC+YPDCDScLV7eQlX8deLLUJqjByOjs9a94\\/llMShiNT9AM\\/7aan6FX8CC8kJEIRaC+YPDCDScLV7eQlX8deLLUJqjByOjs9GJngkGAL4J5Eq1FExOXVDz2uUfGChcvEmvf2PRrT3WZd7vlPdMALscRtviylr4X2Lk03xhqSo7cPxzpi9jT1y5ItaKE2yNWdN7aqXhSdPwBKnqWI1ja1QmgvmDwwg0nC1e3kJV\\/HXiy1Caowcjo7PbqoQ49+1vYlU\\/QDP+2mp+hHv7aAJQSpmggjx5fpgsjAOE1LTf+pW5C0qQhWWyyyXjf22wIFKautVPpZV\\/QlltHHecFv5lG6jwgjx5fpgsjAOE1LTf+pW5C0qQhWWyyyXjf22wIFKautrhquuK125zc3tqpeFJ0\\/AMANWxWfbdkCaC+YPDCDScLV7eQlX8deLLUJqjByOjs9GJngkGAL4J5czFRDk6ud85r39j0a091mZqcDYTeDFvjEbb4spa+F9i5NN8YakqO3D8c6YvY09ctAmN7ZdV6tnipImgq83Dxyp+n\\/VofYgOZqCZSlCB\\/aT0njb5ZHfoR9FfQfQj9q397V7eQlX8deLIdqUpSAUfZDmjW0usw7k7GbJFJB4nFNZODdxoG45NaC2xMcEJgEBNMtxnjbDXJ7sNqKS92YmBKvU\\/QDP+2mp+hxzv++u7JIVwgjx5fpgsjAOE1LTf+pW5C0qQhWWyyyXnsXXFgQtlpsQmkUQd5hlLsa24NFZRmbdoSfiW\\/GyEYaxG2+LKWvhfYuTTfGGpKjtw\\/HOmL2NPXLEVhWuqgxiAWzzBroIEg7JgYXxKk\\/rPmDKxDu+e47Qq7YvIyhLtCZ3YRSQtSWseT6axni1CU85Xm4gcTPFJanyZr39j0a091mU4UhWb5TsKhJ42+WR36EfSixwjTV9IJKEw4\\/uZJofWTQf4Qj18XEGgYXxKk\\/rPmDxS7zRV0YGsxJ42+WR36EfSixwjTV9IJK1gcU\\/0wCdeulXOAW46RItgYXxKk\\/rPmDLvkw\\/m2A0f6Q2hIP4INJa3shTPPOiZdMAEudERHre9TNpa5wS65hcqfp\\/1aH2IDmnS\\/gMc+3zbSQ2hIP4INJa3shTPPOiZdMG+z7U9dxMeOibJvX1Ogzky1Q8+5y9ywRBhfEqT+s+YML+HzGYChhoNi8jKEu0JndRRPZicj6uS1rGeLUJTzleQNrSmu+JdgTmvf2PRrT3WY1VQQvGjTA4sRtviylr4X2MXQfiBmVhlUPxzpi9jT1ywZ9R2OE9qjiQZQIQTEoOcoGF8SpP6z5g9AP+O5HHVjO2LyMoS7Qmd1FE9mJyPq5LWsZ4tQlPOV5Q9joxh2A\\/Ema9\\/Y9GtPdZnsb70OnI\\/MG2LyMoS7Qmd1FE9mJyPq5LWsZ4tQlPOV5TuSuTJmdlsAk9iT8jWZhpgEACdab0g2iCCPHl+mCyMA4TUtN\\/6lbkC+sWBU6ULRBjWA3E\\/w8i7pZnT1El5v7iqfp\\/1aH2IDmuRd9eEtAoTOQ2hIP4INJa8lM3pkafMOW\\/9JiIZLU9EplWS7ypMFIbQY5+5zhV9wnmyRSQeJxTWTg3caBuOTWgsM9e0YRYjmtLpcGm1y69RyddZcp514AWVP0Az\\/tpqfo5t7yQMKhl9oII8eX6YLIwDhNS03\\/qVuQ5W6u3zjWGHEUMH5m3uDTrYpXSqz22vL5N7aqXhSdPwDpXKRnAGhkAMRtviylr4X2FbqhJ4BnHSkPxzpi9jT1y7TF4PtL92N1N7aqXhSdPwBe6FI6LLfk+mgvmDwwg0nCZI6q63QsweW1Caowcjo7PS1MNOdvBDxLOJZHRvdOv6Ka9\\/Y9GtPdZoWj6PS+aOF1xG2+LKWvhfYVuqEngGcdKQ\\/HOmL2NPXL6ChGiLfig6KGYdpvKHZq6wYXxKk\\/rPmDuN5h49VVcv\\/YvIyhLtCZ3fWpuGc3VYx+axni1CU85Xl2C1iitGI\\/DwZnq0lBfq7mp+n\\/VofYgObzUDJLa3LqVknjb5ZHfoR9EQxlN4rNW78ckTx6uKCsUXKiHA3Ozw\\/eBhfEqT+s+YNqRsFRcKe3Sknjb5ZHfoR9EQxlN4rNW79\\/1wi8NGGPl6oXLLmBHKGiBhfEqT+s+YMJrc\\/XPZu7bknjb5ZHfoR9EQxlN4rNW78MEmFaASuKmktUSS\\/eGthqBhfEqT+s+YN5zlBXM60d1ti8jKEu0Jnd9am4ZzdVjH5rGeLUJTzleZn5pJBCveRCIav59COzqEo3tqpeFJ0\\/ANe8sf1Q3y2taC+YPDCDScJgL7ft1frwTLUJqjByOjs9Li8z0qOBcC5T9AM\\/7aan6D+w2Mwmg6yFCCPHl+mCyMA4TUtN\\/6lbkC+b62\\/aQMjB3ftN0w0iVeqLlETDprZYF5ES6vIsngYGCCPHl+mCyMA4TUtN\\/6lbkC+b62\\/aQMjBZ0V8eOFPx4emTNJeQbUUx2gKJDXrCIv7CCPHl+mCyMA4TUtN\\/6lbkC+b62\\/aQMjBmSqggjMMSVB\\/F3i41i1UiG7j3zCEVuHUmyRSQeJxTWTg3caBuOTWgnItwFBCME4Lq1aS565x6Nske\\/n\\/9eboCqw3Ks7GCbOue\\/CV13mq+lFvBt5ur1hhsB3BOXu1dTwEFavJnSHhB+iDw41DEzVX9tDwXFBXgjBgg\\/MIzLsDoSqQ2hIP4INJa0ttc8ewEDeD0LEKxALckszB7pDdtMmbOqfp\\/1aH2IDmL50SiukFp\\/1J42+WR36EfREMZTeKzVu\\/f9cIvDRhj5dMPR71AHbc8QYXxKk\\/rPmD7xlYoBi538zYvIyhLtCZ3fWpuGc3VYx+axni1CU85XnLxvz2\\/WQrrpr39j0a091mb8d9Q+XeAC7Ebb4spa+F9o0FQENV9oXaD8c6YvY09cv8r1KidjS5TIPfsd3ZxWGNU\\/QDP+2mp+gsd0bKCwwElwgjx5fpgsjAOE1LTf+pW5Avm+tv2kDIwcTG4HFC0NT0i5REw6a2WBdYown5bZ6rZwgjx5fpgsjAOE1LTf+pW5Avm+tv2kDIwW+Ho+75iHY89C37GgjoBQyod8sUBx9fnggjx5fpgsjAOE1LTf+pW5C0+ALV9FLSlVTBSP7kAE2KtjVfrYM834o3tqpeFJ0\\/AEx\\/p7jQPHHFaC+YPDCDScLVf+u14cASwbUJqjByOjs9hiiVIMDlsIBT9AM\\/7aan6BsdFWWFsnPYCCPHl+mCyMA4TUtN\\/6lbkLT4AtX0UtKVMm0uLTpKKeCmTNJeQbUUx6f8fq9schw9CCPHl+mCyMA4TUtN\\/6lbkLT4AtX0UtKVq4sMV9Xq8O8k9iT8jWZhpm\\/zp2W58mk\\/CCPHl+mCyMA4TUtN\\/6lbkLT4AtX0UtKVpMYUdJvF+C+LlETDprZYFxteo+LclOHpCCPHl+mCyMA4TUtN\\/6lbkLT4AtX0UtKVffZUh\\/qftz+fa4DvMWN\\/Fcp2vFAar6c7BhfEqT+s+YOI3bo2e\\/FSgNi8jKEu0Jndk+FU9HG9W95rGeLUJTzleYpkbxTn54wop+n\\/VofYgOZ78JXXear6UW8G3m6vWGGwmMzbVUT5zaEA56OXwVJDYj9rWaHQlIomsBtAfxSldAcQNPEW61G0OG8G3m6vWGGwmMzbVUT5zaGwZRaXK66QaJ6cGCuoCUZ4FuSqTKlmjKTEMuaWo48PCggjx5fpgsjAOE1LTf+pW5C0+ALV9FLSlQgPkask4RMrgcnUhF5w52rCfsfDNfpeSQgjx5fpgsjAOE1LTf+pW5C0+ALV9FLSlb4XIbgYvGEAvVslJDqNGVH9e1yJRwWjHwgjx5fpgsjAOE1LTf+pW5C0+ALV9FLSlX32VIf6n7c\\/08\\/Xep\\/AYU\\/difo9Dnx1vwgjx5fpgsjAOE1LTf+pW5AKiPFKowAh0stXTxSGlDZ6AUlevzNrfgA3tqpeFJ0\\/AENphl1gItWaaC+YPDCDScJJGM63Z3TcDbUJqjByOjs9X9hI59F04ojafWVA86tXz5CAYq+X25nEhmHabyh2ausGF8SpP6z5gxdetwg2xvTj2LyMoS7Qmd1e0GRB09z1qGsZ4tQlPOV5yTbygckK4waa9\\/Y9GtPdZjDgCS5y04b8xG2+LKWvhfYHpngmb3Nf0w\\/HOmL2NPXLwmHUaAdzRlKGC0BOdM4lkwYXxKk\\/rPmD0VvKJMu0hhLYvIyhLtCZ3V7QZEHT3PWoaxni1CU85XlBWGth50Y0Fpr39j0a091mTDWpchC1BNPEbb4spa+F9gemeCZvc1\\/TD8c6YvY09ct6UQTYoo3sh0PC9IIgBxPqEYUivRwv\\/lqZBMdLgbUs3G8G3m6vWGGwuSBl91MXGCpL1qfCzkPbcaXIfoWhg7at0PBcUFeCMGCvZtvI+cUicAgjx5fpgsjAOE1LTf+pW5AKiPFKowAh0m1eQY5iSIzaq5ZR6Ww35eQ3tqpeFJ0\\/AHBx6ohrPnrPaC+YPDCDScJJGM63Z3TcDbUJqjByOjs91dm4oYykR1xT9AM\\/7aan6CkqvKEDE2WdCCPHl+mCyMA4TUtN\\/6lbkAqI8UqjACHSwiVaQRXubPxZciICRZCJzje2ql4UnT8AYz\\/BeitvXEPEbb4spa+F9lwioiRZ2IXoD8c6YvY09cvA8WeuOLfjgje2ql4UnT8Axdv5zEmsQM9oL5g8MINJwlFHlGOl6cR5tQmqMHI6Oz3uxFek1ekTkFP0Az\\/tpqfodnE2p5cGEkJoL5g8MINJwlFHlGOl6cR5tQmqMHI6Oz31nuWxXdes6V5kG\\/Xd8m1smvf2PRrT3WYH2Ax9sds30cRtviylr4X2XCKiJFnYhegPxzpi9jT1y8UbbmwVa9uGEu\\/C9Vv2PGwGF8SpP6z5g6O4xvnvwbWtSeNvlkd+hH05RwbsQDJdrhMOP7mSaH1k8AFWBX9PVC8daUz01q72R9DwXFBXgjBgtc9gYW4JV8CQ2hIP4INJa7ACcI7KMSglQeHrXbOB8\\/9GR5pWLCBLoafp\\/1aH2IDmg\\/MIzLsDoSqQ2hIP4INJa7ACcI7KMSglaVWsPpfxy3LeYOyufbgSyqfp\\/1aH2IDmaotNT3m7SUCQ2hIP4INJa7ACcI7KMSglLs8ZdDysbsFg0HFsc\\/ti96fp\\/1aH2IDmL9aXw4+8S9uQ2hIP4INJa7ACcI7KMSgl0lhrvFrtY9GvWRXi\\/eCzXqfp\\/1aH2IDmZFiT4oViSHqQ2hIP4INJa7ACcI7KMSglEJNP6NbAyZew0zVlECgFlqfp\\/1aH2IDmF+o85MrxfciQ2hIP4INJa7ACcI7KMSglLs8ZdDysbsG8kqzuZcZ\\/TGxercGIUKG2uR5NtIx8DKubJFJB4nFNZODdxoG45NaC9SIJoIuAWiOHEGXPnGyhO9fh9KNKTSSvBhfEqT+s+YNjmyoOvUtW1Unjb5ZHfoR9OUcG7EAyXa4TDj+5kmh9ZH+CIFS73iBpBhfEqT+s+YOPCNM4yDv3Ydi8jKEu0JndTB+8hX55Du5rGeLUJTzlealw1X\\/ObYQEQ1PcszOKjzCn6f9Wh9iA5q3MJAZ0PeKvkNoSD+CDSWuwAnCOyjEoJWlVrD6X8ctyTV+1bK7JwT+yLPzgd22Gs9jleK4b5owlEDTxFutRtDhvBt5ur1hhsN9dTq8dJgRBOixA5gxfc4w+ykztUbwIsBbkqkypZoykIIpRvdFAFNebJFJB4nFNZODdxoG45NaCd+VkQkq7xnAFw8+ws8KrVbKmgCGL9ZGoGAJW314+z\\/ibJFJB4nFNZODdxoG45NaCd+VkQkq7xnCcospwuYxoKVP0Az\\/tpqfoCGFXUNJ\\/65BoL5g8MINJwqqAVuY6orZ7tQmqMHI6Oz05GPErfBgQoKRpAmbK\\/op9mvf2PRrT3WbydgZCyyg6Ldi8jKEu0JndCMvMcQjlGs5rGeLUJTzleeBeSaoZSLD3OdEVx58fzTg6V5\\/AdQo6SQgjx5fpgsjAOE1LTf+pW5CveGtYBdIIMxLsxRCsY3\\/hBhfEqT+s+YP4ImUtzzkip0njb5ZHfoR9c4pi38KvRPLMwrJ72YMTpT0JkOlm6wdDU\\/QDP+2mp+hlN5CqQtI2fWgvmDwwg0nCqoBW5jqitnu1Caowcjo7PSD5qLyoh11877fjdEcHakvQ8FxQV4IwYNW1K2uQyHEHkNoSD+CDSWtvADx0XdWUmtCxCsQC3JLMZi9UzbNwrsqDbzEcmJNfQl7Nf+dwWzTBEToB+WmgNZJoL5g8MINJwjOz\\/LfUL+pOtQmqMHI6Oz2x6ph5ff10tHQnyZGHWbuK0PBcUFeCMGAK2qWGHf9HSZDaEg\\/gg0lrbwA8dF3VlJrQsQrEAtySzOpt7wsJqFrrbCgdy+z2pvZezX\\/ncFs0we38imGhAwDjaC+YPDCDScLcvCJtMxERZLUJqjByOjs96VdbToKB1ftT9AM\\/7aan6PAcpHiQBzbJaC+YPDCDScLcvCJtMxERZLUJqjByOjs9dDD2f7Oi06dT9AM\\/7aan6H\\/WAGrpqz7ICCPHl+mCyMA4TUtN\\/6lbkH2R9b7vh5ozrUQ00Foc51SLlETDprZYF6+CrmLRdrxbmyRSQeJxTWTg3caBuOTWgll\\/gR\\/+FUxCkIJ7VN\\/yVm5KpDyBq8m77djleK4b5owlkvk6gJrGr8ZvBt5ur1hhsMw1N1jFFXJeGWcVZ13su4EGKHQ\\/qvoHP\\/lhUsYwuaMUkvk6gJrGr8ZvBt5ur1hhsMw1N1jFFXJePY1tHkp0E\\/02EUZpNXMuE9DwXFBXgjBgfqhuqC0WFPCQ2hIP4INJa3kdLsn770CHjbzAIGhA\\/50Lf\\/LUhxtuj6fp\\/1aH2IDmHNPl2rRjA5qQ2hIP4INJa3kdLsn770CH0u\\/ZtU\\/LfSKCk2LR3YDA2afp\\/1aH2IDmR+qrC0BCfLOQ2hIP4INJa+hTM2I\\/nmGpz17oLVXD3j\\/vOvPVnoWkY6fp\\/1aH2IDmPppdk274vCmQ2hIP4INJa+hTM2I\\/nmGpkNwR00\\/zADV8Kzn4zE7F6afp\\/1aH2IDmPK2GymnTXVWQ2hIP4INJa+hTM2I\\/nmGp8FQ4PJxLldrpMasyG06Nwqfp\\/1aH2IDmVBs5kkLmJjhJ42+WR36EfTCto8WthjALDBJhWgErippqmVChN88V2QYXxKk\\/rPmDDGJB2eaYFXzYvIyhLtCZ3RQYvbBbpLV5axni1CU85Xk3GRoGPvpiH+oBNzgDQ3h\\/p+n\\/VofYgObKNtRsgkKjfUnjb5ZHfoR9M6CVcpnLJOTVqenUosItd0qkzN7bNqc4p+n\\/VofYgOY2mpUyglgPPZDaEg\\/gg0lrSHCirZfQ5iVWHgbGUFkTiA5E1Jfav+Zkg28xHJiTX0LY5XiuG+aMJXvwldd5qvpRbwbebq9YYbCJGgBCyE0Oe7Sn3Y1GpraxTl6ExaniGhjWRIS+KuBfuH0A2tj8NQf9bwbebq9YYbCJGgBCyE0Oe6ed0Q04Uk8gk3IQnCcD0jV\\/bk+qSRy7Dwylw0dME7\\/GmyRSQeJxTWTg3caBuOTWgoz7nz3Ml2PPu6160+XMQRkdkixhvIzGdQYXxKk\\/rPmDvg+cqF2kg6hJ42+WR36EfTCto8WthjAL1gcU\\/0wCdetC7IYi6YG1WAYXxKk\\/rPmD\\/hY2K1aLvutJ42+WR36EfTCto8WthjALk2VKusSJIl+80ost9lqNNje2ql4UnT8A6vcL02HOQ6poL5g8MINJwpNlSrrEiSJftQmqMHI6Oz0W4DdGD8Cd+VP0Az\\/tpqfo4ohjOR8W1jwII8eX6YLIwDhNS03\\/qVuQhooFuYL2TFw6Tz0+n5XDyL1bJSQ6jRlRmbSVm1DtJF+bJFJB4nFNZODdxoG45NaCjPufPcyXY88Bz1FOmpykpKWj\\/ZUs8RzoBhfEqT+s+YPiyt01wXlY4di8jKEu0JndFBi9sFuktXlrGeLUJTzlefdPGFTj1lBGmvf2PRrT3WZZe5XLa7dwbMRtviylr4X2Ez9rzxZi4BsPxzpi9jT1y9YHRjcXU\\/uDN7aqXhSdPwD+vF53ZTG+LWgvmDwwg0nCk2VKusSJIl+1Caowcjo7PVY3e6w7MX680f1zBC7sQCCa9\\/Y9GtPdZr5411fBUhr6R+qrC0BCfLOQ2hIP4INJa0hwoq2X0OYlVh4GxlBZE4gHgowdpgTyBafp\\/1aH2IDmbsV2BvFJ\\/3tJ42+WR36EfezNOLQq3U3EDBJhWgEripp+QgBo7\\/RSMwYXxKk\\/rPmDurKTfi0\\/1hOS+TqAmsavxm8G3m6vWGGwfVFdEAdN\\/6hhJZ3nmoPzWHbq9LuuKOFg0PBcUFeCMGDa238CQcTMfJDaEg\\/gg0lrFsgdxkceYtmwxehypXLdCcW78LQI3tmtN7aqXhSdPwDKe6jJRr+GSWgvmDwwg0nCED4PIod10yO1Caowcjo7PVo0klAduu0YU\\/QDP+2mp+ifruqAXldokmgvmDwwg0nCED4PIod10yO1Caowcjo7PYK+Oy2SxRdr+6Tj0HhoBI3BJ7kanTLIQ0tPyk1fXzpwkNoSD+CDSWsWyB3GRx5i2S\\/ObX\\/oWz1l1ecwrWUZlQyn6f9Wh9iA5qvCs9HzxxHFkNoSD+CDSWtFWuNQ80rD5FYeBsZQWROIZ7ye\\/EEJFt2n6f9Wh9iA5g\\/KsOHqSiq0kNoSD+CDSWtFWuNQ80rD5O4KJB6ja7ZF78JIMxevTS43tqpeFJ0\\/AH7P1uZJ1zNSaC+YPDCDScJUhDnMv3PkdbUJqjByOjs9xRB\\/rNq50HnoIBci3y9\\/Jpr39j0a091mrte7ZMaOj6LEbb4spa+F9vsyrM29Vd4XD8c6YvY09cu\\/cWwPih\\/avmX0291gaSFDofeZacGPKHObJFJB4nFNZODdxoG45NaCkPkNbOgEwPW2ccsD0LRJIeIuHu\\/l8k7iRQYyA3otbUObJFJB4nFNZODdxoG45NaCkPkNbOgEwPXBCwmJHGECUu3orqocw1Evy0Am6K\\/EhJd9ANrY\\/DUH\\/W8G3m6vWGGwEg5vzNLzyRhiSTXLUIHdB7I62zyJGFI2mvf2PRrT3WYqLJRiBlPULcRtviylr4X2SgqHQ5Grrf8Pxzpi9jT1y3zrF8v5EY\\/SbCgdy+z2pvbaVdC8DWtCrQgjx5fpgsjAOE1LTf+pW5Bxn\\/AVubMw9M5n0Lxyi\\/6OlepXhPST5n+n6f9Wh9iA5k8UZ3aqyl0vkNoSD+CDSWu4beCWhnGGAIajcREX1EXWpLvtHRpx0yE3tqpeFJ0\\/AC+vjxVxfsdVxG2+LKWvhfZKCodDkaut\\/w\\/HOmL2NPXL0FM224t4iBL0ZH7rBlbuY3ofWiW+MNJ2myRSQeJxTWTg3caBuOTWgmdgHSXIeY7tgCHyhl8IYRBPSGK3fRqQBQYXxKk\\/rPmDTTLmf583Q6lJ42+WR36EfXIU7HxhLbzlEw4\\/uZJofWRynVZjqKzNBgYXxKk\\/rPmDnVk9WKN\\/w7ZJ42+WR36EfXIU7HxhLbzlf9cIvDRhj5c57obdBfIvRAYXxKk\\/rPmDwiwlyYILu4hJ42+WR36EfXIU7HxhLbzlf9cIvDRhj5cVub59BwPWDlP0Az\\/tpqfohPZouIUsKpgII8eX6YLIwDhNS03\\/qVuQVdOTh\\/sKDuAO8rBAMp8vGy6WFPgeiCHgN7aqXhSdPwCDUDZG6GY8K2gvmDwwg0nCXrgdLd2YjE61Caowcjo7PX28\\/tak0tcnU\\/QDP+2mp+i3uE0v85WoFmgvmDwwg0nCXrgdLd2YjE61Caowcjo7PR2xjwlBC\\/h2U\\/QDP+2mp+i7CUyz3VAGtQgjx5fpgsjAOE1LTf+pW5BV05OH+woO4EfmgyRDJDHU1qpcFTBtDejssVSkLfmiiAgjx5fpgsjAOE1LTf+pW5BV05OH+woO4E9CiTp25vMmAjORDvFWXK4a24NFZRmbdlQVUH4kCf9vxG2+LKWvhfbWpbWBDMHD9w\\/HOmL2NPXLStA2tkX+HjhaE7fmW9yM1cpIGpnSdZTVmyRSQeJxTWTg3caBuOTWgiCXXWLdh\\/5o4KjSo0ifYkKS8tvizroCD4TYk8GoqloemyRSQeJxTWTg3caBuOTWgiCXXWLdh\\/5oKsr6qSB2d+apqra9FldZcFP0Az\\/tpqfoidLDr4OK92wII8eX6YLIwDhNS03\\/qVuQ56oJbAyN3vMeSjYofb72wDxKD3U13hJrN6EosZPLj9cII8eX6YLIwDhNS03\\/qVuQ56oJbAyN3vPuuvSLCGQFumefsMOmbreIzV2juwT+EFrQ8FxQV4IwYNBt\\/DvgVL1vCCPHl+mCyMA4TUtN\\/6lbkOeqCWwMjd7zBnl5vCnu0MclwHFqE2IctArh4bXia6m3BhfEqT+s+YO3eKCKNIFVJdi8jKEu0JndT6pQ+xNlGE1rGeLUJTzlefg8vyPrdmjqzliZf\\/8V6jWn6f9Wh9iA5qeFZmbVzF+fkNoSD+CDSWuzzK6sovV+XHhHFeC3AwMCWjBb+iBGLOyn6f9Wh9iA5sXpYQ529OiISeNvlkd+hH2y3pZKX3OG39YHFP9MAnXrZCR6lxEsIaAGF8SpP6z5g6eaDsee\\/cpASeNvlkd+hH2y3pZKX3OG39YHFP9MAnXrNtpULy+79PMGF8SpP6z5gyTK\\/7QRwL5xSeNvlkd+hH2y3pZKX3OG39YHFP9MAnXrBimd9Wd8fbQGF8SpP6z5g9b6vfo4qcL+SeNvlkd+hH2y3pZKX3OG3xyRPHq4oKxRutkVXBFwN8uE5yR3Oxcol8EnuRqdMshDD8qw4epKKrSQ2hIP4INJa7PMrqyi9X5c8DXU\\/ksI1gIHlQ7wHn5xqUL3s2ht+2AwBhfEqT+s+YM5GnyMGczRHQ==\",\"Msg\":\"成功\",\"MsgCode\":\"1\",\"Sign\":\"LraY4E8Gzw+V+FkiTkDy6A==\"}";
        decode(data);
        System.out.println(System.currentTimeMillis() -b);
    }


}
