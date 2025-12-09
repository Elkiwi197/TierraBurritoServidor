package com.tierraburritoservidor.dao.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tierraburritoservidor.common.Constantes;
import com.tierraburritoservidor.dao.model.IngredienteDB;
import com.tierraburritoservidor.dao.model.PedidoDB;
import com.tierraburritoservidor.dao.model.PlatoDB;
import com.tierraburritoservidor.dao.model.UsuarioDB;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DocumentPojoParser {

    private final Gson gson = new GsonBuilder()
            .create();

    public UsuarioDB documentToUsuarioDB(Document document) {
        UsuarioDB usuarioDB = gson.fromJson(document.toJson(), UsuarioDB.class);
        usuarioDB.set_id(document.getObjectId(Constantes._ID));
        return usuarioDB;
    }

    public PlatoDB documentToPlatoDB(Document document) {
        PlatoDB platoDB = gson.fromJson(document.toJson(), PlatoDB.class);
        platoDB.set_id(document.getObjectId(Constantes._ID));
        List<ObjectId> ingredientes = (List<ObjectId>) document.get(Constantes.INGREDIENTES);
        platoDB.setIngredientes(ingredientes);
        return platoDB;
    }

    public IngredienteDB documentToProductoDB(Document document) {
        IngredienteDB ingredienteDB = gson.fromJson(document.toJson(), IngredienteDB.class);
        ingredienteDB.set_id(document.getObjectId(Constantes._ID));
        return ingredienteDB;
    }

}
