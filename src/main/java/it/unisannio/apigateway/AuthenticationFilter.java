package it.unisannio.apigateway;

import it.unisannio.apigateway.dto.SessionDTO;
import it.unisannio.apigateway.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


@RefreshScope
@Component
public class AuthenticationFilter implements GatewayFilter {

    private RouterValidator routerValidator;
    private UserService userService;

    @Autowired
    public AuthenticationFilter(RouterValidator routerValidator, UserService userService) {
        this.routerValidator = routerValidator;
        this.userService = userService;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        if (routerValidator.isSecured.test(request)) {
            if (this.isAuthMissing(request))
                return this.onError(exchange, "Authorization header is missing in request", HttpStatus.UNAUTHORIZED);

            final String token = this.getAuthHeader(request);

            SessionDTO session = userService.validateSession(token);
            if (session == null || !session.isAuthenticated())
                return this.onError(exchange, "Authorization header is invalid", HttpStatus.UNAUTHORIZED);

            if (!routerValidator.isGranted(request.getPath().value(), session.getRoles()))
                return this.onError(exchange, "Authorization authority is not granted", HttpStatus.UNAUTHORIZED);

            // this.populateRequestWithHeaders(exchange, token);
        }
        return chain.filter(exchange);
    }


    /*PRIVATE*/
    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }

    private String getAuthHeader(ServerHttpRequest request) {
        return request.getHeaders().getOrEmpty("Authorization").get(0);
    }

    private boolean isAuthMissing(ServerHttpRequest request) {
        return !request.getHeaders().containsKey("Authorization");
    }

}
