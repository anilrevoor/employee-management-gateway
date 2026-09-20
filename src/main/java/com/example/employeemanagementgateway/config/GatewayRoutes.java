package com.example.employeemanagementgateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import java.time.Duration;

import static org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions.uri;
import static org.springframework.cloud.gateway.server.mvc.filter.Bucket4jFilterFunctions.rateLimit;
import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;
import static org.springframework.cloud.gateway.server.mvc.predicate.GatewayRequestPredicates.path;

@Configuration
public class GatewayRoutes {

    @Value("${employee.service.url}")
    private String employeeServiceUrl;

    @Value("${employee.service.path}")
    private String employeeServicePath;

    @Value("${employee.service.route-id}")
    private String employeeServiceRouteId;

    @Value("${gateway.rate-limit.capacity}")
    private long rateLimitCapacity;

    @Value("${gateway.rate-limit.period-seconds}")
    private long rateLimitPeriodSeconds;

    @Bean
    public RouterFunction<ServerResponse> employeeServiceRoute() {

        return route(employeeServiceRouteId)
                .route(
                        path(employeeServicePath),
                        http()
                )
                .before(uri(employeeServiceUrl))
                .filter(rateLimit(config -> config
                        .setCapacity(rateLimitCapacity)
                        .setPeriod(Duration.ofSeconds(rateLimitPeriodSeconds))
                        .setKeyResolver(request ->
                                request.servletRequest()
                                        .getUserPrincipal()
                                        .getName())
                ))
                .build();
    }
}