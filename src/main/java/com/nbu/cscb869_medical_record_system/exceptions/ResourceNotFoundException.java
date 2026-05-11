package com.nbu.cscb869_medical_record_system.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String resource, String type, String data) {
        super(String.format("%s not found with %s: %s", resource, type, data));
    }

    public ResourceNotFoundException(String resource, Long data){
        super(String.format("%s not found with id: %d", resource, data));
    }
}
