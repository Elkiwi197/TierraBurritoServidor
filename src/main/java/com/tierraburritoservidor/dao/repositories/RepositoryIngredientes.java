package com.tierraburritoservidor.dao.repositories;


import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.tierraburritoservidor.common.Constantes;
import com.tierraburritoservidor.common.ConstantesInfo;
import com.tierraburritoservidor.dao.RepositoryIngredientesInterface;
import com.tierraburritoservidor.dao.model.IngredienteDB;
import com.tierraburritoservidor.dao.model.PlatoDB;
import com.tierraburritoservidor.dao.util.DocumentPojoParser;
import com.tierraburritoservidor.dao.util.IngredienteIdManager;
import com.tierraburritoservidor.errors.exceptions.IngredienteNoEncontradoException;
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
public class RepositoryIngredientes implements RepositoryIngredientesInterface {


    private final String COLLECTION_NAME = Constantes.COLECCION_INGREDIENTES;
    private final DocumentPojoParser documentPojoParser;
    private final IngredienteIdManager ingredienteIdManager;
    private final MongoTemplate mongoTemplate;


    @EventListener(ApplicationReadyEvent.class)
    @Override
    public void inicializarIngredientes() {
        try {
            Query query = new Query();
            List<IngredienteDB> ingredientes = mongoTemplate.find(query, IngredienteDB.class, COLLECTION_NAME);
            ingredientes.forEach(ingrediente -> {
                if (ingredienteIdManager.getId(ingrediente.get_id()) == null) {
                    ingredienteIdManager.anadirObjectId(ingrediente.get_id());
                }
            });
        } catch (Exception e) {
            log.error(ConstantesInfo.ERROR_INICIALIZANDO_INGREDIENTES, e.getMessage(), e);
        }
    }

    @Override
    public List<IngredienteDB> getIngredientes() {
        List<IngredienteDB> productos = new ArrayList<>();
        try {
            MongoCollection<Document> collection = mongoTemplate.getCollection(COLLECTION_NAME);
            List<Document> documents = collection.find().into(new ArrayList<>());
            documents.forEach(document ->
                    productos.add(documentPojoParser.documentToProductoDB(document)));
        } catch (Exception e) {
            log.error(ConstantesInfo.ERROR_LEYENDO_PRODUCTOS, e.getMessage(), e);
        }
        return productos;
    }

    @Override
    public IngredienteDB getIngredienteByNombre(String nombre) {
        IngredienteDB producto = new IngredienteDB();
        try {
            MongoCollection<Document> collection = mongoTemplate.getCollection(COLLECTION_NAME);
            Document document = collection.find(Filters.eq(Constantes.NOMBRE, nombre)).first();
            if (document != null) {
                producto = documentPojoParser.documentToProductoDB(document);
            } else {
                producto = null;
            }
        } catch (Exception e) {
            log.error(ConstantesInfo.ERROR_LEYENDO_PRODUCTO_POR_NOMBRE, e.getMessage(), e);

        }
        if (producto == null) {
            throw new IngredienteNoEncontradoException();
        }
        return producto;
    }

    @Override
    public IngredienteDB getIngredienteByObjectId(ObjectId objectId) {
        IngredienteDB producto = new IngredienteDB();
        try {
            MongoCollection<Document> collection = mongoTemplate.getCollection(COLLECTION_NAME);

            Document document = collection.find(Filters.eq(Constantes._ID, objectId)).first();
            if (document != null) {
                producto = documentPojoParser.documentToProductoDB(document);
            } else {
                throw new IngredienteNoEncontradoException();
            }

        } catch (Exception e) {
            log.error(ConstantesInfo.ERROR_LEYENDO_PRODUCTO_POR_ID, e.getMessage(), e);

        }
        return producto;
    }

    @Override
    public List<IngredienteDB> getExtrasByPlatoDB(PlatoDB platoDB) {
        List<IngredienteDB> extrasDB = new ArrayList<>();

        try {
            MongoCollection<Document> collection = mongoTemplate.getCollection(COLLECTION_NAME);
            List<Document> documents = collection.find().into(new ArrayList<>());
            HashMap<ObjectId, Integer> newIds = new HashMap<>();
            documents.forEach(document -> {
                IngredienteDB extraDB = documentPojoParser.documentToProductoDB(document);
                if (!platoDB.getIngredientes().contains(extraDB.get_id())) {
                    extrasDB.add(extraDB);
                }
            });
            ingredienteIdManager.setIngredienteIds(newIds);
        } catch (Exception e) {
            log.error(ConstantesInfo.ERROR_LEYENDO_PRODUCTOS, e.getMessage(), e);
        }

        return extrasDB;
    }


}
