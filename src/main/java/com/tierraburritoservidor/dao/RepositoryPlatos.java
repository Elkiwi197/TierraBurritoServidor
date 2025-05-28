package com.tierraburritoservidor.dao;

import com.tierraburritoservidor.domain.model.Ingredientes;
import com.tierraburritoservidor.domain.model.Plato;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class RepositoryPlatos {


    private List<Plato> platos = List.of(
            new Plato(1, "Nachos", List.of(
                    Ingredientes.FRIJOLES, Ingredientes.PICO_DE_GALLO, Ingredientes.SALSA_DE_QUESO, Ingredientes.GUACAMOLE, Ingredientes.CREMA_AGRIA
            ), List.of(), 5.99, "https://www.tierraburritos.com/wp-content/uploads/02_Nachos-1.jpg"),
            new Plato(2, "Burrito", List.of(
                    Ingredientes.ARROZ_BLANCO, Ingredientes.FRIJOLES, Ingredientes.PICO_DE_GALLO, Ingredientes.CARNITAS, Ingredientes.GUACAMOLE
            ), List.of(), 12.99, "https://www.tierraburritos.com/wp-content/uploads/14_Burrito.jpg"),
            new Plato(3, "Tacos", List.of(
                    Ingredientes.CARNITAS, Ingredientes.VERDURAS, Ingredientes.QUESO_RALLADO, Ingredientes.PICO_DE_GALLO, Ingredientes.GUACAMOLE, Ingredientes.SALSA_DE_QUESO
            ), List.of(), 8.99, "https://www.tierraburritos.com/wp-content/uploads/10_Tacos-1.jpg"),
            new Plato(4, "Desnudo", List.of(
                    Ingredientes.CARNITAS, Ingredientes.ARROZ_BLANCO, Ingredientes.MAIZ, Ingredientes.PICO_DE_GALLO, Ingredientes.VERDURAS, Ingredientes.GUACAMOLE
            ), List.of(), 10.99, "https://www.tierraburritos.com/wp-content/uploads/Desnudo_1-2.jpg"),
            new Plato(5, "Ensalada", List.of(
                    Ingredientes.LECHUGA, Ingredientes.MAIZ, Ingredientes.ARROZ_BLANCO, Ingredientes.PICO_DE_GALLO, Ingredientes.QUESO_RALLADO, Ingredientes.FRIJOLES, Ingredientes.GUACAMOLE
            ), List.of(), 9.99, "https://www.tierraburritos.com/wp-content/uploads/Ensalada_1-2.jpg")
    );

    public List<Plato> getAllPlatos() {
        return platos;
    }

    public void addPlato(Plato plato) {
        int id = 0;
        boolean repetido = true;
        while (repetido) {
            id = (int) (Math.random() * 100 + 1);
            int finalId = id;
            if (platos.stream().noneMatch(p -> p.getId() == finalId)) {
                repetido = false;
            }
        }
        plato.setId(id);
        platos.add(plato);
    }

    public void updatePlato(Plato plato) {
        Plato platoUpdate = platos.stream()
                .filter(u -> u.getId() == plato.getId())
                .findFirst()
                .orElse(null);
        if (platoUpdate != null) {
            platoUpdate.setNombre(platoUpdate.getNombre());
            platoUpdate.setPrecio(platoUpdate.getPrecio());
            platoUpdate.setRutaFoto(platoUpdate.getRutaFoto());
        }
    }

    public void deletePlato(int id) {
        platos.removeIf(p -> p.getId() == id);
    }

    public Plato getPlatoByNombre(String nombre) {
        return platos.stream()
                .filter(p -> p.getNombre().equalsIgnoreCase(nombre))
                .findFirst()
                .orElse(null);
    }

    public Plato getPlatoById(int id) {
        return platos.stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElse(null);
    }
}

