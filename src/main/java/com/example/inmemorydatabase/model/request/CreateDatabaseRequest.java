package com.example.inmemorydatabase.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CreateDatabaseRequest {

    @JsonProperty ("database_name")
    private String databaseName;
}
