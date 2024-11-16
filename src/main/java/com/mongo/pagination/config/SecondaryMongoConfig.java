package com.mongo.pagination.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(
        basePackages = "com.mongo.pagination.repository.secondary",
        mongoTemplateRef = "secondaryMongoTemplate"
)
public class SecondaryMongoConfig {

    @Bean(name = "secondaryMongoTemplate")
    public MongoTemplate secondaryMongoTemplate(
            @Qualifier("secondaryMongoClient") MongoClient mongoClient) {
        return new MongoTemplate(mongoClient, "new_demo");
    }

    @Bean(name = "secondaryMongoClient")
    public MongoClient mongoClient(
            @Value("${spring.data.mongodb.secondary.uri}") String connectionString) {
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString("mongodb://localhost:27017/new_demo"))
                .build();
        return MongoClients.create(settings);
    }

}
