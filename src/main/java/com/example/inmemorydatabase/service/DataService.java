package com.example.inmemorydatabase.service;

import com.example.inmemorydatabase.exceptions.BadRequestException;
import com.example.inmemorydatabase.model.*;
import com.example.inmemorydatabase.model.request.GetDataRequest;
import com.example.inmemorydatabase.model.request.InsertDataRequest;
import com.example.inmemorydatabase.model.response.ResponseMessage;
import com.example.inmemorydatabase.utils.JsonUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class DataService {

    @Autowired
    private DatabaseService databaseService;

    @Autowired
    private TableService tableService;

    private static final String COLUMN_VALUE_IS_MANDATORY = "column value is mandatory";

    private static final String INVALID_DATA_TYPE = "invalid data type";

    private static final String STRING_LENGTH_OUT_OF_RANGE = "string length out of range";

    private static final String INTEGER_OUT_OF_RANGE = "integer value out of range";

    private static final String COLUMN_DOES_NOT_EXIST = "column does not exist";

    private static final String INSERT_SUCCESSFUL = "insert successful";

    private static final String INSERT_UNSUCCESSFUL = "insert unsuccessful";

    private static final String STRING_TYPE = "str";

    private static final String INT_TYPE = "int";

    private static final ObjectMapper OBJECT_MAPPER = JsonUtils.getObjectMapper();


    public String insertDataIntoTable(String request) throws Exception {

        try {
            InsertDataRequest insertDataRequest = OBJECT_MAPPER.readValue(request, InsertDataRequest.class);

            Database database = databaseService.getDatabase(insertDataRequest.getDatabaseName());

            Table table = tableService.getTable(database, insertDataRequest.getTableName());

            Cell[] cells = createRow(table, insertDataRequest.getData());

            table.getRows().add(cells);

            return OBJECT_MAPPER.writeValueAsString(new ResponseMessage(INSERT_SUCCESSFUL));
        } catch (Exception ex) {
            log.error("Error occurred while inserting data", ex);
            return OBJECT_MAPPER.writeValueAsString(new ResponseMessage(INSERT_UNSUCCESSFUL + ". " + ex.getMessage()));
        }
    }

    private Cell[] createRow(Table table, Map<String, Object> data) throws BadRequestException {

        Cell[] cells = new Cell[table.getColumns().size()];
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            Cell cell = new Cell();
            cell.setColumnName(entry.getKey());
            cell.setValue(entry.getValue());

            Column column = table.getColumns().stream().filter(c ->
                    c.getName().equalsIgnoreCase(cell.getColumnName()))
                    .findFirst().orElse(null);

            if (column != null) {
                validateCellWithColumnConstraints(cell, column, data);
                int indexOfColumn = table.getColumns().indexOf(column);
                cells[indexOfColumn] = cell;
            } else {
                throw new BadRequestException(COLUMN_DOES_NOT_EXIST);
            }
        }
        return cells;
    }

    private void validateCellWithColumnConstraints(Cell cell, Column column, Map<String, Object> data) throws BadRequestException {

        if (column.isMandatory() && (cell == null || cell.getValue() == null || !data.containsKey(column.getName()))) {
            throw new BadRequestException(COLUMN_VALUE_IS_MANDATORY + ": " + column.getName());
        }

        if (column.getType().equalsIgnoreCase(STRING_TYPE) && !(cell.getValue() instanceof String)) {
            throw new BadRequestException(INVALID_DATA_TYPE + ", " + column.getName() + " should be a String");
        } else if (column.getType().equalsIgnoreCase(INT_TYPE) && !(cell.getValue() instanceof Integer)) {
            throw new BadRequestException(INVALID_DATA_TYPE + ", " + column.getName() + " should be a Integer");
        }

        if ((cell.getValue() instanceof String) && (((String) cell.getValue()).length() < column.getMin()
                || ((String) cell.getValue()).length() > column.getMax())) {
            throw new BadRequestException(STRING_LENGTH_OUT_OF_RANGE + ", should be between " +
                    column.getMin() + " & " + column.getMax() + " for column " + column.getName());
        }

        if ((cell.getValue() instanceof Integer) && ((Integer) cell.getValue() < column.getMin()
                || (Integer) cell.getValue() > column.getMax())) {
            throw new BadRequestException(INTEGER_OUT_OF_RANGE + ", should be between " +
                    column.getMin() + " & " + column.getMax() + " for column " + column.getName());
        }
    }

    public String getAllData(String request) throws Exception {

        GetDataRequest getDataRequest = OBJECT_MAPPER.readValue(request, GetDataRequest.class);

        Database database = databaseService.getDatabase(getDataRequest.getDatabaseName());

        Table table = tableService.getTable(database, getDataRequest.getTableName());

        return OBJECT_MAPPER.writeValueAsString(table.getRows());
    }

    public String getData(String columnName, String value, String request) throws Exception {

        GetDataRequest getDataRequest = OBJECT_MAPPER.readValue(request, GetDataRequest.class);

        Database database = databaseService.getDatabase(getDataRequest.getDatabaseName());

        Table table = tableService.getTable(database, getDataRequest.getTableName());

        Column column = table.getColumns().stream().filter(c ->
                c.getName().equalsIgnoreCase(columnName))
                .findFirst().orElse(null);

        List<Cell[]> filteredDataList = new ArrayList<>();

        if (column != null) {
            int indexOfColumn = table.getColumns().indexOf(column);
            for (Cell[] cells : table.getRows()) {
                if (cells[indexOfColumn].getValue().toString().equals(value)) {
                    filteredDataList.add(cells);
                }
            }
        } else {
            throw new BadRequestException(COLUMN_DOES_NOT_EXIST);
        }

        return OBJECT_MAPPER.writeValueAsString(filteredDataList);
    }
}
