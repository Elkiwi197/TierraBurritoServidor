package com.tierraburritoservidor.domain.service;


import com.tierraburritoservidor.dao.RepositoryProductos;
import com.tierraburritoservidor.domain.model.Producto;
import com.tierraburritoservidor.errors.exceptions.ProductoNoEncontradoException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServiceProductos {

    private final RepositoryProductos repositoryProductos;


    public List<Producto> getAllProductos() {
        return repositoryProductos.getProductos();
    }

    public Producto getProductoById(int idProducto){
        Producto producto = repositoryProductos.getProductoById(idProducto);
        if(producto==null){
            throw new ProductoNoEncontradoException();
        }
        return producto;
    }

    public Producto getProductoByNombre(String nombreProducto){
        Producto producto = repositoryProductos.getProductoByNombre(nombreProducto);
        if(producto==null){
            throw new ProductoNoEncontradoException();
        }
        return producto;
    }
}
