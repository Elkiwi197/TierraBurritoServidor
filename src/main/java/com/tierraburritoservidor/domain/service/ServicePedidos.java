package com.tierraburritoservidor.domain.service;

import com.tierraburritoservidor.dao.model.PedidoDB;
import com.tierraburritoservidor.dao.repositories.RepositoryPedidos;
import com.tierraburritoservidor.dao.util.DocumentPojoParser;
import com.tierraburritoservidor.domain.model.Pedido;
import com.tierraburritoservidor.domain.util.DatabaseUiParser;
import com.tierraburritoservidor.errors.exceptions.PedidosNoEncontradoException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ServicePedidos {

    private final RepositoryPedidos repositoryPedidos;
    private final DatabaseUiParser databaseUiParser;


    public List<Pedido> getPedidosByCorreo(String correoCliente) {
        List<PedidoDB> pedidosDB = repositoryPedidos.getPedidosByCorreo(correoCliente);
        if (pedidosDB.isEmpty()) {
            throw new PedidosNoEncontradoException();
        }
        List<Pedido> pedidos = new ArrayList<>();
        pedidosDB.forEach(pedidoDB -> pedidos.add(databaseUiParser.pedidoDBtoPedido(pedidoDB)));
        return pedidos;
    }

    public String addPedido(Pedido pedido) {
        PedidoDB pedidoDB = databaseUiParser.pedidoToPedidoDB(pedido);
        return repositoryPedidos.addPedido(pedidoDB);
    }


}
