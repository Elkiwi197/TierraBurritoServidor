package com.tierraburritoservidor.dao.repositories;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.tierraburritoservidor.common.Constantes;
import com.tierraburritoservidor.common.ConstantesInfo;
import com.tierraburritoservidor.dao.RepositoryPlatosInterface;
import com.tierraburritoservidor.dao.model.PlatoDB;
import com.tierraburritoservidor.dao.util.DocumentPojoParser;
import com.tierraburritoservidor.dao.util.IngredienteIdManager;
import com.tierraburritoservidor.dao.util.PlatoIdManager;
import com.tierraburritoservidor.errors.exceptions.PlatoNoEncontradoException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Log4j2
@Repository
@RequiredArgsConstructor
public class RepositoryPlatos implements RepositoryPlatosInterface {

    private final String COLLECTION_NAME = Constantes.COLECCION_PLATOS;

    private final DocumentPojoParser documentPojoParser;
    private final PlatoIdManager platoIdManager;
    private final IngredienteIdManager ingredienteIdManager;
    private final MongoTemplate mongoTemplate;


    @EventListener(ApplicationReadyEvent.class)
    @Override
    public void inicializarPlatos() {
        try {
            List<PlatoDB> platos = new ArrayList<>();
            Query query = new Query();
            platos = mongoTemplate.find(query, PlatoDB.class, COLLECTION_NAME);

            platos.forEach(plato -> {
                if (platoIdManager.getId(plato.get_id()) == null) {
                    platoIdManager.anadirObjectId(plato.get_id());
                }
            });
        } catch (Exception e) {
            log.error(ConstantesInfo.ERROR_INICIALIZANDO_PLATOS, e.getMessage(), e);
        }
    }

    @Override
    public List<PlatoDB> getAllPlatos() {
        List<PlatoDB> platos = new ArrayList<>();
        try {
            MongoCollection<Document> collection = mongoTemplate.getCollection(COLLECTION_NAME);
            List<Document> documents = collection.find().into(new ArrayList<>());
            HashMap<ObjectId, Integer> newPlatoIds = new HashMap<>();
            HashMap<ObjectId, Integer> newProductoIds = new HashMap<>();
            documents.forEach(document -> {
                PlatoDB plato = documentPojoParser.documentToPlatoDB(document);
                List<ObjectId> idsIngredientesReales = document.getList(Constantes.INGREDIENTES, ObjectId.class);
                idsIngredientesReales.forEach(objectId -> {
                    if (!newProductoIds.containsKey(objectId)) {
                        newProductoIds.put(objectId, newProductoIds.size() + 1);
                    }
                });
                plato.setIngredientes(idsIngredientesReales);
                platos.add(plato);
            });
        } catch (Exception e) {
            log.error(ConstantesInfo.ERROR_LEYENDO_PLATOS, e.getMessage(), e);
        }
        return platos;
    }


    @Override
    public PlatoDB getPlatoById(ObjectId id) {
        PlatoDB plato = new PlatoDB();
        try {
            MongoCollection<Document> collection = mongoTemplate.getCollection(COLLECTION_NAME);

            Document document = collection.find(Filters.eq(Constantes._ID, id)).first();
            if (document != null) {
                plato = documentPojoParser.documentToPlatoDB(document);
                plato.getIngredientes().forEach(i -> {
                    if (ingredienteIdManager.getId(i) == null) {
                        ingredienteIdManager.anadirObjectId(i);
                    }
                });
            } else {
                plato = null;
            }
        } catch (Exception e) {
            log.error(ConstantesInfo.ERROR_LEYENDO_PLATO_POR_ID, e.getMessage(), e);
        }
        if (plato == null){
            throw new PlatoNoEncontradoException();
        }
        return plato;
    }
}

