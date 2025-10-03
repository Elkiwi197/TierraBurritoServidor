package com.tierraburritoservidor.dao.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tierraburritoservidor.dao.model.PedidoDB;
import com.tierraburritoservidor.dao.model.PlatoDB;
import com.tierraburritoservidor.dao.model.ProductoDB;
import com.tierraburritoservidor.dao.model.UsuarioDB;
import com.tierraburritoservidor.domain.model.Plato;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DocumentPojoParser {

    private Gson gson = new GsonBuilder()
            .create();

    public UsuarioDB documentToUsuarioDB(Document document) {
        UsuarioDB usuarioDB = gson.fromJson(document.toJson(), UsuarioDB.class);
        usuarioDB.set_id(document.getObjectId("_id"));
        return usuarioDB;
    }

    public PlatoDB documentToPlatoDB(Document document) {
        PlatoDB platoDB = gson.fromJson(document.toJson(), PlatoDB.class);
        platoDB.set_id(document.getObjectId("_id"));
        List<ObjectId> ingredientes = (List<ObjectId>) document.get("ingredientes");
        platoDB.setIngredientes(ingredientes);
        return platoDB;
    }

    public ProductoDB documentToProductoDB(Document document) {
        ProductoDB productoDB = gson.fromJson(document.toJson(), ProductoDB.class);
        productoDB.set_id(document.getObjectId("_id"));
        return productoDB;
    }

    public PedidoDB documentToPedidoDB(Document document) {
        PedidoDB pedido = gson.fromJson(document.toJson(), PedidoDB.class);
        pedido.set_id(document.getObjectId("_id"));
        List<PlatoDB> platos = (List<PlatoDB>) document.get("platos");
        return pedido;
    }
}
