package com.tierraburritoservidor.dao;

import com.tierraburritoservidor.dao.model.PlatoDB;
import com.tierraburritoservidor.domain.model.Plato;

import java.util.List;

public interface RepositoryPlatosInterface {
    List<PlatoDB> getAllPlatos();

    PlatoDB getPlatoById(int id);
}
