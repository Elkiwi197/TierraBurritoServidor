package com.tierraburritoservidor.dao.repositories;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.tierraburritoservidor.common.ConstantesErrores;
import com.tierraburritoservidor.dao.RepositoryUsuariosInterface;
import com.tierraburritoservidor.dao.model.UsuarioDB;
import com.tierraburritoservidor.dao.util.DocumentParser;
import com.tierraburritoservidor.dao.util.MongoUtil;
import com.tierraburritoservidor.dao.util.UserIdManager;
import com.tierraburritoservidor.domain.model.Usuario;
import com.tierraburritoservidor.errors.exceptions.CorreoYaExisteException;
import com.tierraburritoservidor.errors.exceptions.UsuarioNoEncontradoException;
import lombok.extern.log4j.Log4j2;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Log4j2
@Repository
public class RepositoryUsuarios implements RepositoryUsuariosInterface {

    private final String COLLECTION_NAME = "Patients";

    private DocumentParser documentParser;
    private UserIdManager userIdManager;
    private Gson gson = new GsonBuilder()
            .create();


    public RepositoryUsuarios(DocumentParser documentParser, UserIdManager userIdManager) {
        this.documentParser = documentParser;
        this.userIdManager = userIdManager;
    }

    public List<UsuarioDB> getUsuariosActivados() {
        List<UsuarioDB> usuarios = new ArrayList<>();
        try {
            MongoDatabase database = MongoUtil.getDatabase();
            MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);
            List<Document> documents = collection.find().into(new ArrayList<>());
            HashMap<ObjectId, Integer> newIds = new HashMap<>();
            documents.forEach(document -> {
                usuarios.add(documentParser.documentToUsuarioDB(document));
                newIds.put(document.getObjectId("_id"), newIds.size() + 1);
            });
            userIdManager.setUserIds(newIds);
        } catch (Exception e) {
            log.error("Error getting usuarios: {}", e.getMessage(), e);
        } finally {
            MongoUtil.close();
        }
        return usuarios;
    }

    public void crearUsuarioDesactivado(UsuarioDB usuario) {
        try {
            MongoDatabase database = MongoUtil.getDatabase();
            MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);

            Document existingUser = collection.find(Filters.eq("correo", usuario.getCorreo())).first();
            if (existingUser != null) {
                throw new CorreoYaExisteException();
            }

            Document usuarioDocument = Document.parse(gson.toJson(usuario));
            collection.insertOne(usuarioDocument);

        } catch (Exception e) {
            log.error(ConstantesErrores.ERROR_CREANDO_USUARIO, e.getMessage(), e);
            throw new RuntimeException(ConstantesErrores.ERROR_CREANDO_USUARIO);
        } finally {
            MongoUtil.close();
        }
    }


    public UsuarioDB getUsuarioByCorreo(String correo) {
        UsuarioDB usuario = new UsuarioDB();
        try {
            MongoDatabase database = MongoUtil.getDatabase();
            MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);

            // Buscar directamente por el campo "correo"
            Document document = collection.find(Filters.eq("correo", correo)).first();
            if (document != null) {
                usuario = documentParser.documentToUsuarioDB(document);
            } else {
                throw new UsuarioNoEncontradoException();
            }

        } catch (Exception e) {
            log.error("Error al obtener el usuario por correo: {}", e.getMessage(), e);

        } finally {
            MongoUtil.close();
        }
        return usuario;
    }


    public void activarUsuario(int id) {
        Usuario usuario = usuariosDesactivados.stream()
                .filter(u -> u.getId() == id)
                .findFirst()
                .orElse(null);
        if (usuario != null) {
            usuario.setActivado(true);
            usuariosActivados.add(usuario);
            usuariosDesactivados.remove(usuario);
            log.info("Usuario activado");
        } else {
            throw new UsuarioNoEncontradoException();
        }
    }


    public UsuarioDB getUsuarioById(int id) {
        Usuario usuario = usuariosActivados.stream()
                .filter(u -> u.getId() == id)
                .findFirst()
                .orElse(null);
        if (usuario == null) {
            usuario = usuariosDesactivados.stream()
                    .filter(u -> u.getId() == id)
                    .findFirst()
                    .orElse(null);
        }
        if (usuario == null) {
            throw new UsuarioNoEncontradoException();
        }
        return usuario;
    }
}
