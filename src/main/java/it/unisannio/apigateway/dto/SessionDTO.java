package it.unisannio.apigateway.dto;

import java.io.Serializable;
import java.util.List;

public class SessionDTO implements Serializable {

    private boolean authenticated;
    private List<String> roles;

    public SessionDTO() { }

    public SessionDTO(boolean authenticated, List<String> roles) {
        this.authenticated = authenticated;
        this.roles = roles;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
