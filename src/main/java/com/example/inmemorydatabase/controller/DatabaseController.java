package com.example.inmemorydatabase.controller;

import com.example.inmemorydatabase.service.DatabaseService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class DatabaseController {

    @Autowired
    private DatabaseService databaseService;

    @PostMapping (value = "/database/create")
    public ResponseEntity<String> createDatabase(@RequestBody String request) throws JsonProcessingException {

        String response = databaseService.createDatabase(request);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        return new ResponseEntity<>(response, httpHeaders, HttpStatus.OK);
    }

    @GetMapping (value = "/database/getall")
    public ResponseEntity<String> getAllDatabases() throws JsonProcessingException {

        String response = databaseService.getAllDatabases();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        return new ResponseEntity<>(response, httpHeaders, HttpStatus.OK);
    }

    @DeleteMapping (value = "/database/delete")
    public ResponseEntity<String> deleteDatabase(@RequestParam String name) throws JsonProcessingException {

        String response = databaseService.deleteDatabase(name.toLowerCase());

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        return new ResponseEntity<>(response, httpHeaders, HttpStatus.OK);
    }
}
