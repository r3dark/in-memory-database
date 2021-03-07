package com.example.inmemorydatabase.controller;

import com.example.inmemorydatabase.service.TableService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class TableController {

    @Autowired
    private TableService tableService;

    @PostMapping (value = "/table/create")
    public ResponseEntity<String> createTable(@RequestBody String request) throws JsonProcessingException {

        String response = tableService.createTable(request);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        return new ResponseEntity<>(response, httpHeaders, HttpStatus.OK);
    }

    @GetMapping (value = "/table/getall")
    public ResponseEntity<String> getAllTables(@RequestParam ("database_name") String databaseName) throws JsonProcessingException {

        String response = tableService.getAllTables(databaseName);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        return new ResponseEntity<>(response, httpHeaders, HttpStatus.OK);
    }

    @DeleteMapping (value = "/table/delete")
    public ResponseEntity<String> deleteTable(@RequestBody String request) throws JsonProcessingException {

        String response = tableService.deleteTable(request);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        return new ResponseEntity<>(response, httpHeaders, HttpStatus.OK);
    }
}
