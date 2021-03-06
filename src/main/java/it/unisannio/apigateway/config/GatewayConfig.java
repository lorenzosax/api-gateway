package it.unisannio.apigateway.config;

import it.unisannio.apigateway.AuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    AuthenticationFilter filter;

    @Autowired
    public GatewayConfig(AuthenticationFilter filter) {
        this.filter = filter;
    }

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("user-service", r -> r.path("/api/city/users/**", "/api/city/tickets/**")
                        .filters(f -> f.filter(filter).rewritePath("/api/city/","/api/"))
                        .uri("lb://user-service"))

                .route("trip-service", r -> r.path("/api/city/stations/**", "/api/city/routes/**",
                                                                "/api/city/notifications/**")
                        .filters(f -> f.filter(filter).rewritePath("/api/city/","/api/"))
                        .uri("lb://trip-service"))

                .route("vehicle-service", r -> r.path( "/api/city/vehicles/**", "/api/city/drivers/**")
                        .filters(f -> f.filter(filter).rewritePath("/api/city/","/api/"))
                        .uri("lb://vehicle-service"))
                .build();
    }

}
