package com.tierraburritoservidor.dao.repositories;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.tierraburritoservidor.common.Constantes;
import com.tierraburritoservidor.dao.model.PlatoDB;
import com.tierraburritoservidor.dao.util.DocumentPojoParser;
import com.tierraburritoservidor.dao.util.IngredienteIdManager;
import com.tierraburritoservidor.dao.util.PlatoIdManager;
import com.tierraburritoservidor.errors.exceptions.PlatoNoEncontradoException;
import org.bson.Document;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class RepositoryPlatosTest {

    @Mock
    private DocumentPojoParser documentPojoParser;

    @Mock
    private PlatoIdManager platoIdManager;

    @Mock
    private IngredienteIdManager ingredienteIdManager;

    @Mock
    private MongoTemplate mongoTemplate;

    @Mock
    private MongoCollection<Document> mongoCollection;

    @InjectMocks
    private RepositoryPlatos repositoryPlatos;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void inicializarPlatos_shouldAddIdsForPlatos() {
        //GIVEN
        List<PlatoDB> platos = new ArrayList<>();
        PlatoDB plato = new PlatoDB();
        plato.set_id(new ObjectId());
        platos.add(plato);

        //WHEN
        when(mongoTemplate.find(any(), eq(PlatoDB.class), anyString())).thenReturn(platos);
        when(platoIdManager.getId(plato.get_id())).thenReturn(null);

        //THEN
        repositoryPlatos.inicializarPlatos();
        verify(platoIdManager, times(1)).anadirObjectId(plato.get_id());
    }

    @Test
    void getAllPlatos_shouldReturnListOfPlatosAndSetIds() {
        //GIVEN
        Document doc1 = new Document();
        ObjectId id1 = new ObjectId();
        doc1.put(Constantes._ID, id1);
        doc1.put(Constantes.INGREDIENTES, new ArrayList<ObjectId>());
        List<Document> documents = List.of(doc1);

        //WHEN
        when(mongoTemplate.getCollection(Constantes.COLECCION_PLATOS))
                .thenReturn(mongoCollection);

        FindIterable<Document> iterable = mock(FindIterable.class);
        when(mongoCollection.find())
                .thenReturn(iterable);

        when(iterable.into(anyList()))
                .thenAnswer(invocation -> {
                    List<Document> list = invocation.getArgument(0);
                    list.addAll(documents);
                    return list;
                });
        PlatoDB platoMock = new PlatoDB();
        platoMock.set_id(id1);
        platoMock.setIngredientes(new ArrayList<>());

        when(documentPojoParser.documentToPlatoDB(doc1))
                .thenReturn(platoMock);

        //THEN
        List<PlatoDB> result = repositoryPlatos.getAllPlatos();
        assertEquals(1, result.size());
        assertEquals(id1, result.get(0).get_id());
        verify(platoIdManager, times(1)).setPlatoIds(any(HashMap.class));
        verify(ingredienteIdManager, times(1)).setIngredienteIds(any(HashMap.class));
    }


    @Test
    void getPlatoById_shouldReturnPlato() {
        //GIVEN
        ObjectId objectId = new ObjectId();
        Document document = new Document();
        document.put(Constantes._ID, objectId);
        List<ObjectId> ingredientes = new ArrayList<>();
        PlatoDB plato = new PlatoDB();
        plato.set_id(objectId);
        plato.setIngredientes(ingredientes);
        document.put(Constantes.INGREDIENTES, ingredientes);

        //WHEN
        when(mongoTemplate.getCollection(Constantes.COLECCION_PLATOS)).thenReturn(mongoCollection);
        FindIterable<Document> findIterable = mock(FindIterable.class);
        when(mongoCollection.find(Filters.eq(Constantes._ID, objectId))).thenReturn(findIterable);
        when(findIterable.first()).thenReturn(document);
        when(documentPojoParser.documentToPlatoDB(document)).thenReturn(plato);

        //THEN
        PlatoDB result = repositoryPlatos.getPlatoById(objectId);
        assertEquals(objectId, result.get_id());
        verify(platoIdManager, times(0)).anadirObjectId(any());
    }


    @Test
    void getPlatoById_shouldThrowWhenPlatoNotFound() {
        //GIVEN
        ObjectId id = new ObjectId();
        FindIterable<Document> findIterable = mock(FindIterable.class);

        //WHEN
        when(mongoTemplate.getCollection(Constantes.COLECCION_PLATOS))
                .thenReturn(mongoCollection);
        when(mongoCollection.find(Filters.eq(Constantes._ID, id)))
                .thenReturn(findIterable);
        when(findIterable.first()).thenReturn(null);

        //THEN
        assertThrows(PlatoNoEncontradoException.class,
                () -> repositoryPlatos.getPlatoById(id));
    }


}
