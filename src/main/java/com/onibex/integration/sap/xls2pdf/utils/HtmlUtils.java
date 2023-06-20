package com.onibex.integration.sap.xls2pdf.utils;

public class HtmlUtils {
    private HtmlUtils() {
    }

    public static String getLinkFromPath(String path) {
        return "<a href=\"" + path + "\">" + path + "</a>";
    }
}
