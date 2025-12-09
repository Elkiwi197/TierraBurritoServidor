package com.tierraburritoservidor.dao.util;

import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Setter
@Component
public class IngredienteIdManager {


    private HashMap<ObjectId, Integer> ingredienteIds = new HashMap<>();

    public Integer getId(ObjectId objectId) {
        return ingredienteIds.get(objectId);
    }

    public ObjectId createNewId() {
        boolean repeated = true;
        int newId = ingredienteIds.size()+1;
        ObjectId objectId = new ObjectId();
        do {
            if (ingredienteIds.containsKey(objectId)) {
                objectId = new ObjectId();
            } else {
                repeated = false;
            }
        } while (repeated);
        ingredienteIds.put(objectId, newId);
        return objectId;
    }

    public ObjectId getObjectId(int id) {
        if (ingredienteIds.containsValue(id)) {
            for (HashMap.Entry<ObjectId, Integer> entry : ingredienteIds.entrySet()) {
                if (entry.getValue().equals(id)) {
                    return entry.getKey();
                }
            }
        }
        return null;
    }

    public void anadirObjectId(ObjectId objectId) {
        ingredienteIds.put(objectId, ingredienteIds.size()+1);
    }
}