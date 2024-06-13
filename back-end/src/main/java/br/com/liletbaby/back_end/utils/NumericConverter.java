package br.com.liletbaby.back_end.utils;

/**
 * NumericConverter class.
 *
 * @author Wender Couto
 * @since 0.0.1-SNAPSHOT
 */

public class NumericConverter {

    public static Integer safeParseInteger(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        try {
            int parsedValue = Integer.parseInt(value.trim());
            return parsedValue > 0 ? parsedValue : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static Double safeParseDouble(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        try {
            double parsedValue = Double.parseDouble(value.trim());
            return parsedValue > 0 ? parsedValue : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
