package com.tierraburritoservidor.config;


import com.tierraburritoservidor.common.Constantes;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = false, jsr250Enabled = true)
@Log4j2
public class ConfigurationBeans {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public Key key() {
        final MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance(Constantes.SHA_512);
            messageDigest.update("clave".getBytes(StandardCharsets.UTF_8)); //todo guardar clave en application.properties
            final SecretKeySpec key = new SecretKeySpec(
                    messageDigest.digest(), 0, 64, Constantes.AES);
            SecretKey keyConfig = Keys.hmacShaKeyFor(key.getEncoded());
            return keyConfig;
        } catch (NoSuchAlgorithmException e) {
            log.error(this.getClass().getName(), e.getMessage());
        }
        return null;
    }


}
