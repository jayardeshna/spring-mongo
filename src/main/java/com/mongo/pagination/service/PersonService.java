package com.mongo.pagination.service;

import com.mongo.pagination.model.DataTableResponse;
import com.mongo.pagination.model.Person;
import com.mongo.pagination.transfer.UserTransfer;

import java.util.List;

public interface PersonService {
    void addPerson(List<UserTransfer> userTransfer);

    DataTableResponse getPaginatedPersons(int page, int size);

    List<Person> getPaginatedPersonsWithCursor(String cursor, int size);
}
