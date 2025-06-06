package com.tierraburritoservidor.dao;

import com.tierraburritoservidor.common.Constantes;
import com.tierraburritoservidor.domain.model.*;
import com.tierraburritoservidor.errors.exceptions.PedidoNoEncontradoException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class RepositoryPedidos {


    private List<Pedido> pedidos = new ArrayList<>(Arrays.asList(
            new Pedido(1, "Calle falsa 123", "pepe@correo.es", List.of(
                    new Plato(4, "Desnudo", List.of(
                            new Producto(4, Ingredientes.CARNITAS.name(), 3.60, "https://www.tierraburritos.com/wp-content/uploads/Carnitas-1-1140x1050.jpg"),
                            new Producto(1, Ingredientes.ARROZ_BLANCO.name(), 0.0, "https://www.tierraburritos.com/wp-content/uploads/ArrozBlanco-1140x1050.png"),
                            new Producto(10, Ingredientes.MAIZ.name(), 0.0, "https://www.tierraburritos.com/wp-content/uploads/Maiz-1-1140x1050.jpg"),
                            new Producto(20, Ingredientes.VERDURAS.name(), 0.0, "https://www.tierraburritos.com/wp-content/uploads/Verduras-1140x1050.png")
                    ), List.of(), 10.99, "https://www.tierraburritos.com/wp-content/uploads/Desnudo_1-2.jpg"),
                    new Plato(3, "Tacos", List.of(
                            new Producto(4, Ingredientes.CARNITAS.name(), 3.60, "https://www.tierraburritos.com/wp-content/uploads/Carnitas-1-1140x1050.jpg"),
                            new Producto(20, Ingredientes.VERDURAS.name(), 0.0, "https://www.tierraburritos.com/wp-content/uploads/Verduras-1140x1050.png"),
                            new Producto(13, Ingredientes.QUESO_RALLADO.name(), 0.0, "https://www.tierraburritos.com/wp-content/uploads/Queso-1140x1050.jpg"),
                            new Producto(11, Ingredientes.PICO_DE_GALLO.name(), 0.0, "https://www.tierraburritos.com/wp-content/uploads/Picodegallo-1140x1050.jpg"),
                            new Producto(7, Ingredientes.GUACAMOLE.name(), 2.50, "https://www.tierraburritos.com/wp-content/uploads/Guacamole-1140x1050.png"),
                            new Producto(19, Ingredientes.SALSA_DE_QUESO.name(), 0.0, "https://www.tierraburritos.com/wp-content/uploads/SalsaQueso-1140x1050.jpg")), List.of(), 8.99, "https://www.tierraburritos.com/wp-content/uploads/10_Tacos-1.jpg")
            ), List.of(), 19.98, EstadoPedido.ENTREGADO)));



    public String addPedido(Pedido pedido) {
        int id = 0;
        boolean repetido = true;
        while (repetido) {
            id = (int) (Math.random() * 100 + 1);
            int finalId = id;
            if (pedidos.stream().noneMatch(p -> p.getId() == finalId)) {
                repetido = false;
            }
        }
        pedido.setEstado(EstadoPedido.EN_PREPARACION);
        pedido.setId(id);
        pedidos.add(pedido);
        return Constantes.PEDIDO_HECHO;
    }




    public Pedido getPedidoById(int id) {
        return pedidos.stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElse(null);
    }



    public List<Pedido> getPedidosByCorreo(String correoCliente) {
        return pedidos.stream()
                .filter(p -> p.getCorreoCliente().equals(correoCliente))
                .toList();
    }
}

