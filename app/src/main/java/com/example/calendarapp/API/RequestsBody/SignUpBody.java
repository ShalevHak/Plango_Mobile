package com.example.calendarapp.API.RequestsBody;

public class SignUpBody {
    public String name;
    public String email;
    public String password;
    public String passwordConfirm;

    public SignUpBody(String name, String email, String password, String passwordConfirm) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.passwordConfirm = passwordConfirm;
    }
}
