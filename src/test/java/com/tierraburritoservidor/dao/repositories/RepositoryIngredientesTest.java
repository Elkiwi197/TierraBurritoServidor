package com.tierraburritoservidor.dao.repositories;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.tierraburritoservidor.common.Constantes;
import com.tierraburritoservidor.dao.model.IngredienteDB;
import com.tierraburritoservidor.dao.model.PlatoDB;
import com.tierraburritoservidor.dao.util.DocumentPojoParser;
import com.tierraburritoservidor.dao.util.IngredienteIdManager;
import com.tierraburritoservidor.errors.exceptions.IngredienteNoEncontradoException;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class RepositoryIngredientesTest {

    @Mock
    private DocumentPojoParser documentPojoParser;

    @Mock
    private IngredienteIdManager ingredienteIdManager;

    @Mock
    private MongoTemplate mongoTemplate;

    @Mock
    private MongoCollection<Document> mongoCollection;

    @InjectMocks
    private RepositoryIngredientes repositoryIngredientes;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void inicializarIngredientes() {
        //GIVEN
        List<IngredienteDB> ingredientes = new ArrayList<>();
        IngredienteDB ingrediente = new IngredienteDB();
        ingrediente.set_id(new ObjectId());
        ingredientes.add(ingrediente);

        //WHEN
        when(mongoTemplate.find(any(), eq(IngredienteDB.class), anyString())).thenReturn(ingredientes);
        when(ingredienteIdManager.getId(ingrediente.get_id())).thenReturn(null);

        //THEN
        repositoryIngredientes.inicializarIngredientes();
        verify(ingredienteIdManager, times(1)).anadirObjectId(ingrediente.get_id());
    }

    @Test
    void getIngredientes_shouldReturnListAndSetIds() {
        //GIVEN
        ObjectId id1 = new ObjectId();
        Document doc1 = new Document(Constantes._ID, id1);
        List<Document> docs = List.of(doc1);
        FindIterable<Document> iterable = mock(FindIterable.class);

        //WHEN
        when(mongoTemplate.getCollection(anyString())).thenReturn(mongoCollection);
        when(mongoCollection.find()).thenReturn(iterable);
        when(iterable.into(anyList())).thenAnswer(invocation -> docs);
        IngredienteDB ing1 = new IngredienteDB();
        ing1.set_id(id1);
        when(documentPojoParser.documentToProductoDB(doc1)).thenReturn(ing1);

        //THEN
        List<IngredienteDB> result = repositoryIngredientes.getIngredientes();
        assertEquals(1, result.size());
        assertEquals(id1, result.get(0).get_id());
        verify(ingredienteIdManager, times(1))
                .setIngredienteIds(any(HashMap.class));
    }

    @Test
    void getIngredienteByNombre_shouldReturnIngrediente() {
        //GIVEN
        String nombre = "Carne";
        Document doc = new Document();
        IngredienteDB ingrediente = new IngredienteDB();

        //WHEN
        when(mongoCollection.find(Filters.eq(Constantes.NOMBRE, nombre)))
                .thenReturn(mock(FindIterable.class));
        when(mongoCollection.find(Filters.eq(Constantes.NOMBRE, nombre)).first())
                .thenReturn(doc);
        when(documentPojoParser.documentToProductoDB(doc))
                .thenReturn(ingrediente);

        //THEN
        IngredienteDB result = repositoryIngredientes.getIngredienteByNombre(nombre);
        assertNotNull(result);
    }

    @Test
    void getIngredienteByNombre_ThrowExceptionWhenNotFound() {
        //GIVEN
        String nombre = " ";
        MongoCollection<Document> collection = mock(MongoCollection.class);
        FindIterable<Document> iterable = mock(FindIterable.class);

        //WHEN
        when(mongoTemplate.getCollection(anyString())).thenReturn(collection);
        when(collection.find(any(Bson.class))).thenReturn(iterable);
        when(iterable.first()).thenReturn(null);

        //THEN
        assertThrows(IngredienteNoEncontradoException.class,
                () -> repositoryIngredientes.getIngredienteByNombre(nombre));
    }


    @Test
    void getIngredienteByObjectId_shouldReturnIngrediente() {
        //GIVEN
        ObjectId id = new ObjectId();
        Document doc = new Document();
        IngredienteDB ingrediente = new IngredienteDB();

        //WHEN
        when(mongoCollection.find(Filters.eq(Constantes._ID, id)))
                .thenReturn(mock(FindIterable.class));
        when(mongoCollection.find(Filters.eq(Constantes._ID, id)).first())
                .thenReturn(doc);
        when(documentPojoParser.documentToProductoDB(doc))
                .thenReturn(ingrediente);

        //THEN
        IngredienteDB result = repositoryIngredientes.getIngredienteByObjectId(id);
        assertNotNull(result);
    }

    @Test
    void getIngredienteByObjectId_shouldReturnEmptyWhenNotFound() {
        //GIVEN
        ObjectId id = new ObjectId();
        MongoCollection<Document> collection = mock(MongoCollection.class);
        FindIterable<Document> iterable = mock(FindIterable.class);

        //WHEN
        when(mongoTemplate.getCollection(anyString())).thenReturn(collection);
        when(collection.find((Bson) any())).thenReturn(iterable);
        when(iterable.first()).thenReturn(null);

        //THEN
        IngredienteDB result = repositoryIngredientes.getIngredienteByObjectId(id);
        assertNotNull(result);
        assertNull(result.get_id()); // Devuelve objeto vacío, no excepción
    }


    @Test
    void getExtrasByPlatoDB_shouldReturnExtras() {
        //GIVEN
        ObjectId id1 = new ObjectId();
        ObjectId id2 = new ObjectId();
        Document doc1 = new Document(Constantes._ID, id1);
        Document doc2 = new Document(Constantes._ID, id2);
        List<Document> docs = List.of(doc1, doc2);
        FindIterable<Document> iterable = mock(FindIterable.class);

        //WHEN
        when(mongoTemplate.getCollection(anyString())).thenReturn(mongoCollection);
        when(mongoCollection.find()).thenReturn(iterable);
        when(iterable.into(anyList())).thenAnswer(invocation -> docs);


        IngredienteDB ingrediente1 = new IngredienteDB();
        ingrediente1.set_id(id1);
        IngredienteDB ingrediente2 = new IngredienteDB();
        ingrediente2.set_id(id2);

        when(documentPojoParser.documentToProductoDB(doc1)).thenReturn(ingrediente1);
        when(documentPojoParser.documentToProductoDB(doc2)).thenReturn(ingrediente2);

        PlatoDB plato = new PlatoDB();
        plato.setIngredientes(List.of(id1));

        //THEN
        List<IngredienteDB> result = repositoryIngredientes.getExtrasByPlatoDB(plato);
        assertEquals(1, result.size());
        assertEquals(id2, result.get(0).get_id());
        verify(ingredienteIdManager, times(1))
                .setIngredienteIds(any(HashMap.class));
    }

}
