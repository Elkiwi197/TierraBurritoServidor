package com.tierraburritoservidor.domain.service;

import com.tierraburritoservidor.dao.RepositoryPlatos;
import com.tierraburritoservidor.domain.model.Plato;
import com.tierraburritoservidor.errors.exceptions.PedidoNoEncontradoException;
import com.tierraburritoservidor.errors.exceptions.PlatoNoEncontradoException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServicePlatos {

    private final RepositoryPlatos repositoryPlatos;

    public List<Plato> getAllPlatos() {
        return repositoryPlatos.getAllPlatos();
    }

    public Plato getPlatoById(int id) {
        Plato plato = repositoryPlatos.getPlatoById(id);
        if (plato == null){
            throw new PlatoNoEncontradoException();
        }
        return plato;
    }
}
