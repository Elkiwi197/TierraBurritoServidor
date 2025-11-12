package com.tierraburritoservidor.errors;

import com.tierraburritoservidor.errors.exceptions.*;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlers {


    @ExceptionHandler(CodigoActivacionIncorrectoException.class)
    public ResponseEntity<ApiError> handleException(CodigoActivacionIncorrectoException e) {
        ApiError apiError = new ApiError(e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiError);
    }


    @ExceptionHandler(CorreoException.class)
    public ResponseEntity<ApiError> handleException(CorreoException e) {
        ApiError apiError = new ApiError(e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiError);
    }


    @ExceptionHandler(CorreoYaExisteException.class)
    public ResponseEntity<ApiError> handleException(CorreoYaExisteException e) {
        ApiError apiError = new ApiError(e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiError);
    }


    @ExceptionHandler(PedidoNoEncontradoException.class)
    public ResponseEntity<ApiError> handleException(PedidoNoEncontradoException e) {
        ApiError apiError = new ApiError(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
    }

    @ExceptionHandler(PedidosNoEncontradoException.class)
    public ResponseEntity<ApiError> handleException(PedidosNoEncontradoException e) {
        ApiError apiError = new ApiError(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
    }

    @ExceptionHandler(PlatoNoEncontradoException.class)
    public ResponseEntity<ApiError> handleException(PlatoNoEncontradoException e) {
        ApiError apiError = new ApiError(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
    }

    @ExceptionHandler(ProductoNoEncontradoException.class)
    public ResponseEntity<ApiError> handleException(ProductoNoEncontradoException e) {
        ApiError apiError = new ApiError(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
    }


    @ExceptionHandler(UsuarioContrasenaIncorrectosException.class)
    public ResponseEntity<ApiError> handleException(UsuarioContrasenaIncorrectosException e) {
        ApiError apiError = new ApiError(e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiError);
    }


    @ExceptionHandler(UsuarioNoActivadoException.class)
    public ResponseEntity<ApiError> handleException(UsuarioNoActivadoException e) {
        ApiError apiError = new ApiError(e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiError);
    }


    @ExceptionHandler(UsuarioNoEncontradoException.class)
    public ResponseEntity<ApiError> handleException(UsuarioNoEncontradoException e) {
        ApiError apiError = new ApiError(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
    }


    @ExceptionHandler(UsuarioYaActivadoException.class)
    public ResponseEntity<ApiError> handleException(UsuarioYaActivadoException e) {
        ApiError apiError = new ApiError(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }


    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ApiError> handleException(ExpiredJwtException e) {
        ApiError apiError = new ApiError(e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiError);
    }
}