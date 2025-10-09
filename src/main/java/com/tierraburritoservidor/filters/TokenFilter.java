package com.tierraburritoservidor.filters;

import com.tierraburritoservidor.common.Constantes;
import com.tierraburritoservidor.common.ConstantesErrores;
import com.tierraburritoservidor.config.ConfigurationBeans;
import com.tierraburritoservidor.config.auth.ConfigurationTokens;
import com.tierraburritoservidor.errors.exceptions.TokenCaducadoException;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
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
            throws IOException, ServletException {

        String authHeader = request.getHeader(Constantes.AUTHORIZATION_HEADER);
        if (authHeader == null || !authHeader.startsWith(Constantes.BEARER)) {
            log.warn(ConstantesErrores.TOKEN_NO_PROPORCIONADO);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, ConstantesErrores.TOKEN_INVALIDO);
            return;
        }

        String token = authHeader.substring(7);

        try {
            if (!configurationTokens.validarToken(token)) {
                log.error(ConstantesErrores.ERROR_VALIDAR_TOKEN);
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, ConstantesErrores.TOKEN_INVALIDO_O_EXPIRADO);
                return;
            }

            String correo = configurationTokens.getCorreo(token);
            log.info(Constantes.USUARIO_AUTENTICADO, correo);

            filterChain.doFilter(request, response);

        } catch (TokenCaducadoException e) {
            log.warn("Token expirado: {}", e.getMessage());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
        } catch(ExpiredJwtException e){
            log.warn("Token expirado: {}", e.getMessage());
            throw new TokenCaducadoException();
        } catch (Exception e) {
            log.error("Error inesperado al validar token: {}", e.getMessage(), e);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, ConstantesErrores.TOKEN_INVALIDO_O_EXPIRADO);
        }
    }


    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.contains("/signup") || path.contains("/login") || path.contains("/favicon")
                //|| path.contains("/")
                ;
                //todo borrar este ultimo, mientras este activo no compruebara tokens
    }
}
