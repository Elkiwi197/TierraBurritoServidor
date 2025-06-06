package com.tierraburritoservidor.dao;

import com.tierraburritoservidor.domain.model.Ingredientes;
import com.tierraburritoservidor.domain.model.Plato;
import com.tierraburritoservidor.domain.model.Producto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class RepositoryPlatos {


    private final List<Plato> platos = List.of(
            new Plato(1, "Nachos", List.of(
                    new Producto(6, Ingredientes.FRIJOLES.name(), 0.0, "https://www.tierraburritos.com/wp-content/uploads/Frijoles-1-1140x1050.jpg"),
                    new Producto(11, Ingredientes.PICO_DE_GALLO.name(), 0.0, "https://www.tierraburritos.com/wp-content/uploads/Picodegallo-1140x1050.jpg"),
                    new Producto(19, Ingredientes.SALSA_DE_QUESO.name(), 0.0, "https://www.tierraburritos.com/wp-content/uploads/SalsaQueso-1140x1050.jpg"),
                    new Producto(7, Ingredientes.GUACAMOLE.name(), 2.50, "https://www.tierraburritos.com/wp-content/uploads/Guacamole-1140x1050.png"),
                    new Producto(5, Ingredientes.CREMA_AGRIA.name(), 0.0, "https://www.tierraburritos.com/wp-content/uploads/SalsaAgria-1140x1050.jpg")
            ), List.of(), 5.99, "https://www.tierraburritos.com/wp-content/uploads/02_Nachos-1.jpg"),
            new Plato(2, "Burrito", List.of(
                    new Producto(1, Ingredientes.ARROZ_BLANCO.name(), 0.0, "https://www.tierraburritos.com/wp-content/uploads/ArrozBlanco-1140x1050.png"),
                    new Producto(6, Ingredientes.FRIJOLES.name(), 0.0, "https://www.tierraburritos.com/wp-content/uploads/Frijoles-1-1140x1050.jpg"),
                    new Producto(11, Ingredientes.PICO_DE_GALLO.name(), 0.0, "https://www.tierraburritos.com/wp-content/uploads/Picodegallo-1140x1050.jpg"),
                    new Producto(4, Ingredientes.CARNITAS.name(), 3.60, "https://www.tierraburritos.com/wp-content/uploads/Carnitas-1-1140x1050.jpg"),
                    new Producto(7, Ingredientes.GUACAMOLE.name(), 2.50, "https://www.tierraburritos.com/wp-content/uploads/Guacamole-1140x1050.png")
            ), List.of(), 12.99, "https://www.tierraburritos.com/wp-content/uploads/14_Burrito.jpg"),
            new Plato(3, "Tacos", List.of(
                    new Producto(4, Ingredientes.CARNITAS.name(), 3.60, "https://www.tierraburritos.com/wp-content/uploads/Carnitas-1-1140x1050.jpg"),
                    new Producto(20, Ingredientes.VERDURAS.name(), 0.0, "https://www.tierraburritos.com/wp-content/uploads/Verduras-1140x1050.png"),
                    new Producto(13, Ingredientes.QUESO_RALLADO.name(), 0.0, "https://www.tierraburritos.com/wp-content/uploads/Queso-1140x1050.jpg"),
                    new Producto(11, Ingredientes.PICO_DE_GALLO.name(), 0.0, "https://www.tierraburritos.com/wp-content/uploads/Picodegallo-1140x1050.jpg"),
                    new Producto(7, Ingredientes.GUACAMOLE.name(), 2.50, "https://www.tierraburritos.com/wp-content/uploads/Guacamole-1140x1050.png"),
                    new Producto(19, Ingredientes.SALSA_DE_QUESO.name(), 0.0, "https://www.tierraburritos.com/wp-content/uploads/SalsaQueso-1140x1050.jpg")
            ), List.of(), 8.99, "https://www.tierraburritos.com/wp-content/uploads/10_Tacos-1.jpg"),
            new Plato(4, "Desnudo", List.of(
                    new Producto(4, Ingredientes.CARNITAS.name(), 3.60, "https://www.tierraburritos.com/wp-content/uploads/Carnitas-1-1140x1050.jpg"),
                    new Producto(1, Ingredientes.ARROZ_BLANCO.name(), 0.0, "https://www.tierraburritos.com/wp-content/uploads/ArrozBlanco-1140x1050.png"),
                    new Producto(10, Ingredientes.MAIZ.name(), 0.0, "https://www.tierraburritos.com/wp-content/uploads/Maiz-1-1140x1050.jpg"),
                    new Producto(11, Ingredientes.PICO_DE_GALLO.name(), 0.0, "https://www.tierraburritos.com/wp-content/uploads/Picodegallo-1140x1050.jpg"),
                    new Producto(20, Ingredientes.VERDURAS.name(), 0.0, "https://www.tierraburritos.com/wp-content/uploads/Verduras-1140x1050.png"),
                    new Producto(7, Ingredientes.GUACAMOLE.name(), 2.50, "https://www.tierraburritos.com/wp-content/uploads/Guacamole-1140x1050.png")
            ), List.of(), 10.99, "https://www.tierraburritos.com/wp-content/uploads/Desnudo_1-2.jpg"),
            new Plato(5, "Ensalada", List.of(
                    new Producto(9, Ingredientes.LECHUGA.name(), 0.0, "https://www.tierraburritos.com/wp-content/uploads/Ensalada-1140x1050.jpg"),
                    new Producto(10, Ingredientes.MAIZ.name(), 0.0, "https://www.tierraburritos.com/wp-content/uploads/Maiz-1-1140x1050.jpg"),
                    new Producto(1, Ingredientes.ARROZ_BLANCO.name(), 0.0, "https://www.tierraburritos.com/wp-content/uploads/ArrozBlanco-1140x1050.png"),
                    new Producto(11, Ingredientes.PICO_DE_GALLO.name(), 0.0, "https://www.tierraburritos.com/wp-content/uploads/Picodegallo-1140x1050.jpg"),
                    new Producto(13, Ingredientes.QUESO_RALLADO.name(), 0.0, "https://www.tierraburritos.com/wp-content/uploads/Queso-1140x1050.jpg"),
                    new Producto(6, Ingredientes.FRIJOLES.name(), 0.0, "https://www.tierraburritos.com/wp-content/uploads/Frijoles-1-1140x1050.jpg"),
                    new Producto(7, Ingredientes.GUACAMOLE.name(), 2.50, "https://www.tierraburritos.com/wp-content/uploads/Guacamole-1140x1050.png")
            ), List.of(), 9.99, "https://www.tierraburritos.com/wp-content/uploads/Ensalada_1-2.jpg")
    );

    public List<Plato> getAllPlatos() {
        return platos;
    }



    public Plato getPlatoById(int id) {
        return platos.stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElse(null);
    }
}

