package com.tierraburritoservidor.dao;

import com.tierraburritoservidor.domain.model.EstadoPedido;
import com.tierraburritoservidor.domain.model.Ingredientes;
import com.tierraburritoservidor.domain.model.Pedido;
import com.tierraburritoservidor.domain.model.Plato;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class RepositoryPedidos {


    private List<Pedido> pedidos = List.of(
            new Pedido(1, 1, "Calle falsa 123", "pepe@correo.es", List.of(
                    new Plato(4, "Desnudo", List.of(
                            Ingredientes.CARNITAS, Ingredientes.ARROZ_BLANCO, Ingredientes.MAIZ, Ingredientes.PICO_DE_GALLO, Ingredientes.VERDURAS, Ingredientes.GUACAMOLE
                    ), List.of(), 10.99, "https://www.tierraburritos.com/wp-content/uploads/Desnudo_1-2.jpg"),
                    new Plato(3, "Tacos", List.of(
                            Ingredientes.CARNITAS, Ingredientes.VERDURAS, Ingredientes.QUESO_RALLADO, Ingredientes.PICO_DE_GALLO, Ingredientes.GUACAMOLE, Ingredientes.SALSA_DE_QUESO
                    ), List.of(), 8.99, "https://www.tierraburritos.com/wp-content/uploads/10_Tacos-1.jpg")
            ), List.of(), 19.98, EstadoPedido.CLIENTE_ELIGIENDO));


    public List<Pedido> getPedidos() {
        return pedidos;
    }

    public Pedido addPedido(Pedido pedido) {
        int id = 0;
        boolean repetido = true;
        while (repetido) {
            id = (int) (Math.random() * 100 + 1);
            int finalId = id;
            if (pedidos.stream().noneMatch(p -> p.getId() == finalId)) {
                repetido = false;
            }
        }
        pedido.setId(id);
        pedidos.add(pedido);
        return pedido;
    }

    public void updateEstadoPedido(Pedido pedido) {
        Pedido pedidoUpdate = pedidos.stream()
                .filter(u -> u.getId() == pedido.getId())
                .findFirst()
                .orElse(null);
        if (pedidoUpdate != null) {
            pedidoUpdate.setEstado(pedido.getEstado());
        }
    }

    public void deletePedido(int id) {
        pedidos.removeIf(p -> p.getId() == id);
    }

    public List<Pedido> getPedidosByUsuario(int idUsuario) {
        return pedidos.stream()
                .filter(p -> p.getIdCliente() == idUsuario)
                .toList();
    }

    public Pedido getPedidoById(int id) {
        return pedidos.stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public Pedido getPedidoActual(String correoCliente) {
        return pedidos.stream()
                .filter(p -> p.getCorreoCliente().equals(correoCliente) &&
                        p.getEstado().equals(EstadoPedido.CLIENTE_ELIGIENDO))
                .findFirst()
                .orElse(null);
    }

    public void updatePedido(Pedido pedido) {
        Pedido pedidoUpdate = getPedidoById(pedido.getId());
        pedidoUpdate.setIdCliente(pedido.getIdCliente());
        pedidoUpdate.setDireccion(pedido.getDireccion());
        pedidoUpdate.setCorreoCliente(pedido.getCorreoCliente());
        pedidoUpdate.setOtros(pedido.getOtros());
        pedidoUpdate.setPlatos(pedido.getPlatos());
        pedidoUpdate.setPrecio(pedido.getPrecio());
        pedidoUpdate.setEstado(pedido.getEstado());

    }
}

