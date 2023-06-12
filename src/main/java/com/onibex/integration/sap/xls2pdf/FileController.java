package com.onibex.integration.sap.xls2pdf;


import com.itextpdf.licensing.base.LicenseKey;
import com.itextpdf.pdfoffice.OfficeConverter;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Log4j
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("api/files")
public class FileController {

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

    @GetMapping("/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename) throws IOException {
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
        File pdfFile = File.createTempFile("pdf", null);
        try {
            log.info("Loading iText license...");
            LicenseKey.loadLicenseFile(new File("license.json"));
            log.info("converting xlsx into pdf ...");
            OfficeConverter.convertOfficeSpreadsheetToPdf(dataFile.getInputStream(),
                    new FileOutputStream(pdfFile));
            log.info("done!");
        } catch (Exception e1) {
           log.error(e1);
        }
        Path file = Paths.get(pdfFile.getAbsolutePath());
        Resource resource = new UrlResource(file.toUri());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "application/pdf")
                .header(HttpHeaders.CONTENT_DISPOSITION, "att"
                        + "catchment; filename=\"\"")
                .body(resource);
    }
}
