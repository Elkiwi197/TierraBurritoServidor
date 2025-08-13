package com.tierraburritoservidor.ui.controllers;


import com.tierraburritoservidor.common.ConstantesErrores;
import com.tierraburritoservidor.config.auth.AuthenticationResponse;
import com.tierraburritoservidor.config.auth.ConfigurationTokens;
import com.tierraburritoservidor.domain.service.ServiceUsuarios;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("")
@RequiredArgsConstructor
public class LoginRestController {

    private final ConfigurationTokens configurationTokens;
    private final ServiceUsuarios serviceUsuarios;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestParam @NotBlank String correo, @RequestParam @NotBlank String contrasena) {
        serviceUsuarios.comprobarCredenciales(correo, contrasena);
        String accessToken = configurationTokens.crearToken(correo, 1200);
        String refreshToken = configurationTokens.crearToken(correo, 20000);
        return ResponseEntity.ok().body(
                AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build());
    }

    @PostMapping("auth/refresh")
    public AuthenticationResponse refresh(@RequestBody String refreshToken) {
        if (!configurationTokens.validarToken(refreshToken)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, ConstantesErrores.TOKEN_INVALIDO);
        }
        String correoUsuario = configurationTokens.getCorreo(refreshToken);
        String newAccessToken = configurationTokens.crearToken(correoUsuario, 1200000);
        return AuthenticationResponse.builder()
                .accessToken(newAccessToken)
                .build();
    }
}
