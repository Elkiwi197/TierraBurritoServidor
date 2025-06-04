package com.tierraburritoservidor.errors.exceptions;

import com.tierraburritoservidor.common.ConstantesErrores;

public class CorreoException extends RuntimeException{
    public CorreoException() {
        super(ConstantesErrores.NO_SE_PUDO_MANDAR_CORREO);
    }
}
