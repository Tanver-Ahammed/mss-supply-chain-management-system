package com.therap.supply.chain.user.exception;

/**
 * @Author: Md. Tanver Ahammed,
 * ICT, MBSTU
 * */

public class ResourceNotFoundException extends RuntimeException {

    private String resourceName;

    private String resourceFieldName;

    private Long resourceFieldValue;

    public ResourceNotFoundException(String resourceName, String resourceFieldName, Long resourceFieldValue) {
        super(String.format("%s is not found %s: %d", resourceName, resourceFieldName, resourceFieldValue));
    }

}
