package com.tierraburritoservidor.dao;

import com.tierraburritoservidor.dao.model.PedidoDB;

import java.util.List;

public interface RepositoryPedidosInterface {

    List<PedidoDB> getPedidosByCorreoCliente(String correoCliente);

    String addPedido(PedidoDB pedido);

    List<PedidoDB> getPedidosEnPreparacion();

    String aceptarPedido(int idPedido, String correoRepartidor);

    String cancelarPedido(int idPedido, String correo);

    PedidoDB getPedidoAceptado(String correoRepartidor);
}
