package com.tierraburritoservidor.config;


import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.tierraburritoservidor.common.Constantes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
public class ConfigurationMongo {

    @Bean
    public MongoClient mongoClient() {
        return MongoClients.create("mongodb://root:root@localhost:27017/");
    }

    @Bean
    public MongoTemplate mongoTemplate(MongoClient mongoClient) {
        return new MongoTemplate(mongoClient, Constantes.TIERRA_BURRITO);
    }
}

