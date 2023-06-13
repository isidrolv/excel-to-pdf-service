package com.onibex.integration.sap.xls2pdf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class })
public class ExcelToPdfServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExcelToPdfServiceApplication.class, args);
    }

}
