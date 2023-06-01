package com.onibex.integration.sap.xls2pdf;

import java.net.MalformedURLException;

public class DownloadException extends RuntimeException {
    public DownloadException(MalformedURLException e) {
        super("Error: " + e.getMessage());
    }
}
