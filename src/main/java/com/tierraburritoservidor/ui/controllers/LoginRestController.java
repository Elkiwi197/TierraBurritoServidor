package com.tierraburritoservidor.ui.controllers;


import com.tierraburritoservidor.common.ConstantesInfo;
import com.tierraburritoservidor.config.auth.AuthenticationResponse;
import com.tierraburritoservidor.config.auth.ConfigurationTokens;
import com.tierraburritoservidor.domain.model.TipoUsuario;
import com.tierraburritoservidor.domain.service.ServiceUsuarios;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("")
@RequiredArgsConstructor
@Log4j2
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
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, ConstantesInfo.TOKEN_INVALIDO);
        }
        String correoUsuario = configurationTokens.getCorreo(refreshToken);
   //     TipoUsuario tipoUsuario = serviceUsuarios.getUsuarioByCorreo(correoUsuario).getTipoUsuario();
        String newAccessToken = configurationTokens.crearToken(correoUsuario, 120000);
        log.info(ConstantesInfo.TOKEN_RENOVADO_A + correoUsuario);
        return AuthenticationResponse.builder()
                .accessToken(newAccessToken)
//esto no deberia hacer falta        .tfipoUsuario(tipoUsuario)
                .build();
    }
}
