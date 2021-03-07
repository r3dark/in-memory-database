package com.example.inmemorydatabase.service;

import com.example.inmemorydatabase.exceptions.BadRequestException;
import com.example.inmemorydatabase.model.Database;
import com.example.inmemorydatabase.model.DatabaseContainer;
import com.example.inmemorydatabase.model.request.CreateDatabaseRequest;
import com.example.inmemorydatabase.model.response.ResponseMessage;
import com.example.inmemorydatabase.utils.JsonUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DatabaseService {

    private static final String DATABASE_CREATED = "database created";

    private static final String DATABASE_ALREADY_PRESENT = "database already present";

    private static final String DATABASE_NOT_PRESENT = "database not present";

    private static final ObjectMapper OBJECT_MAPPER = JsonUtils.getObjectMapper();


    public String createDatabase(String request) throws JsonProcessingException {

        try {
            CreateDatabaseRequest databaseRequest = OBJECT_MAPPER.readValue(request, CreateDatabaseRequest.class);
            Database database = DatabaseContainer.getInstance()
                    .getDatabases().putIfAbsent(databaseRequest.getDatabaseName().toLowerCase(), new Database());
            ResponseMessage responseMessage = new ResponseMessage();
            if (database == null) {
                responseMessage.setMessage(DATABASE_CREATED);
            } else {
                responseMessage.setMessage(DATABASE_ALREADY_PRESENT);
            }
            return OBJECT_MAPPER.writeValueAsString(responseMessage);
        } catch (Exception ex) {
            log.error("Error occurred while creating database: {}", request, ex);
            throw ex;
        }
    }

    public String getAllDatabases() throws JsonProcessingException {
        return OBJECT_MAPPER.writeValueAsString(
                DatabaseContainer.getInstance().getDatabases());
    }

    public String deleteDatabase(String name) throws JsonProcessingException {
        Database database = DatabaseContainer.getInstance().getDatabases().remove(name);
        return database != null ? OBJECT_MAPPER.writeValueAsString(database) :
                OBJECT_MAPPER.writeValueAsString(new ResponseMessage(DATABASE_NOT_PRESENT));
    }

    public Database getDatabase(String databaseName) throws Exception {

        Database database = DatabaseContainer.getInstance().getDatabases().get(databaseName);

        if (database != null) {
            return database;
        } else {
            throw new BadRequestException(DATABASE_NOT_PRESENT);
        }
    }
}
