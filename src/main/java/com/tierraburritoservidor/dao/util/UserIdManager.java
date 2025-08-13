package com.tierraburritoservidor.dao.util;

import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class UserIdManager {


    @Setter
    private HashMap<ObjectId, Integer> userIds = new HashMap<>();

    public Integer getId(ObjectId objectId) {
        return userIds.get(objectId);
    }

    public ObjectId createNewId(int newId) {
        boolean repeated = true;
        ObjectId objectId = new ObjectId();
        do {
            if (userIds.containsKey(objectId)) {
                objectId = new ObjectId();
            } else {
                repeated = false;
            }
        } while (repeated);
        userIds.put(objectId, newId);
        return objectId;
    }

    public ObjectId getObjectId(int id) {
        if (userIds.containsValue(id)) {
            for (HashMap.Entry<ObjectId, Integer> entry : userIds.entrySet()) {
                if (entry.getValue().equals(id)) {
                    return entry.getKey();
                }
            }
        }
        return null;
    }
}


