package com.tierraburritoservidor.errors.exceptions;

import com.tierraburritoservidor.common.ConstantesInfo;

public class CorreoException extends RuntimeException{
    public CorreoException() {
        super(ConstantesInfo.NO_SE_PUDO_MANDAR_CORREO);
    }
}
