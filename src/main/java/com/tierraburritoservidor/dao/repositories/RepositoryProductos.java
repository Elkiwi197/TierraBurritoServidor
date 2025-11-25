package com.tierraburritoservidor.dao.repositories;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.tierraburritoservidor.common.Constantes;
import com.tierraburritoservidor.common.ConstantesInfo;
import com.tierraburritoservidor.dao.RepositoryProductosInterface;
import com.tierraburritoservidor.dao.model.PlatoDB;
import com.tierraburritoservidor.dao.model.ProductoDB;
import com.tierraburritoservidor.dao.util.DocumentPojoParser;
import com.tierraburritoservidor.dao.util.ProductoIdManager;
import com.tierraburritoservidor.errors.exceptions.ProductoNoEncontradoException;
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
public class RepositoryProductos implements RepositoryProductosInterface {


    private final String COLLECTION_NAME = Constantes.PRODUCTOS;

    private final DocumentPojoParser documentPojoParser;
    private final ProductoIdManager productoIdManager;
    private final MongoTemplate mongoTemplate;
    private final Gson gson = new GsonBuilder()
            .create();




    public List<ProductoDB> getIngredientes() {
        List<ProductoDB> productos = new ArrayList<>();
        try {
            MongoCollection<Document> collection = mongoTemplate.getCollection(COLLECTION_NAME);
            List<Document> documents = collection.find().into(new ArrayList<>());
            HashMap<ObjectId, Integer> newIds = new HashMap<>();
            documents.forEach(document -> {
                productos.add(documentPojoParser.documentToProductoDB(document));
                newIds.put(document.getObjectId(Constantes._ID), newIds.size() + 1);
            });
            productoIdManager.setProductoIds(newIds);
        } catch (Exception e) {
            log.error( ConstantesInfo.ERROR_LEYENDO_PRODUCTOS, e.getMessage(), e);
        }
        return productos;
    }



    public ProductoDB getProductoByNombre(String nombre) {
        ProductoDB producto = new ProductoDB();
        try {
            MongoCollection<Document> collection = mongoTemplate.getCollection(COLLECTION_NAME);

            Document document = collection.find(Filters.eq(Constantes.NOMBRE, nombre)).first();
            if (document != null) {
                producto = documentPojoParser.documentToProductoDB(document);
            } else {
                throw new ProductoNoEncontradoException();
            }

        } catch (Exception e) {
            log.error(ConstantesInfo.ERROR_LEYENDO_PRODUCTO_POR_NOMBRE, e.getMessage(), e);

        }
        return producto;
    }

    @Override
    public ProductoDB getProductoByObjectId(ObjectId objectId) {
        ProductoDB producto = new ProductoDB();
        try {
            MongoCollection<Document> collection = mongoTemplate.getCollection(COLLECTION_NAME);

            Document document = collection.find(Filters.eq(Constantes._ID, objectId)).first();
            if (document != null) {
                producto = documentPojoParser.documentToProductoDB(document);
            } else {
                throw new ProductoNoEncontradoException();
            }

        } catch (Exception e) {
            log.error(ConstantesInfo.ERROR_LEYENDO_PRODUCTO_POR_ID, e.getMessage(), e);

        }
        return producto;
    }

    @Override
    public List<ProductoDB> getExtrasByPlatoDB(PlatoDB platoDB) {
        List<ProductoDB> extrasDB = new ArrayList<>();

        try {
            MongoCollection<Document> collection = mongoTemplate.getCollection(COLLECTION_NAME);
            List<Document> documents = collection.find().into(new ArrayList<>());
            HashMap<ObjectId, Integer> newIds = new HashMap<>();
            documents.forEach(document -> {
                ProductoDB extraDB = documentPojoParser.documentToProductoDB(document);
                if (!platoDB.getIngredientes().contains(extraDB.get_id())){
                    extrasDB.add(extraDB);
                }
            });
            productoIdManager.setProductoIds(newIds);
        } catch (Exception e) {
            log.error( ConstantesInfo.ERROR_LEYENDO_PRODUCTOS, e.getMessage(), e);
        }

        return extrasDB;
    }


}
