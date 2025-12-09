package com.tierraburritoservidor.dao.repositories;

import com.mongodb.client.MongoCollection;
import com.tierraburritoservidor.common.Constantes;
import com.tierraburritoservidor.common.ConstantesInfo;
import com.tierraburritoservidor.dao.model.PedidoDB;
import com.tierraburritoservidor.dao.util.DocumentPojoParser;
import com.tierraburritoservidor.dao.util.PedidoIdManager;
import com.tierraburritoservidor.dao.util.PlatoIdManager;
import com.tierraburritoservidor.domain.model.EstadoPedido;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class RepositoryPedidosTest {

    @Mock
    private DocumentPojoParser documentPojoParser;

    @Mock
    private PlatoIdManager platoIdManager;

    @Mock
    private PedidoIdManager pedidoIdManager;

    @Mock
    private MongoTemplate mongoTemplate;
    @Mock
    private MongoConverter mongoConverter;

    @Mock
    private MongoCollection<Document> mongoCollection;

    @InjectMocks
    private RepositoryPedidos repositoryPedidos;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(mongoTemplate.getCollection(anyString())).thenReturn(mongoCollection);
        when(mongoTemplate.getConverter()).thenReturn(mongoConverter);
    }


    @Test
    void inicializarPedidos() {
        //GIVEN
        List<PedidoDB> pedidos = new ArrayList<>();
        PedidoDB pedido = new PedidoDB();
        pedido.set_id(new ObjectId());
        pedidos.add(pedido);

        //WHEN
        when(mongoTemplate.find(any(), eq(PedidoDB.class), anyString())).thenReturn(pedidos);
        when(pedidoIdManager.getPedidoId(pedido.get_id())).thenReturn(null);

        //THEN
        repositoryPedidos.inicializarPedidos();
        verify(pedidoIdManager, times(1)).getPedidoId(pedido.get_id());
    }

    @Test
    void addPedido_shouldAddPedidoSuccessfully() {
        //GIVEN
        PedidoDB pedido = new PedidoDB();
        pedido.set_id(new ObjectId());
        Document mockDocument = new Document();
        mockDocument.put("_id", pedido.get_id());

        //WHEN
        when(mongoTemplate.getCollection(anyString())).thenReturn(mongoCollection);
        when(mongoTemplate.getConverter().convertToMongoType(pedido)).thenReturn(mockDocument);
        String result = repositoryPedidos.addPedido(pedido);

        //THEN
        assertEquals("Pedido hecho", result);
        verify(mongoCollection, times(1)).insertOne(mockDocument);
        verify(pedidoIdManager, times(1)).anadirPedidoObjectId(pedido.get_id());
    }


    @Test
    void getPedidosByCorreoCliente_shouldReturnPedidosInReverseAndAddMissingIds() {
        // Given
        PedidoDB pedido1 = new PedidoDB();
        pedido1.set_id(new ObjectId());
        PedidoDB pedido2 = new PedidoDB();
        pedido2.set_id(new ObjectId());
        List<PedidoDB> pedidosMock = new ArrayList<>();
        pedidosMock.add(pedido1);
        pedidosMock.add(pedido2);

        //WHEN
        when(mongoTemplate.find(any(Query.class), eq(PedidoDB.class), eq("Pedidos")))
                .thenReturn(pedidosMock);

        //THEN
        List<PedidoDB> result = repositoryPedidos.getPedidosByCorreoCliente("cliente@correo.com");
        ArgumentCaptor<Query> queryCaptor = ArgumentCaptor.forClass(Query.class);
        verify(mongoTemplate, times(1)).find(queryCaptor.capture(), eq(PedidoDB.class), eq("Pedidos"));
        Query capturedQuery = queryCaptor.getValue();

        assertThat(capturedQuery.getQueryObject().get("correoCliente")).isEqualTo("cliente@correo.com");
        assertThat(result.get(0)).isEqualTo(pedido2);
        assertThat(result.get(1)).isEqualTo(pedido1);
        assertThat(pedidoIdManager.getPedidoId(pedido1.get_id())).isNotNull();
        assertThat(pedidoIdManager.getPedidoId(pedido2.get_id())).isNotNull();
    }

    @Test
    void getPedidosEnPreparacion_shouldReturnPedidosInReverseAndAddMissingIds() {
        //GIVEN
        PedidoDB pedido1 = new PedidoDB();
        pedido1.set_id(new ObjectId());
        PedidoDB pedido2 = new PedidoDB();
        pedido2.set_id(new ObjectId());
        List<PedidoDB> pedidosMock = new ArrayList<>();
        pedidosMock.add(pedido1);
        pedidosMock.add(pedido2);

        //WHEN
        when(mongoTemplate.find(any(Query.class), eq(PedidoDB.class), eq("Pedidos")))
                .thenReturn(pedidosMock);

        //THEN
        List<PedidoDB> result = repositoryPedidos.getPedidosEnPreparacion();

        //Compruebo que llamo al MongoTemplate con la query correcta
        ArgumentCaptor<Query> queryCaptor = ArgumentCaptor.forClass(Query.class);
        verify(mongoTemplate, times(1))
                .find(queryCaptor.capture(), eq(PedidoDB.class), eq(Constantes.COLECCION_PEDIDOS));
        Query capturedQuery = queryCaptor.getValue();
        assertThat(capturedQuery.getQueryObject().get("estado"))
                .isEqualTo(EstadoPedido.EN_PREPARACION.toString());

        //Compruebo que los pedidos están en orden invertido
        assertThat(result.get(0)).isEqualTo(pedido2);
        assertThat(result.get(1)).isEqualTo(pedido1);

        //Compruebo que se añadieron los ObjectId a pedidoIdManager
        assertThat(pedidoIdManager.getPedidoId(pedido1.get_id())).isNotNull();
        assertThat(pedidoIdManager.getPedidoId(pedido2.get_id())).isNotNull();
    }

    @Test
    void repartirPedido_shouldAcceptPedido_whenPedidoDoesNotExist() {
        //WHEN
        when(mongoTemplate.find(any(Query.class), eq(PedidoDB.class), anyString())).thenReturn(Collections.emptyList());
        when(mongoTemplate.getCollection(anyString())).thenReturn(mongoCollection);
        when(pedidoIdManager.getPedidoObjectId(123)).thenReturn(new ObjectId("64dfb35b59a8f70001e3a1c4"));

        //THEN
        String resultado = repositoryPedidos.repartirPedido(123, "repartidor@correo.com");
        verify(mongoCollection, times(2)).updateOne(any(), (Bson) any());
        assertEquals(ConstantesInfo.PEDIDO_ACEPTADO, resultado);
    }

    @Test
    void repartirPedido_shouldReturnErrorMessage_whenPedidoAlreadyExists() {
        //GIVEN
        PedidoDB pedidoExistente = new PedidoDB();

        //WHEN
        when(mongoTemplate.find(any(Query.class), eq(PedidoDB.class), anyString())).thenReturn(List.of(pedidoExistente));

        //THEN
        String resultado = repositoryPedidos.repartirPedido(123, "repartidor@correo.com");
        verify(mongoTemplate, never()).getCollection(anyString());
        verify(mongoCollection, never()).updateOne(any(), (Bson) any());
        assertEquals(ConstantesInfo.NO_PUEDES_ACEPTAR_VARIOS_PEDIDOS_A_LA_VEZ, resultado);
    }

    @Test
    void repartirPedido_shouldReturnErrorMessage_whenExceptionOccurs() {
        //WHEN
        when(mongoTemplate.find(any(Query.class), eq(PedidoDB.class), anyString())).thenThrow(new RuntimeException("Error de prueba"));

        //THEN
        String resultado = repositoryPedidos.repartirPedido(123, "repartidor@correo.com");
        assertEquals(ConstantesInfo.ERROR_ACEPTANDO_PEDIDO, resultado);
    }


    @Test
    void cancelarPedido_shouldCancelPedidoSuccessfully() {
        //WHEN
        when(mongoTemplate.getCollection(anyString())).thenReturn(mongoCollection);
        when(pedidoIdManager.getPedidoObjectId(123)).thenReturn(new ObjectId("64dfb35b59a8f70001e3a1c4"));

        //THEN
        String resultado = repositoryPedidos.cancelarPedido(123, "repartidor@correo.com");
        verify(mongoCollection).updateOne(any(Bson.class), any(Bson.class));
        assertEquals(ConstantesInfo.PEDIDO_CANCELADO, resultado);
    }

    @Test
    void cancelarPedido_shouldReturnErrorMessage_whenExceptionOccurs() {
        //WHEN
        when(mongoTemplate.getCollection(anyString())).thenThrow(new RuntimeException("Error de prueba"));

        //THEN
        String resultado = repositoryPedidos.cancelarPedido(123, "repartidor@correo.com");
        assertEquals(ConstantesInfo.ERROR_CANCELANDO_PEDIDO, resultado);
    }

    @Test
    void getPedidoEnRepartoByRepartidor_shouldReturnPedidoWhenExists() {
        //GIVEN
        PedidoDB pedidoMock = new PedidoDB();
        pedidoMock.set_id(new ObjectId());

        //WHEN
        when(mongoTemplate.findOne(any(Query.class), eq(PedidoDB.class), eq("Pedidos")))
                .thenReturn(pedidoMock);

        //THEN
        PedidoDB resultado = repositoryPedidos.getPedidoEnRepartoByRepartidor("repartidor@correo.com");
        ArgumentCaptor<Query> queryCaptor = ArgumentCaptor.forClass(Query.class);
        verify(mongoTemplate).findOne(queryCaptor.capture(), eq(PedidoDB.class), eq("Pedidos"));
        Query queryUsada = queryCaptor.getValue();
        assertThat(queryUsada.getQueryObject().get("repartidor")).isEqualTo("repartidor@correo.com");
        assertThat(queryUsada.getQueryObject().get("estado")).isEqualTo(EstadoPedido.EN_REPARTO.name());
        assertThat(resultado).isEqualTo(pedidoMock);
    }

    @Test
    void getPedidoEnRepartoByRepartidor_shouldReturnNullWhenNoPedido() {
        //WHEN
        when(mongoTemplate.findOne(any(Query.class), eq(PedidoDB.class), eq("Pedidos")))
                .thenReturn(null);

        //THEN
        PedidoDB resultado = repositoryPedidos.getPedidoEnRepartoByRepartidor("repartidor@correo.com");
        assertThat(resultado).isNull();
    }

    @Test
    void getPedidosRepartidos_shouldReturnPedidosInReverseAndAddMissingIds() {
        //GIVEN
        PedidoDB pedido1 = new PedidoDB();
        pedido1.set_id(new ObjectId());
        PedidoDB pedido2 = new PedidoDB();
        pedido2.set_id(new ObjectId());
        List<PedidoDB> pedidosMock = new ArrayList<>();
        pedidosMock.add(pedido1);
        pedidosMock.add(pedido2);

        //WHEN
        when(mongoTemplate.find(any(Query.class), eq(PedidoDB.class), eq("Pedidos"))).thenReturn(pedidosMock);
        when(pedidoIdManager.getPedidoId(pedido1.get_id())).thenReturn(null);
        when(pedidoIdManager.getPedidoId(pedido2.get_id())).thenReturn(null);

        //THEN
        List<PedidoDB> resultado = repositoryPedidos.getPedidosRepartidos("repartidor@correo.com");
        ArgumentCaptor<Query> queryCaptor = ArgumentCaptor.forClass(Query.class);
        verify(mongoTemplate).find(queryCaptor.capture(), eq(PedidoDB.class), eq("Pedidos"));
        Query queryUsada = queryCaptor.getValue();
        assertThat(queryUsada.getQueryObject().get("repartidor")).isEqualTo("repartidor@correo.com");
        assertThat(resultado.get(0)).isEqualTo(pedido2);
        assertThat(resultado.get(1)).isEqualTo(pedido1);
        verify(pedidoIdManager).anadirPedidoObjectId(pedido1.get_id());
        verify(pedidoIdManager).anadirPedidoObjectId(pedido2.get_id());
    }

    @Test
    void entregarPedido_shouldUpdateEstadoAndReturnSuccess() {
        //WHEN
        when(mongoTemplate.getCollection(anyString())).thenReturn(mongoCollection);
        when(pedidoIdManager.getPedidoObjectId(123)).thenReturn(new ObjectId("64dfb35b59a8f70001e3a1c4"));

        //THEN
        String resultado = repositoryPedidos.entregarPedido(123, "repartidor@correo.com");
        verify(mongoCollection).updateOne(any(Bson.class), any(Bson.class));
        assertEquals(ConstantesInfo.PEDIDO_ENTREGADO, resultado);
    }

    @Test
    void entregarPedido_shouldReturnErrorMessage_whenException() {
        //WHEN
        when(mongoTemplate.getCollection(anyString())).thenThrow(new RuntimeException("Error de prueba"));

        //THEN
        String resultado = repositoryPedidos.entregarPedido(123, "repartidor@correo.com");
        assertEquals(ConstantesInfo.ERROR_ENTREGANDO_PEDIDO, resultado);
    }

    // Tests para noRepartirEstePedido

    @Test
    void noRepartirEstePedido_shouldUpdateEstadoUnsetRepartidorAndReturnSuccess() {
        //WHEN
        when(mongoTemplate.getCollection(anyString())).thenReturn(mongoCollection);
        when(pedidoIdManager.getPedidoObjectId(123)).thenReturn(new ObjectId("64dfb35b59a8f70001e3a1c4"));

        //THEN
        String resultado = repositoryPedidos.noRepartirEstePedido(123);
        verify(mongoCollection, times(2)).updateOne(any(Bson.class), any(Bson.class));
        assertEquals(ConstantesInfo.PEDIDO_SIN_REPARTIDOR, resultado);
    }

    @Test
    void noRepartirEstePedido_shouldReturnErrorMessage_whenException() {
        //WHEN
        when(mongoTemplate.getCollection(anyString())).thenThrow(new RuntimeException("Error de prueba"));

        //THEN
        String resultado = repositoryPedidos.noRepartirEstePedido(123);
        assertEquals(ConstantesInfo.ERROR_ACTUALIZANDO_PEDIDO, resultado);
    }
}