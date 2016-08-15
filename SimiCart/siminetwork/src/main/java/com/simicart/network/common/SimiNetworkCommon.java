package com.simicart.network.common;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by frank on 8/12/16.
 */
public class SimiNetworkCommon {

    public static boolean isRefreshCart = false;
    public static boolean isAddParameter = true;

    public static boolean validateString(String content) {
        if (null == content) {
            return false;
        }
        if (content.equals("")) {
            return false;
        }
        if (content.equals("null")) {
            return false;
        }

        return true;
    }

    public static final String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = MessageDigest.getInstance(MD5);
            if (s != null) {
                digest.update(s.getBytes());
            }
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return s;
    }
}
