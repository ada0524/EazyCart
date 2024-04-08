//package org.example.onlineshoppingcontent.exception;
//
//import org.aspectj.lang.annotation.AfterThrowing;
//import org.aspectj.lang.annotation.Aspect;
//import org.springframework.stereotype.Component;
//
//@Aspect
//@Component
//public class ExceptionAspect {
//
//    @AfterThrowing(
//            pointcut = "execution(* org.example.onlineshoppingcontent.service.*.*(..))",
//            throwing = "ex"
//    )
//    public void handleException(Exception ex) {
//        if (ex instanceof NotEnoughInventoryException) {
//            handleNotEnoughInventoryException((NotEnoughInventoryException) ex);
//        } else if (ex instanceof OrderNotFoundException) {
//            handleOrderNotFoundException((OrderNotFoundException) ex);
//        } else {
//            handleGenericException(ex);
//        }
//    }
//
//    private void handleNotEnoughInventoryException(NotEnoughInventoryException ex) {
//        // Handle NotEnoughInventoryException-specific logic
//        System.out.println("NotEnoughInventoryException caught: " + ex.getMessage());
//    }
//
//    private void handleOrderNotFoundException(OrderNotFoundException ex) {
//        // Handle OrderNotFoundException-specific logic
//        System.out.println("OrderNotFoundException caught: " + ex.getMessage());
//    }
//
//    private void handleGenericException(Exception ex) {
//        // Handle generic exception logic
//        System.out.println("Generic Exception caught: " + ex.getMessage());
//    }
//}
