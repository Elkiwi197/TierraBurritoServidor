package com.tierraburritoservidor.dao;

import com.tierraburritoservidor.domain.model.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class UsuarioRepository {

    private static List<Usuario> usuarios = List.of();

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void addUsuario(Usuario usuario) {
        int id = 0;
        boolean repetido = true;
        while(repetido){
            id = (int) (Math.random() * 100 + 1);
            int finalId = id;
            if (usuarios.stream().noneMatch(u -> u.getId() == finalId)){
                repetido = false;
            }
        }
        usuario.setId(id);
        usuarios.add(usuario);
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

}
