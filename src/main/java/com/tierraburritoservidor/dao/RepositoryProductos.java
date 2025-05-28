package com.tierraburritoservidor.dao;


import com.tierraburritoservidor.domain.model.Producto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class RepositoryProductos {

    private List<Producto> productos = List.of(
            new Producto(1, "Nachos", 5.99, "https://b2291319.smushcdn.com/2291319/wp-content/uploads/02_Nachos-1.jpg?lossy=1&strip=1&webp=1"),
            new Producto(2, "Burrito", 12.99, "https://b2291319.smushcdn.com/2291319/wp-content/uploads/03_Burrito.jpg?lossy=1&strip=1&webp=1"),
            new Producto(3, "Tacos", 8.99, "https://b2291319.smushcdn.com/2291319/wp-content/uploads/06_Tacos.jpg?lossy=1&strip=1&webp=1"),
            new Producto(4, "Desnudo", 10.99, "https://b2291319.smushcdn.com/2291319/wp-content/uploads/Desnudo_2.jpg?lossy=1&strip=1&webp=1"),
            new Producto(5, "Ensalada", 9.99, "https://b2291319.smushcdn.com/2291319/wp-content/uploads/Ensalada_1-2.jpg?lossy=1&strip=1&webp=1")
    );

    public List<Producto> getProductos() {
        return productos;
    }

    public void addProducto(Producto producto) {
        int id = 0;
        boolean repetido = true;
        while (repetido) {
            id = (int) (Math.random() * 100 + 1);
            int finalId = id;
            if (productos.stream().noneMatch(p -> p.getId() == finalId)) {
                repetido = false;
            }
        }
        producto.setId(id);
        productos.add(producto);
    }

    public void updateProducto(Producto producto) {
        Producto productoUpdate = productos.stream()
                .filter(u -> u.getId() == producto.getId())
                .findFirst()
                .orElse(null);
        if (productoUpdate != null) {
            productoUpdate.setNombre(productoUpdate.getNombre());
            productoUpdate.setPrecio(productoUpdate.getPrecio());
            productoUpdate.setRutaFoto(productoUpdate.getRutaFoto());
        }
    }

    public void deleteProducto(int id) {
        productos.removeIf(p -> p.getId() == id);
    }

    public Producto getProductoByNombre(String nombre) {
        return productos.stream()
                .filter(p -> p.getNombre().equals(nombre))
                .findFirst()
                .orElse(null);
    }

    public Producto getProductoById(int id) {
        return productos.stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElse(null);
    }
}
