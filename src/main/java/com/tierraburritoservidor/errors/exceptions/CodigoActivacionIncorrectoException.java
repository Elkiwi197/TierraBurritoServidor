package com.tierraburritoservidor.errors.exceptions;

import com.tierraburritoservidor.common.ConstantesInfo;

public class CodigoActivacionIncorrectoException extends RuntimeException {
    public CodigoActivacionIncorrectoException(){
        super(ConstantesInfo.CODIGO_ACTIVACION_INCORRECTO);
    }
}
