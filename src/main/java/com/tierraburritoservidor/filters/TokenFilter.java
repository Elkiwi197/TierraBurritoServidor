package com.tierraburritoservidor.filters;

import com.tierraburritoservidor.common.Constantes;
import com.tierraburritoservidor.config.ConfigurationBeans;
import com.tierraburritoservidor.config.auth.ConfigurationTokens;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Log4j2
@RequiredArgsConstructor
public class TokenFilter extends OncePerRequestFilter {

    private final ConfigurationTokens configurationTokens;
    private final ConfigurationBeans configurationBeans;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException {

        String authHeader = request.getHeader(Constantes.AUTHORIZATION_HEADER);
        if (authHeader == null || !authHeader.startsWith(Constantes.BEARER_)) {
            log.warn(Constantes.TOKEN_NO_PROPORCIONADO);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, Constantes.TOKEN_INVALIDO);
            return;
        }

        String token = authHeader.substring(7);

        if (!configurationTokens.validarToken(token)) {
            log.error(Constantes.ERROR_VALIDAR_TOKEN);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, Constantes.TOKEN_INVALIDO_O_EXPIRADO);
            return;
        }

        try {
            String user = configurationTokens.getCorreo(token); //todo cambiar a correo
            log.info(Constantes.USUARIO_AUTENTICADO, user);

            filterChain.doFilter(request, response);

        } catch (Exception e) {
            log.error(Constantes.ERROR_VALIDAR_TOKEN + "{}", e.getMessage());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, Constantes.TOKEN_INVALIDO_O_EXPIRADO);
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.contains("/signup") || path.contains("/login") || path.contains("/favicon");
    }
}
