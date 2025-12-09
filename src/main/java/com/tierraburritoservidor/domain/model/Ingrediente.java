package com.tierraburritoservidor.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ingrediente {
    private int id;
    private String nombre;
    private double precio;
    private String rutaFoto;
}
