package com.tierraburritoservidor.errors.exceptions;

import com.tierraburritoservidor.common.ConstantesInfo;

public class    UsuarioYaActivadoException extends RuntimeException {
    public UsuarioYaActivadoException() {
        super(ConstantesInfo.USUARIO_YA_ACTIVADO);
    }
}


