package com.tierraburritoservidor.ui.controllers;


import com.tierraburritoservidor.config.ConfigurationBeans;
import com.tierraburritoservidor.config.auth.ConfigurationTokens;
import com.tierraburritoservidor.domain.service.ServiceUsuarios;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Key;

@RestController
@RequestMapping("")
@RequiredArgsConstructor
public class LoginRestController {

    private final ConfigurationTokens configurationTokens;
    private final ConfigurationBeans configurationBeans;
    private final ServiceUsuarios serviceUsuarios;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam @NotBlank String correo, @RequestParam @NotBlank String contrasena) {
        serviceUsuarios.comprobarCredenciales(correo, contrasena);
        Key key = configurationBeans.key();
        return ResponseEntity.ok().body(configurationTokens.crearToken(correo, key));
    }
}
