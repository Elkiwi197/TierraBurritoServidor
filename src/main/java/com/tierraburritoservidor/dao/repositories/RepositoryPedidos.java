package com.tierraburritoservidor.dao.repositories;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.client.MongoCollection;
import com.tierraburritoservidor.common.Constantes;
import com.tierraburritoservidor.common.ConstantesErrores;
import com.tierraburritoservidor.dao.RepositoryPedidosInterface;
import com.tierraburritoservidor.dao.model.PedidoDB;
import com.tierraburritoservidor.dao.model.PlatoDB;
import com.tierraburritoservidor.dao.util.DocumentPojoParser;
import com.tierraburritoservidor.dao.util.PedidoIdManager;
import com.tierraburritoservidor.dao.util.ProductoIdManager;
import com.tierraburritoservidor.domain.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    private List<Pedido> pedidos = new ArrayList<>(Arrays.asList(
            new Pedido(1, "Calle falsa 123", "pepe@correo.es", List.of(
                    new Plato(4, "Desnudo", List.of(
                            new Producto(4, Ingredientes.CARNITAS.name(), 3.60, "https://www.tierraburritos.com/wp-content/uploads/Carnitas-1-1140x1050.jpg"),
                            new Producto(1, Ingredientes.ARROZ_BLANCO.name(), 0.0, "https://www.tierraburritos.com/wp-content/uploads/ArrozBlanco-1140x1050.png"),
                            new Producto(10, Ingredientes.MAIZ.name(), 0.0, "https://www.tierraburritos.com/wp-content/uploads/Maiz-1-1140x1050.jpg"),
                            new Producto(20, Ingredientes.VERDURAS.name(), 0.0, "https://www.tierraburritos.com/wp-content/uploads/Verduras-1140x1050.png")
                    ), List.of(), 10.99, "https://www.tierraburritos.com/wp-content/uploads/Desnudo_1-2.jpg"),
                    new Plato(3, "Tacos", List.of(
                            new Producto(4, Ingredientes.CARNITAS.name(), 3.60, "https://www.tierraburritos.com/wp-content/uploads/Carnitas-1-1140x1050.jpg"),
                            new Producto(20, Ingredientes.VERDURAS.name(), 0.0, "https://www.tierraburritos.com/wp-content/uploads/Verduras-1140x1050.png"),
                            new Producto(13, Ingredientes.QUESO_RALLADO.name(), 0.0, "https://www.tierraburritos.com/wp-content/uploads/Queso-1140x1050.jpg"),
                            new Producto(11, Ingredientes.PICO_DE_GALLO.name(), 0.0, "https://www.tierraburritos.com/wp-content/uploads/Picodegallo-1140x1050.jpg"),
                            new Producto(7, Ingredientes.GUACAMOLE.name(), 2.50, "https://www.tierraburritos.com/wp-content/uploads/Guacamole-1140x1050.png"),
                            new Producto(19, Ingredientes.SALSA_DE_QUESO.name(), 0.0, "https://www.tierraburritos.com/wp-content/uploads/SalsaQueso-1140x1050.jpg")), List.of(), 8.99, "https://www.tierraburritos.com/wp-content/uploads/10_Tacos-1.jpg")
            ), List.of(), 19.98, EstadoPedido.ENTREGADO)));


    public String addPedido(PedidoDB pedido) {
//        int id = 0;
//        boolean repetido = true;
//        while (repetido) {
//            id = (int) (Math.random() * 100 + 1);
//            int finalId = id;
//            if (pedidos.stream().noneMatch(p -> p.getId() == finalId)) {
//                repetido = false;
//            }
//        }
//        pedido.setEstado(EstadoPedido.EN_PREPARACION);
//        pedido.setId(id);
//        pedidos.add(pedido);
//        return Constantes.PEDIDO_HECHO;

        try {
            MongoCollection<Document> collection = mongoTemplate.getCollection(COLLECTION_NAME);

            pedido.setEstado(EstadoPedido.EN_PREPARACION.name());
            Document pedidoDocument = (Document) mongoTemplate.getConverter().convertToMongoType(pedido); //todo mapear asi los objetos
            collection.insertOne(pedidoDocument);
            ObjectId generatedObjectId = pedidoDocument.getObjectId("_id");
            pedidoIdManager.anadirPedidoObjectId(generatedObjectId);
        } catch (Exception e) {
            log.error(ConstantesErrores.ERROR_CREANDO_PEDIDO, e.getMessage(), e);
            throw new RuntimeException(ConstantesErrores.ERROR_CREANDO_PEDIDO);
        }
        return Constantes.PEDIDO_HECHO;
    }


    public List<PedidoDB> getPedidosByCorreo(String correoCliente) {
        List<PedidoDB> pedidos = new ArrayList<>();
        try {
            MongoCollection<Document> collection = mongoTemplate.getCollection(COLLECTION_NAME);

            //List<Document> documents = (List<Document>) mongoTemplate.getConverter().convertToMongoType(collection);
            Query query = new Query();
            query.addCriteria(Criteria.where("correoCliente").is(correoCliente));
            pedidos = mongoTemplate.find(query, PedidoDB.class, COLLECTION_NAME);

            //(Filters.eq("correoCliente", correoCliente)).into(new ArrayList<>());

//            documents.forEach(document ->
//                    pedidos.add(documentPojoParser.documentToPedidoDB(document)));

            pedidos.forEach(pedido -> {
                if (pedidoIdManager.getPedidoId(pedido.get_id()) == null) {
                    pedidoIdManager.anadirPedidoObjectId(pedido.get_id());
                }
            });
//            documents.forEach(pedido -> pedido.getList("platos", PlatoDB.class)
//                    .forEach(plato -> {
//                        List<ObjectId> ingredientes = new ArrayList<>();
//                        plato.getIngredientes().forEach(ingrediente -> {
//                            ingredientes.add(ingrediente);
//                        });
//                        if (pedidoIdManager.getPlatoId(plato.get_id()) == null) {
//                            pedidoIdManager.anadirPlatoObjectId(plato.get_id());
//                        } else {
//                            pedidoIdManager.anadirPlatoObjectId(plato.get_id());
//                        }
//                    }));
        } catch (Exception e) {
            log.error("Error al obtener pedidos por correo: {}", e.getMessage(), e);

        }
        return pedidos;
    }


}

