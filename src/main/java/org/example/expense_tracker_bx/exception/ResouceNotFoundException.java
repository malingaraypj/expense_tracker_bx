package org.example.expense_tracker_bx.exception;

public class ResouceNotFoundException extends RuntimeException{

    public ResouceNotFoundException(String resource, String field, Object value){
        super(String.format("%s not found with %s : %s",resource,field,value));
    }
}
