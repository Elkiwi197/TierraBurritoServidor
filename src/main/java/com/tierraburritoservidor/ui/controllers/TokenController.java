package com.tierraburritoservidor.ui.controllers;


import com.tierraburritoservidor.config.ConfigurationBeans;
import com.tierraburritoservidor.config.ConfigurationTokens;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Key;

@RestController
@RequestMapping("/logintoken")
@RequiredArgsConstructor
public class TokenController {

    private final ConfigurationTokens configurationTokens;
    private final ConfigurationBeans  configurationBeans;

    @PostMapping("/login")
    public String login(@RequestParam @NotBlank String nombreUsuario){
        Key key = configurationBeans.key();
        return configurationTokens.crearToken(nombreUsuario, key);
    }

    @GetMapping("/validate")
    public String validateToken(@RequestParam String token){
        Key key = configurationBeans.key();
        return configurationTokens.validarToken(token, key);
    }

}
