package com.mongo.pagination.controller;

import com.mongo.pagination.model.RestResponse;
import com.mongo.pagination.service.PersonService;
import com.mongo.pagination.transfer.UserTransfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/user")
@RestController
public class PersonController {

    @Autowired
    private PersonService personService;

    @PostMapping("/add")
    RestResponse addData(){
        personService.addPerson();
        return new RestResponse(true, "data added successfully");
    }

    @GetMapping("/transfer-data-offset")
    RestResponse transferDataUsingOffset(@RequestParam(defaultValue = "10") int size){
        personService.transferDataWithOffset(size);
        return new RestResponse(true, "data transferred successfully");
    }

    @GetMapping("/transfer-data-cursor")
    RestResponse transferDataUsingCursor(@RequestParam(defaultValue = "10") int size,
                                         @RequestParam(required = false) String cursor){
        personService.transferDataWithCursor(size, cursor);
        return new RestResponse(true, "data transferred successfully");
    }

    @GetMapping("/offset")
    RestResponse getDataWithOffset(@RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "10") int size) {

        return new RestResponse(true, personService.getPaginatedPersons(page, size));
    }

    @GetMapping("/cursor")
    RestResponse getDataWithCursor(@RequestParam(required = false) String cursor,
                                   @RequestParam(defaultValue = "10") int size){
        return new RestResponse(true, personService.getPaginatedPersonsWithCursor(cursor, size));
    }

}
