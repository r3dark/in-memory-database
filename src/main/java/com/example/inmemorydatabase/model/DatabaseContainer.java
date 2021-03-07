package com.example.inmemorydatabase.model;

import java.util.HashMap;
import java.util.Map;

public class DatabaseContainer {

    private final Map<String, Database> databaseMap;

    private static DatabaseContainer databaseContainer;

    private DatabaseContainer() {
        databaseMap = new HashMap<>();
    }

    public static DatabaseContainer getInstance() {
        if (databaseContainer == null) {
            synchronized (DatabaseContainer.class) {
                if (databaseContainer == null) {
                    databaseContainer = new DatabaseContainer();
                }
            }
        }
        return databaseContainer;
    }

    public Map<String, Database> getDatabases() {
        return databaseMap;
    }

    @Override
    public String toString() {
        return "DatabaseContainer{" +
                "databaseMap=" + databaseMap +
                '}';
    }
}
