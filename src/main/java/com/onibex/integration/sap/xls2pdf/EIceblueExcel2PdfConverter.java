package com.onibex.integration.sap.xls2pdf;

import com.spire.xls.FileFormat;
import com.spire.xls.Workbook;

import java.io.File;

public class EIceblueExcel2PdfConverter {
    public static void main(String[] args) {
        
    }

    private static boolean isFileExists(Sources xls) {
        File file = new File(xls.getPath());
        return file.exists() && file.isFile();
    }

    public static void convert(String xls) {
        Workbook workbook = new Workbook();
        workbook.loadFromFile(xls);
//        workbook.getConverterSetting().setSheetFitToPage(true);
        workbook.getConverterSetting().setSheetFitToWidth(true);
        final String outputPath = Util.getOutputPath(xls);
        workbook.saveToFile(outputPath, FileFormat.PDF);
    }
}