package com.tierraburritoservidor.dao.repositories;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.tierraburritoservidor.common.Constantes;
import com.tierraburritoservidor.common.ConstantesInfo;
import com.tierraburritoservidor.dao.RepositoryUsuariosInterface;
import com.tierraburritoservidor.dao.model.PedidoDB;
import com.tierraburritoservidor.dao.model.UsuarioDB;
import com.tierraburritoservidor.dao.util.DocumentPojoParser;
import com.tierraburritoservidor.dao.util.UserIdManager;
import com.tierraburritoservidor.errors.exceptions.CorreoYaExisteException;
import com.tierraburritoservidor.errors.exceptions.UsuarioNoEncontradoException;
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

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;
import static com.mongodb.client.model.Updates.unset;

@Log4j2
@Repository
@RequiredArgsConstructor
public class RepositoryUsuarios implements RepositoryUsuariosInterface {

    private final String COLLECTION_NAME = Constantes.COLECCION_USUARIOS;

    private final DocumentPojoParser documentPojoParser;
    private final UserIdManager userIdManager;
    private final Gson gson = new GsonBuilder()
            .create();
    private final MongoTemplate mongoTemplate;


    @EventListener(ApplicationReadyEvent.class)
    @Override
    public void inicializarUsuarios() {
        try {
            List<UsuarioDB> usuarios = new ArrayList<>();
            Query query = new Query();
            usuarios = mongoTemplate.find(query, UsuarioDB.class, COLLECTION_NAME);

            usuarios.forEach(usuario -> {
                if (userIdManager.getId(usuario.get_id()) == null) {
                    userIdManager.anadirObjectId(usuario.get_id());
                }
            });
        } catch (Exception e) {
            log.error(ConstantesInfo.ERROR_INICIALIZANDO_USUARIOS, e.getMessage(), e);
        }
    }

    @Override
    public List<UsuarioDB> getUsuariosActivados() {
        List<UsuarioDB> usuarios = new ArrayList<>();
        try {
            MongoCollection<Document> collection = mongoTemplate.getCollection(COLLECTION_NAME);
            List<Document> documents = collection.find().into(new ArrayList<>());
            HashMap<ObjectId, Integer> newIds = new HashMap<>();
            documents.forEach(document -> {
                usuarios.add(documentPojoParser.documentToUsuarioDB(document));
                newIds.put(document.getObjectId(Constantes._ID), newIds.size() + 1);
            });
            userIdManager.setUserIds(newIds);
        } catch (Exception e) {
            log.error(ConstantesInfo.ERROR_LEYENDO_USUARIOS, e.getMessage(), e);
        }
        return usuarios;
    }


    @Override
    public void crearUsuarioDesactivado(UsuarioDB usuario) {
        MongoCollection<Document> collection = mongoTemplate.getCollection(COLLECTION_NAME);
        Document existingUser = collection.find(Filters.eq(Constantes.CORREO, usuario.getCorreo())).first();
        if (existingUser != null) {
            throw new CorreoYaExisteException();
        }

        try {
            Document usuarioDocument = Document.parse(gson.toJson(usuario));
            collection.insertOne(usuarioDocument);
            ObjectId generatedObjectId = usuarioDocument.getObjectId(Constantes._ID);
            userIdManager.anadirObjectId(generatedObjectId);
        } catch (Exception e) {
            log.error(ConstantesInfo.ERROR_CREANDO_USUARIO, e.getMessage(), e);
            throw new RuntimeException(ConstantesInfo.ERROR_CREANDO_USUARIO);
        }
    }



    @Override
    public UsuarioDB getUsuarioByCorreo(String correo) {
        try {
            Document document = mongoTemplate.getCollection(COLLECTION_NAME)
                    .find(Filters.eq(Constantes.CORREO, correo))
                    .first();
            if (document != null) {
                return documentPojoParser.documentToUsuarioDB(document);
            } else {
                throw new UsuarioNoEncontradoException();
            }
        } catch (UsuarioNoEncontradoException e) {
            log.warn(e.getMessage());
            return null;
        } catch (Exception e) {
            log.error(ConstantesInfo.ERROR_LEYENDO_USUARIOS, e.getMessage(), e);
            return null;
        }
    }


    @Override
    public void activarUsuario(int id) {
        try {
            MongoCollection<Document> collection = mongoTemplate.getCollection(COLLECTION_NAME);
            collection.updateOne(
                    eq(Constantes._ID, userIdManager.getObjectId(id)),
                    set(Constantes.ACTIVADO, true)
            );
            collection.updateOne(
                    eq(Constantes._ID, userIdManager.getObjectId(id)),
                    unset(Constantes.CODIGO_ACTIVACION)
            );
        } catch (Exception e) {
            log.error(ConstantesInfo.ERROR_ACTIVANDO_USUARIO, e.getMessage(), e);
        }
    }


    @Override
    public UsuarioDB getUsuarioById(int id) {
        UsuarioDB usuario = new UsuarioDB();
        try {
            MongoCollection<Document> collection = mongoTemplate.getCollection(COLLECTION_NAME);

            ObjectId objectId = userIdManager.getObjectId(id);
            Document document = collection.find(Filters.eq(Constantes._ID, objectId)).first();
            if (document != null) {
                usuario = documentPojoParser.documentToUsuarioDB(document);
            } else {
                throw new UsuarioNoEncontradoException();
            }

        } catch (Exception e) {
            log.error(ConstantesInfo.ERROR_LEYENDO_USUARIO_POR_ID, e.getMessage(), e);

        }
        return usuario;
    }


}
