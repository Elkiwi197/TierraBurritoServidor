package com.tierraburritoservidor.domain.service;

import com.tierraburritoservidor.dao.RepositoryPedidos;
import com.tierraburritoservidor.domain.model.Pedido;
import com.tierraburritoservidor.domain.model.Plato;
import com.tierraburritoservidor.errors.exceptions.PedidoNoEncontradoException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ServicePedidos {

    private final RepositoryPedidos repositoryPedidos;


    public Pedido getPedidoById(int id) {
        Pedido pedido = repositoryPedidos.getPedidoById(id);
        if (pedido == null) {
            throw new PedidoNoEncontradoException();
        }
        return pedido;
    }

    public List<Pedido> getPedidoByUsuarioId(int idUsuario) {
        return repositoryPedidos.getPedidosByUsuario(idUsuario);
    }

    public Pedido getPedidoActual(String correoCliente) {
        Pedido pedido = repositoryPedidos.getPedidoActual(correoCliente);
        if (pedido == null) {
            throw new PedidoNoEncontradoException();
        }
        return pedido;
    }

    public Pedido addPedido(Pedido pedido) {
        return repositoryPedidos.addPedido(pedido);
    }

    public void addPlato(Plato plato, String correoCliente) {
        Pedido pedido;
        pedido = repositoryPedidos.getPedidoActual(correoCliente);
        if (pedido == null) {
            throw new PedidoNoEncontradoException();
        }
        List<Plato> platos = new ArrayList<>(pedido.getPlatos());
        platos.add(plato);
        pedido.setPlatos(platos);
        repositoryPedidos.updatePedido(pedido);
    }
}
