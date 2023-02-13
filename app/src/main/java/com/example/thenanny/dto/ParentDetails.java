package com.example.thenanny.dto;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.Serializable;
import java.util.Map;

public class ParentDetails implements UserDetails, Serializable {
    private Integer numOfChildren,minAge,maxAge;
    private String firstname, lastname, email, password, phone, address;


    public ParentDetails() {
    }

    public ParentDetails(String firstname, String lastname, String email, String password, String phone, String address, Integer numOfChildren,Integer minAge,Integer maxAge) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.address = address;
        this.numOfChildren = numOfChildren;
        this.minAge=minAge;
        this.maxAge=maxAge;
    }

    @Override
    public String toString() {
        return "ParentDetails{" +
                "numOfChildren=" + numOfChildren +
                ", minAge=" + minAge +
                ", maxAge=" + maxAge +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                '}';
    }

    public Integer getNumOfChildrenField() {
        return numOfChildren;
    }

    public void setNumOfChildrenField(Integer numOfChildrenField) {
        this.numOfChildren = numOfChildrenField;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Map<String, Object> useDetailsToMap() {
        ObjectMapper mapObject = new ObjectMapper();
        return mapObject.convertValue(this, Map.class);
    }

    public Integer getMinAge() {
        return minAge;
    }

    public void setMinAge(Integer minAge) {
        this.minAge = minAge;
    }

    public Integer getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(Integer maxAge) {
        this.maxAge = maxAge;
    }
}
