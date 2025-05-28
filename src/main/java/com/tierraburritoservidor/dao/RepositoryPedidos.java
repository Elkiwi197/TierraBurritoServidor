package com.tierraburritoservidor.dao;

import com.tierraburritoservidor.domain.model.Pedido;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class RepositoryPedidos {


    private List<Pedido> pedidos = List.of();

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
}

