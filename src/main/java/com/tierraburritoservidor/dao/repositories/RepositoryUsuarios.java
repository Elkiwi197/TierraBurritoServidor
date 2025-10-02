package com.tierraburritoservidor.dao.repositories;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.tierraburritoservidor.common.ConstantesErrores;
import com.tierraburritoservidor.dao.RepositoryUsuariosInterface;
import com.tierraburritoservidor.dao.model.UsuarioDB;
import com.tierraburritoservidor.dao.util.DocumentPojoParser;
import com.tierraburritoservidor.dao.util.UserIdManager;
import com.tierraburritoservidor.errors.exceptions.CorreoYaExisteException;
import com.tierraburritoservidor.errors.exceptions.UsuarioNoEncontradoException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;

@Log4j2
@Repository
@RequiredArgsConstructor
public class RepositoryUsuarios implements RepositoryUsuariosInterface {

    private final String COLLECTION_NAME = "Usuarios";

    private final DocumentPojoParser documentPojoParser;
    private final UserIdManager userIdManager;
    private final Gson gson = new GsonBuilder()
            .create();
    private final MongoTemplate mongoTemplate;



    public List<UsuarioDB> getUsuariosActivados() {
        List<UsuarioDB> usuarios = new ArrayList<>();
        try {
            MongoCollection<Document> collection = mongoTemplate.getCollection(COLLECTION_NAME);
            List<Document> documents = collection.find().into(new ArrayList<>());
            HashMap<ObjectId, Integer> newIds = new HashMap<>();
            documents.forEach(document -> {
                usuarios.add(documentPojoParser.documentToUsuarioDB(document));
                newIds.put(document.getObjectId("_id"), newIds.size() + 1);
            });
            userIdManager.setUserIds(newIds);
        } catch (Exception e) {
            log.error( ConstantesErrores.ERROR_LEYENDO_USUARIOS, e.getMessage(), e);
        }
        return usuarios;
    }

    @Override
    public void cargarIdsUsuarios() {
        try {
            MongoCollection<Document> collection = mongoTemplate.getCollection(COLLECTION_NAME);
            List<Document> documents = collection.find().into(new ArrayList<>());
            HashMap<ObjectId, Integer> newIds = new HashMap<>();
            documents.forEach(document -> {
                newIds.put(document.getObjectId("_id"), newIds.size() + 1);
            });
            userIdManager.setUserIds(newIds);
        } catch (Exception e) {
            log.error( ConstantesErrores.ERROR_LEYENDO_USUARIOS, e.getMessage(), e);
        }
    }

    public void crearUsuarioDesactivado(UsuarioDB usuario) {
        try {
            MongoCollection<Document> collection = mongoTemplate.getCollection(COLLECTION_NAME);
            Document existingUser = collection.find(Filters.eq("correo", usuario.getCorreo())).first();
            if (existingUser != null) {
                throw new CorreoYaExisteException();
            }
            Document usuarioDocument = Document.parse(gson.toJson(usuario));
            collection.insertOne(usuarioDocument);
            ObjectId generatedObjectId = usuarioDocument.getObjectId("_id");
            userIdManager.anadirObjectId(generatedObjectId);
        } catch (Exception e) {
            log.error(ConstantesErrores.ERROR_CREANDO_USUARIO, e.getMessage(), e);
            throw new  RuntimeException(ConstantesErrores.ERROR_CREANDO_USUARIO);
        }
    }


    public UsuarioDB getUsuarioByCorreo(String correo) {
        try {
            Document document = mongoTemplate.getCollection(COLLECTION_NAME)
                    .find(Filters.eq("correo", correo))
                    .first();
            if (document != null) {
                return documentPojoParser.documentToUsuarioDB(document);
            } else {
                throw new UsuarioNoEncontradoException();
            }
        } catch (Exception e) {
            log.error("Error al obtener el usuario por correo: {}", e.getMessage(), e);
            // Aquí podrías lanzar una excepción o retornar null, según el diseño de tu aplicación
            return null;
        }
    }


    public void activarUsuario(int id) {
        try {
            MongoCollection<Document> collection = mongoTemplate.getCollection(COLLECTION_NAME);
            collection.updateOne(
                    eq("_id", userIdManager.getObjectId(id)),
                    set("activado", true)
            );
        } catch (Exception e) {
            log.error("Error actualizando usuario: {}", e.getMessage(), e);
        }
    }


    public UsuarioDB getUsuarioById(int id) {
        UsuarioDB usuario = new UsuarioDB();
        try {
            MongoCollection<Document> collection = mongoTemplate.getCollection(COLLECTION_NAME);

            ObjectId objectId = userIdManager.getObjectId(id);
            Document document = collection.find(Filters.eq("_id", objectId)).first();
            if (document != null) {
                usuario = documentPojoParser.documentToUsuarioDB(document);
            } else {
                throw new UsuarioNoEncontradoException();
            }

        } catch (Exception e) {
            log.error("Error al obtener el usuario por ID: {}", e.getMessage(), e);

        }
        return usuario;
    }
}
