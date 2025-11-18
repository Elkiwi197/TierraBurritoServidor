package com.tierraburritoservidor.filters;

import com.tierraburritoservidor.common.Constantes;
import com.tierraburritoservidor.common.ConstantesInfo;
import com.tierraburritoservidor.config.auth.ConfigurationTokens;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Log4j2
@RequiredArgsConstructor
public class TokenFilter extends OncePerRequestFilter {

    private final ConfigurationTokens configurationTokens;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException {

        String authHeader = request.getHeader(Constantes.AUTHORIZATION_HEADER);
        if (authHeader == null || !authHeader.startsWith(Constantes.BEARER)) {
            log.warn(ConstantesInfo.TOKEN_NO_PROPORCIONADO);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, ConstantesInfo.TOKEN_NO_PROPORCIONADO);
            throw new AuthenticationException(ConstantesInfo.TOKEN_NO_PROPORCIONADO) {
            };
        }

        String token = authHeader.substring(7);

        try {
            if (!configurationTokens.validarToken(token)) {
                log.error(ConstantesInfo.ERROR_VALIDANDO_TOKEN);
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, ConstantesInfo.TOKEN_INVALIDO_O_EXPIRADO);
                throw new AuthenticationException(ConstantesInfo.TOKEN_INVALIDO_O_EXPIRADO) {
                };
            }

            String correo = configurationTokens.getCorreo(token);
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            correo,
                            null,
                            null
                    );
            SecurityContextHolder.getContext().setAuthentication(authentication);


            log.info(ConstantesInfo.TOKEN_VALIDO + " " + correo);
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            log.warn(ConstantesInfo.TOKEN_EXPIRADO);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, ConstantesInfo.TOKEN_EXPIRADO);
            throw new AuthenticationException(ConstantesInfo.TOKEN_EXPIRADO) {};
        } catch (Exception e) {
            log.error(ConstantesInfo.ERROR_VALIDANDO_TOKEN, e.getMessage());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, ConstantesInfo.TOKEN_INVALIDO);
            throw new AuthenticationException(ConstantesInfo.TOKEN_INVALIDO) {};
        }
    }


    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.contains("/signup")
                || path.contains("/login")
                || path.contains("/favicon")
                || path.contains("/auth/refresh")
                //   || path.contains("/")
                ;
    }
}
