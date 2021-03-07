package com.example.inmemorydatabase.model;

import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@ToString
public class Database {

    private final Map<String, Table> tables;

    public Database() {
        tables = new HashMap<>();
    }

    public Map<String, Table> getTables() {
        return tables;
    }
}
