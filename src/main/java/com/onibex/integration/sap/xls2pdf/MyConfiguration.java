/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.onibex.integration.sap.xls2pdf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 *
 * @author isidrolv
 */
@Configuration
@EnableWebSecurity
public class MyConfiguration {
    @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
      http.csrf().disable();
      http.authorizeHttpRequests()
              .requestMatchers("/**").permitAll()
              .anyRequest()
              .permitAll();
     return http.build();
  }
}
