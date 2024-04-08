package org.example.springsecurityauth.aop;

import org.apache.tomcat.websocket.AuthenticationException;
import org.example.springsecurityauth.domain.response.ErrorResponse;
import org.example.springsecurityauth.exception.InvalidCredentialsException;
import org.example.springsecurityauth.exception.UserAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class MyExceptionHandler {

    @ExceptionHandler(value = {UserAlreadyExistsException.class})
    public ResponseEntity<ErrorResponse> handleRegisterException(UserAlreadyExistsException e){
        return new ResponseEntity(ErrorResponse.builder().message(e.getMessage()).build(), HttpStatus.OK);
    }

    @ExceptionHandler(value = {InvalidCredentialsException.class})
    public ResponseEntity<ErrorResponse> handleLoginException(InvalidCredentialsException e){
        return new ResponseEntity(ErrorResponse.builder().message(e.getMessage()).build(), HttpStatus.OK);
    }
}
// Use exceptionHandler to handle exceptions in one place.
// Return an HTTP message to the user instead of throwing an HTTP error.