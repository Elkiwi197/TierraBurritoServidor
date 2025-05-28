package com.tierraburritoservidor.domain.service;

import com.tierraburritoservidor.dao.RepositoryPlatos;
import com.tierraburritoservidor.domain.model.Plato;
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
}
