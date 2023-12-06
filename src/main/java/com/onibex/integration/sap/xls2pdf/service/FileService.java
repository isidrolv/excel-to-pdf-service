package com.onibex.integration.sap.xls2pdf.service;


import com.aspose.cells.License;
import com.aspose.cells.PdfCompliance;
import com.aspose.cells.PdfSaveOptions;
import com.aspose.cells.Workbook;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Slf4j
@Service
public class FileService {

    @Value("${files.path}")
    private String filesPath;

    private License license;

    @Autowired
    public FileService(License license) {
        this.license = license;
    }


    /**
     * Convert a xlsx file into pdf
     * @param dataFile
     * @return
     */
    public ResponseEntity<byte[]> convertExcel(MultipartFile dataFile) {
        try {
            log.info("converting xlsx into pdf ..." + this.license);
            Workbook workbook = new Workbook(dataFile.getInputStream());
            log.info("Is licensed? " + workbook.isLicensed());
            PdfSaveOptions options = new PdfSaveOptions();
            options.setCompliance(PdfCompliance.PDF_A_1_A);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            workbook.save(baos, options);
            log.info("Conversion of xlsx file - done!");
            log.info("preparing for disposition...");
            byte[] resource = baos.toByteArray();
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(resource);
        } catch (Exception e1) {
            log.error("an error occurred while trying to convert xlsx file: {0}", e1);
            return ResponseEntity
                    .internalServerError()
                    .header("Exception", e1.getMessage())
                    .build();
        }
    }

    /**
     * Convert a docx file into pdf
     * @param dataFile
     * @return
     * @throws IOException
     */
    public ResponseEntity<byte[]> convertWord(MultipartFile dataFile) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        XWPFDocument document = new XWPFDocument(dataFile.getInputStream());
        try {
            Document pdfDocument = new Document();
            PdfWriter.getInstance(pdfDocument, outputStream);
            pdfDocument.open();

            for (XWPFParagraph paragraph : document.getParagraphs()) {
                String text = paragraph.getText();
                pdfDocument.add(new Paragraph(text));
            }
            pdfDocument.close();
            log.info("Done");
        } catch (Exception e) {
            log.error("an error ocurred while trying to convert doc file: {0}", e);
        }

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .body(outputStream.toByteArray());
    }
}
