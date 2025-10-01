package com.tierraburritoservidor.dao.util;

import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Setter
@Component
public class PedidoIdManager {


    private HashMap<ObjectId, Integer> pedidoIds = new HashMap<>();


    public Integer getId(ObjectId objectId) {
        return pedidoIds.get(objectId);
    }

    public ObjectId createNewId() {
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

    public ObjectId getObjectId(int id) {
        if (pedidoIds.containsValue(id)) {
            for (HashMap.Entry<ObjectId, Integer> entry : pedidoIds.entrySet()) {
                if (entry.getValue().equals(id)) {
                    return entry.getKey();
                }
            }
        }
        return null;
    }

    public void anadirObjectId(ObjectId objectId) {
        pedidoIds.put(objectId, pedidoIds.size()+1);

    }
}

