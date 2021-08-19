package com.erdaldalkiran.demospringcloudgateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.factory.SetPathGatewayFilterFactory;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.context.annotation.Bean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@SpringBootApplication
public class DemoSpringCloudGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoSpringCloudGatewayApplication.class, args);
    }

//    @Bean
//    RouteLocator gateway(RouteLocatorBuilder rlb) {
//        return rlb
//            .routes()
//            .route(predicateSpec ->  predicateSpec
//                .path("/hello")
//                .filters(gatewayFilterSpec -> gatewayFilterSpec.setPath("/guides"))
//                .uri("https://spring.io/"))
//            .route("twitter", predicateSpec -> predicateSpec
//                .path("/twitter/**")
//                .filters( gatewayFilterSpec -> gatewayFilterSpec
//                    .rewritePath(
//                        "/twitter/(?<handle>.*)",
//                        "/@${handle}"))
//                .uri("https://twitter.com/")
//            )
//            .build();
//    }

    @Bean
    RouteLocator customBuild(SetPathGatewayFilterFactory ff) {
        var singleRoute = Route.async()
            .id("ciko")
            .uri("https://google.com")
//            .filter((exchange, chain) -> {
//                var request = exchange.getRequest().mutate().path("/erdal-was-here!").build();
//                return chain.filter(exchange.mutate().request(request).build());
//            })
            .filter(new OrderedGatewayFilter(ff.apply(config -> config.setTemplate("/erdal-was-here!")), 1))
            .asyncPredicate(serverWebExchange -> {
                var path = serverWebExchange.getRequest().getURI().getPath();
                return Mono.just(path.contains("ciko"));
            })
            .build();

        return () -> Flux.just(singleRoute);
    }
}
