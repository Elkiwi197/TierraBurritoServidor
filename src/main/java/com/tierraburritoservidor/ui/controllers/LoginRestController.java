package com.tierraburritoservidor.ui.controllers;


import com.tierraburritoservidor.common.Constantes;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
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
        log.info(correo + ConstantesInfo.INICIO_SESION);
        return ResponseEntity.ok().body(
                AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .tipoUsuario(tipoUsuario)
                        .build());
    }

    @PostMapping("/auth/refresh")
    public AuthenticationResponse refresh(@RequestHeader(Constantes.AUTHORIZATION_HEADER) String authorizationHeader) {
        log.info("Header recibido: {}", authorizationHeader);
        String refreshToken = authorizationHeader.replace("Bearer ", "");

        if (!configurationTokens.validarToken(refreshToken)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, ConstantesInfo.TOKEN_INVALIDO);
        } else {

            String correoUsuario = configurationTokens.getCorreo(refreshToken);

            TipoUsuario tipoUsuario = serviceUsuarios.getUsuarioByCorreo(correoUsuario).getTipoUsuario();
            String newAccessToken = configurationTokens.crearToken(correoUsuario, 120000);
            String newRefreshToken = configurationTokens.crearToken(correoUsuario, 900000);

            log.info(ConstantesInfo.TOKEN_RENOVADO_A + correoUsuario);

            return AuthenticationResponse.builder()
                    .accessToken(newAccessToken)
                    .refreshToken(newRefreshToken)
                    .tipoUsuario(tipoUsuario)
                    .build();
        }
    }
}