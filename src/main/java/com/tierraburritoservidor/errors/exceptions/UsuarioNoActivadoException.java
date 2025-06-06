package com.tierraburritoservidor.errors.exceptions;

import com.tierraburritoservidor.common.ConstantesErrores;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UsuarioNoActivadoException extends RuntimeException {
    public UsuarioNoActivadoException() {
        super(ConstantesErrores.USUARIO_NO_ACTIVADO);
    }

}
