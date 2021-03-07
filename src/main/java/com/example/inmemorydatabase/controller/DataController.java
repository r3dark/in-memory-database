package com.example.inmemorydatabase.controller;

import com.example.inmemorydatabase.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class DataController {

    @Autowired
    private DataService dataService;

    @PostMapping (value = "/data/insert")
    public ResponseEntity<String> insertDataIntoTable(@RequestBody String request) throws Exception {

        String response = dataService.insertDataIntoTable(request);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        return new ResponseEntity<>(response, httpHeaders, HttpStatus.OK);
    }

    @GetMapping (value = "/data/getall")
    public ResponseEntity<String> getAllData(@RequestBody String request) throws Exception {

        String response = dataService.getAllData(request);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        return new ResponseEntity<>(response, httpHeaders, HttpStatus.OK);
    }

    @GetMapping (value = "/data/get")
    public ResponseEntity<String> getData(@RequestParam ("column") String columnName,
                                             @RequestParam ("value") String value,
                                             @RequestBody String request) throws Exception {

        String response = dataService.getData(columnName, value, request);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        return new ResponseEntity<>(response, httpHeaders, HttpStatus.OK);
    }
}
