package com.onibex.integration.sap.xls2pdf;

public class Util {
    public static String getOutputPath(String sourcePath) {
        int index = sourcePath.lastIndexOf(".");
        return sourcePath.substring(0, ++index) + "pdf";
    }
}
