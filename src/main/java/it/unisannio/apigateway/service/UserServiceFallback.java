package it.unisannio.apigateway.service;

import it.unisannio.apigateway.dto.SessionDTO;
import org.springframework.stereotype.Component;

@Component("userServiceFallback")
public class UserServiceFallback implements UserService {

    @Override
    public SessionDTO validateSession(String token) {
        return new SessionDTO(false);
    }

    @Override
    public boolean validateTicket(String ott) {
        return false;
    }
}
