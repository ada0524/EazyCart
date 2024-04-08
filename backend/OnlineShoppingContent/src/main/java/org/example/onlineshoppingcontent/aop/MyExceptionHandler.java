package org.example.onlineshoppingcontent.aop;

import org.example.onlineshoppingcontent.dto.error.ErrorResponse;
import org.example.onlineshoppingcontent.exception.NoResultException;
import org.example.onlineshoppingcontent.exception.NotEnoughInventoryException;
import org.example.onlineshoppingcontent.exception.OrderNotCancelableException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class MyExceptionHandler {

    @ExceptionHandler(value = {NotEnoughInventoryException.class})
    public ResponseEntity<ErrorResponse> handleRegisterException(NotEnoughInventoryException e){
        return new ResponseEntity(ErrorResponse.builder().message(e.getMessage()).build(), HttpStatus.OK);
    }

    @ExceptionHandler(value = {OrderNotCancelableException.class})
    public ResponseEntity<ErrorResponse> handleRegisterException(OrderNotCancelableException e) {
        return new ResponseEntity(ErrorResponse.builder().message(e.getMessage()).build(), HttpStatus.OK);
    }

    @ExceptionHandler(value = {NoResultException.class})
    public ResponseEntity<ErrorResponse> handleRegisterException(NoResultException e) {
        return new ResponseEntity(ErrorResponse.builder().message(e.getMessage()).build(), HttpStatus.OK);
    }

}