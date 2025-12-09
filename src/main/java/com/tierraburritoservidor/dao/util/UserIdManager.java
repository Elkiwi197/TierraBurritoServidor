package com.tierraburritoservidor.dao.util;

import jakarta.inject.Singleton;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Setter
@Component
public class UserIdManager {


    private Map<ObjectId, Integer> userIds = new HashMap<>();

    public Integer getId(ObjectId objectId) {
        return userIds.get(objectId);
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

    public void anadirObjectId(ObjectId objectId) {
        userIds.put(objectId, userIds.size()+1);
    }
}


