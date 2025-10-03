package com.tierraburritoservidor.dao.util;

import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Setter
@Component
public class PedidoIdManager {


    private HashMap<ObjectId, Integer> pedidoIds = new HashMap<>();
    private HashMap<ObjectId, Integer> platosPedidoIds = new HashMap<>();

    public Integer getPedidoId(ObjectId objectId) {
        return pedidoIds.get(objectId);
    }

    public ObjectId createNewPedidoId() {
        boolean repeated = true;
        int newId = pedidoIds.size()+1;
        ObjectId objectId = new ObjectId();
        do {
            if (pedidoIds.containsKey(objectId)) {
                objectId = new ObjectId();
            } else {
                repeated = false;
            }
        } while (repeated);
        pedidoIds.put(objectId, newId);
        return objectId;
    }

    public ObjectId getPedidoObjectId(int id) {
        if (pedidoIds.containsValue(id)) {
            for (HashMap.Entry<ObjectId, Integer> entry : pedidoIds.entrySet()) {
                if (entry.getValue().equals(id)) {
                    return entry.getKey();
                }
            }
        }
        return null;
    }

    public void anadirPedidoObjectId(ObjectId objectId) {
        pedidoIds.put(objectId, pedidoIds.size()+1);
    }

    public Integer getPlatoId(ObjectId objectId) {
        return platosPedidoIds.get(objectId);
    }

    public ObjectId createNewPlatoId() {
        boolean repeated = true;
        int newId = platosPedidoIds.size()+1;
        ObjectId objectId = new ObjectId();
        do {
            if (platosPedidoIds.containsKey(objectId)) {
                objectId = new ObjectId();
            } else {
                repeated = false;
            }
        } while (repeated);
        platosPedidoIds.put(objectId, newId);
        return objectId;
    }

    public ObjectId getPlatoObjectId(int id) {
        if (platosPedidoIds.containsValue(id)) {
            for (HashMap.Entry<ObjectId, Integer> entry : platosPedidoIds.entrySet()) {
                if (entry.getValue().equals(id)) {
                    return entry.getKey();
                }
            }
        }
        return null;
    }

    public void anadirPlatoObjectId(ObjectId objectId) {
        platosPedidoIds.put(objectId, platosPedidoIds.size()+1);
    }
}

