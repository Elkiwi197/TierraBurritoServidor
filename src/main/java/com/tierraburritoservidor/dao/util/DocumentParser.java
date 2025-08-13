package com.tierraburritoservidor.dao.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tierraburritoservidor.dao.model.UsuarioDB;
import org.bson.Document;
import org.springframework.stereotype.Component;

@Component
public class DocumentParser {

    private Gson gson = new GsonBuilder()
            .create();

    public UsuarioDB documentToUsuarioDB(Document document) {
        //comprobar si se pasa bien el id
        UsuarioDB usuarioDB = gson.fromJson(document.toJson(), UsuarioDB.class);
        usuarioDB.setId(document.getObjectId("_id"));
        return usuarioDB;
    }
}
