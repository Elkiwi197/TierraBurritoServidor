package com.tierraburritoservidor.errors.exceptions;

import com.tierraburritoservidor.common.ConstantesErrores;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class TokenCaducadoException  extends RuntimeException{
    public TokenCaducadoException() {
        super(ConstantesErrores.TOKEN_CADUCADO);
    }
}