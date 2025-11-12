package com.tierraburritoservidor.dao.repositories;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.client.MongoCollection;
import com.tierraburritoservidor.common.Constantes;
import com.tierraburritoservidor.common.ConstantesErrores;
import com.tierraburritoservidor.dao.RepositoryPedidosInterface;
import com.tierraburritoservidor.dao.model.PedidoDB;
import com.tierraburritoservidor.dao.util.DocumentPojoParser;
import com.tierraburritoservidor.dao.util.PedidoIdManager;
import com.tierraburritoservidor.dao.util.ProductoIdManager;
import com.tierraburritoservidor.domain.model.EstadoPedido;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;

@Repository
@RequiredArgsConstructor
@Log4j2
public class RepositoryPedidos implements RepositoryPedidosInterface {

    private final String COLLECTION_NAME = "Pedidos";

    private final DocumentPojoParser documentPojoParser;
    private final PedidoIdManager pedidoIdManager;
    private final ProductoIdManager productoIdManager;
    private final MongoTemplate mongoTemplate;
    private final Gson gson = new GsonBuilder()
            .create();


    public String addPedido(PedidoDB pedido) {
        try {
            MongoCollection<Document> collection = mongoTemplate.getCollection(COLLECTION_NAME);

            pedido.setEstado(EstadoPedido.EN_PREPARACION.name());
            Document pedidoDocument = (Document) mongoTemplate.getConverter().convertToMongoType(pedido); //todo mapear asi los objetos
            collection.insertOne(pedidoDocument);
            ObjectId generatedObjectId = pedidoDocument.getObjectId("_id");
            pedidoIdManager.anadirPedidoObjectId(generatedObjectId);
            log.info("Pedido añadido: " + pedidoIdManager.getPedidoId(pedido.get_id()) + ", " + pedido.get_id());
        } catch (Exception e) {
            log.error(ConstantesErrores.ERROR_CREANDO_PEDIDO, e.getMessage(), e);
            throw new RuntimeException(ConstantesErrores.ERROR_CREANDO_PEDIDO);
        }
        return Constantes.PEDIDO_HECHO;
    }


    public List<PedidoDB> getPedidosByCorreoCliente(String correoCliente) {
        List<PedidoDB> pedidos = new ArrayList<>();
        try {

            Query query = new Query();
            query.addCriteria(Criteria.where("correoCliente").is(correoCliente));
            pedidos = mongoTemplate.find(query, PedidoDB.class, COLLECTION_NAME);

            pedidos.forEach(pedido -> {
                if (pedidoIdManager.getPedidoId(pedido.get_id()) == null) {
                    pedidoIdManager.anadirPedidoObjectId(pedido.get_id());
                }
            });
        } catch (Exception e) {
            log.error("Error al obtener pedidos por correo: {}", e.getMessage(), e);

        }
        return pedidos;
    }

    @Override
    public List<PedidoDB> getPedidosEnPreparacion() {
        List<PedidoDB> pedidos = new ArrayList<>();
        try {

            Query query = new Query();
            query.addCriteria(Criteria.where("estado").is(EstadoPedido.EN_PREPARACION.toString()));
            pedidos = mongoTemplate.find(query, PedidoDB.class, COLLECTION_NAME);

            pedidos.forEach(pedido -> {
                if (pedidoIdManager.getPedidoId(pedido.get_id()) == null) {
                    pedidoIdManager.anadirPedidoObjectId(pedido.get_id());
                }
            });
        } catch (Exception e) {
            log.error("Error al obtener pedidos por correo: {}", e.getMessage(), e);

        }
        return pedidos;
    }

    @Override
    public String aceptarPedido(int idPedido, String correoRepartidor) {
        try {

                Query query = new Query();
            query.addCriteria(
                    Criteria.where("repartidor").is(correoRepartidor)
                            .and("estado").in(
                                    EstadoPedido.ACEPTADO.toString(),
                                    EstadoPedido.EN_REPARTO.toString()
                            )
            );

            List<PedidoDB> pedidos = mongoTemplate.find(query, PedidoDB.class, COLLECTION_NAME);

            if (pedidos.isEmpty()) {
                MongoCollection<Document> collection = mongoTemplate.getCollection(COLLECTION_NAME);
                collection.updateOne(
                        eq("_id", pedidoIdManager.getPedidoObjectId(idPedido)),
                        set("estado", EstadoPedido.ACEPTADO)
                );
                collection.updateOne(
                        eq("_id", pedidoIdManager.getPedidoObjectId(idPedido)),
                        set("repartidor", correoRepartidor)
                );
                log.info("Pedido aceptado por " + correoRepartidor);
            } else {
                return Constantes.NO_PUEDES_ACEPTAR_VARIOS_PEDIDOS_A_LA_VEZ;
            }
        } catch (Exception e) {
            log.error("Error actualizando pedido: {}", e.getMessage(), e);
            return ConstantesErrores.ERROR_ACEPTANDO_PEDIDO;
        }
        return Constantes.PEDIDO_ACEPTADO;
    }

    @Override
    public String cancelarPedido(int idPedido, String correo) {
        try {
            MongoCollection<Document> collection = mongoTemplate.getCollection(COLLECTION_NAME);
            collection.updateOne(
                    eq("_id", pedidoIdManager.getPedidoObjectId(idPedido)),
                    set("estado", EstadoPedido.CANCELADO)
            );
            log.info("Pedido " + idPedido + " cancelado por " + correo);
        } catch (Exception e) {
            log.error("Error actualizando pedido: {}", e.getMessage(), e);
            return ConstantesErrores.ERROR_CANCELANDO_PEDIDO;
        }
        return Constantes.PEDIDO_CANCELADO;
    }

    @Override
    public PedidoDB getPedidoAceptado(String correoRepartidor) {
        PedidoDB pedidoDB = null;
        try {
            Query query = new Query();
            query.addCriteria(Criteria.where("repartidor").is(correoRepartidor)
                    .and("estado").is(EstadoPedido.ACEPTADO.name()));
            pedidoDB = mongoTemplate.findOne(query, PedidoDB.class, COLLECTION_NAME);
            if (pedidoDB == null){
                log.info("El repartidor no tiene ningún pedido aceptado");
            }
        } catch (Exception e) {
            log.error("Error al obtener pedido aceptado: {}", e.getMessage(), e);
        }
        return pedidoDB;
    }

    public void inicializarPedidos() {
        try {
            List<PedidoDB> pedidos = new ArrayList<>();
            Query query = new Query();
            pedidos = mongoTemplate.find(query, PedidoDB.class, COLLECTION_NAME);

            pedidos.forEach(pedido -> {
                if (pedidoIdManager.getPedidoId(pedido.get_id()) == null) {
                    pedidoIdManager.anadirPedidoObjectId(pedido.get_id());
                }
            });
        } catch (Exception e) {
            log.error("Error al inicializar pedidos: {}", e.getMessage(), e);

        }
    }
}

