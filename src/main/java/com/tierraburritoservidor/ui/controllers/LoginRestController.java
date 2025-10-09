package com.tierraburritoservidor.ui.controllers;


import com.tierraburritoservidor.common.ConstantesErrores;
import com.tierraburritoservidor.config.auth.AuthenticationResponse;
import com.tierraburritoservidor.config.auth.ConfigurationTokens;
import com.tierraburritoservidor.domain.model.TipoUsuario;
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
        TipoUsuario tipoUsuario = serviceUsuarios.getUsuarioByCorreo(correo).getTipoUsuario();
        String accessToken = configurationTokens.crearToken(correo, 120000);
        String refreshToken = configurationTokens.crearToken(correo, 900000);
        return ResponseEntity.ok().body(
                AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .tipoUsuario(tipoUsuario)
                        .build());
    }

    @PostMapping("auth/refresh")
    public AuthenticationResponse refresh(@RequestBody String refreshToken) {
        if (!configurationTokens.validarToken(refreshToken)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, ConstantesErrores.TOKEN_INVALIDO);
        }
        String correoUsuario = configurationTokens.getCorreo(refreshToken);
        TipoUsuario tipoUsuario = serviceUsuarios.getUsuarioByCorreo(correoUsuario).getTipoUsuario();
        String newAccessToken = configurationTokens.crearToken(correoUsuario, 1200000);
        return AuthenticationResponse.builder()
                .accessToken(newAccessToken)
//esto no deberia hacer falta        .tfipoUsuario(tipoUsuario)
                .build();
    }
}
