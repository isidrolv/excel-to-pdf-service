package com.onibex.integration.sap.xls2pdf.service;


import com.onibex.integration.sap.xls2pdf.controller.DownloadException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileService {

    @Value("${files.path}")
    private String filesPath;

    public Resource download(String filename) throws DownloadException {
        try {
            Path file = Paths.get(filesPath)
                    .resolve(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new CreatePdfException("Could not read the file!");
            }
        } catch (MalformedURLException | CreatePdfException ex) {
            throw new DownloadException(ex.getMessage());
        }
    }
}
