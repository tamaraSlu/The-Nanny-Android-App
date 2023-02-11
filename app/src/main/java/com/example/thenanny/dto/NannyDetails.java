package com.example.thenanny.dto;

import java.util.Date;

public class NannyDetails extends UserDetails{
    private Date birthDate;
    private Date startWorkDate;
    private Integer hourlyWage;
    public NannyDetails(){
        super();

    }

    public NannyDetails(String firstname, String lastname, String email, String password, String phone,Integer hourlyWage,String address,Date birthDate, Date startWorkDate) {
        super(firstname, lastname, email, password, phone,address);
        this.birthDate = birthDate;
        this.startWorkDate = startWorkDate;
        this.hourlyWage = hourlyWage;
    }

    public Date getStartWorkDate() {
        return startWorkDate;
    }

    public void setStartWorkDate(Date startWorkDate) {
        this.startWorkDate = startWorkDate;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public Integer getHourlyWage() {
        return hourlyWage;
    }

    public void setHourlyWage(Integer hourlyWage) {
        this.hourlyWage = hourlyWage;
    }
}
