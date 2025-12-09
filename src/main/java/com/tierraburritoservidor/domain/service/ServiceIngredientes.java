package com.tierraburritoservidor.domain.service;


import com.tierraburritoservidor.dao.model.PlatoDB;
import com.tierraburritoservidor.dao.model.IngredienteDB;
import com.tierraburritoservidor.dao.repositories.RepositoryIngredientes;
import com.tierraburritoservidor.dao.repositories.RepositoryPlatos;
import com.tierraburritoservidor.dao.util.PlatoIdManager;
import com.tierraburritoservidor.domain.model.Ingrediente;
import com.tierraburritoservidor.domain.model.Plato;
import com.tierraburritoservidor.domain.util.DatabaseUiParser;
import com.tierraburritoservidor.errors.exceptions.IngredienteNoEncontradoException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ServiceIngredientes {

    private final RepositoryIngredientes repositoryIngredientes;
    private final DatabaseUiParser databaseUiParser;
    private final RepositoryPlatos repositoryPlatos;
    private final PlatoIdManager platoIdManager;


    public Ingrediente getIngredienteByNombre(String nombreIngrediente) {
        IngredienteDB ingredienteDB = repositoryIngredientes.getIngredienteByNombre(nombreIngrediente);
        if (ingredienteDB == null) {
            throw new IngredienteNoEncontradoException();
        }
        return databaseUiParser.ingredienteDBtoIngrediente(ingredienteDB);
    }

    public List<Ingrediente> getIngredientesByPlato(Plato plato) {
        List<Ingrediente> ingredientes = new ArrayList<>();
        plato.getIngredientes().forEach(ingrediente -> ingredientes.add(getIngredienteByNombre(ingrediente.getNombre())));
        return ingredientes;
    }

    public List<Ingrediente> getExtrasByPlato(Plato plato) {
        List<Ingrediente> extras = new ArrayList<>();
        PlatoDB platoDB = repositoryPlatos.getPlatoById(platoIdManager.getObjectId(plato.getId()));
        List<IngredienteDB> extrasDB = repositoryIngredientes.getExtrasByPlatoDB(platoDB);
        extrasDB.forEach(extraDB -> extras.add(databaseUiParser.ingredienteDBtoIngrediente(extraDB)));
        return extras;
    }
}
