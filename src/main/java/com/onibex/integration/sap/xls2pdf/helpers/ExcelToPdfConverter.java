package com.onibex.integration.sap.xls2pdf.helpers;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.exceptions.OLE2NotOfficeXmlFileException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Slf4j
/**
 @deprecated
 */
public class ExcelToPdfConverter {

    private String path = null;
    private static final ExcelToPdfConverter INSTANCE = new ExcelToPdfConverter();
    private static final String SOURCE_NAME = "libreoffice";

    private ExcelToPdfConverter() {
        this.path = System.getProperty(SOURCE_NAME);
        if (this.path == null) {
            if (new File(SOURCE_NAME).exists()) {
                path = new File(SOURCE_NAME).getAbsolutePath();
            } else {
                String office = getClass().getResource("/" + SOURCE_NAME).getPath();
                File file = new File(office);
                path = file.getAbsolutePath();
            }
        }
    }

    public static ExcelToPdfConverter getInstance() {
        return INSTANCE;
    }

    /**
     * @param inputDocument
     * @param outputDir
     * @return
     * @throws Exception
     * @deprecated
     */
    @Deprecated(forRemoval = true)
    public File convert(File inputDocument, File outputDir) throws Exception {
        File outputFile = null;
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }
        try {
            // Start an office process and connect to the started instance (on port 2002).

            Workbook sourceWorkbook = createWorkbook(inputDocument);

            try (ByteArrayOutputStream baout = new ByteArrayOutputStream()) {
                sourceWorkbook.write(baout);
            }
            String fileName = getFileName(inputDocument.getName());
            outputFile = new File(outputDir.getAbsolutePath() + File.separatorChar + fileName);

            sourceWorkbook.close();
        } catch (Exception ex) {
            log.error("An error occurred at ExcelToPdfConverter.convert(): {0}", ex);
        }
        return outputFile;
    }

    private Workbook createWorkbook(File inputDocument) throws IOException, InvalidFormatException {
        Workbook sourceWorkbook;
        try {
            sourceWorkbook = new XSSFWorkbook(inputDocument);
        } catch (OLE2NotOfficeXmlFileException e) {
            try (FileInputStream fileInputStream = new FileInputStream(inputDocument.getAbsoluteFile())) {
                sourceWorkbook = new HSSFWorkbook(fileInputStream);
            }
        }
        return sourceWorkbook;
    }

    private String getFileName(String name) {
        String fileName = name.substring(0, name.lastIndexOf(".")) + ".pdf";
        String invalidCharRemoved = fileName.replaceAll("[\\\\/:*?\"<>|]", "");
        log.info(invalidCharRemoved);
        return invalidCharRemoved;
    }
}
