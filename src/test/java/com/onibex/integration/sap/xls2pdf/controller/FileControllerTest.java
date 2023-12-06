package com.onibex.integration.sap.xls2pdf.controller;

import com.onibex.integration.sap.xls2pdf.service.CreatePdfException;
import com.onibex.integration.sap.xls2pdf.service.FileService;
import com.onibex.integration.sap.xls2pdf.utils.Sources;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

import static com.onibex.integration.sap.xls2pdf.controller.FileController.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class FileControllerTest {

    @Mock
    private FileService fileService;

    private FileController fileController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        fileController = new FileController(fileService);
        }

    @Test
    void testHealthCheck() {
        ResponseEntity<String> response = fileController.healthCheck();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Service is ok!", response.getBody());
    }

    @Test
    void testConvertExcelToPdfWithXlsxFile() throws IOException {
        MultipartFile mockFile = new MockMultipartFile("data.xlsx", "somefile.txt", APPLICATION_VND_XLSX, new byte[0]);

        when(fileService.convertExcel(mockFile)).thenReturn(ResponseEntity.ok(new byte[0]));

        ResponseEntity<byte[]> response = fileController.convertExcelToPdf(mockFile);

        assertEquals(HttpStatus.OK, response.getStatusCode());

    }

    @Test
    void testConvertExcelToPdfWithUnsupportedFileType() throws IOException {
        MultipartFile mockFile = new MockMultipartFile(Sources.ASFU.name(), Sources.ASFU.getPath(), "text/plain", new byte[0]);

        ResponseEntity<byte[]> response = fileController.convertExcelToPdf(mockFile);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testConvertExcelToPdfWithXlsFile() throws IOException {
        MultipartFile mockFile = new MockMultipartFile(Sources.UPP.name(), Sources.UPP.getPath(), APPLICATION_VND_MS_EXCEL, new byte[0]);
        when(fileService.convertExcel(mockFile)).thenReturn(ResponseEntity.ok(new byte[0]));

        ResponseEntity<byte[]> response = fileController.convertExcelToPdf(mockFile);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testConvertWordFile() throws IOException {
        MultipartFile mockFile = new MockMultipartFile(Sources.KAZAN_SBIT.name(), Sources.FEU.getPath(), APPLICATION_VND_WORD_DOCUMENT, new byte[0]);
        when(fileService.convertWord(mockFile)).thenReturn(ResponseEntity.ok(new byte[0]));

        ResponseEntity<byte[]> response = fileController.convertExcelToPdf(mockFile);

        assertEquals(HttpStatus.OK, response.getStatusCode());

    }

    @Test
    void testConvertWordFileWithException() throws IOException {
        CreatePdfException exception = new CreatePdfException("Error: Could not read the file!");

        MultipartFile mockFile = new MockMultipartFile(Sources.BIKTTS.name(), Sources.BIKTTS.getPath(), APPLICATION_VND_WORD_DOCUMENT, new byte[0]);
        when(fileService.convertWord(mockFile)).thenThrow(exception);

        assertThrows(CreatePdfException.class, () -> fileController.convertExcelToPdf(mockFile));
        assertNotNull(exception);
    }
}
