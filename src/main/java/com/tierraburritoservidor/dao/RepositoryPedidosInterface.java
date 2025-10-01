package com.tierraburritoservidor.dao;

import com.tierraburritoservidor.dao.model.PedidoDB;
import com.tierraburritoservidor.domain.model.Pedido;

import java.util.List;

public interface RepositoryPedidosInterface {

    List<PedidoDB> getPedidosByCorreo(String correoCliente);

    String addPedido(PedidoDB pedido);

}
