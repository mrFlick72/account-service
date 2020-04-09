package it.valeriovaudi.familybudget.accountservice;

import io.micrometer.core.instrument.MeterRegistry;
import it.valeriovaudi.familybudget.accountservice.web.endpoint.ContextPathProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.web.server.WebFilter;

import java.util.Optional;

@EnableCaching
@EnableIntegration
@SpringBootApplication
public class AccountServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AccountServiceApplication.class, args);
    }

    @Bean
    public ContextPathProvider contextPathProvider(ServerProperties  serverProperties){
        String contextPath = Optional.ofNullable(serverProperties.getServlet().getContextPath()).orElse("/");
        return new ContextPathProvider(contextPath);
    }

    @Bean
    @Profile("!kubernetes")
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

    @Bean
    MeterRegistryCustomizer<MeterRegistry> configurer(@Value("${spring.application.name:}") String applicationName) {
        return (registry) -> registry.config().commonTags("application", applicationName);
    }

}
