package com.example.thenanny.dto;
import androidx.annotation.NonNull;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

public class NannyDetails implements UserDetails, Serializable {
    private Date birthDate;
    private String id,firstname, lastname, email, password, phone, address, startWorkYear;
    private Integer hourlyWage,minAge,maxAge;
    private Boolean isApproved;
    private byte[] profile_image;

    public NannyDetails() {
    }

    public NannyDetails(String firstname, String lastname, String email, String password, String phone, Integer hourlyWage, String address, Date birthDate, String startWorkYear,Integer minAge,Integer maxAge) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.address = address;
        this.birthDate = birthDate;
        this.startWorkYear = startWorkYear;
        this.hourlyWage = hourlyWage;
        this.minAge=minAge;
        this.maxAge=maxAge;
        this.isApproved=false;
        this.id="";
        this.profile_image=null;
    }

    @Override
    public String toString() {
        return "NannyDetails{" +
                "birthDate=" + birthDate +
                ", id='" + id + '\'' +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", startWorkYear='" + startWorkYear + '\'' +
                ", hourlyWage=" + hourlyWage +
                ", minAge=" + minAge +
                ", maxAge=" + maxAge +
                ", isApproved=" + isApproved +
                ", profile_image=" + Arrays.toString(profile_image) +
                '}';
    }

    public String getStartWorkDate() {
        return startWorkYear;
    }

    public void setStartWorkDate(String startWorkDate) {
        this.startWorkYear = startWorkDate;
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

    public Boolean getApproved() {
        return isApproved;
    }

    public void setApproved(Boolean approved) {
        isApproved = approved;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public byte[] getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(byte[] profile_image) {
        this.profile_image = profile_image;
    }
}
