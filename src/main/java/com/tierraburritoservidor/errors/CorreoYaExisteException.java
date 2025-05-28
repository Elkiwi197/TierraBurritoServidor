package com.tierraburritoservidor.errors;

import com.tierraburritoservidor.common.ConstantesErrores;

public class CorreoYaExisteException  extends RuntimeException{
    public CorreoYaExisteException() {
        super(ConstantesErrores.CORREO_YA_EXISTE);
    }
}
