package com.example.inmemorydatabase.model.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;

@Getter
@Setter
@ToString
@Component
public class ResponseMessage {

    private String message;

    public ResponseMessage(){}

    public ResponseMessage(String message) {
        this.message = message;
    }
}
