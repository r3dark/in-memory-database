package com.example.inmemorydatabase.model.request;

import com.example.inmemorydatabase.model.Table;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CreateTableRequest {

    @JsonProperty ("database_name")
    private String databaseName;

    private Table table;
}
