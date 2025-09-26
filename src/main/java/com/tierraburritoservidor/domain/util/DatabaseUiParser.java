package com.tierraburritoservidor.domain.util;

import com.tierraburritoservidor.dao.model.PlatoDB;
import com.tierraburritoservidor.dao.model.ProductoDB;
import com.tierraburritoservidor.dao.model.UsuarioDB;
import com.tierraburritoservidor.dao.util.PlatoIdManager;
import com.tierraburritoservidor.dao.util.ProductoIdManager;
import com.tierraburritoservidor.dao.util.UserIdManager;
import com.tierraburritoservidor.domain.model.Plato;
import com.tierraburritoservidor.domain.model.Producto;
import com.tierraburritoservidor.domain.model.TipoUsuario;
import com.tierraburritoservidor.domain.model.Usuario;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DatabaseUiParser {

    private final UserIdManager userIdManager;
    private final PlatoIdManager platoIdManager;
    private final ProductoIdManager productoIdManager;

    public DatabaseUiParser(UserIdManager userIdManager, PlatoIdManager platoIdManager, ProductoIdManager productoIdManager) {
        this.userIdManager = userIdManager;
        this.platoIdManager = platoIdManager;
        this.productoIdManager = productoIdManager;
    }

    public Usuario usuarioDbToUsuario(UsuarioDB usuarioDB) {
        Usuario usuario = new Usuario();
        usuario.setCodigoActivacion(usuarioDB.getCodigoActivacion());
        usuario.setNombre(usuarioDB.getNombre());
        usuario.setActivado(usuarioDB.isActivado());
        usuario.setContrasena(usuarioDB.getContrasena());
        usuario.setCorreo(usuarioDB.getCorreo());

        usuario.setId(userIdManager.getId(usuarioDB.get_id()));
        if (usuarioDB.getTipoUsuario().equals(TipoUsuario.CLIENTE)) {
            usuario.setTipoUsuario(TipoUsuario.CLIENTE);
        } else if (usuarioDB.getTipoUsuario().equals(TipoUsuario.REPARTIDOR)) {
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

        //usuarioDB.set_id(userIdManager.createNewId());
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

        plato.setId(platoIdManager.getId(platoDB.get_id()));
        return plato;
        //todo si falla el de userdb to user este falla tambien
    }

    public Producto productoDBtoProducto(ProductoDB productoDB) {
        Producto producto = new Producto();
        producto.setNombre(productoDB.getNombre());
        producto.setPrecio(productoDB.getPrecio());
        producto.setRutaFoto(productoDB.getRutafoto());

        producto.setId(productoIdManager.getId(productoDB.get_id()));
        return producto;
        //todo si falla el de userdb to user este falla tambien
    }
}
