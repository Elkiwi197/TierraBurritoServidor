package com.tierraburritoservidor.errors.exceptions;

import com.tierraburritoservidor.common.ConstantesInfo;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class ContrasenaIncorrectaException extends RuntimeException{
    public ContrasenaIncorrectaException() {
        super(ConstantesInfo.CONTRASENA_INCORRECTA);
    }
}
