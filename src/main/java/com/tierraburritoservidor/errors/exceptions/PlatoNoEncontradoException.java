package com.tierraburritoservidor.errors.exceptions;

import com.tierraburritoservidor.common.ConstantesErrores;

public class PlatoNoEncontradoException extends RuntimeException {
    public PlatoNoEncontradoException() {
        super(ConstantesErrores.PLATO_NO_ENCONTRADO);
    }
}
