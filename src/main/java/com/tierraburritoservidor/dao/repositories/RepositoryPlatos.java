package com.tierraburritoservidor.dao.repositories;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.tierraburritoservidor.common.ConstantesErrores;
import com.tierraburritoservidor.dao.RepositoryPlatosInterface;
import com.tierraburritoservidor.dao.model.PlatoDB;
import com.tierraburritoservidor.dao.util.DocumentPojoParser;
import com.tierraburritoservidor.dao.util.PlatoIdManager;
import com.tierraburritoservidor.dao.util.ProductoIdManager;
import com.tierraburritoservidor.errors.exceptions.PlatoNoEncontradoException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Log4j2
@Repository
@RequiredArgsConstructor
public class RepositoryPlatos implements RepositoryPlatosInterface {

    private final String COLLECTION_NAME = "Platos";

    private final DocumentPojoParser documentPojoParser;
    private final PlatoIdManager platoIdManager;
    private final ProductoIdManager productoIdManager;
    private final MongoTemplate mongoTemplate;
    private final Gson gson = new GsonBuilder()
            .create();


    public List<PlatoDB> getAllPlatos() {
        List<PlatoDB> platos = new ArrayList<>();
        try {
            MongoCollection<Document> collection = mongoTemplate.getCollection(COLLECTION_NAME);
            List<Document> documents = collection.find().into(new ArrayList<>());
            HashMap<ObjectId, Integer> newPlatoIds = new HashMap<>();
            HashMap<ObjectId, Integer> newProductoIds = new HashMap<>();
            List<ObjectId> objectIdsIngredientes = new ArrayList<>();
            documents.forEach(document -> {
                PlatoDB plato = documentPojoParser.documentToPlatoDB(document);
                newPlatoIds.put(document.getObjectId("_id"), newPlatoIds.size() + 1);
                List<ObjectId> idsIngredientesReales = document.getList("ingredientes", ObjectId.class);
                idsIngredientesReales.forEach(objectId -> {
                    if (!newProductoIds.containsKey(objectId)) {
                        newProductoIds.put(objectId, newProductoIds.size() + 1);
                    }
                });
                plato.setIngredientes(idsIngredientesReales);
                platos.add(plato);
            });

            platoIdManager.setPlatoIds(newPlatoIds);
            productoIdManager.setProductoIds(newProductoIds);

            //platoDB.set_id(document.getObjectId("_id"));

        } catch (Exception e) {
            log.error(ConstantesErrores.ERROR_LEYENDO_PLATOS, e.getMessage(), e);
        }
        return platos;
    }


    public PlatoDB getPlatoById(ObjectId id) {
        PlatoDB plato = new PlatoDB();
        try {
            MongoCollection<Document> collection = mongoTemplate.getCollection(COLLECTION_NAME);

            Document document = collection.find(Filters.eq("_id", id)).first();
            if (document != null) {
                plato = documentPojoParser.documentToPlatoDB(document);
                plato.getIngredientes().forEach(i -> {
                    if (productoIdManager.getId(i) == null) {
                        productoIdManager.anadirObjectId(i);
                    }
                });
            } else {
                throw new PlatoNoEncontradoException();
            }
        } catch (Exception e) {
            log.error("Error al obtener el plato por id: {}", e.getMessage(), e);

        }
        return plato;
    }
}

