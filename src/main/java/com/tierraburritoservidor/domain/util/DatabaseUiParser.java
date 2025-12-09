package com.tierraburritoservidor.domain.util;

import com.tierraburritoservidor.dao.model.IngredienteDB;
import com.tierraburritoservidor.dao.model.PedidoDB;
import com.tierraburritoservidor.dao.model.PlatoDB;
import com.tierraburritoservidor.dao.model.UsuarioDB;
import com.tierraburritoservidor.dao.util.IngredienteIdManager;
import com.tierraburritoservidor.dao.util.PedidoIdManager;
import com.tierraburritoservidor.dao.util.PlatoIdManager;
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
    private final IngredienteIdManager ingredienteIdManager;
    private final PedidoIdManager pedidoIdManager;

    public DatabaseUiParser(UserIdManager userIdManager, PlatoIdManager platoIdManager, IngredienteIdManager ingredienteIdManager, PedidoIdManager pedidoIdManager) {
        this.userIdManager = userIdManager;
        this.platoIdManager = platoIdManager;
        this.ingredienteIdManager = ingredienteIdManager;
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


    public Ingrediente ingredienteDBtoIngrediente(IngredienteDB ingredienteDB) {
        Ingrediente ingrediente = new Ingrediente();
        ingrediente.setNombre(ingredienteDB.getNombre());
        ingrediente.setPrecio(ingredienteDB.getPrecio());
        ingrediente.setRutaFoto(ingredienteDB.getRutafoto());

        if (ingredienteIdManager.getId(ingredienteDB.get_id()) == null) {
            ingredienteIdManager.anadirObjectId(ingredienteDB.get_id());
        }

        ingrediente.setId(ingredienteIdManager.getId(ingredienteDB.get_id()));
        return ingrediente;
    }



    public PedidoDB pedidoToPedidoDB(Pedido pedido) {
        PedidoDB pedidoDB = new PedidoDB();
        pedidoDB.setCorreoCliente(pedido.getCorreoCliente());
        pedidoDB.setDireccion(pedido.getDireccion());
        pedidoDB.setEstado(pedido.getEstado().toString());
        pedidoDB.setPrecio(Double.parseDouble(String.format("%.2f", pedido.getPrecio()).replace(",", ".")));
        pedidoDB.setHoraLlegada(pedido.getHoraLlegada());

        if (!pedido.getCorreoRepartidor().isEmpty()) {
            pedidoDB.setRepartidor(pedido.getCorreoRepartidor());
        }

        List<PlatoDB> platosDB = new ArrayList<>();
        pedido.getPlatos().forEach(plato -> {
            PlatoDB platoDB = platoPedidoToPlatoDBPedido(plato);
            platosDB.add(platoDB);
            if (pedidoIdManager.getPlatoId(platoDB.get_id()) == null) {
                pedidoIdManager.anadirPlatoObjectId(platoDB.get_id());
            }
        });
        pedidoDB.setPlatos(platosDB);

        pedidoDB.set_id(pedidoIdManager.createNewPedidoId());

        return pedidoDB;
    }

    public Pedido pedidoDBtoPedido(PedidoDB pedidoDB) {
        Pedido pedido = new Pedido();
        List<Plato> platos = new ArrayList<>();

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

        switch (EstadoPedido.valueOf(pedidoDB.getEstado())) {
            case CLIENTE_ELIGIENDO -> pedido.setEstado(EstadoPedido.CLIENTE_ELIGIENDO);
            case EN_PREPARACION -> pedido.setEstado(EstadoPedido.EN_PREPARACION);
            case EN_REPARTO -> pedido.setEstado(EstadoPedido.EN_REPARTO);
            case CANCELADO -> pedido.setEstado(EstadoPedido.CANCELADO);
            case ENTREGADO -> pedido.setEstado(EstadoPedido.ENTREGADO);
        }

        pedido.setId(pedidoIdManager.getPedidoId(pedidoDB.get_id()));

        return pedido;
    }

    private PlatoDB platoPedidoToPlatoDBPedido(Plato plato) {
        PlatoDB platoDB = new PlatoDB();
        platoDB.setNombre(plato.getNombre());
        platoDB.setRutaFoto(plato.getRutaFoto());
        platoDB.setPrecio(plato.getPrecio());

        List<ObjectId> ingredientes = new ArrayList<>();
        List<ObjectId> extras = new ArrayList<>();
        plato.getIngredientes().forEach(i -> ingredientes.add(ingredienteIdManager.getObjectId(i.getId())));
        plato.getExtras().forEach(e -> extras.add(ingredienteIdManager.getObjectId(e.getId())));
        platoDB.setIngredientes(ingredientes);
        platoDB.setExtras(extras);
        platoDB.set_id(platoIdManager.createNewId());
        return platoDB;
    }

}
