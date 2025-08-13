package com.tierraburritoservidor.dao;

import com.tierraburritoservidor.domain.model.Pedido;

import java.util.List;

public interface RepositoryPedidosInterface {

    List<Pedido> getPedidosByCorreo(String correoCliente);

    String addPedido(Pedido pedido);

}
