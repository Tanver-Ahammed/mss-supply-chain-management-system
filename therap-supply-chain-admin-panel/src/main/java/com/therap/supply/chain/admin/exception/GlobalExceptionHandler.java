package com.therap.supply.chain.admin.exception;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @Author: Md. Tanver Ahammed,
 * ICT, MBSTU
 * */

@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = ResourceNotFoundException.class)
    public String resourceNotFoundException(ResourceNotFoundException exception, Model model) {
        model.addAttribute("error_message", exception.getMessage());
        return "exception_page";
    }

    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = Exception.class)
    public String exception(Exception exception, Model model) {
        model.addAttribute("error_message", exception.getMessage());
        return "exception_page";
    }

}
