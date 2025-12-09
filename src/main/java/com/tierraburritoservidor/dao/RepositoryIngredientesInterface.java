package com.tierraburritoservidor.dao;

import com.tierraburritoservidor.dao.model.IngredienteDB;
import com.tierraburritoservidor.dao.model.PlatoDB;
import org.bson.types.ObjectId;

import java.util.List;

public interface RepositoryIngredientesInterface {
    void inicializarIngredientes();

    List<IngredienteDB> getIngredientes();

    IngredienteDB getIngredienteByNombre(String nombre);

    IngredienteDB getIngredienteByObjectId(ObjectId objectId);

    List<IngredienteDB> getExtrasByPlatoDB(PlatoDB platoDB);
}
