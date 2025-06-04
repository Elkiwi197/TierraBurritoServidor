package com.tierraburritoservidor.errors.exceptions;

import com.tierraburritoservidor.common.ConstantesErrores;

public class UsuarioYaActivadoException extends RuntimeException {
    public UsuarioYaActivadoException() {
        super(ConstantesErrores.USUARIO_YA_ACTIVADO);
    }
}


