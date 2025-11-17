package com.tierraburritoservidor.errors.exceptions;

import com.tierraburritoservidor.common.ConstantesInfo;

public class UsuarioNoEncontradoException extends RuntimeException{
    public UsuarioNoEncontradoException() {
        super(ConstantesInfo.USUARIO_NO_ENCONTRADO);
    }
}
