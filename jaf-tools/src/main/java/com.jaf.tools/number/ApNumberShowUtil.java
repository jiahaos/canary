package com.jaf.tools.number;

import java.math.BigDecimal;
import java.text.NumberFormat;

/**
 * Created by hsk on 2016/2/2.
 */
public final class ApNumberShowUtil {

    public static String tenThousand(int n) {
        String result = String.valueOf(
                (new BigDecimal(n)).divide(new BigDecimal(10000)).
                        setScale(2, BigDecimal.ROUND_DOWN).doubleValue()
        );
        if (result.endsWith(".00") && result.length() >= 3) {
            return result.substring(0, result.length() - 3);
        }
        if (result.endsWith(".0") && result.length() >= 2) {
            return result.substring(0, result.length() - 2);
        }
        return result;
    }

    public static String tenThousand(String n) {
        String result = String.valueOf(
                (new BigDecimal(n)).divide(new BigDecimal(10000)).
                        setScale(2, BigDecimal.ROUND_DOWN).doubleValue()
        );
        if (result.endsWith(".00") && result.length() >= 3) {
            return result.substring(0, result.length() - 3);
        }
        if (result.endsWith(".0") && result.length() >= 2) {
            return result.substring(0, result.length() - 2);
        }
        return result;
    }

    public static String calculatePercent(int count, int total) {
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(2);
        numberFormat.setMinimumFractionDigits(2);
        return numberFormat.format((float) count / (float) total);
    }

}
