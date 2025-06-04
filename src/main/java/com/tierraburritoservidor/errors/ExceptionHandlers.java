package com.tierraburritoservidor.errors;

import com.tierraburritoservidor.errors.exceptions.*;
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
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiError);
    }


    @ExceptionHandler(ProductoNoEncontradoException.class)
    public ResponseEntity<ApiError> handleException(ProductoNoEncontradoException e) {
        ApiError apiError = new ApiError(e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiError);
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
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiError);
    }


    @ExceptionHandler(UsuarioYaActivadoException.class)
    public ResponseEntity<ApiError> handleException(UsuarioYaActivadoException e) {
        ApiError apiError = new ApiError(e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiError);
    }


    @ExceptionHandler(TokenCaducadoException.class)
    public ResponseEntity<ApiError> handleException(TokenCaducadoException e) {
        ApiError apiError = new ApiError(e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiError);
    }





}