package com.tierraburritoservidor.dao.util;

import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Setter
@Component
public class ProductoIdManager {


    private HashMap<ObjectId, Integer> productoIds = new HashMap<>();

    public Integer getId(ObjectId objectId) {
        return productoIds.get(objectId);
    }

    public ObjectId createNewId() {
        boolean repeated = true;
        int newId = productoIds.size()+1;
        ObjectId objectId = new ObjectId();
        do {
            if (productoIds.containsKey(objectId)) {
                objectId = new ObjectId();
            } else {
                repeated = false;
            }
        } while (repeated);
        productoIds.put(objectId, newId);
        return objectId;
    }

    public ObjectId getObjectId(int id) {
        if (productoIds.containsValue(id)) {
            for (HashMap.Entry<ObjectId, Integer> entry : productoIds.entrySet()) {
                if (entry.getValue().equals(id)) {
                    return entry.getKey();
                }
            }
        }
        return null;
    }

    public void anadirObjectId(ObjectId objectId) {
        productoIds.put(objectId, productoIds.size()+1);

    }
}