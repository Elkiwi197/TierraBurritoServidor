package com.tierraburritoservidor.dao;

import com.tierraburritoservidor.domain.model.TipoUsuario;
import com.tierraburritoservidor.domain.model.Usuario;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Repository
public class RepositoryUsuarios {

    private List<Usuario> usuarios = new ArrayList<>(Arrays.asList(
            new Usuario(1, "pepe", "pepe", "pepe@correo.es", TipoUsuario.CLIENTE),
            new Usuario(2, "fulanito", "fulanito", "fulanito@correo.es", TipoUsuario.REPARTIDOR)
    ));

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public int addUsuario(Usuario usuario) {
        int id = 0;
        boolean repetido = true;
        while (repetido) {
            id = (int) (Math.random() * 100 + 1);
            int finalId = id;
            if (usuarios.stream().noneMatch(u -> u.getId() == finalId)) {
                repetido = false;
            }
        }
        usuario.setId(id);
        usuarios.add(usuario);
        return id;
    }

    public void updateUsuario(Usuario usuario) {
        Usuario usuarioUpdate = usuarios.stream()
                .filter(u -> u.getId() == usuario.getId())
                .findFirst()
                .orElse(null);
        if (usuarioUpdate != null) {
            usuarioUpdate.setContrasena(usuario.getContrasena());
            usuarioUpdate.setCorreo(usuarioUpdate.getCorreo());
            usuarioUpdate.setNombre(usuarioUpdate.getNombre());
        }
    }

    public void deleteUsuario(int id) {
        usuarios.removeIf(u -> u.getId() == id);
    }

    public Usuario getUsuarioByNombre(String nombre) {
        return usuarios.stream()
                .filter(u -> u.getNombre().equals(nombre))
                .findFirst()
                .orElse(null);
    }
}
