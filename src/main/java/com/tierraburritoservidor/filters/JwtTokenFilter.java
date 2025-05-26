package com.tierraburritoservidor.filters;

import com.tierraburritoservidor.common.Constantes;
import com.tierraburritoservidor.config.ConfigurationBeans;
import com.tierraburritoservidor.config.ConfigurationTokens;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.Key;

@Log4j2
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private final ConfigurationTokens configurationTokens;
    private final ConfigurationBeans configurationBeans;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        logger.info(this.getClass().getName());
        String token = request.getParameter(Constantes.TOKEN);
        Key key = configurationBeans.key();
        if (request.getServletPath().equals("/logintoken/login")) {
            filterChain.doFilter(request, response);
            return;
        }
        try {
            configurationTokens.validarToken(token, key);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, Constantes.NO_AUTORIZADO);
        }
        filterChain.doFilter(request, response);

    }


    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return "/logintoken/login".equals(path); //todo meterlo en constantes
    }

}
