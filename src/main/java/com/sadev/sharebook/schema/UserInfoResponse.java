package com.sadev.sharebook.schema;

import com.sadev.sharebook.user.User;

public class UserInfoResponse {
    private Integer id;
    private String firstName;
    private String lastName;
    private String email;

    public UserInfoResponse() {
    }

    public UserInfoResponse(Integer id, String firstName, String lastName, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    //Get lender
    public static UserInfoResponse getUserInfoResponse(User user){
        return new UserInfoResponse(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail());
    }
}
