package com.tierraburritoservidor.errors.exceptions;

import com.tierraburritoservidor.common.ConstantesErrores;

public class CorreoYaExisteException  extends RuntimeException{
    public CorreoYaExisteException() {
        super(ConstantesErrores.CORREO_YA_EXISTE);
    }
}
