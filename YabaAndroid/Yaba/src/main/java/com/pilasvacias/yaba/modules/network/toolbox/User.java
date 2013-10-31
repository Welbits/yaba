package com.pilasvacias.yaba.modules.network.toolbox;

import java.util.ArrayList;
import java.util.List;

public class User {

    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String oldPassword;
    private String password;
    private String userType;
    private String role;
    private String promotionalCode;
    private boolean validated;
    private String partnerId;
    private String modifyDate;
    private String lastAdviceDate;
    private Object lastAdvice;
    private String locationDate;
    private String weatherDate;
    private List<String> resourceAvatar = new ArrayList<String>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Object getPromotionalCode() {
        return promotionalCode;
    }

    public void setPromotionalCode(String promotionalCode) {
        this.promotionalCode = promotionalCode;
    }

    public boolean isValidated() {
        return validated;
    }

    public void setValidated(boolean validated) {
        this.validated = validated;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public String getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(String modifyDate) {
        this.modifyDate = modifyDate;
    }

    public String getLastAdviceDate() {
        return lastAdviceDate;
    }

    public void setLastAdviceDate(String lastAdviceDate) {
        this.lastAdviceDate = lastAdviceDate;
    }

    public Object getLastAdvice() {
        return lastAdvice;
    }

    public void setLastAdvice(Object lastAdvice) {
        this.lastAdvice = lastAdvice;
    }

    public String getLocationDate() {
        return locationDate;
    }

    public void setLocationDate(String locationDate) {
        this.locationDate = locationDate;
    }

    public String getWeatherDate() {
        return weatherDate;
    }

    public void setWeatherDate(String weatherDate) {
        this.weatherDate = weatherDate;
    }

    public List<String> getResourceAvatar() {
        return resourceAvatar;
    }

    public void setResourceAvatar(List<String> resourceAvatar) {
        this.resourceAvatar = resourceAvatar;
    }

    public static class RestResponse extends RestModel<User> {
    }
}
