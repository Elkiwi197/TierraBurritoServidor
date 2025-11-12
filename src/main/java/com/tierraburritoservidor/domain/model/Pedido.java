package com.tierraburritoservidor.domain.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.tierraburritoservidor.domain.util.DateDeserializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pedido {
    private int id;
    private String direccion;
    private String correoCliente;
    private List<Plato> platos;
    private List<Producto> otros;
    private double precio;
    private EstadoPedido estado;
    private String correoRepartidor;

    @JsonDeserialize(using = DateDeserializer.class)
    private LocalDateTime horaLlegada;
}
