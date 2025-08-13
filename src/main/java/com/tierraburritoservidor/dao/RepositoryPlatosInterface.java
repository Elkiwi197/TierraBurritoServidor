package com.tierraburritoservidor.dao;

import com.tierraburritoservidor.domain.model.Plato;

import java.util.List;

public interface RepositoryPlatosInterface {
    List<Plato> getAllPlatos();

    Plato getPlatoById(int id);
}
