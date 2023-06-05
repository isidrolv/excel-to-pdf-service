package com.onibex.integration.sap.xls2pdf;

import java.io.File;
import java.io.FileOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.UrlResource;
import org.springframework.web.multipart.MultipartFile;

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
//        XSSFWorkbook wb = new XSSFWorkbook(dataFile.getInputStream());

        File pdfFile = null;
        File tempFile = File.createTempFile("xtemp", null);
        String sourcefile = tempFile.getAbsolutePath();
        final Path path = Paths.get(tempFile.getAbsolutePath()).toAbsolutePath();
        Files.write(path, dataFile.getBytes(), StandardOpenOption.CREATE);
//        File excelFile = new File(tempFile);
        File pdfOutDir = new File(path.getParent().toString());
//        Thread t = new Thread(() -> {
        try {

        pdfFile =    ExcelToPdfConverter
                    .getInstance()
                    .convert(tempFile, pdfOutDir);
            //EIceblueExcel2PdfConverter.convert(excelFile.getAbsolutePath());
        } catch (Exception e1) {

            System.err.println(e1);
        } finally {
            //convertButton.setEnabled(true);
        }
//        });
//        t.start();

        Path file = Paths.get(pdfFile.getAbsolutePath());
        Resource resource = new UrlResource(file.toUri());
        return (ResponseEntity<Resource>) ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "application/pdf")
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"\"")
                .body(resource);
    }
}
