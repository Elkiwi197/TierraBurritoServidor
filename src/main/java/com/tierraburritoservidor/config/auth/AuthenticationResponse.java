package com.tierraburritoservidor.config.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tierraburritoservidor.domain.model.TipoUsuario;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("refresh_token")
    private String refreshToken;

    @JsonProperty("tipo_usuario")
    private TipoUsuario tipoUsuario;

}
