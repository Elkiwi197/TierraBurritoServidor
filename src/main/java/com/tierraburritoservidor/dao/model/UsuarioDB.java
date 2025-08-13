package com.tierraburritoservidor.dao.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDB {
    private ObjectId id;
    private String nombre;
    private String contrasena;
    private String correo;
    private String tipoUsuario;
    private boolean activado;
    private String codigoActivacion;
}
