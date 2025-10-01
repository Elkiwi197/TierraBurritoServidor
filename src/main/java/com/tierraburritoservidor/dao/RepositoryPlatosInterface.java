package com.tierraburritoservidor.dao;

import com.tierraburritoservidor.dao.model.PlatoDB;
import org.bson.types.ObjectId;

import java.util.List;

public interface RepositoryPlatosInterface {
    List<PlatoDB> getAllPlatos();

    PlatoDB getPlatoById(ObjectId id);
}
