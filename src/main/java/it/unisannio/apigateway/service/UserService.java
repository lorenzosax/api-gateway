package it.unisannio.apigateway.service;

import it.unisannio.apigateway.dto.SessionDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;


@FeignClient(name = "user-service", fallback = UserServiceFallback.class)
public interface UserService {

    @PostMapping("/api/users/validate-session")
    SessionDTO validateSession(@RequestBody String token);

    @GetMapping("/api/tickets/{ott}/validate")
    boolean validateTicket(@PathVariable String ott);

}
