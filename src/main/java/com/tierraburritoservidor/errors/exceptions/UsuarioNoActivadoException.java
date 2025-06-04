package com.tierraburritoservidor.errors.exceptions;

import com.tierraburritoservidor.common.ConstantesErrores;

public class UsuarioNoActivadoException extends RuntimeException {
    public UsuarioNoActivadoException() {
        super(ConstantesErrores.USUARIO_NO_ACTIVADO);
    }

}
