package com.onibex.integration.sap.xls2pdf.controller;

public class DownloadException extends Exception {
    public DownloadException(String message) {
        super("Error: " + message);
    }
}
