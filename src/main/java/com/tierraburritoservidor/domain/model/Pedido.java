package com.tierraburritoservidor.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pedido {
    private int id;
    private int idCliente;
    private String direccion;
    private String nombreCliente;
    private List<Plato> platos;
    private List<Producto> otros;
    private double precio;
    private EstadoPedido estado;
}
