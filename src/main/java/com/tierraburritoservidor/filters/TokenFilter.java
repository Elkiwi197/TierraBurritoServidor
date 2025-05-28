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
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import org.springframework.http.HttpHeaders;

@Component
@Log4j2
@RequiredArgsConstructor
public class TokenFilter extends OncePerRequestFilter {

    private final ConfigurationTokens configurationTokens;
    private final ConfigurationBeans configurationBeans;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info(this.getClass().getSimpleName(), Constantes.VALIDANDO_TOKEN);

        String token = request.getHeader(HttpHeaders.AUTHORIZATION).split(" ")[1].trim();
        try {
            configurationTokens.validarToken(token, configurationBeans.key());
        } catch (Exception e){
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request){
        String path = request.getRequestURI();
        return path.contains("/signup") || path.contains("/login");
    }
}
