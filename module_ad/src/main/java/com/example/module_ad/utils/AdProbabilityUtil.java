package com.example.module_ad.utils;

public class AdProbabilityUtil {


    public static double showAdProbability(String time) {
        if (time.length()!=5) {
            return 0.5;
        }

        if (time.startsWith("0")) {
            return 0;
        } else if (time.startsWith("100")) {

            return 1;
        } else {
            String substring = time.substring(3, 5);
            int values = Integer.valueOf(substring);
            double i = (double) values / 100;

            return i;
        }

    }

}
