package com.example.thenanny.dto;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public class ParentDetails extends UserDetails {
    private Integer numOfChildrenField;


    public ParentDetails() {
        super();

    }

    public ParentDetails(String firstname, String lastname, String email, String password, String phone, String address, Integer numOfChildrenField) {
        super(firstname, lastname, email, password, phone, address);
        this.numOfChildrenField = numOfChildrenField;
    }

    public Integer getNumOfChildrenField() {
        return numOfChildrenField;
    }

    public void setNumOfChildrenField(Integer numOfChildrenField) {
        this.numOfChildrenField = numOfChildrenField;
    }

    public Map<String, Object> useDetailsToMap() {
        ObjectMapper mapObject = new ObjectMapper();
        return mapObject.convertValue(this, Map.class);
    }

}
