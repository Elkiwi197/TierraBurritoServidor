package com.tierraburritoservidor.domain.service;

import com.tierraburritoservidor.dao.RepositoryUsuarios;
import com.tierraburritoservidor.domain.model.Usuario;
import com.tierraburritoservidor.errors.exceptions.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class ServiceUsuarios {

    private final RepositoryUsuarios repositoryUsuarios;

    public void comprobarCredenciales(String correo, String contrasena) {
        Usuario usuario = repositoryUsuarios.getUsuarioByCorreo(correo);
        if (!usuario.isActivado()){
            throw new UsuarioNoActivadoException();
        }
        if (!usuario.getContrasena().equals(contrasena)) {
            throw new UsuarioContrasenaIncorrectosException();
        }
    }

    public String crearUsuarioDesactivado(Usuario usuario) {
        byte[] salt = new byte[16];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(salt);
        String codigo = Base64.getUrlEncoder().encodeToString(salt);
        if (repositoryUsuarios.getUsuariosActivados()
                .stream()
                .noneMatch(u -> u.getCorreo().equals(usuario.getCorreo()))) {
            usuario.setActivado(false);
            usuario.setCodigoActivacion(codigo);
            return repositoryUsuarios.crearUsuarioDesactivado(usuario);
        } else {
            throw new CorreoYaExisteException();
        }
    }


    public void activarUsuario(int id, String codigo) {
        Usuario usuario = repositoryUsuarios.getUsuarioById(id);
        if (!usuario.isActivado()) {
            if (usuario.getCodigoActivacion().equals(codigo)) {
                repositoryUsuarios.activarUsuario(id);
            } else {
                throw new CodigoActivacionIncorrectoException();
            }
        } else {
            throw new UsuarioYaActivadoException();
        }

    }

    public Usuario getUsuarioByCorreo(String correo) {
        return repositoryUsuarios.getUsuarioByCorreo(correo);
    }
}
