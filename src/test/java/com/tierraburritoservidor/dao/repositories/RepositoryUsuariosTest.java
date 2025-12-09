package com.tierraburritoservidor.dao.repositories;

import com.mongodb.client.model.Filters;
import com.tierraburritoservidor.common.Constantes;
import com.tierraburritoservidor.dao.model.UsuarioDB;
import com.tierraburritoservidor.dao.util.DocumentPojoParser;
import com.tierraburritoservidor.dao.util.UserIdManager;
import com.tierraburritoservidor.errors.exceptions.CorreoYaExisteException;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RepositoryUsuariosTest {

    @Mock
    private MongoTemplate mongoTemplate;

    @Mock
    private DocumentPojoParser documentPojoParser;

    @Mock
    private UserIdManager userIdManager;

    @InjectMocks
    private RepositoryUsuarios repositoryUsuarios;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void inicializarUsuarios_shouldAddMissingIds() {
        //GIVEN
        List<UsuarioDB> usuarios = new ArrayList<>();
        UsuarioDB usuarioDB = new UsuarioDB();
        usuarioDB.set_id(new ObjectId());
        usuarios.add(usuarioDB);

        //WHEN
        when(mongoTemplate.find(any(), eq(UsuarioDB.class), anyString())).thenReturn(usuarios);
        when(userIdManager.getId(usuarioDB.get_id())).thenReturn(null);

        //THEN
        repositoryUsuarios.inicializarUsuarios();
        verify(userIdManager, times(1)).anadirObjectId(usuarioDB.get_id());
    }

    @Test
    void crearUsuarioDesactivado_shouldInsertUser_whenEmailNotExists() {
        //GIVEN
        UsuarioDB usuario = new UsuarioDB();
        usuario.setCorreo("test@correo.com");
        com.mongodb.client.MongoCollection<Document> collection = mock(com.mongodb.client.MongoCollection.class);

        //WHEN
        when(mongoTemplate.getCollection(anyString())).thenReturn(collection);
        com.mongodb.client.FindIterable<Document> findIterable = mock(com.mongodb.client.FindIterable.class);
        when(collection.find(Filters.eq(Constantes.CORREO, usuario.getCorreo()))).thenReturn(findIterable);
        when(findIterable.first()).thenReturn(null);
        doAnswer(invocation -> {
            Document doc = invocation.getArgument(0);
            doc.put(Constantes._ID, new ObjectId()); // Aquí Mongo genera el ObjectId
            return null;
        }).when(collection).insertOne(any(Document.class));

        //THEN
        repositoryUsuarios.crearUsuarioDesactivado(usuario);
        verify(collection, times(1)).insertOne(any(Document.class));
        verify(userIdManager, times(1)).anadirObjectId(any(ObjectId.class));
    }

    @Test
    void crearUsuarioDesactivado_shouldThrow_whenEmailExists() {
        //GIVEN
        UsuarioDB usuario = new UsuarioDB();
        usuario.setCorreo("exists@correo.com");
        com.mongodb.client.MongoCollection<Document> collection = mock(com.mongodb.client.MongoCollection.class);

        //WHEN
        when(mongoTemplate.getCollection(anyString())).thenReturn(collection);
        when(collection.find(Filters.eq(Constantes.CORREO, usuario.getCorreo()))).thenReturn(mock(com.mongodb.client.FindIterable.class));
        when(collection.find(Filters.eq(Constantes.CORREO, usuario.getCorreo())).first()).thenReturn(new Document());

        //THEN
        assertThrows(CorreoYaExisteException.class, () -> repositoryUsuarios.crearUsuarioDesactivado(usuario));
    }

    @Test
    void getUsuarioByCorreo_shouldReturnUser_whenExists() {
        //GIVEN
        String correo = "user@correo.com";
        Document document = new Document();
        UsuarioDB usuario = new UsuarioDB();

        //WHEN
        when(mongoTemplate.getCollection(anyString())).thenReturn(mock(com.mongodb.client.MongoCollection.class));
        com.mongodb.client.MongoCollection<Document> collection = mongoTemplate.getCollection("usuarios");
        when(collection.find(Filters.eq(Constantes.CORREO, correo))).thenReturn(mock(com.mongodb.client.FindIterable.class));
        when(collection.find(Filters.eq(Constantes.CORREO, correo)).first()).thenReturn(document);
        when(documentPojoParser.documentToUsuarioDB(document)).thenReturn(usuario);

        //THEN
        UsuarioDB result = repositoryUsuarios.getUsuarioByCorreo(correo);
        assertEquals(usuario, result);
    }

    @Test
    void getUsuarioByCorreo_shouldReturnNull_whenNotFound() {
        //GIVEN
        String correo = "notfound@correo.com";
        com.mongodb.client.MongoCollection<Document> collection = mock(com.mongodb.client.MongoCollection.class);

        //WHEN
        when(mongoTemplate.getCollection(anyString())).thenReturn(collection);
        when(collection.find(Filters.eq(Constantes.CORREO, correo))).thenReturn(mock(com.mongodb.client.FindIterable.class));
        when(collection.find(Filters.eq(Constantes.CORREO, correo)).first()).thenReturn(null);

        //THEN
        UsuarioDB result = repositoryUsuarios.getUsuarioByCorreo(correo);
        assertNull(result);
    }

    @Test
    void getUsuarioById_shouldReturnUser_whenExists() {
        //GIVEN
        int id = 1;
        ObjectId objectId = new ObjectId();
        UsuarioDB usuario = new UsuarioDB();
        Document document = new Document();

        //WHEN
        when(userIdManager.getObjectId(id)).thenReturn(objectId);
        com.mongodb.client.MongoCollection<Document> collection = mock(com.mongodb.client.MongoCollection.class);
        when(mongoTemplate.getCollection(anyString())).thenReturn(collection);
        when(collection.find(Filters.eq(Constantes._ID, objectId))).thenReturn(mock(com.mongodb.client.FindIterable.class));
        when(collection.find(Filters.eq(Constantes._ID, objectId)).first()).thenReturn(document);
        when(documentPojoParser.documentToUsuarioDB(document)).thenReturn(usuario);

        //THEN
        UsuarioDB result = repositoryUsuarios.getUsuarioById(id);
        assertEquals(usuario, result);
    }

    @Test
    void getUsuarioById_shouldReturnEmptyUser_whenNotFound() {
        //GIVEN
        int id = 1;
        ObjectId objectId = new ObjectId();

        //WHEN
        when(userIdManager.getObjectId(id)).thenReturn(objectId);
        com.mongodb.client.MongoCollection<Document> collection = mock(com.mongodb.client.MongoCollection.class);
        when(mongoTemplate.getCollection(anyString())).thenReturn(collection);
        when(collection.find(Filters.eq(Constantes._ID, objectId))).thenReturn(mock(com.mongodb.client.FindIterable.class));
        when(collection.find(Filters.eq(Constantes._ID, objectId)).first()).thenReturn(null);

        //THEN
        UsuarioDB result = repositoryUsuarios.getUsuarioById(id);
        assertNotNull(result);
        assertNull(result.getCorreo());
    }
}
