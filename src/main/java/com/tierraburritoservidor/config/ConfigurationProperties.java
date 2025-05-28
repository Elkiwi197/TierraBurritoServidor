package com.tierraburritoservidor.config;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.Properties;

@Configuration
@Getter
@Log4j2
public class ConfigurationProperties {


    private String clave;

    private static ConfigurationProperties configuracion;


    public static ConfigurationProperties getInstance() {
        if (configuracion == null) {
            configuracion = new ConfigurationProperties();
        }
        return configuracion;
    }

    public ConfigurationProperties() {
        try {
            Properties p = new Properties();
            p.load(getClass().getClassLoader()
                    .getResourceAsStream("application.properties"));
            this.clave = p.getProperty("clave");
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

}
