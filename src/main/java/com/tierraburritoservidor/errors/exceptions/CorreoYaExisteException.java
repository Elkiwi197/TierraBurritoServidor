package com.tierraburritoservidor.errors.exceptions;

import com.tierraburritoservidor.common.ConstantesInfo;

public class CorreoYaExisteException  extends RuntimeException{
    public CorreoYaExisteException() {
        super(ConstantesInfo.CORREO_YA_EXISTE);
    }
}
