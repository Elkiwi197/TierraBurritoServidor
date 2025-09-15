package com.tierraburritoservidor.dao.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tierraburritoservidor.dao.model.UsuarioDB;
import org.bson.Document;
import org.springframework.stereotype.Component;

@Component
public class DocumentPojoParser {

    private Gson gson = new GsonBuilder()
            .create();

    public UsuarioDB documentToUsuarioDB(Document document) {
        //este metodo se usa al insertar un usuario nuevo y al recibirlo de la bbdd, por lo que al añadirlo
        // se pasa un objectid que mongo cambia automaticamente pero sigue en el useridmanager.
        // Esto significa que el objectid del useridmanager solo existe ahi, y el objectid de la bbdd
        // nunca coincidira con el del useridmanager
        UsuarioDB usuarioDB = gson.fromJson(document.toJson(), UsuarioDB.class);
        usuarioDB.set_id(document.getObjectId("_id"));
        return usuarioDB;
    }
}
