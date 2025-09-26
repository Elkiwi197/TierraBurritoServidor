package com.tierraburritoservidor.dao.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tierraburritoservidor.dao.model.PlatoDB;
import com.tierraburritoservidor.dao.model.ProductoDB;
import com.tierraburritoservidor.dao.model.UsuarioDB;
import com.tierraburritoservidor.domain.model.Plato;
import org.bson.Document;
import org.springframework.stereotype.Component;

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
        return platoDB;
    }

    public ProductoDB documentToProductoDB(Document document) {
        ProductoDB productoDB = gson.fromJson(document.toJson(), ProductoDB.class);
        productoDB.set_id(document.getObjectId("_id"));
        return productoDB;
    }
}
