package com.example.inmemorydatabase.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Column {

    private String name;

    private String type;

    @JsonProperty ("is_mandatory")
    private boolean isMandatory;

    private Integer min;

    private Integer max;
}
