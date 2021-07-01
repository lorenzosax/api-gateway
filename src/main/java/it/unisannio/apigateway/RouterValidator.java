package it.unisannio.apigateway;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

@Component
public class RouterValidator {

    public static final List<String> openApiEndpoints = List.of(
            "/api/city/users",
            "/api/city/stations",
            "/api/city/routes"
    );

    public static final Map<String, String> privateApiAuthority = Map.ofEntries(
            Map.entry("/api/city/trips", "ROLE_PASSENGER"),
            Map.entry("/api/city/notifications", "ROLE_PASSENGER")
    );

    public Predicate<ServerHttpRequest> isSecured =
            request -> openApiEndpoints
                    .stream()
                    .noneMatch(uri -> request.getURI().getPath().contains(uri));

    public boolean isGranted(String path, List<String> userRoles) {
        boolean granted = false;
        for (Map.Entry<String, String> api : privateApiAuthority.entrySet()){
            if (path.contains(api.getKey())) {
                for (String userRole: userRoles) {
                    if (userRole.contains(api.getValue())) {
                        granted = true;
                        break;
                    }
                }
                break;
            }
        }
        return granted;
    }

}
