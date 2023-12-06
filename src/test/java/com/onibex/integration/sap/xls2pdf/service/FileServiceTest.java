package com.onibex.integration.sap.xls2pdf.service;

import com.aspose.cells.License;
import com.aspose.cells.SaveOptions;
import com.aspose.cells.Workbook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
//import org.powermock.api.mockito.PowerMockito;
//import static org.powermock.api.mockito.PowerMockito.whenNew;

class FileServiceTest {

    private FileService fileService;

    @Mock
    private License license;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        fileService = new FileService(license);
    }

    @Test
    void convertExcel_shouldConvertXlsxToPdf() throws Exception {
        // Create a mock MultipartFile
        byte[] fileContent = "test data".getBytes();
        MultipartFile multipartFile = new MockMultipartFile("file", "test.xlsx", MediaType.APPLICATION_OCTET_STREAM_VALUE, fileContent);

        // Mock the behavior of the Workbook and License classes
        Workbook workbook = mock(Workbook.class);
        when(workbook.isLicensed()).thenReturn(true);
        //whenNew(Workbook.class).withAnyArguments().thenReturn(workbook);

        // Call the method under test
        ResponseEntity<byte[]> response = fileService.convertExcel(multipartFile);

        // Verify the response
        assertEquals(MediaType.APPLICATION_PDF, response.getHeaders().getContentType());
        assertArrayEquals(fileContent, response.getBody());

        // Verify the workbook and license interactions
        verify(workbook).save(anyString(), any(SaveOptions.class));
        verify(license).setLicense(any(InputStream.class));
    }

    @Test
    void convertExcel_shouldHandleException() throws IOException {
        // Create a mock MultipartFile
        byte[] fileContent = "test data".getBytes();
        MultipartFile multipartFile = new MockMultipartFile("file", "test.xlsx", MediaType.APPLICATION_OCTET_STREAM_VALUE, fileContent);

        // Mock the behavior of the Workbook and License classes to throw an exception
        //assertThrows(Exception.class, () -> {
           // whenNew(Workbook.class).withAnyArguments()
           //         .thenThrow(new Exception("Failed to convert xlsx file"));
        //});

        // Call the method under test
        ResponseEntity<byte[]> response = fileService.convertExcel(multipartFile);

        // Verify the response
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void convertWord_shouldConvertDocxToPdf() throws IOException {
        // Create a mock MultipartFile
        byte[] fileContent = "test data".getBytes();
        MultipartFile multipartFile = new MockMultipartFile("file", "test.docx", MediaType.APPLICATION_OCTET_STREAM_VALUE, fileContent);

        // Call the method under test
        ResponseEntity<byte[]> response = fileService.convertWord(multipartFile);

        // Verify the response
        assertEquals(MediaType.APPLICATION_PDF, response.getHeaders().getContentType());
        assertArrayEquals(fileContent, response.getBody());
    }

    // Add more test cases for different scenarios and edge cases
}
