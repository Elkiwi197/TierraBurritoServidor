package com.tierraburritoservidor.dao.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "Usuarios")
public class UsuarioDB {
    @Id
    private ObjectId _id;
    private String nombre;
    private String contrasena;
    private String correo;
    private String tipoUsuario;
    private boolean activado;
    private String codigoActivacion;
}
