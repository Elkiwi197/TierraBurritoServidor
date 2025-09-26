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
@Document(collection = "Productos")
public class ProductoDB {
    @Id
    private ObjectId _id;
    private String nombre;
    private double precio;
    private String rutafoto;
}