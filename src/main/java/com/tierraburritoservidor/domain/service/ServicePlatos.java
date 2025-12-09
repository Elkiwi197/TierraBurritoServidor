
package com.tierraburritoservidor.domain.service;

import com.tierraburritoservidor.dao.model.PlatoDB;
import com.tierraburritoservidor.dao.model.IngredienteDB;
import com.tierraburritoservidor.dao.repositories.RepositoryIngredientes;
import com.tierraburritoservidor.dao.repositories.RepositoryPlatos;
import com.tierraburritoservidor.dao.util.PlatoIdManager;
import com.tierraburritoservidor.domain.model.Ingrediente;
import com.tierraburritoservidor.domain.model.Plato;
import com.tierraburritoservidor.domain.util.DatabaseUiParser;
import com.tierraburritoservidor.errors.exceptions.PlatoNoEncontradoException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ServicePlatos {

    private final RepositoryPlatos repositoryPlatos;
    private final RepositoryIngredientes repositoryIngredientes;
    private final PlatoIdManager platoIdManager;
    private final DatabaseUiParser databaseUiParser;


    public List<Plato> getAllPlatos() {
        List<PlatoDB> platosDB = repositoryPlatos.getAllPlatos();
        List<Plato> platos = new ArrayList<>();
        platosDB.forEach(p -> {
            Plato plato = databaseUiParser.platoDBtoPlato(p);
            List<Ingrediente> ingredientes = new ArrayList<>();
            p.getIngredientes().forEach(objectId ->
                    ingredientes.add(databaseUiParser.ingredienteDBtoIngrediente(
                            repositoryIngredientes.getIngredienteByObjectId(objectId))));
            plato.setIngredientes(ingredientes);
            platos.add(plato);
        });

        return platos;
    }

    public Plato getPlatoById(int id) {
        PlatoDB platoDB = repositoryPlatos.getPlatoById(platoIdManager.getObjectId(id));
        if (platoDB == null) {
            throw new PlatoNoEncontradoException();
        }
        List<Ingrediente> ingredientes = new ArrayList<>();
        List<Ingrediente> extras = new ArrayList<>();
        Plato plato = databaseUiParser.platoDBtoPlato(platoDB);
        platoDB.getIngredientes().forEach(objectId ->
                ingredientes.add(databaseUiParser.ingredienteDBtoIngrediente(
                        repositoryIngredientes.getIngredienteByObjectId(objectId))));
        plato.setIngredientes(ingredientes);
        List<IngredienteDB> extrasDB = repositoryIngredientes.getExtrasByPlatoDB(platoDB);
        extrasDB.forEach(e -> extras.add(databaseUiParser.ingredienteDBtoIngrediente(e)));
        plato.setIngredientes(ingredientes);
        plato.setExtras(extras);
        return plato;
    }
}
