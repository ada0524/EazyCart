package org.example.onlineshoppingcontent.exception;

public class OrderNotCancelableException extends RuntimeException {
    public OrderNotCancelableException(String message) {
        super(message);
    }
}
