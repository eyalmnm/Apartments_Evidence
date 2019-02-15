package md.starlab.apartmentsevidenceapp.utils;


import android.graphics.Paint;

/**
 * Created by Sergey Ostrovsky
 * on 7/16/18
 */
public class StringUtils {

    /**
     * Check whether the given string is null or empty
     *
     * @param str the string to be checked
     * @return true if the given string in null or empty
     */
    public static boolean isNullOrEmpty(String str) {
        if (str == null)
            return true;
        str = str.replaceAll("/?", "").replaceAll("<", "").replaceAll(">", "")
                .replaceAll("&", "").replaceAll("\"", "").replaceAll("\'", "")
                .replaceAll(";", "").replaceAll("\n", "").replaceAll("\r", "")
                .replaceAll("\t", "").trim();
        return str.trim().length() == 0;
    }

    public static float getStringWidth(Paint textPaint, String text) {
        if ((null == text) || text.isEmpty()) return 0;
        return textPaint.measureText(text, 0, text.length());
    }

}
