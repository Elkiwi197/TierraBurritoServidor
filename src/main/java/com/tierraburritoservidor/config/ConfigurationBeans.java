package com.tierraburritoservidor.config;


import com.tierraburritoservidor.common.Constantes;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Configuration
@RequiredArgsConstructor
@Log4j2
public class ConfigurationBeans {

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public Key key() {
        final MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance(Constantes.SHA_512);
            messageDigest.update(ConfigurationProperties.getInstance().getClave()
                    .getBytes(StandardCharsets.UTF_8));
            final SecretKeySpec key = new SecretKeySpec(
                    messageDigest.digest(), 0, 64, Constantes.AES);
            return Keys.hmacShaKeyFor(key.getEncoded());
        } catch (NoSuchAlgorithmException e) {
            log.error(e.getMessage(), e);

        }
        return null;
    }


    @Bean
    public PasswordEncoder crearPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
