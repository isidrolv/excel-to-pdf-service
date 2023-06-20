package com.onibex.integration.sap.xls2pdf;

import com.aspose.cells.License;
import com.onibex.integration.sap.xls2pdf.config.ConvertToPdfServiceConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.isA;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class ExcelToPdfServiceApplicationTest {

    @Autowired
    private ConvertToPdfServiceConfiguration config;

    @Test
    void contextLoads() {
        assertTrue(config.license().isLicenseSet());
        assertThat(config.license(), isA(License.class));
        Assert.isTrue(true, "dummy!"); // dummy test to avoid "No tests found for given includes" error
    }

}
