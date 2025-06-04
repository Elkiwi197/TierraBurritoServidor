package com.tierraburritoservidor.errors.exceptions;

import com.tierraburritoservidor.common.ConstantesErrores;

public class TokenCaducadoException  extends RuntimeException{
    public TokenCaducadoException() {
        super(ConstantesErrores.TOKEN_CADUCADO);
    }
}