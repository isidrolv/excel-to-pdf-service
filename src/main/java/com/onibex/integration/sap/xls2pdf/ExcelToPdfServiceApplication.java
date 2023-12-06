package com.onibex.integration.sap.xls2pdf;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.MultipartConfigElement;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class ExcelToPdfServiceApplication {

    @Value("${files.maxUploadSize}")
    private Long maxUploadSize;
    public static void main(String[] args) {
        SpringApplication.run(ExcelToPdfServiceApplication.class, args);
    }



    @Bean(name = "multipartConfigElement")
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize(DataSize.ofBytes(maxUploadSize));
        factory.setMaxRequestSize(DataSize.ofBytes(maxUploadSize));
        return factory.createMultipartConfig();
    }

}
