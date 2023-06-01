package com.onibex.integration.sap.xls2pdf;

public class HtmlUtils {
    public static String getLinkFromPath(String path) {
        return "<a href=\"" + path + "\">" + path + "</a>";
    }
}
