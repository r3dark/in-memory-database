package com.example.inmemorydatabase.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Row {

    private Cell[] cells;

    public Row(int size) {
        cells = new Cell[size];
    }
}
