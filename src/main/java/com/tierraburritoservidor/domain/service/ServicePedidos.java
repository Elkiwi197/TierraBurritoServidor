package com.tierraburritoservidor.domain.service;

import com.tierraburritoservidor.dao.model.PedidoDB;
import com.tierraburritoservidor.dao.repositories.RepositoryPedidos;
import com.tierraburritoservidor.dao.repositories.RepositoryProductos;
import com.tierraburritoservidor.domain.model.Pedido;
import com.tierraburritoservidor.domain.model.Plato;
import com.tierraburritoservidor.domain.model.Producto;
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
    private final RepositoryProductos repositoryProductos;
    private final DatabaseUiParser databaseUiParser;


    public List<Pedido> getPedidosByCorreo(String correoCliente) {
        List<PedidoDB> pedidosDB = repositoryPedidos.getPedidosByCorreo(correoCliente);
        if (pedidosDB.isEmpty()) {
            throw new PedidosNoEncontradoException();
        }
        List<Pedido> pedidos = new ArrayList<>();
        pedidosDB.forEach(pedidoDB -> {
            Pedido pedido = databaseUiParser.pedidoDBtoPedido(pedidoDB);
            pedido.getPlatos().clear();
            pedidoDB.getPlatos().forEach(platoDB -> {
                Plato plato = new Plato();
                List<Producto> ingredientes = new ArrayList<>();
                platoDB.getIngredientes().forEach(objectId ->
                        ingredientes.add(databaseUiParser.productoDBtoProducto(repositoryProductos.getProductoByObjectId(objectId))));
                plato = databaseUiParser.platoDBtoPlato(platoDB);
                plato.setIngredientes(ingredientes);
                pedido.getPlatos().add(plato);
            });
            pedidos.add(pedido);
        });
        pedidos.forEach(pedido -> pedido.getPlatos()
                .forEach(plato -> {
                    List<Producto> ingredientes = new ArrayList<>();
                    plato.getIngredientes()
                            .forEach(ingrediente -> ingredientes.add(databaseUiParser.productoDBtoProducto(repositoryProductos.getProductoByNombre(ingrediente.getNombre()))));
                    plato.setIngredientes(ingredientes);
                }));
        return pedidos;
    }

    public String addPedido(Pedido pedido) {
        PedidoDB pedidoDB = databaseUiParser.pedidoToPedidoDB(pedido);
        return repositoryPedidos.addPedido(pedidoDB);
    }


}
