package it.unisannio.apigateway.dto;

import it.unisannio.apigateway.Role;

import java.io.Serializable;
import java.util.List;

public class SessionDTO implements Serializable {

    private String jwt;
    private List<Role> roles;

    public SessionDTO() { }

    public SessionDTO(String jwt, List<Role> roles) {
        this.jwt = jwt;
        this.roles = roles;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
}
