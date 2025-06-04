package com.tierraburritoservidor.errors.exceptions;

import com.tierraburritoservidor.common.ConstantesErrores;

public class CodigoActivacionIncorrectoException extends RuntimeException {
    public CodigoActivacionIncorrectoException(){
        super(ConstantesErrores.CODIGO_ACTIVACION_INCORRECTO);
    }
}
