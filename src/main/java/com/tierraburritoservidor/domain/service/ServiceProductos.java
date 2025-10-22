package com.tierraburritoservidor.domain.service;


import com.tierraburritoservidor.dao.model.ProductoDB;
import com.tierraburritoservidor.dao.repositories.RepositoryPlatos;
import com.tierraburritoservidor.dao.repositories.RepositoryProductos;
import com.tierraburritoservidor.dao.util.PlatoIdManager;
import com.tierraburritoservidor.dao.util.ProductoIdManager;
import com.tierraburritoservidor.domain.model.Plato;
import com.tierraburritoservidor.domain.model.Producto;
import com.tierraburritoservidor.domain.util.DatabaseUiParser;
import com.tierraburritoservidor.errors.exceptions.ProductoNoEncontradoException;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ServiceProductos {

    private final RepositoryProductos repositoryProductos;
    private final DatabaseUiParser databaseUiParser;
    private final RepositoryPlatos repositoryPlatos;
    private final PlatoIdManager platoIdManager;
    private final ProductoIdManager productoIdManager;


    public Producto getProductoByNombre(String nombreProducto) {
        ProductoDB productoDB = repositoryProductos.getProductoByNombre(nombreProducto);
        if (productoDB == null) {
            throw new ProductoNoEncontradoException();
        }
        return databaseUiParser.productoDBtoProducto(productoDB);
        //todo parsear
    }

    public List<Producto> getIngredientesByPlato(Plato plato) {
        List<Producto> ingredientes = new ArrayList<>();
        plato.getIngredientes().forEach(ingrediente -> ingredientes.add(getProductoByNombre(ingrediente.getNombre())));
        return ingredientes;
    }

    public List<Producto> getExtrasByPlato(Plato plato) {
        List<Producto> extras = new ArrayList<>();
        List<ObjectId> ingredientesPlato = new ArrayList<>();
        plato.getIngredientes().forEach(i -> ingredientesPlato.add(productoIdManager.getObjectId(i.getId())));
        repositoryProductos.getIngredientes().forEach(i -> {
            if (!ingredientesPlato.contains(i.get_id())){
                extras.add(databaseUiParser.productoDBtoProducto(i));
                // Llama 20 veces a la BBDD, pero si la linea de arriba falla se pone
                // extras.add(getProductoByNombre(i.getNombre()));
            }
        });
        return extras;
    }
}
