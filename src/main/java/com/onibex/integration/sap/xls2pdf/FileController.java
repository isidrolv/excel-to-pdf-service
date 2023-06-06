package com.onibex.integration.sap.xls2pdf;


import com.itextpdf.licensing.base.LicenseKey;
import com.itextpdf.pdfoffice.OfficeConverter;
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

import org.springframework.core.io.UrlResource;
import org.springframework.web.multipart.MultipartFile;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.File;
import java.io.IOException;

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

       
        File myDir = new File("Temp");
        if (!myDir.exists()) {
            myDir.mkdirs();
        }
        File tempFileN = File.createTempFile("xtemp", null);
        File tempFile = new File("Temp/" + tempFileN.getName());
        String sourcefile = tempFile.getAbsolutePath();
        final Path path = Paths.get(tempFile.getAbsolutePath()).toAbsolutePath();
        Files.write(path, dataFile.getBytes(), StandardOpenOption.CREATE);
         File pdfFile = File.createTempFile("pdf", null);
//        File excelFile = new File(tempFile);
        //File pdfOutDir = new File();

        // Load your source Excel file inside the Workbook object.
//        Thread t = new Thread(() -> {
        try {

            //Workbook workbook = new Workbook(tempFile.getAbsolutePath());
            final String pdfFilename = tempFile.getAbsolutePath() + ".pdf";

// Save the workbook in PDF format.
            //workbook.save(pdfFilename, SaveFormat.PDF);
            
            //pdfFile = ExcelToPdfConverter
            //        .getInstance()
            //        .convert(tempFile, pdfOutDir);
            
            //pdfFile = new File(pdfFilename);
            
            // Workbook workbook = new Workbook();
       // workbook.loadFromFile(tempFile.getAbsolutePath());
//        workbook.getConverterSetting().setSheetFitToPage(true);
       // workbook.getConverterSetting().setSheetFitToWidth(true);
       // workbook.saveToFile(Util.getOutputPath(tempFile.getAbsolutePath()), FileFormat.PDF);
       
        LicenseKey.loadLicenseFile(new File("license.json"));

        OfficeConverter.convertOfficeSpreadsheetToPdf(dataFile.getInputStream(), 
              new FileOutputStream(pdfFile));

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
                .header(HttpHeaders.CONTENT_DISPOSITION, "att"
                        + "achment; filename=\"\"")
                .body(resource);
    }
}
