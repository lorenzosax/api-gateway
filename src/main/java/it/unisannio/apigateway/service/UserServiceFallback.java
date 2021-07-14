package it.unisannio.apigateway.service;

import it.unisannio.apigateway.dto.SessionDTO;
import org.springframework.stereotype.Component;

@Component("userServiceFallback")
public class UserServiceFallback implements UserService {

    @Override
    public SessionDTO validateSession(String token) {
        return null;
    }

    @Override
    public boolean validateTicket(String sourceIpAddress, String ott) {
        return false;
    }
}
