package com.tierraburritoservidor.dao.util;

import jakarta.inject.Singleton;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Setter
@Component
public class PlatoIdManager {


    private HashMap<ObjectId, Integer> platoIds = new HashMap<>();


    public Integer getId(ObjectId objectId) {
        return platoIds.get(objectId);
    }

    public ObjectId createNewId() {
        boolean repeated = true;
        int newId = platoIds.size()+1;
        ObjectId objectId = new ObjectId();
        do {
            if (platoIds.containsKey(objectId)) {
                objectId = new ObjectId();
            } else {
                repeated = false;
            }
        } while (repeated);
        platoIds.put(objectId, newId);
        return objectId;
    }

    public ObjectId getObjectId(int id) {
        if (platoIds.containsValue(id)) {
            for (HashMap.Entry<ObjectId, Integer> entry : platoIds.entrySet()) {
                if (entry.getValue().equals(id)) {
                    return entry.getKey();
                }
            }
        }
        return null;
    }

    public void anadirObjectId(ObjectId objectId) {
        platoIds.put(objectId, platoIds.size()+1);

    }
}

