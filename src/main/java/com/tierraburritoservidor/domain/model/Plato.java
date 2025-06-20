package com.tierraburritoservidor.domain.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Plato {
    private int id;
    private String nombre;
    private List<Producto> ingredientes;
    private List<Producto> extras;
    private double precio;
    private String rutaFoto;
}
