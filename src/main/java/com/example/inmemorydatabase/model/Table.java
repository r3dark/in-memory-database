package com.example.inmemorydatabase.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class Table {

    @JsonProperty ("table_name")
    private String name;

    private List<Column> columns;

    private List<Cell[]> rows;

    public Table() {
        columns = new ArrayList<>();
        rows = new ArrayList<>();
    }
}
