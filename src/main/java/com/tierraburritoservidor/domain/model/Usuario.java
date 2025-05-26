package com.tierraburritoservidor.domain.model;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Usuario {
    private int id;
    private String nombre;
    private String contrasena;
    private String correo;
}
