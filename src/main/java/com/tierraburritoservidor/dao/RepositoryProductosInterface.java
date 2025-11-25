package com.tierraburritoservidor.dao;

import com.tierraburritoservidor.dao.model.PlatoDB;
import com.tierraburritoservidor.dao.model.ProductoDB;
import org.bson.types.ObjectId;

import java.util.List;

public interface RepositoryProductosInterface {
    List<ProductoDB> getIngredientes();

    ProductoDB getProductoByNombre(String nombre);

    ProductoDB getProductoByObjectId(ObjectId objectId);

    List<ProductoDB> getExtrasByPlatoDB(PlatoDB platoDB);
}
