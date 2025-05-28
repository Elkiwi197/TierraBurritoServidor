package com.tierraburritoservidor.domain.service;

import com.tierraburritoservidor.dao.RepositoryPedidos;
import com.tierraburritoservidor.domain.model.Pedido;
import com.tierraburritoservidor.errors.PedidoNoEncontradoException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServicePedidos {

    private final RepositoryPedidos repositoryPedidos;


    public Pedido getPedidoById(int id) {
        Pedido pedido = repositoryPedidos.getPedidoById(id);
        if (pedido == null){
            throw new PedidoNoEncontradoException();
        }
        return pedido;
    }

    public List<Pedido> getPedidoByUsuarioId(int idUsuario) {
        return repositoryPedidos.getPedidosByUsuario(idUsuario);
    }
}
