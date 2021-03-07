package com.example.inmemorydatabase.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Cell {

    private String columnName;

    private Object value;
}
