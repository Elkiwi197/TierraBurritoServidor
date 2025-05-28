package com.tierraburritoservidor.errors;

import com.tierraburritoservidor.common.ConstantesErrores;

public class UsuarioNoEncontradoException extends RuntimeException{
    public UsuarioNoEncontradoException() {
        super(ConstantesErrores.USUARIO_NO_ENCONTRADO);
    }
}
