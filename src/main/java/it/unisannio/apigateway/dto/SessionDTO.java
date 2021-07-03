package it.unisannio.apigateway.dto;

import it.unisannio.apigateway.Role;

import java.io.Serializable;
import java.util.List;

public class SessionDTO implements Serializable {

    private boolean authenticated;
    private List<Role> roles;

    public SessionDTO() { }

    public SessionDTO(boolean authenticated) {
        this.authenticated = authenticated;
    }

    public SessionDTO(boolean authenticated, List<Role> roles) {
        this.authenticated = authenticated;
        this.roles = roles;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
}
