package com.tierraburritoservidor.domain.service;

import com.tierraburritoservidor.dao.model.UsuarioDB;
import com.tierraburritoservidor.dao.repositories.RepositoryUsuarios;
import com.tierraburritoservidor.domain.model.Usuario;
import com.tierraburritoservidor.domain.util.DatabaseUiParser;
import com.tierraburritoservidor.errors.exceptions.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class ServiceUsuarios {

    private final RepositoryUsuarios repositoryUsuarios;
    private final DatabaseUiParser databaseUiParser;

    public void comprobarCredenciales(String correo, String contrasena) {
        Usuario usuario = databaseUiParser.usuarioDbToUsuario(repositoryUsuarios.getUsuarioByCorreo(correo));
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
            repositoryUsuarios.crearUsuarioDesactivado(databaseUiParser.usuarioToUsuarioDb(usuario));
            return codigo;
        } else {
            throw new CorreoYaExisteException();
        }
    }


    public void activarUsuario(int id, String codigo) {
        UsuarioDB usuario = repositoryUsuarios.getUsuarioById(id);
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
        return databaseUiParser.usuarioDbToUsuario(repositoryUsuarios.getUsuarioByCorreo(correo));
    }
}
