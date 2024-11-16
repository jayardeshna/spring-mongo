package com.mongo.pagination.repository.primary;

import com.mongo.pagination.model.Person;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PersonRepositoryPrimary extends MongoRepository<Person, String> {
}
