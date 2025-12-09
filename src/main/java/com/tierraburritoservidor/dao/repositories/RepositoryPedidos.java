package com.tierraburritoservidor.dao.repositories;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.client.MongoCollection;
import com.tierraburritoservidor.common.Constantes;
import com.tierraburritoservidor.common.ConstantesInfo;
import com.tierraburritoservidor.dao.RepositoryPedidosInterface;
import com.tierraburritoservidor.dao.model.PedidoDB;
import com.tierraburritoservidor.dao.util.PedidoIdManager;
import com.tierraburritoservidor.domain.model.EstadoPedido;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;
import static com.mongodb.client.model.Updates.unset;

@Repository
@RequiredArgsConstructor
@Log4j2
public class RepositoryPedidos implements RepositoryPedidosInterface {

    private final String COLLECTION_NAME = Constantes.COLECCION_PEDIDOS;

    private final PedidoIdManager pedidoIdManager;
    private final MongoTemplate mongoTemplate;


    @EventListener(ApplicationReadyEvent.class)
    @Override
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
            log.error(ConstantesInfo.ERROR_INICIALIZANDO_PEDIDOS, e.getMessage(), e);
        }
    }


    @Override
    public String addPedido(PedidoDB pedido) {
        try {
            MongoCollection<Document> collection = mongoTemplate.getCollection(COLLECTION_NAME);

            pedido.setEstado(EstadoPedido.EN_PREPARACION.name());
           // Document pedidoDocument = Document.parse(gson.toJson(pedido));
            Document pedidoDocument = (Document) mongoTemplate.getConverter().convertToMongoType(pedido);
            if (pedidoDocument!= null) {
                collection.insertOne(pedidoDocument);
                ObjectId generatedObjectId = pedidoDocument.getObjectId(Constantes._ID);
                pedidoIdManager.anadirPedidoObjectId(generatedObjectId);
                log.info(ConstantesInfo.PEDIDO_ANADIDO_ + pedidoIdManager.getPedidoId(pedido.get_id()) + ", " + pedido.get_id());
            } else {
                log.error(ConstantesInfo.ERROR_CREANDO_PEDIDO, ConstantesInfo.PEDIDO_NULO);
            }
        } catch (Exception e) {
            log.error(ConstantesInfo.ERROR_CREANDO_PEDIDO, e.getMessage(), e);
        }
        return ConstantesInfo.PEDIDO_HECHO;
    }


    @Override
    public List<PedidoDB> getPedidosByCorreoCliente(String correoCliente) {
        List<PedidoDB> pedidos = new ArrayList<>();
        try {

            Query query = new Query();
            query.addCriteria(Criteria.where(Constantes.CORREO_CLIENTE).is(correoCliente));
            pedidos = mongoTemplate.find(query, PedidoDB.class, COLLECTION_NAME);

            pedidos.forEach(pedido -> {
                if (pedidoIdManager.getPedidoId(pedido.get_id()) == null) {
                    pedidoIdManager.anadirPedidoObjectId(pedido.get_id());
                }
            });
            pedidos = pedidos.reversed();
        } catch (Exception e) {
            log.error(ConstantesInfo.ERROR_LEYENDO_PEDIDOS_DE_CLIENTE, e.getMessage(), e);

        }
        return pedidos;
    }

    @Override
    public List<PedidoDB> getPedidosEnPreparacion() {
        List<PedidoDB> pedidos = new ArrayList<>();
        try {

            Query query = new Query();
            query.addCriteria(Criteria.where(Constantes.ESTADO).is(EstadoPedido.EN_PREPARACION.toString()));
            pedidos = mongoTemplate.find(query, PedidoDB.class, COLLECTION_NAME);

            pedidos.forEach(pedido -> {
                if (pedidoIdManager.getPedidoId(pedido.get_id()) == null) {
                    pedidoIdManager.anadirPedidoObjectId(pedido.get_id());
                }
            });
            pedidos = pedidos.reversed();
        } catch (Exception e) {
            log.error(ConstantesInfo.ERROR_LEYENDO_PEDIDOS_EN_PREPARACION, e.getMessage(), e);

        }
        return pedidos;
    }

    @Override
    public String repartirPedido(int idPedido, String correoRepartidor) {
        try {

            Query query = new Query();
            query.addCriteria(
                    Criteria.where(Constantes._ID).is(idPedido)

            );

            List<PedidoDB> pedidos = mongoTemplate.find(query, PedidoDB.class, COLLECTION_NAME);

            if (pedidos.isEmpty()) {
                MongoCollection<Document> collection = mongoTemplate.getCollection(COLLECTION_NAME);
                collection.updateOne(
                        eq(Constantes._ID, pedidoIdManager.getPedidoObjectId(idPedido)),
                        set(Constantes.ESTADO, EstadoPedido.EN_REPARTO)
                );
                collection.updateOne(
                        eq(Constantes._ID, pedidoIdManager.getPedidoObjectId(idPedido)),
                        set(Constantes.REPARTIDOR, correoRepartidor)
                );
                log.info(ConstantesInfo.PEDIDO_ACEPTADO_POR + correoRepartidor);
            } else {
                return ConstantesInfo.NO_PUEDES_ACEPTAR_VARIOS_PEDIDOS_A_LA_VEZ;
            }
        } catch (Exception e) {
            log.error(ConstantesInfo.ERROR_ACEPTANDO_PEDIDO_, e.getMessage(), e);
            return ConstantesInfo.ERROR_ACEPTANDO_PEDIDO;
        }
        return ConstantesInfo.PEDIDO_ACEPTADO;
    }

    @Override
    public String cancelarPedido(int idPedido, String correoRepartidor) {
        try {
            MongoCollection<Document> collection = mongoTemplate.getCollection(COLLECTION_NAME);
            collection.updateOne(
                    eq(Constantes._ID, pedidoIdManager.getPedidoObjectId(idPedido)),
                    set(Constantes.ESTADO, EstadoPedido.CANCELADO)
            );
            log.info(Constantes.PEDIDO + " " + idPedido + ConstantesInfo.CANCELADO_POR + correoRepartidor);
        } catch (Exception e) {
            log.error(ConstantesInfo.ERROR_CANCELANDO_PEDIDO_, e.getMessage(), e);
            return ConstantesInfo.ERROR_CANCELANDO_PEDIDO;
        }
        return ConstantesInfo.PEDIDO_CANCELADO;
    }

    @Override
    public PedidoDB getPedidoEnRepartoByRepartidor(String correoRepartidor) {
        PedidoDB pedidoDB = null;
        try {
            Query query = new Query();
            query.addCriteria(Criteria.where(Constantes.REPARTIDOR).is(correoRepartidor)
                    .and(Constantes.ESTADO).is(EstadoPedido.EN_REPARTO.name()));
            pedidoDB = mongoTemplate.findOne(query, PedidoDB.class, COLLECTION_NAME);
            if (pedidoDB == null) {
                log.info(ConstantesInfo.REPARTIDOR_SIN_PEDIDOS_ACEPTADOS);
            }
        } catch (Exception e) {
            log.error(ConstantesInfo.ERROR_LEYENDO_PEDIDO_ACEPTADO, e.getMessage(), e);
        }
        return pedidoDB;
    }

    @Override
    public List<PedidoDB> getPedidosRepartidos(String correoRepartidor) {
        List<PedidoDB> pedidos = new ArrayList<>();
        try {

            Query query = new Query();
            query.addCriteria(Criteria.where(Constantes.REPARTIDOR).is(correoRepartidor));
            pedidos = mongoTemplate.find(query, PedidoDB.class, COLLECTION_NAME);

            pedidos.forEach(pedido -> {
                if (pedidoIdManager.getPedidoId(pedido.get_id()) == null) {
                    pedidoIdManager.anadirPedidoObjectId(pedido.get_id());
                }
            });
            pedidos = pedidos.reversed();
        } catch (Exception e) {
            log.error(ConstantesInfo.ERROR_LEYENDO_PEDIDOS_DE_REPARTIDOR, e.getMessage(), e);

        }
        return pedidos;
    }

    @Override
    public String entregarPedido(int idPedido, String correoRepartidor) {
        try {
            MongoCollection<Document> collection = mongoTemplate.getCollection(COLLECTION_NAME);
            collection.updateOne(
                    eq(Constantes._ID, pedidoIdManager.getPedidoObjectId(idPedido)),
                    set(Constantes.ESTADO, EstadoPedido.ENTREGADO)
            );
            log.info(Constantes.PEDIDO + " " + idPedido + ConstantesInfo.ENTREGADO_POR + correoRepartidor);
        } catch (Exception e) {
            log.error(ConstantesInfo.ERROR_ENTREGANDO_PEDIDO_, e.getMessage(), e);
            return ConstantesInfo.ERROR_ENTREGANDO_PEDIDO;
        }
        return ConstantesInfo.PEDIDO_ENTREGADO;
    }

    @Override
    public String noRepartirEstePedido(int idPedido) {
        try {
            MongoCollection<Document> collection = mongoTemplate.getCollection(COLLECTION_NAME);
            collection.updateOne(
                    eq(Constantes._ID, pedidoIdManager.getPedidoObjectId(idPedido)),
                    set(Constantes.ESTADO, EstadoPedido.EN_PREPARACION)
            );
            collection.updateOne(
                    eq(Constantes._ID, pedidoIdManager.getPedidoObjectId(idPedido)),
                    unset(Constantes.REPARTIDOR)
            );
            log.info(Constantes.PEDIDO + " " + idPedido + Constantes.EN_PREPARACION);
        } catch (Exception e) {
            log.error(ConstantesInfo.ERROR_ACTUALIZANDO_PEDIDO_, e.getMessage(), e);
            return ConstantesInfo.ERROR_ACTUALIZANDO_PEDIDO;
        }
        return ConstantesInfo.PEDIDO_SIN_REPARTIDOR;
    }
}

