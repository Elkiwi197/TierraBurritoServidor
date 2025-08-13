package com.tierraburritoservidor.domain.service;


import com.tierraburritoservidor.dao.repositories.RepositoryProductos;
import com.tierraburritoservidor.domain.model.Plato;
import com.tierraburritoservidor.domain.model.Producto;
import com.tierraburritoservidor.errors.exceptions.ProductoNoEncontradoException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ServiceProductos {

    private final RepositoryProductos repositoryProductos;


    public Producto getProductoByNombre(String nombreProducto) {
        Producto producto = repositoryProductos.getProductoByNombre(nombreProducto);
        if (producto == null) {
            throw new ProductoNoEncontradoException();
        }
        return producto;
    }

    public List<Producto> getIngredientesByPlato(Plato plato) {
        List<Producto> ingredientes = new ArrayList<>();
        plato.getIngredientes().forEach(ingrediente -> ingredientes.add(getProductoByNombre(ingrediente.getNombre())));
        return ingredientes;
    }

    public List<Producto> getExtrasByPlato(Plato plato) {
        List<Producto> extras = new ArrayList<>();
        repositoryProductos.getIngredientes().forEach(i -> {
            if (!plato.getIngredientes().contains(i)) {
                extras.add(i);
            }
        });
        return extras;
    }
}
