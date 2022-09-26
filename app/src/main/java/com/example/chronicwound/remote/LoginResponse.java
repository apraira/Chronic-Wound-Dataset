package com.example.chronicwound.remote;

public class LoginResponse {

    private Integer _id;
    private String name, username, email, password;

    public Integer get_id() {
        return _id;
    }

    public void set_id(Integer _id) {
        this._id = _id;
    }

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    public String getUsername() {return username;}
    public void setUsername(String username) {this.username=username;}

    public String getEmail(){return email;}
    public void setEmail(String email) {this.email=email;}

    public String getPassword(){return password;}
    public void setPassword(String password){this.password=password;}

}
