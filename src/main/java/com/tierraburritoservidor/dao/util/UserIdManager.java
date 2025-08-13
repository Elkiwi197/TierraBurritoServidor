package com.tierraburritoservidor.dao.util;

import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class UserIdManager {


    @Setter
    private HashMap<ObjectId, Integer> patientIds = new HashMap<>();

    public Integer getId(ObjectId objectId) {
        return patientIds.get(objectId);
    }

    public ObjectId createNewID(int newId) {
        boolean repeated = true;
        ObjectId objectId = new ObjectId();
        do {
            if (patientIds.containsKey(objectId)) {
                objectId = new ObjectId();
            } else {
                repeated = false;
            }
        } while (repeated);
        patientIds.put(objectId, newId);
        return objectId;
    }

    public ObjectId getObjectId(int id) {
        if (patientIds.containsValue(id)) {
            for (HashMap.Entry<ObjectId, Integer> entry : patientIds.entrySet()) {
                if (entry.getValue().equals(id)) {
                    return entry.getKey();
                }
            }
        }
        return null;
    }
}


