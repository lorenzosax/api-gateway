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

import java.util.Objects;


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

        if (routerValidator.isWebsocket.test(request)) {
            String ticket = this.getTicketFromWebsocketRequest(request);
            String sourceIpAddress = Objects.requireNonNull(request.getRemoteAddress()).getAddress().getHostAddress();
            if (!userService.validateTicket(sourceIpAddress, ticket))
                return this.onError(exchange, "Websocket ticket is invalid", HttpStatus.UNAUTHORIZED);
        } else if (routerValidator.isSecured.test(request)) {
            final String token = this.getTokenFromRequest(request);

            if (token == null)
                return this.onError(exchange, "Authorization header is missing in request", HttpStatus.UNAUTHORIZED);

            SessionDTO session = userService.validateSession(token);
            if (session == null || !session.isAuthenticated())
                return this.onError(exchange, "Authorization header is invalid", HttpStatus.UNAUTHORIZED);

            if (!routerValidator.isGranted(request.getPath().value(), session.getRoles()))
                return this.onError(exchange, "Authorization authority is not granted", HttpStatus.UNAUTHORIZED);

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

    private String getTokenFromRequest(ServerHttpRequest request) {
        String token = null;
        if (!this.isAuthHeaderMissing(request))
            token = this.getAuthHeader(request);

        return token;
    }

    private boolean isAuthHeaderMissing(ServerHttpRequest request) {
        return !request.getHeaders().containsKey("Authorization");
    }

    private String getTicketFromWebsocketRequest(ServerHttpRequest request) {
        String ticket = null;
        String query = request.getURI().getQuery();
        if (query != null) {
            String[] parameters = query.split("&");
            for (String param : parameters) {
                String[] p = param.split("=");
                if (p[0].equals("ticket")) {
                    ticket = p[1];
                    break;
                }
            }
        }
        return ticket;
    }

}
