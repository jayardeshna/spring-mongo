package com.mongo.pagination.repository.secondary;

import com.mongo.pagination.model.Person;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PersonRepositorySecondary extends MongoRepository<Person, String> {

}
