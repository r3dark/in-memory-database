package com.example.inmemorydatabase.service;

import com.example.inmemorydatabase.exceptions.BadRequestException;
import com.example.inmemorydatabase.model.Column;
import com.example.inmemorydatabase.model.Database;
import com.example.inmemorydatabase.model.DatabaseContainer;
import com.example.inmemorydatabase.model.Table;
import com.example.inmemorydatabase.model.request.CreateTableRequest;
import com.example.inmemorydatabase.model.request.DeleteTableRequest;
import com.example.inmemorydatabase.model.response.ResponseMessage;
import com.example.inmemorydatabase.utils.JsonUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class TableService {

    private static final Integer INT_MAX = 1024;

    private static final Integer INT_MIN = -1024;

    private static final Integer STRING_MAX = 20;

    private static final Integer STRING_MIN = 0;

    private static final String STRING_TYPE = "str";

    private static final String INT_TYPE = "int";

    private static final String COLUMN_CONSTRAINTS_VALID = "column constraints valid";

    private static final String TABLE_ALREADY_EXISTS = "table already exists";

    private static final String TABLE_CREATED = "table created";

    private static final String DATABASE_DOES_NOT_EXISTS = "database does not exists";

    private static final String TABLE_DOES_NOT_EXIST = "table does not exist";

    private static final String TABLE_DELETED = "table deleted";

    private static final ObjectMapper OBJECT_MAPPER = JsonUtils.getObjectMapper();


    public String createTable(String request) throws JsonProcessingException {

        try {
            CreateTableRequest createTableRequest = OBJECT_MAPPER.readValue(request, CreateTableRequest.class);

            Database database = DatabaseContainer.getInstance()
                    .getDatabases().get(createTableRequest.getDatabaseName().toLowerCase());

            ResponseMessage responseMessage = new ResponseMessage();

            boolean isTablePresent;
            if (database != null) {
                isTablePresent = database.getTables().containsKey(createTableRequest.getTable().getName().toLowerCase());
            } else {
                responseMessage.setMessage(DATABASE_DOES_NOT_EXISTS);
                return OBJECT_MAPPER.writeValueAsString(responseMessage);
            }

            if (!isTablePresent) {
                String columnValidity = validateColumns(createTableRequest.getTable().getColumns());

                if (columnValidity.equals(COLUMN_CONSTRAINTS_VALID)) {
                    Table table = new Table();
                    table.setName(createTableRequest.getTable().getName().toLowerCase());
                    table.getColumns().addAll(createTableRequest.getTable().getColumns());
                    addTableToDatabase(database, table);
                    responseMessage.setMessage(TABLE_CREATED);
                } else {
                    responseMessage.setMessage(columnValidity);
                }
            } else {
                responseMessage.setMessage(TABLE_ALREADY_EXISTS);
            }
            return OBJECT_MAPPER.writeValueAsString(responseMessage);
        } catch (Exception ex) {
            log.error("Error occurred while creating table", ex);
            throw ex;
        }
    }

    private void addTableToDatabase(Database database, Table table) {

        database.getTables().put(table.getName(), table);
    }

    public String validateColumns(List<Column> columns) {

        String message = COLUMN_CONSTRAINTS_VALID;

        for (Column column : columns) {
            if (column.getType().equalsIgnoreCase(STRING_TYPE)) {
                if (column.getMax() > STRING_MAX || column.getMin() < STRING_MIN) {
                    message = column.getName() + " length should be between " + STRING_MIN + " and " + STRING_MAX;
                    break;
                }
            } else if (column.getType().equalsIgnoreCase(INT_TYPE)) {
                if (column.getMax() > INT_MAX || column.getMin() < INT_MIN) {
                    message = column.getName() + " range is " + INT_MIN + " and " + INT_MAX;
                    break;
                }
            } else {
                message = column.getType() + " is not supported";
                break;
            }
        }

        return message;
    }

    public String getAllTables(String databaseName) throws JsonProcessingException {

        if (DatabaseContainer.getInstance().getDatabases().containsKey(databaseName.toLowerCase())) {
            return OBJECT_MAPPER.writeValueAsString(DatabaseContainer.getInstance().getDatabases()
                    .get(databaseName.toLowerCase()).getTables());
        } else {
            ResponseMessage responseMessage = new ResponseMessage();
            responseMessage.setMessage(DATABASE_DOES_NOT_EXISTS);
            return OBJECT_MAPPER.writeValueAsString(responseMessage);
        }
    }

    public String deleteTable(String request) throws JsonProcessingException {

        DeleteTableRequest deleteTableRequest = OBJECT_MAPPER.readValue(request, DeleteTableRequest.class);

        ResponseMessage responseMessage = new ResponseMessage();

        if (DatabaseContainer.getInstance().getDatabases().containsKey(deleteTableRequest.getDatabaseName().toLowerCase())) {
            Table table = DatabaseContainer.getInstance().getDatabases()
                    .get(deleteTableRequest.getDatabaseName().toLowerCase()).getTables()
                    .remove(deleteTableRequest.getTableName().toLowerCase());

            responseMessage.setMessage(table == null ? TABLE_DOES_NOT_EXIST : TABLE_DELETED);
        } else {
            responseMessage.setMessage(DATABASE_DOES_NOT_EXISTS);
        }

        return OBJECT_MAPPER.writeValueAsString(responseMessage);
    }

    public Table getTable(Database database, String tableName) throws Exception {

        Table table = database.getTables().get(tableName);

        if (table != null) {
            return table;
        } else {
            throw new BadRequestException(TABLE_DOES_NOT_EXIST);
        }
    }
}
