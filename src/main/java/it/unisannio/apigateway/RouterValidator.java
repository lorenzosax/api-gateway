package it.unisannio.apigateway;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

@Component
public class RouterValidator {

    public static final List<String> openEndpoints = List.of(
            "/api/city/users",
            "/api/city/stations",
            "/api/city/routes"
    );

    public static final List<String> websocketEndpoints = List.of(
            "/api/city/notifications",
            "/api/city/drivers"
    );

    public static final Map<String, List<Role>> privateEndpointsAuthority = Map.ofEntries(
            Map.entry("/api/city/trips", Collections.singletonList(Role.ROLE_PASSENGER)),
            Map.entry("/api/city/vehicles", Collections.singletonList(Role.ROLE_MANAGER)),
            Map.entry("/api/city/tickets", Arrays.asList(Role.ROLE_PASSENGER, Role.ROLE_DRIVER))
    );

    public Predicate<ServerHttpRequest> isSecured =
            request -> openEndpoints
                    .stream()
                    .noneMatch(uri -> request.getURI().getPath().contains(uri));

    public Predicate<ServerHttpRequest> isWebsocket =
            request -> websocketEndpoints
                    .stream()
                    .anyMatch(uri -> request.getURI().getPath().contains(uri));

    public boolean isGranted(String path, List<Role> userRoles) {
        for (Map.Entry<String, List<Role>> api : privateEndpointsAuthority.entrySet()){
            if (path.contains(api.getKey())) {
                if (CollectionUtils.containsAny(api.getValue(), userRoles))
                    return true;
            }
        }
        return false;
    }

}
