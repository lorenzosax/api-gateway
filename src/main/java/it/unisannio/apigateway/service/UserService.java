package it.unisannio.apigateway.service;

import it.unisannio.apigateway.dto.SessionDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient(name = "user-service")
public interface UserService {

    @PostMapping("/api/users/validate-session")
    SessionDTO validateSession(@RequestBody String token);

}
