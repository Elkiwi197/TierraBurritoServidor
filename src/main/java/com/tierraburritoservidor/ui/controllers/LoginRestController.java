package com.tierraburritoservidor.ui.controllers;


import com.tierraburritoservidor.config.ConfigurationBeans;
import com.tierraburritoservidor.config.ConfigurationTokens;
import com.tierraburritoservidor.domain.service.ServiceUsuarios;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Key;

@RestController
@RequestMapping("/logintoken")
@RequiredArgsConstructor
public class LoginRestController {

    private final ConfigurationTokens configurationTokens;
    private final ConfigurationBeans configurationBeans;
    private final ServiceUsuarios serviceUsuarios;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam @NotBlank String nombreUsuario, @RequestParam @NotBlank String contrasena) {
        serviceUsuarios.comprobarCredenciales(nombreUsuario, contrasena);
        Key key = configurationBeans.key();
        return ResponseEntity.ok().body(configurationTokens.crearToken(nombreUsuario, key));
    }
}
