package com.mongo.pagination.service.impl;

import com.mongo.pagination.model.DataTableResponse;
import com.mongo.pagination.model.Person;
import com.mongo.pagination.repository.primary.PersonRepositoryPrimary;
import com.mongo.pagination.repository.secondary.PersonRepositorySecondary;
import com.mongo.pagination.service.PersonService;
import com.mongo.pagination.transfer.UserTransfer;
import com.mongodb.client.MongoCursor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PersonServiceImpl implements PersonService {

    @Autowired
    private PersonRepositoryPrimary personRepositoryPrimary;

    @Autowired
    private PersonRepositorySecondary personRepositorySecondary;

    @Autowired
    @Qualifier("primaryMongoTemplate")
    private MongoTemplate primaryMongoTemplate;

    @Autowired
    @Qualifier("secondaryMongoTemplate")
    private MongoTemplate secondaryMongoTemplate;

    @Transactional
    @Override
    public void transferDataWithOffset(int size) {
        long totalRecords = personRepositoryPrimary.count(); // Get total count
        long batches = (totalRecords / size) + 1;

        for (int i = 0; i < batches; i++) {
            // Use Pageable to fetch records in batches
            Pageable pageable = PageRequest.of(i, size);
            List<Person> batch = personRepositoryPrimary.findAll(pageable).getContent();

            if (!batch.isEmpty()) {
                // Save batch to secondary database
                personRepositorySecondary.saveAll(batch);
            }
            log.info("Batch {} of {} processed.", i + 1, batches);
        }
        personRepositoryPrimary.deleteAll();
        log.info("Data transfer completed!");
    }

    @Transactional
    @Override
    public void transferDataWithCursor(int size, String cursor) {
        Query query = new Query();

        if (cursor != null) {
            query.addCriteria(Criteria.where("_id").gt(cursor));
        }
        query.limit(size);
        List<Person> batch = primaryMongoTemplate.find(query, Person.class);
        if (!batch.isEmpty()) {
            personRepositorySecondary.saveAll(batch);
        }
        deleteBatchFromPrimary(batch);
    }

    private void deleteBatchFromPrimary(List<Person> batch) {
        List<String> ids = batch.stream()
                .map(Person::getId)
                .collect(Collectors.toList());

        Query deleteQuery = new Query(Criteria.where("_id").in(ids));
        primaryMongoTemplate.remove(deleteQuery, "person");
    }


    public void addPerson() {
        List<Person> personList = new ArrayList<>();

        for (int i = 0; i < 500000; i++) {
            Person person = new Person();
            person.setEmail("user" + i + "@example.com");
            person.setFirstName(generateRandomString(5)); // Generates random 5-character first name
            person.setLastName(generateRandomString(7));  // Generates random 7-character last name
            personList.add(person);
        }

        personRepositoryPrimary.saveAll(personList);
    }

    private String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            stringBuilder.append(characters.charAt(random.nextInt(characters.length())));
        }

        return stringBuilder.toString();
    }

    @Override
    public DataTableResponse getPaginatedPersons(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Person> users =  personRepositoryPrimary.findAll(pageable);
        DataTableResponse data = new DataTableResponse();
        data.setData(users.getContent());
        data.setRecordsTotal(users.getTotalElements());
        data.setRecordsFiltered(users.getNumberOfElements());
        return data;
    }

    @Override
    public List<Person> getPaginatedPersonsWithCursor(String cursor, int size) {
        Query query = new Query();
        if (cursor != null) {
            query.addCriteria(Criteria.where("_id").gt(cursor));
        }
        query.limit(size);
        return primaryMongoTemplate.find(query, Person.class);
    }
}
