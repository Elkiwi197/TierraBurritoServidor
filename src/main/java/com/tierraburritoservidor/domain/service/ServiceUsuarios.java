package com.tierraburritoservidor.domain.service;

import com.tierraburritoservidor.dao.RepositoryUsuarios;
import com.tierraburritoservidor.domain.model.Usuario;
import com.tierraburritoservidor.errors.CorreoYaExisteException;
import com.tierraburritoservidor.errors.UsuarioContrasenaIncorrectosException;
import com.tierraburritoservidor.errors.UsuarioNoEncontradoException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ServiceUsuarios {

    private final RepositoryUsuarios repositoryUsuarios;

    public boolean comprobarCredenciales(String nombre, String contrasena) {
        Usuario usuario = repositoryUsuarios.getUsuarioByNombre(nombre);
        if (usuario != null) {
            if (usuario.getContrasena().equals(contrasena)){
                return true;
            } else {
                throw new UsuarioContrasenaIncorrectosException();
            }
        } else {
            throw new UsuarioNoEncontradoException();
        }
    }

    public int addUsuario(Usuario usuario) {
        if (repositoryUsuarios.getUsuarios()
                .stream()
                .noneMatch(u -> u.getCorreo().equals(usuario.getCorreo()))) {
            return repositoryUsuarios.addUsuario(usuario);
        } else {
            throw new CorreoYaExisteException();
        }
    }

    public Usuario getUsuarioByNombre(String nombre) {
        return repositoryUsuarios.getUsuarioByNombre(nombre);
    }
}
