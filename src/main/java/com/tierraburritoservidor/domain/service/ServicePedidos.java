package com.tierraburritoservidor.domain.service;

import com.tierraburritoservidor.dao.model.PedidoDB;
import com.tierraburritoservidor.dao.repositories.RepositoryPedidos;
import com.tierraburritoservidor.dao.repositories.RepositoryProductos;
import com.tierraburritoservidor.dao.repositories.RepositoryUsuarios;
import com.tierraburritoservidor.domain.model.Pedido;
import com.tierraburritoservidor.domain.model.Plato;
import com.tierraburritoservidor.domain.model.Producto;
import com.tierraburritoservidor.domain.util.DatabaseUiParser;
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
    private final RepositoryProductos repositoryProductos;
    private final DatabaseUiParser databaseUiParser;
    private final RepositoryUsuarios repositoryUsuarios;


    public List<Pedido> getPedidosByCorreoCliente(String correoCliente) {
        repositoryProductos.getIngredientes();
        List<PedidoDB> pedidosDB = repositoryPedidos.getPedidosByCorreoCliente(correoCliente);
        if (pedidosDB.isEmpty()) {
            throw new PedidosNoEncontradoException();
        }
        List<Pedido> pedidos = new ArrayList<>();
        pedidosDB.forEach(pedidoDB -> {
            Pedido pedido = databaseUiParser.pedidoDBtoPedido(pedidoDB);
            pedido.getPlatos().clear();
            pedidoDB.getPlatos().forEach(platoDB -> {
                Plato plato = databaseUiParser.platoDBtoPlato(platoDB);
                List<Producto> ingredientes = new ArrayList<>();
                platoDB.getIngredientes().forEach(objectId ->
                        ingredientes.add(databaseUiParser.productoDBtoProducto(repositoryProductos.getProductoByObjectId(objectId))));
                plato.setIngredientes(ingredientes);
                List<Producto> extras = new ArrayList<>();
                platoDB.getExtras().forEach(objectId ->
                        extras.add(databaseUiParser.productoDBtoProducto(repositoryProductos.getProductoByObjectId(objectId))));
                plato.setExtras(extras);
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
        repositoryProductos.getIngredientes(); //Para inicializar los IDs de los ingredientes
        PedidoDB pedidoDB = databaseUiParser.pedidoToPedidoDB(pedido);
        return repositoryPedidos.addPedido(pedidoDB);
    }


    public List<Pedido> getPedidosEnPreparacion() {
        List<PedidoDB> pedidosDB = repositoryPedidos.getPedidosEnPreparacion();
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

    public String aceptarPedido(int idPedido, String correoRepartidor) {
        return repositoryPedidos.aceptarPedido(idPedido, correoRepartidor);
    }

    public String cancelarPedido(int idPedido, String correoRepartidor) {
        return repositoryPedidos.cancelarPedido(idPedido, correoRepartidor);
    }

    public String noRepartirEstePedido(int idPedido) {
        return repositoryPedidos.noRepartirEstePedido(idPedido);
    }

    public Pedido getPedidoAceptado(String correoRepartidor) {
        repositoryPedidos.inicializarPedidos();
        repositoryProductos.getIngredientes();
        PedidoDB pedidoDB = repositoryPedidos.getPedidoAceptado(correoRepartidor);
        if (pedidoDB != null) {
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
            pedido.getPlatos().
                    forEach(plato ->
                    {
                        List<Producto> ingredientes = new ArrayList<>();
                        plato.getIngredientes()
                                .forEach(ingrediente -> ingredientes.add(databaseUiParser.productoDBtoProducto(repositoryProductos.getProductoByNombre(ingrediente.getNombre()))));
                        plato.setIngredientes(ingredientes);
                    });
            return pedido;
        } else {
            throw new PedidoNoEncontradoException();
        }
    }

    public List<Pedido> getPedidosRepartidos(String correoRepartidor) {
        repositoryProductos.getIngredientes();
        List<PedidoDB> pedidosDB = repositoryPedidos.getPedidosRepartidos(correoRepartidor);
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

    public String entregarPedido(int idPedido, String correoRepartidor) {
        return repositoryPedidos.entregarPedido(idPedido, correoRepartidor);
    }
}

