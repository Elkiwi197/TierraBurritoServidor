package com.tierraburritoservidor.dao;

import com.tierraburritoservidor.dao.model.UsuarioDB;
import com.tierraburritoservidor.domain.model.Usuario;

import java.util.List;

public interface RepositoryUsuariosInterface {
    void crearUsuarioDesactivado(UsuarioDB usuario);

    UsuarioDB getUsuarioByCorreo(String correo);

    void activarUsuario(int id);

    UsuarioDB getUsuarioById(int id);

    List<UsuarioDB> getUsuariosActivados();
}
