package com.tierraburritoservidor.domain.service;

import com.tierraburritoservidor.dao.model.PedidoDB;
import com.tierraburritoservidor.dao.repositories.RepositoryPedidos;
import com.tierraburritoservidor.dao.repositories.RepositoryIngredientes;
import com.tierraburritoservidor.dao.repositories.RepositoryUsuarios;
import com.tierraburritoservidor.domain.model.Ingrediente;
import com.tierraburritoservidor.domain.model.Pedido;
import com.tierraburritoservidor.domain.model.Plato;
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
    private final RepositoryIngredientes repositoryIngredientes;
    private final DatabaseUiParser databaseUiParser;
    private final RepositoryUsuarios repositoryUsuarios;


    public List<Pedido> getPedidosByCorreoCliente(String correoCliente) {
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
                List<Ingrediente> ingredientes = new ArrayList<>();
                platoDB.getIngredientes().forEach(objectId ->
                        ingredientes.add(databaseUiParser.ingredienteDBtoIngrediente(repositoryIngredientes.getIngredienteByObjectId(objectId))));
                plato.setIngredientes(ingredientes);
                List<Ingrediente> extras = new ArrayList<>();
                platoDB.getExtras().forEach(objectId ->
                        extras.add(databaseUiParser.ingredienteDBtoIngrediente(repositoryIngredientes.getIngredienteByObjectId(objectId))));
                plato.setExtras(extras);
                pedido.getPlatos().add(plato);
            });
            pedidos.add(pedido);
        });
        pedidos.forEach(pedido -> pedido.getPlatos()
                .forEach(plato -> {
                    List<Ingrediente> ingredientes = new ArrayList<>();
                    plato.getIngredientes()
                            .forEach(ingrediente -> ingredientes.add(databaseUiParser.ingredienteDBtoIngrediente(repositoryIngredientes.getIngredienteByNombre(ingrediente.getNombre()))));
                    plato.setIngredientes(ingredientes);
                }));
        return pedidos;
    }

    public String addPedido(Pedido pedido) {
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
                List<Ingrediente> ingredientes = new ArrayList<>();
                List<Ingrediente> extras = new ArrayList<>();
                platoDB.getIngredientes().forEach(objectId ->
                        ingredientes.add(databaseUiParser.ingredienteDBtoIngrediente(repositoryIngredientes.getIngredienteByObjectId(objectId))));
                platoDB.getExtras().forEach(objectId ->
                        extras.add(databaseUiParser.ingredienteDBtoIngrediente(repositoryIngredientes.getIngredienteByObjectId(objectId))));
                plato = databaseUiParser.platoDBtoPlato(platoDB);
                plato.setIngredientes(ingredientes);
                plato.setExtras(extras);
                pedido.getPlatos().add(plato);
            });
            pedidos.add(pedido);
        });
        pedidos.forEach(pedido -> pedido.getPlatos()
                .forEach(plato -> {
                    List<Ingrediente> ingredientes = new ArrayList<>();
                    List<Ingrediente> extras = new ArrayList<>();
                    plato.getIngredientes()
                            .forEach(ingrediente -> ingredientes.add(databaseUiParser.ingredienteDBtoIngrediente(repositoryIngredientes.getIngredienteByNombre(ingrediente.getNombre()))));
                    plato.setIngredientes(ingredientes);
                    plato.getExtras()
                            .forEach(extra -> extras.add(databaseUiParser.ingredienteDBtoIngrediente(repositoryIngredientes.getIngredienteByNombre(extra.getNombre()))));
                    plato.setExtras(extras);
                }));
        return pedidos;
    }

    public String repartirPedido(int idPedido, String correoRepartidor) {
        return repositoryPedidos.repartirPedido(idPedido, correoRepartidor);
    }

    public String cancelarPedido(int idPedido, String correoRepartidor) {
        return repositoryPedidos.cancelarPedido(idPedido, correoRepartidor);
    }

    public String noRepartirEstePedido(int idPedido) {
        return repositoryPedidos.noRepartirEstePedido(idPedido);
    }

    public Pedido getPedidoEnRepartoByRepartidor(String correoRepartidor) {
        PedidoDB pedidoDB = repositoryPedidos.getPedidoEnRepartoByRepartidor(correoRepartidor);
        if (pedidoDB != null) {
            Pedido pedido = databaseUiParser.pedidoDBtoPedido(pedidoDB);
            pedido.getPlatos().clear();
            pedidoDB.getPlatos().forEach(platoDB -> {
                Plato plato = new Plato();
                List<Ingrediente> ingredientes = new ArrayList<>();
                platoDB.getIngredientes().forEach(objectId ->
                        ingredientes.add(databaseUiParser.ingredienteDBtoIngrediente(repositoryIngredientes.getIngredienteByObjectId(objectId))));
                plato = databaseUiParser.platoDBtoPlato(platoDB);
                plato.setIngredientes(ingredientes);
                pedido.getPlatos().add(plato);
            });
            pedido.getPlatos().
                    forEach(plato ->
                    {
                        List<Ingrediente> ingredientes = new ArrayList<>();
                        plato.getIngredientes()
                                .forEach(ingrediente -> ingredientes.add(databaseUiParser.ingredienteDBtoIngrediente(repositoryIngredientes.getIngredienteByNombre(ingrediente.getNombre()))));
                        plato.setIngredientes(ingredientes);
                    });
            return pedido;
        } else {
            throw new PedidoNoEncontradoException();
        }
    }

    public List<Pedido> getPedidosRepartidos(String correoRepartidor) {
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
                List<Ingrediente> ingredientes = new ArrayList<>();
                platoDB.getIngredientes().forEach(objectId ->
                        ingredientes.add(databaseUiParser.ingredienteDBtoIngrediente(repositoryIngredientes.getIngredienteByObjectId(objectId))));
                plato = databaseUiParser.platoDBtoPlato(platoDB);
                plato.setIngredientes(ingredientes);
                pedido.getPlatos().add(plato);
            });
            pedidos.add(pedido);
        });
        pedidos.forEach(pedido -> pedido.getPlatos()
                .forEach(plato -> {
                    List<Ingrediente> ingredientes = new ArrayList<>();
                    plato.getIngredientes()
                            .forEach(ingrediente -> ingredientes.add(databaseUiParser.ingredienteDBtoIngrediente(repositoryIngredientes.getIngredienteByNombre(ingrediente.getNombre()))));
                    plato.setIngredientes(ingredientes);
                }));
        return pedidos;
    }

    public String entregarPedido(int idPedido, String correoRepartidor) {
        return repositoryPedidos.entregarPedido(idPedido, correoRepartidor);
    }
}

