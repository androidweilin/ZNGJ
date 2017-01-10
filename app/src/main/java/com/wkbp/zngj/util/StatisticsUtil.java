package com.wkbp.zngj.util;

/**
 * Created by weilin on 2016/11/11 13:11
 */
public class StatisticsUtil {

    /**
     * 求单个数组中最大值
     */
    public static int getAryMax(int[] ary) {
        int n = 0;
        for (int i = 0; i < ary.length; i++) {
            if (ary[i] > n) {
                n = ary[i];
            }
        }
        return n;
    }

    /**
     * 求四个数组中最大值
     */
    public static int getAryMax(String[] ygValues, String[] dhValues, String[] hwValues, String[]
            utValues) {
        int n = 0;
        for (int i = 0; i < ygValues.length; i++) {
            if (Integer.valueOf(ygValues[i]) > n) {
                n = Integer.valueOf(ygValues[i]);
            }
        }
        for (int i = 0; i < dhValues.length; i++) {
            if (Integer.valueOf(dhValues[i]) > n) {
                n = Integer.valueOf(dhValues[i]);
            }
        }
        for (int i = 0; i < hwValues.length; i++) {
            if (Integer.valueOf(hwValues[i]) > n) {
                n = Integer.valueOf(hwValues[i]);
            }
        }
        for (int i = 0; i < utValues.length; i++) {
            if (Integer.valueOf(utValues[i]) > n) {
                n = Integer.valueOf(utValues[i]);
            }
        }
        return n;
    }

    /**
     * 根据最大值求出纵坐标的间隔数值(纵坐标7个数值)
     */
    public static int getAvgValues(int n) {
        //求出纵坐标每隔的数值间隔
        n = n % 7 == 0 ? n / 7 : n / 7 + 1;
        //求出每个间隔的向上整数
        if (n / 10000 > 0) {
            n = n % 10000 == 0 ? n : (n / 10000 + 1) * 10000;
        } else if (n / 1000 > 0) {
            n = n % 1000 == 0 ? n : (n / 1000 + 1) * 1000;
        } else if (n / 100 > 0) {
            n = n % 100 == 0 ? n : (n / 100 + 1) * 100;
        } else if (n / 10 > 0) {
            n = n % 10 == 0 ? n : (n / 10 + 1) * 10;
        }
        return n;
    }

    /**
     * 求出纵坐标的string数组
     *
     * @param n
     * @return
     */
    public static String[] getValuesY(int n) {
        int max = n * 7;
        String[] valuesY = new String[8];
        for (int i = 0; i < 8; i++) {
            valuesY[i] = String.valueOf(max - n * i);
        }
        return valuesY;
    }
}
