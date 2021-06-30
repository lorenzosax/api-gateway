package it.unisannio.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableEurekaClient
public class ApiGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiGatewayApplication.class, args);
	}

	@Bean
	public RouteLocator myRoutes(RouteLocatorBuilder builder) {
		return builder.routes()
				.route(r -> r.path("/api/city/trip/**", "/api/city/station/**",
											"/api/city/route/**", "/api/city/notifications/**")
						.filters(f -> f.rewritePath("/api/city/","/api/"))
						.uri("lb://TRIP-SERVICE")
						.id("tripModule"))
				.route(r -> r.path("/api/city/vehicle/**")
						.filters(f -> f.rewritePath("/api/city/","/api/"))
						.uri("lb://VEHICLE-SERVICE")
						.id("vehicleModule"))
				.build();
	}

}
