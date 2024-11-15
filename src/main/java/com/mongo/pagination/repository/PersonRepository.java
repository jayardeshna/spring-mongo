package com.mongo.pagination.repository;

import com.mongo.pagination.model.Person;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PersonRepository extends MongoRepository<Person, String> {
}
