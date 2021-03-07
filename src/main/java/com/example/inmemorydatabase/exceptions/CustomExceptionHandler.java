package com.example.inmemorydatabase.exceptions;

import com.example.inmemorydatabase.model.response.ResponseMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class CustomExceptionHandler {

    @Autowired
    private ResponseMessage responseMessage;

    @ExceptionHandler (JsonProcessingException.class)
    public ResponseEntity<ResponseMessage> handleJsonProcessingExceptions() {

        responseMessage.setMessage("Invalid Request");
        return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler (BadRequestException.class)
    public ResponseEntity<ResponseMessage> handleBadRequestExceptions(BadRequestException bre) {

        responseMessage.setMessage("Invalid Request. " + bre.getMessage());
        return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler (Exception.class)
    public ResponseEntity<ResponseMessage> handleGeneralExceptions(Exception ex) {

        responseMessage.setMessage(ex.getMessage());
        return new ResponseEntity<>(responseMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
