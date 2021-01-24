package com.example.module_user.utils;

import android.text.TextUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class ApiMapUtil {

    public static Map<String, Object> setMapValues(String service, long currentTimeMillis, int random, String md5, Map<String, String> userInfo) {
        Map<String, Object> map=new HashMap<>();
        map.put(Constants.SERVICE,service);
        map.put(Constants.TIMESTAMP,currentTimeMillis);
        map.put(Constants.NONCE,random);
        map.put(Constants.SIGNATURE,md5);
        map.putAll(userInfo);
        return  map;
    }

    public static Map<String, Object> setStringValues(String service, long currentTimeMillis, int random, String md5, String id) {
        Map<String, Object> map=new HashMap<>();
        map.put(Constants.SERVICE,service);
        map.put(Constants.TIMESTAMP,currentTimeMillis);
        map.put(Constants.NONCE,random);
        map.put(Constants.SIGNATURE,md5);
        map.put("id",id);
        return  map;
    }



    public static Map<String, Object> setOauthValues(String service, long currentTimeMillis, int random, String md5, String token) {
        Map<String, Object> map=new HashMap<>();
        map.put(Constants.SERVICE,service);
        map.put(Constants.TIMESTAMP,currentTimeMillis);
        map.put(Constants.NONCE,random);
        map.put(Constants.SIGNATURE,md5);
        map.put("token",token);
        return  map;
    }


    /**
     * 计算字符串MD5值
     * */
    public static String md5(String string) {
        if (TextUtils.isEmpty(string)) {
            return "";
        }
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(string.getBytes());
            String result = "";
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result += temp;
            }
            return result;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

}
