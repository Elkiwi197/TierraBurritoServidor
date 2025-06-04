package com.tierraburritoservidor.common;

public class Constantes {
    private Constantes() {
    }


    public static final String VALIDANDO_TOKEN = "Validando token";
    public static final String SUBJECT = "Para el servidor";
    public static final String ISSUER = "Servidor";
    public static final String NOMBRE_USUARIO = "NombreUsuario";
    public static final String SHA_512 = "SHA-512";
    public static final String AES = "AES";
    public static final String BEARER = "Bearer";
    public static final String TOKEN = "token";
    public static final String NO_AUTORIZADO = "No autorizado";
    public static final String CABECERA_SIN_BEARER  = "La llamada no tiene bearer en la cabecera";
    public static final String USUARIO_ACTIVADO = "Usuario activado";


    //todo cambiar
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String TOKEN_NO_PROPORCIONADO = "Token no proporcionado o formato incorrecto";
    public static final String TOKEN_INVALIDO = "Token no proporcionado o inválido";
    public static final String ERROR_VALIDAR_TOKEN = "Error al validar el token: ";
    public static final String TOKEN_INVALIDO_O_EXPIRADO = "Token inválido o expirado";
    public static final String USUARIO_AUTENTICADO = "Usuario autenticado: {}";
    public static final String BEARER_ = "Bearer ";

}