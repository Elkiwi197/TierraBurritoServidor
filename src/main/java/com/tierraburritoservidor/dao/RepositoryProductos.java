package com.tierraburritoservidor.dao;


import com.tierraburritoservidor.domain.model.Ingredientes;
import com.tierraburritoservidor.domain.model.Producto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class RepositoryProductos {

    private List<Producto> productos = List.of(
            new Producto(1, Ingredientes.ARROZ_BLANCO.name(), 0.0, "https://www.tierraburritos.com/wp-content/uploads/ArrozBlanco-1140x1050.png"),
            new Producto(2, Ingredientes.ARROZ_INTEGRAL.name(), 0.0, "https://www.tierraburritos.com/wp-content/uploads/ArrozIntegral-1140x1050.png"),
            new Producto(3, Ingredientes.CARNE_BBQ.name(), 0.0, "https://www.tierraburritos.com/wp-content/uploads/Carnitas-1-1-1140x1050.jpg"),
            new Producto(4, Ingredientes.CARNITAS.name(), 0.0, "https://www.tierraburritos.com/wp-content/uploads/Carnitas-1-1140x1050.jpg"),
            new Producto(5, Ingredientes.CREMA_AGRIA.name(), 0.0, "https://www.tierraburritos.com/wp-content/uploads/SalsaAgria-1140x1050.jpg"),
            new Producto(6, Ingredientes.FRIJOLES.name(), 0.0, "https://www.tierraburritos.com/wp-content/uploads/Frijoles-1-1140x1050.jpg"),
            new Producto(7, Ingredientes.GUACAMOLE.name(), 0.0, "https://www.tierraburritos.com/wp-content/uploads/Guacamole-1140x1050.png"),
            new Producto(8, Ingredientes.JALAPENOS.name(), 0.0, "https://www.tierraburritos.com/wp-content/uploads/chipotle-1140x1050.png"),
            new Producto(9, Ingredientes.LECHUGA.name(), 0.0, "https://www.tierraburritos.com/wp-content/uploads/Ensalada-1140x1050.jpg"),
            new Producto(10, Ingredientes.MAIZ.name(), 0.0, "https://www.tierraburritos.com/wp-content/uploads/Maiz-1-1140x1050.jpg"),
            new Producto(11, Ingredientes.PICO_DE_GALLO.name(), 0.0, "https://www.tierraburritos.com/wp-content/uploads/Picodegallo-1140x1050.jpg"),
            new Producto(12, Ingredientes.POLLO.name(), 0.0, "https://www.tierraburritos.com/wp-content/uploads/Pollo-1-1140x1050.jpg"),
            new Producto(13, Ingredientes.QUESO_RALLADO.name(), 0.0, "https://www.tierraburritos.com/wp-content/uploads/Queso-1140x1050.jpg"),
            new Producto(14, Ingredientes.SALSA_666.name(), 0.0, "https://www.tierraburritos.com/wp-content/uploads/Salsa666-1140x1050.jpg"),
            new Producto(15, Ingredientes.SALSA_CHIPOTLE.name(), 0.0, "https://www.tierraburritos.com/wp-content/uploads/Salsachipotle-1140x1050.jpg"),
            new Producto(16, Ingredientes.STEAK.name(), 0.0, "https://www.tierraburritos.com/wp-content/uploads/Steak-1140x1050.png"),
            new Producto(17, Ingredientes.SALSA_MEDIA.name(), 0.0, "https://www.tierraburritos.com/wp-content/uploads/SalsaMedia-1140x1050.jpg"),
            new Producto(18, Ingredientes.SALSA_VERDE.name(), 0.0, "https://www.tierraburritos.com/wp-content/uploads/SalsaVerde-1140x1050.jpg"),
            new Producto(19, Ingredientes.SALSA_DE_QUESO.name(), 0.0, "https://www.tierraburritos.com/wp-content/uploads/SalsaQueso-1140x1050.jpg"),
            new Producto(20, Ingredientes.VERDURAS.name(), 0.0, "https://www.tierraburritos.com/wp-content/uploads/Verduras-1140x1050.png")
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
                .filter(p -> p.getNombre().equalsIgnoreCase(nombre))
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
