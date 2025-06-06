package com.tierraburritoservidor.dao;

import com.tierraburritoservidor.domain.model.TipoUsuario;
import com.tierraburritoservidor.domain.model.Usuario;
import com.tierraburritoservidor.errors.exceptions.UsuarioNoActivadoException;
import com.tierraburritoservidor.errors.exceptions.UsuarioNoEncontradoException;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Log4j2
@Repository
public class RepositoryUsuarios {

    private final List<Usuario> usuariosActivados = new ArrayList<>(Arrays.asList(
            new Usuario(1, "pepe", "pepe", "pepe@correo.es", TipoUsuario.CLIENTE, true, "1234"),
            new Usuario(2, "fulanito", "fulanito", "fulanito@correo.es", TipoUsuario.REPARTIDOR, true, "4321"),
            new Usuario(3, "ivan", "ivan", "elkiwi197@gmail.com", TipoUsuario.CLIENTE, true, "4321")
    ));

    private final List<Usuario> usuariosDesactivados = new ArrayList<>(Arrays.asList(
            new Usuario(3, "camello", "camello", "camello@correo.es", TipoUsuario.CLIENTE, false, "123456789"),
            new Usuario(4, "palazon", "palazon", "palazon@correo.es", TipoUsuario.REPARTIDOR, false, "987654321")
    ));

    public List<Usuario> getUsuariosActivados() {
        return usuariosActivados;
    }

    public String crearUsuarioDesactivado(Usuario usuario) {
        int id = 0;
        boolean repetido = true;
        while (repetido) {
            id = (int) (Math.random() * 100 + 1);
            int finalId = id;
            if (usuariosActivados.stream().noneMatch(u -> u.getId() == finalId) &&
                    usuariosDesactivados.stream().noneMatch(u -> u.getId() == finalId)) {
                repetido = false;
            }
        }
        usuario.setId(id);
        usuariosDesactivados.add(usuario);
        log.info("Usuario creado");
        return usuario.getCodigoActivacion();
    }

    public void updateUsuario(Usuario usuario) {
        comprobarActivacionUsuario(usuario.getId());
        Usuario usuarioUpdate = usuariosActivados.stream()
                .filter(u -> u.getId() == usuario.getId())
                .findFirst()
                .orElse(null);
        if (usuarioUpdate != null) {
            usuarioUpdate.setContrasena(usuario.getContrasena());
            usuarioUpdate.setCorreo(usuarioUpdate.getCorreo());
            usuarioUpdate.setNombre(usuarioUpdate.getNombre());
        }
    }


    public Usuario getUsuarioByCorreo(String correo) {
        Usuario usuario =  usuariosActivados.stream()
                .filter(u -> u.getCorreo().equals(correo))
                .findFirst()
                .orElse(null);
        if (usuario == null){
            usuario =  usuariosDesactivados.stream()
                    .filter(u -> u.getCorreo().equals(correo))
                    .findFirst()
                    .orElse(null);
        }
        if (usuario == null){
            throw new UsuarioNoEncontradoException();
        }
        return usuario;
    }

    public void activarUsuario(int id){
        Usuario usuario = usuariosDesactivados.stream()
                .filter(u -> u.getId() == id)
                .findFirst()
                .orElse(null);
        if (usuario != null){
            usuario.setActivado(true);
            usuariosActivados.add(usuario);
            usuariosDesactivados.remove(usuario);
            log.info("Usuario activado");
        } else {
            throw new UsuarioNoEncontradoException();
        }
    }

    private void comprobarActivacionUsuario(int id){
        Usuario usuario = usuariosActivados.stream()
                .filter(u -> u.getId() == id)
                .findFirst()
                .orElse(null);
        if (usuario == null){
            throw  new UsuarioNoActivadoException();
        }
    }

    public Usuario getUsuarioById(int id) {
       Usuario usuario = usuariosActivados.stream()
                .filter(u -> u.getId() == id)
                .findFirst()
                .orElse(null);
       if (usuario == null){
           usuario = usuariosDesactivados.stream()
                   .filter(u -> u.getId() == id)
                   .findFirst()
                   .orElse(null);
       }
        if (usuario == null){
            throw new UsuarioNoEncontradoException();
        }
        return usuario;
    }
}
