package com.tierraburritoservidor.domain.service;


import com.tierraburritoservidor.dao.model.ProductoDB;
import com.tierraburritoservidor.dao.repositories.RepositoryProductos;
import com.tierraburritoservidor.domain.model.Plato;
import com.tierraburritoservidor.domain.model.Producto;
import com.tierraburritoservidor.domain.util.DatabaseUiParser;
import com.tierraburritoservidor.errors.exceptions.ProductoNoEncontradoException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ServiceProductos {

    private final RepositoryProductos repositoryProductos;
    private final DatabaseUiParser databaseUiParser;


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
        repositoryProductos.getIngredientes().forEach(i -> {
            Producto ingrediente = databaseUiParser.productoDBtoProducto(i);
            if (!plato.getIngredientes().contains(ingrediente)) {
                extras.add(getProductoByNombre(i.getNombre()));
            }
        });
        return extras;
    }
}
