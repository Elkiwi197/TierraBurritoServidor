package com.tierraburritoservidor.errors.exceptions;

import com.tierraburritoservidor.common.ConstantesInfo;

public class PlatoNoEncontradoException extends RuntimeException {
    public PlatoNoEncontradoException() {
        super(ConstantesInfo.PLATO_NO_ENCONTRADO);
    }
}
