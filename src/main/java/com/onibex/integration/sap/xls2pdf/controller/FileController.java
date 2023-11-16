package com.onibex.integration.sap.xls2pdf.controller;

import com.aspose.cells.License;
import com.aspose.cells.PdfCompliance;
import com.aspose.cells.PdfSaveOptions;
import com.aspose.cells.Workbook;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.onibex.integration.sap.xls2pdf.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Optional;

@Slf4j
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("api/files")
public class FileController {

    public static final String APPLICATION_VND_XLSX = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    public static final String APPLICATION_VND_MS_EXCEL = "application/vnd.ms-excel";
    public static final String APPLICATION_VND_WORD_DOCUMENT = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
    private final FileService fileService;

    @Autowired
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping("/healthCheck")
    @ResponseBody
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity
                .ok()
                .body("Service is ok!");
    }

    @PostMapping("/convert")
    @ResponseBody
    public ResponseEntity<byte[]> convertExcelToPdf(@RequestParam("file") MultipartFile dataFile) throws IOException {
        log.info("receiving file...");
        ResponseEntity<byte[]> response = null;
        Optional<String> contentType = Optional.of(dataFile.getContentType());
        switch (contentType.get()) {
            case APPLICATION_VND_XLSX:
                log.info("file type is xlsx");
                response = this.fileService.convertExcel(dataFile);
                break;
            case APPLICATION_VND_MS_EXCEL:
                log.info("file type is xls");
                response = this.fileService.convertExcel(dataFile);
                break;
            case APPLICATION_VND_WORD_DOCUMENT:
                log.info("file type is doc");
                response = this.fileService.convertWord(dataFile);
                break;
            default:
                log.info("file type is not supported");
                return ResponseEntity
                        .badRequest()
                        .build();
        }

        return response;
    }


}
