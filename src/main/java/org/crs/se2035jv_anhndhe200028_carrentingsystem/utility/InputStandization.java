package org.crs.se2035jv_anhndhe200028_carrentingsystem.utility;

public class InputStandization {
    public static String formatName(String string) {
        String[] m = string.trim().split("\\s+");
        string = "";
        for (String str : m) {
            String head = str.substring(0, 1).toUpperCase();
            String tail = str.substring(1).toLowerCase();
            String result = head + tail + " ";
            string += result;
        }
        return string.trim();
    }
}
