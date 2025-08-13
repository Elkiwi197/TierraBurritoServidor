package com.tierraburritoservidor.dao;

import com.tierraburritoservidor.domain.model.Usuario;

public interface RepositoryUsuariosInterface {
    String crearUsuarioDesactivado(Usuario usuario);

    Usuario getUsuarioByCorreo(String correo);

    void activarUsuario(int id);

    Usuario getUsuarioById(int id);
}
