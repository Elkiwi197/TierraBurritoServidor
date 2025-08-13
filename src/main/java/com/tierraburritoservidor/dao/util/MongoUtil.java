package com.tierraburritoservidor.dao.util;


import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class MongoUtil {
    private static final String CONNECTION_STRING = "mongodb://root:root@localhost:27017/";
    private static final String DATABASE_NAME = "TierraBurrito";
    private static MongoClient mongo = null;

    private MongoUtil(MongoClient mongoClient) {

    }

    public static MongoDatabase getDatabase() {
        MongoDatabase db = null;
        try {
            mongo = MongoClients.create(CONNECTION_STRING);
            db = mongo.getDatabase(DATABASE_NAME);
        } catch (Exception e) {
            log.error("Could not get database {}", e.getMessage(), e);
        }
        return db;
    }


    public MongoClient mongoClient() {
        return MongoClients.create("mongodb://root:root@localhost:27017/TierraBurrito");
    }

    public static void close(){
        mongo.close();
    }

    @Bean
    public MongoTemplate mongoTemplate() {
        return new MongoTemplate(mongoClient(), DATABASE_NAME);
    }
}