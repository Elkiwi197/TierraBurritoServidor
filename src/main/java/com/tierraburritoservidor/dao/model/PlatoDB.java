package com.tierraburritoservidor.dao.model;

import com.tierraburritoservidor.common.Constantes;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = Constantes.PLATOS)
public class PlatoDB {
    @Id
    private ObjectId _id;
    private String nombre;
    private List<ObjectId> ingredientes;
    private double precio;
    private String rutaFoto;
}