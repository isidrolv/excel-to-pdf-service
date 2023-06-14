package com.onibex.integration.sap.xls2pdf.controller;

import com.aspose.cells.License;
import com.aspose.cells.PdfCompliance;
import com.aspose.cells.PdfSaveOptions;
import com.aspose.cells.Workbook;

import com.onibex.integration.sap.xls2pdf.service.FileService;
import com.onibex.integration.sap.xls2pdf.utils.Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("api/files")
public class FileController {

    private final License license;
    private final FileService fileService;

    @Autowired
    public FileController(License license, FileService fileService) {
        this.license = license;
        this.fileService = fileService;
        log.info("License found: " + this.license);
    }

    @GetMapping("/healthCheck")
    @ResponseBody
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity
                .ok()
                .body("Service is ok!");
    }

    @GetMapping("/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename) throws IOException, DownloadException {
        Resource file = fileService.download(filename);
        Path path = file.getFile()
                .toPath();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, Files.probeContentType(path))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }

    @PostMapping("/convert")
    @ResponseBody
    public ResponseEntity<Resource> convertExcelToPdf(@RequestParam("file") MultipartFile dataFile) throws IOException {
        log.info("receiving file...");
        try {
            log.info("converting xlsx into pdf ..." + this.license);
            Workbook workbook = new Workbook(dataFile.getInputStream());
            log.info("Is licensed? " + workbook.isLicensed());
            PdfSaveOptions options = new PdfSaveOptions();
            options.setCompliance(PdfCompliance.PDF_A_1_A);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            workbook.save(baos, options);
            log.info("Convertion of xlsx file - done!");
            log.info("preparing for disposition...");
            Resource resource = new ByteArrayResource(baos.toByteArray());
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, "application/pdf")
                    .header(HttpHeaders.CONTENT_DISPOSITION, "att"
                            + "catchment; filename=\"\"")
                    .body(resource);
        } catch (Exception e1) {
            log.error("an error ocurred while trying to convert xlsx file: {0}", e1);
            return ResponseEntity
                    .status(HttpStatusCode.valueOf(505))
                    .build();
        }

    }
}
