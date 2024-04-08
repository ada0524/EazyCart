package org.example.onlineshoppingcontent.exception;

public class NotEnoughInventoryException extends RuntimeException {

    public NotEnoughInventoryException(String message) {
        super(message);
    }
}
