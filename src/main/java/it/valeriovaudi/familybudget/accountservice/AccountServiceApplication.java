package it.valeriovaudi.familybudget.accountservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.web.reactive.function.server.HandlerFilterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.WebFilter;

import java.net.URI;

@EnableCaching
@EnableIntegration
@SpringBootApplication
public class AccountServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AccountServiceApplication.class, args);
    }

    @Bean
    public HandlerFilterFunction contextPathHandlerFilterFunction(ServerProperties serverProperties) {
        String contextPath = serverProperties.getServlet().getContextPath();
        System.out.println(contextPath);
        return (serverRequest, handlerFunction) -> {
            ServerHttpRequest request = serverRequest.exchange().getRequest();


            if (request.getURI().getPath().startsWith(contextPath)) {
                System.out.println("before" + request.getURI());
                URI uri = request.mutate().contextPath(contextPath).build().getURI();
                System.out.println("after" + uri);
                return handlerFunction.handle(ServerRequest.from(serverRequest).uri(uri).build());
            }

            return handlerFunction.handle(serverRequest);
        };
    }

    @Bean
    public WebFilter contextPathWebFilter(ServerProperties serverProperties) {
        String contextPath = serverProperties.getServlet().getContextPath();
        System.out.println(contextPath);
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            if (request.getURI().getPath().startsWith(contextPath)) {
                return chain.filter(
                        exchange.mutate()
                                .request(request.mutate().contextPath(contextPath).build())
                                .build());
            }
            return chain.filter(exchange);
        };
    }
}
