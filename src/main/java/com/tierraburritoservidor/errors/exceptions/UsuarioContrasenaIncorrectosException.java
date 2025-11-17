package com.tierraburritoservidor.errors.exceptions;

import com.tierraburritoservidor.common.ConstantesInfo;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UsuarioContrasenaIncorrectosException extends RuntimeException{
    public UsuarioContrasenaIncorrectosException() {
        super(ConstantesInfo.USUARIO_O_CONTRASENA_INCORRECTOS);
    }
}
