package com.tierraburritoservidor.domain.util;

import com.tierraburritoservidor.dao.model.UsuarioDB;
import com.tierraburritoservidor.dao.util.UserIdManager;
import com.tierraburritoservidor.domain.model.TipoUsuario;
import com.tierraburritoservidor.domain.model.Usuario;
import org.springframework.stereotype.Component;

@Component
public class DatabaseUiParser {

    private final UserIdManager userIdManager;

    public DatabaseUiParser(UserIdManager userIdManager) {
        this.userIdManager = userIdManager;
    }

    public Usuario usuarioDbToUsuario(UsuarioDB usuarioDB){
        Usuario usuario = new Usuario();
        usuario.setCodigoActivacion(usuarioDB.getCodigoActivacion());
        usuario.setNombre(usuarioDB.getNombre());
        usuario.setActivado(usuarioDB.isActivado());
        usuario.setContrasena(usuarioDB.getContrasena());
        usuario.setCorreo(usuarioDB.getCorreo());

        usuario.setId(userIdManager.getId(usuarioDB.get_id()));
        if (usuarioDB.getTipoUsuario().equals(TipoUsuario.CLIENTE)){
            usuario.setTipoUsuario(TipoUsuario.CLIENTE);
        } else if (usuarioDB.getTipoUsuario().equals(TipoUsuario.REPARTIDOR)){
            usuario.setTipoUsuario(TipoUsuario.REPARTIDOR);
        }

        return usuario;
    }

    public UsuarioDB usuarioToUsuarioDb(Usuario usuario){
        UsuarioDB usuarioDB = new UsuarioDB();

        usuarioDB.setCodigoActivacion(usuario.getCodigoActivacion());
        usuarioDB.setNombre(usuario.getNombre());
        usuarioDB.setActivado(usuario.isActivado());
        usuarioDB.setContrasena(usuario.getContrasena());
        usuarioDB.setCorreo(usuario.getCorreo());

        usuarioDB.set_id(userIdManager.createNewId());
        if (usuario.getTipoUsuario().equals(TipoUsuario.CLIENTE)){
            usuarioDB.setTipoUsuario(TipoUsuario.CLIENTE.toString());
        } else if (usuario.getTipoUsuario().equals(TipoUsuario.REPARTIDOR)){
            usuarioDB.setTipoUsuario(TipoUsuario.REPARTIDOR.toString());
        }

        return usuarioDB;
    }


}
