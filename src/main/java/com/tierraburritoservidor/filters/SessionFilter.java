package com.tierraburritoservidor.filters;

import com.tierraburritoservidor.common.Constantes;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static org.springframework.util.ObjectUtils.isEmpty;

@Log4j2
@RequiredArgsConstructor
public class SessionFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        log.info(this.getClass().getName());
        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (isEmpty(header) || !header.startsWith(Constantes.BEARER)) { //dice Oscar que no dejemos esto (???)
            filterChain.doFilter(request, response);
            log.warn(Constantes.CABECERA_SIN_BEARER);
        } else{
            final String token = header.split(" ")[1].trim();
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return "/logintoken/login".equals(path); //todo meterlo en constantes
    }

}
