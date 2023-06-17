package com.onibex.integration.sap.xls2pdf.controller;

import com.aspose.cells.License;
import com.aspose.cells.PdfCompliance;
import com.aspose.cells.PdfSaveOptions;
import com.aspose.cells.Workbook;
import com.onibex.integration.sap.xls2pdf.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Slf4j
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("api/files")
public class FileController {

    private final License license;

    @Autowired
    public FileController(License license, FileService fileService) {
        this.license = license;
        log.info("License found: " + this.license);
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
        try {
            log.info("converting xlsx into pdf ..." + this.license);
            Workbook workbook = new Workbook(dataFile.getInputStream());
            log.info("Is licen shased? " + workbook.isLicensed());
            PdfSaveOptions options = new PdfSaveOptions();
            options.setCompliance(PdfCompliance.PDF_A_1_A);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            workbook.save(baos, options);
            log.info("Convertion of xlsx file - done!");
            log.info("preparing for disposition...");
            byte[] resource = baos.toByteArray();
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(resource);
        } catch (Exception e1) {
            log.error("an error ocurred while trying to convert xlsx file: {0}", e1);
            return ResponseEntity
                    .internalServerError()
                    .build();
        }

    }
}
