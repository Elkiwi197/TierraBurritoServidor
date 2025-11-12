package com.tierraburritoservidor.dao.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "Pedidos")
public class PedidoDB {
    private ObjectId _id;
    private String direccion;
    private String correoCliente;
    private List<PlatoDB> platos;
    private List<ProductoDB> otros;
    private double precio;
    private String estado;
    private String repartidor;
    private LocalDateTime horaLlegada;
}
