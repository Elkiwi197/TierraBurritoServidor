package com.tierraburritoservidor.dao;

import com.tierraburritoservidor.dao.model.UsuarioDB;

import java.util.List;

public interface RepositoryUsuariosInterface {
    void inicializarUsuarios();

    void crearUsuarioDesactivado(UsuarioDB usuario);

    UsuarioDB getUsuarioByCorreo(String correo);

    void activarUsuario(int id);

    UsuarioDB getUsuarioById(int id);

    List<UsuarioDB> getUsuariosActivados();

}
