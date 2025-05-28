package com.tierraburritoservidor.errors;

import com.tierraburritoservidor.common.ConstantesErrores;

public class UsuarioContrasenaIncorrectosException extends RuntimeException{
    public UsuarioContrasenaIncorrectosException() {
        super(ConstantesErrores.USUARIO_O_CONTRASENA_INCORRECTOS);
    }
}
