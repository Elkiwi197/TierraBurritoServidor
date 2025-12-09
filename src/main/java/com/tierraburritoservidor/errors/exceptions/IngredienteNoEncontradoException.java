package com.tierraburritoservidor.errors.exceptions;

import com.tierraburritoservidor.common.ConstantesInfo;

public class IngredienteNoEncontradoException extends RuntimeException {
    public IngredienteNoEncontradoException() {
        super(ConstantesInfo.INGREDIENTE_NO_ENCONTRADO);
    }
}
