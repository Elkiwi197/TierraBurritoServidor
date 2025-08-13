package com.tierraburritoservidor.dao;

import com.tierraburritoservidor.domain.model.Producto;

import java.util.List;

public interface RepositoryProductosInterface {
    List<Producto> getIngredientes();

    Producto getProductoByNombre(String nombre);
}
