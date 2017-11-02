package com.jaf.tools.security;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by hsk on 2016/11/10.
 */
public final class ApMD5Util {

    public static String getMD5(String input) {
        try {
            String hash = (
                    new BigInteger(
                            1,
                            MessageDigest.getInstance("MD5")
                                    .digest(input.getBytes())
                    )
            ).toString(16);
            while (hash.length() < 32) {
                hash = '0' + hash;
            }
            return hash;
        } catch (NoSuchAlgorithmException e) {
            throw new ApSecurityRuntimeException(e);
        }
    }

}
