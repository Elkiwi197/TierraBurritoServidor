package com.tierraburritoservidor.config;


import com.tierraburritoservidor.common.Constantes;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Configuration
@RequiredArgsConstructor
@Log4j2
public class ConfigurationTokens {


    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public String crearToken(String nombreUsuario, Key key) {
        return Jwts.builder()
                .setSubject(Constantes.SUBJECT)
                .setIssuer(Constantes.ISSUER)
                .setExpiration(Date.from(LocalDateTime.now().plusSeconds(60).atZone(ZoneId.systemDefault()).toInstant())) //todo cambiar los segundos cuando acabe de hacer la api
                .claim(Constantes.NOMBRE_USUARIO, nombreUsuario)
                .signWith(key)
                .compact();
    }

    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public boolean validarToken(String token, Key key) {
        Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
        return true;

    }

}
