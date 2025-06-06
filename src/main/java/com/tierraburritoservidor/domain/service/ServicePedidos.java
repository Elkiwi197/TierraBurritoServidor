package com.tierraburritoservidor.domain.service;

import com.tierraburritoservidor.dao.RepositoryPedidos;
import com.tierraburritoservidor.domain.model.Pedido;
import com.tierraburritoservidor.domain.model.Plato;
import com.tierraburritoservidor.errors.exceptions.PedidoNoEncontradoException;
import com.tierraburritoservidor.errors.exceptions.PedidosNoEncontradoException;
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


    public List<Pedido> getPedidosByCorreo(String correoCliente) {
        List<Pedido> pedidos = repositoryPedidos.getPedidosByCorreo(correoCliente);
        if (pedidos.isEmpty()) {
            throw new PedidosNoEncontradoException();
        }
        return pedidos;
    }

    public String addPedido(Pedido pedido) {
        return repositoryPedidos.addPedido(pedido);
    }


}
