package com.example.inmemorydatabase.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

@Getter
@Setter
@ToString
public class InsertDataRequest {

    @JsonProperty ("database_name")
    private String databaseName;

    @JsonProperty ("table_name")
    private String tableName;

    private Map<String, Object> data;
}
