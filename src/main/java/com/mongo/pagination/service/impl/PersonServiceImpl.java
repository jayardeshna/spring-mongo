package com.mongo.pagination.service.impl;

import com.mongo.pagination.model.DataTableResponse;
import com.mongo.pagination.model.Person;
import com.mongo.pagination.repository.PersonRepository;
import com.mongo.pagination.service.PersonService;
import com.mongo.pagination.transfer.UserTransfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PersonServiceImpl implements PersonService {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private MongoTemplate mongoTemplate;


    @Override
    public void addPerson(List<UserTransfer> userTransfers) {
        List<Person> personList = userTransfers.stream().map(userTransfer -> {
            Person person = new Person();
            person.setEmail(userTransfer.getEmail());
            person.setFirstName(userTransfer.getFirstName());
            person.setLastName(userTransfer.getLastName());
            return person;
        }).collect(Collectors.toList());
        personRepository.saveAll(personList);
    }

    @Override
    public DataTableResponse getPaginatedPersons(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Person> users =  personRepository.findAll(pageable);
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
        return mongoTemplate.find(query, Person.class);
    }
}
