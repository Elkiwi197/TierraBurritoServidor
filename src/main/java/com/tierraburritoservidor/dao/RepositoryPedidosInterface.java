package com.tierraburritoservidor.dao;

import com.tierraburritoservidor.dao.model.PedidoDB;

import java.util.List;

public interface RepositoryPedidosInterface {
    void inicializarPedidos();

    List<PedidoDB> getPedidosByCorreoCliente(String correoCliente);

    String addPedido(PedidoDB pedido);

    List<PedidoDB> getPedidosEnPreparacion();

    String repartirPedido(int idPedido, String correoRepartidor);

    String cancelarPedido(int idPedido, String correoRepartidor);

    PedidoDB getPedidoEnRepartoByRepartidor(String correoRepartidor);

    List<PedidoDB> getPedidosRepartidos(String correoRepartidor);

    String entregarPedido(int idPedido, String correoRepartidor);

    String noRepartirEstePedido(int idPedido);
}
