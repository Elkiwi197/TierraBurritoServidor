package com.tierraburritoservidor.domain.util;

import com.tierraburritoservidor.dao.model.PedidoDB;
import com.tierraburritoservidor.dao.model.PlatoDB;
import com.tierraburritoservidor.dao.model.ProductoDB;
import com.tierraburritoservidor.dao.model.UsuarioDB;
import com.tierraburritoservidor.dao.util.PedidoIdManager;
import com.tierraburritoservidor.dao.util.PlatoIdManager;
import com.tierraburritoservidor.dao.util.ProductoIdManager;
import com.tierraburritoservidor.dao.util.UserIdManager;
import com.tierraburritoservidor.domain.model.*;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DatabaseUiParser {

    private final UserIdManager userIdManager;
    private final PlatoIdManager platoIdManager;
    private final ProductoIdManager productoIdManager;
    private final PedidoIdManager pedidoIdManager;

    public DatabaseUiParser(UserIdManager userIdManager, PlatoIdManager platoIdManager, ProductoIdManager productoIdManager, PedidoIdManager pedidoIdManager) {
        this.userIdManager = userIdManager;
        this.platoIdManager = platoIdManager;
        this.productoIdManager = productoIdManager;
        this.pedidoIdManager = pedidoIdManager;
    }

    public Usuario usuarioDbToUsuario(UsuarioDB usuarioDB) {
        Usuario usuario = new Usuario();
        usuario.setCodigoActivacion(usuarioDB.getCodigoActivacion());
        usuario.setNombre(usuarioDB.getNombre());
        usuario.setActivado(usuarioDB.isActivado());
        usuario.setContrasena(usuarioDB.getContrasena());
        usuario.setCorreo(usuarioDB.getCorreo());

        usuario.setId(userIdManager.getId(usuarioDB.get_id()));

        if (usuarioDB.getTipoUsuario().equalsIgnoreCase(TipoUsuario.CLIENTE.toString())) {
            usuario.setTipoUsuario(TipoUsuario.CLIENTE);
        } else if (usuarioDB.getTipoUsuario().equalsIgnoreCase(TipoUsuario.REPARTIDOR.toString())) {
            usuario.setTipoUsuario(TipoUsuario.REPARTIDOR);
        }

        return usuario;
    }

    public UsuarioDB usuarioToUsuarioDb(Usuario usuario) {
        UsuarioDB usuarioDB = new UsuarioDB();

        usuarioDB.setCodigoActivacion(usuario.getCodigoActivacion());
        usuarioDB.setNombre(usuario.getNombre());
        usuarioDB.setActivado(usuario.isActivado());
        usuarioDB.setContrasena(usuario.getContrasena());
        usuarioDB.setCorreo(usuario.getCorreo());

        if (usuario.getTipoUsuario().equals(TipoUsuario.CLIENTE)) {
            usuarioDB.setTipoUsuario(TipoUsuario.CLIENTE.toString());
        } else if (usuario.getTipoUsuario().equals(TipoUsuario.REPARTIDOR)) {
            usuarioDB.setTipoUsuario(TipoUsuario.REPARTIDOR.toString());
        }

        return usuarioDB;
    }


    public Plato platoDBtoPlato(PlatoDB platoDB) {
        Plato plato = new Plato();
        plato.setNombre(platoDB.getNombre());
        plato.setPrecio(platoDB.getPrecio());
        plato.setRutaFoto(platoDB.getRutaFoto());
        plato.setIngredientes(List.of());
        plato.setExtras(List.of());

        if (platoIdManager.getId(platoDB.get_id()) != null) {
            plato.setId(platoIdManager.getId(platoDB.get_id()));
        } else {
            plato.setId(pedidoIdManager.getPlatoId(platoDB.get_id()));
        }
        return plato;
    }



    public Producto productoDBtoProducto(ProductoDB productoDB) {
        Producto producto = new Producto();
        producto.setNombre(productoDB.getNombre());
        producto.setPrecio(productoDB.getPrecio());
        producto.setRutaFoto(productoDB.getRutafoto());

        if (productoIdManager.getId(productoDB.get_id()) == null) {
            productoIdManager.anadirObjectId(productoDB.get_id());
        }

        producto.setId(productoIdManager.getId(productoDB.get_id()));
        return producto;
    }

    public PedidoDB pedidoToPedidoDB(Pedido pedido) {
        PedidoDB pedidoDB = new PedidoDB();
        pedidoDB.setCorreoCliente(pedido.getCorreoCliente());
        pedidoDB.setRepartidor(pedido.getCorreoRepartidor());
        pedidoDB.setDireccion(pedido.getDireccion());
        pedidoDB.setEstado(pedido.getEstado().toString());
        pedidoDB.setPrecio(Double.parseDouble(String.format("%.2f", pedido.getPrecio()).replace(",", ".")));
        pedidoDB.setHoraLlegada(pedido.getHoraLlegada());

        List<PlatoDB> platosDB = new ArrayList<>();
        pedido.getPlatos().forEach(plato -> {
            PlatoDB platoDB = platoPedidoToPlatoDBPedido(plato);
            platosDB.add(platoDB);
            if (pedidoIdManager.getPlatoId(platoDB.get_id()) == null) {
                pedidoIdManager.anadirPlatoObjectId(platoDB.get_id());
            }
        });
        pedidoDB.setPlatos(platosDB);

        List<ProductoDB> productosDB = new ArrayList<>();
        pedido.getOtros().forEach(producto -> {
            ProductoDB productoDB = productoPedidoToProductoDBPedido(producto);
            productosDB.add(productoDB);
        });
        pedidoDB.setOtros(productosDB);

        pedidoDB.set_id(pedidoIdManager.createNewPedidoId());

        return pedidoDB;
    }

    public Pedido pedidoDBtoPedido(PedidoDB pedidoDB) {
        Pedido pedido = new Pedido();
        List<Plato> platos = new ArrayList<>();
        List<Producto> otros = new ArrayList<>();

        pedido.setCorreoCliente(pedidoDB.getCorreoCliente());
        pedido.setCorreoRepartidor(pedidoDB.getRepartidor());
        pedido.setDireccion(pedidoDB.getDireccion());
        pedido.setPrecio(Double.parseDouble(String.format("%.2f", pedidoDB.getPrecio()).replace(",", ".")));
        pedido.setHoraLlegada(pedidoDB.getHoraLlegada());

        pedidoDB.getPlatos().forEach(platoDB -> {
            if (pedidoIdManager.getPlatoId(platoDB.get_id()) == null) {
                pedidoIdManager.anadirPlatoObjectId(platoDB.get_id());
            }
            platos.add(platoDBtoPlato(platoDB));
        });
        pedido.setPlatos(platos);
        pedidoDB.getOtros().forEach(productoDB -> otros.add(productoDBtoProducto(productoDB)));
        pedido.setOtros(otros);

        switch (EstadoPedido.valueOf(pedidoDB.getEstado())) {
            case CLIENTE_ELIGIENDO -> pedido.setEstado(EstadoPedido.CLIENTE_ELIGIENDO);
            case EN_PREPARACION -> pedido.setEstado(EstadoPedido.EN_PREPARACION);
            case ACEPTADO -> pedido.setEstado(EstadoPedido.ACEPTADO);
            case EN_REPARTO -> pedido.setEstado(EstadoPedido.EN_REPARTO);
            case CANCELADO -> pedido.setEstado(EstadoPedido.CANCELADO);
            case ENTREGADO -> pedido.setEstado(EstadoPedido.ENTREGADO);
        }

        pedido.setId(pedidoIdManager.getPedidoId(pedidoDB.get_id()));

        return pedido;
    }

    private ProductoDB productoPedidoToProductoDBPedido(Producto producto) {
        ProductoDB productoDB = new ProductoDB();

        productoDB.setNombre(producto.getNombre());
        productoDB.setRutafoto(producto.getRutaFoto());
        productoDB.setPrecio(producto.getPrecio());
        productoDB.set_id(productoIdManager.getObjectId(producto.getId()));
        return productoDB;
    }

    private PlatoDB platoPedidoToPlatoDBPedido(Plato plato) {
        PlatoDB platoDB = new PlatoDB();
        platoDB.setNombre(plato.getNombre());
        platoDB.setRutaFoto(plato.getRutaFoto());
        platoDB.setPrecio(plato.getPrecio());

        List<ObjectId> ingredientes = new ArrayList<>();
        List<ObjectId> extras = new ArrayList<>();
        plato.getIngredientes().forEach(i -> ingredientes.add(productoIdManager.getObjectId(i.getId())));
        plato.getExtras().forEach(e -> extras.add(productoIdManager.getObjectId(e.getId())));
        platoDB.setIngredientes(ingredientes);
        platoDB.setExtras(extras);
        platoDB.set_id(platoIdManager.createNewId());
        return platoDB;
    }

}
