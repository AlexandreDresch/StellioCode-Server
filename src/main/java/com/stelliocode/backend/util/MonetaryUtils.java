package com.stelliocode.backend.util;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class MonetaryUtils {
    public static String formatToBRL(BigDecimal value) {
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        return currencyFormatter.format(value);
    }
}
