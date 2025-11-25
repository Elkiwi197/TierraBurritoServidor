
package com.tierraburritoservidor.domain.service;

import com.tierraburritoservidor.dao.model.PlatoDB;
import com.tierraburritoservidor.dao.model.ProductoDB;
import com.tierraburritoservidor.dao.repositories.RepositoryPlatos;
import com.tierraburritoservidor.dao.repositories.RepositoryProductos;
import com.tierraburritoservidor.dao.util.PlatoIdManager;
import com.tierraburritoservidor.domain.model.Plato;
import com.tierraburritoservidor.domain.model.Producto;
import com.tierraburritoservidor.domain.util.DatabaseUiParser;
import com.tierraburritoservidor.errors.exceptions.PlatoNoEncontradoException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ServicePlatos {

    private final RepositoryPlatos repositoryPlatos;
    private final RepositoryProductos repositoryProductos;
    private final PlatoIdManager platoIdManager;
    private final DatabaseUiParser databaseUiParser;


    public List<Plato> getAllPlatos() {
        List<PlatoDB> platosDB = repositoryPlatos.getAllPlatos();
        List<Plato> platos = new ArrayList<>();
        platosDB.forEach(p -> {
            Plato plato = databaseUiParser.platoDBtoPlato(p);
            List<Producto> ingredientes = new ArrayList<>();
            p.getIngredientes().forEach(objectId ->
                    ingredientes.add(databaseUiParser.productoDBtoProducto(
                            repositoryProductos.getProductoByObjectId(objectId))));
            plato.setIngredientes(ingredientes);
            platos.add(plato);
        });

        return platos;
    }

    public Plato getPlatoById(int id) {
        PlatoDB platoDB = repositoryPlatos.getPlatoById(platoIdManager.getObjectId(id));
        if (platoDB == null) {
            throw new PlatoNoEncontradoException();
        }
        List<Producto> ingredientes = new ArrayList<>();
        List<Producto> extras = new ArrayList<>();
        Plato plato = databaseUiParser.platoDBtoPlato(platoDB);
        platoDB.getIngredientes().forEach(objectId ->
                ingredientes.add(databaseUiParser.productoDBtoProducto(
                        repositoryProductos.getProductoByObjectId(objectId))));
        plato.setIngredientes(ingredientes);
        List<ProductoDB> extrasDB = repositoryProductos.getExtrasByPlatoDB(platoDB);
        extrasDB.forEach(e -> extras.add(databaseUiParser.productoDBtoProducto(e)));
        plato.setIngredientes(ingredientes);
        plato.setExtras(extras);
        return plato;
    }
}
