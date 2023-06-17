/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.onibex.integration.sap.xls2pdf.config;

import com.aspose.cells.License;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;


import java.io.InputStream;

/**
 * @author isidrolv
 */
@Configuration
@EnableWebSecurity
@Slf4j
public class ConvertToPdfServiceConfiguration {

    @Value("${aspose.licenseFileName}")
    private String licenseFileName;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        log.warn("Disabiling crsf, so all requests are accepted!");
        http.csrf().disable();
        http.authorizeHttpRequests()
                .anyRequest()
                .permitAll();
        return http.build();
    }

    @Bean(name = "license")
    public License license() {
        log.info("Loading Aspose Cells license..." + this.licenseFileName);
        // Initialize License Instance
        License license = new License();
        // Call setLicense method to set license
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            InputStream inputStream = classLoader.getResourceAsStream(licenseFileName);
            log.info("Path to license file: " + classLoader.getResource(licenseFileName).getPath());
            license.setLicense(inputStream);
            log.info(new StringBuilder()
                    .append("Aspose Cells license loaded! \n\tlicensed = ")
                    .append(License.isLicenseSet()).append("\n\tlicense file = ")
                    .append(licenseFileName)
                    .append("\n\t expires = ")
                    .append(License.getSubscriptionExpireDate())
                    .toString());
        } catch (Exception ex) {
            log.error("An error occurred during license loading...{0}", ex);
        }
        return license;
    }
}
