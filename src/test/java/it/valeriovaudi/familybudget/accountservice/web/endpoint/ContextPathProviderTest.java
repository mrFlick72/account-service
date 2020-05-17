package it.valeriovaudi.familybudget.accountservice.web.endpoint;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.web.ServerProperties;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

class ContextPathProviderTest {

    @Test
    public void whenServletPropertiesIsNull() {
        ServerProperties serverProperties = new ServerProperties();
        String contextPath = Optional.ofNullable(serverProperties.getServlet())
                .flatMap(servlet -> Optional.ofNullable(servlet.getContextPath()))
                .orElse("/");
        ContextPathProvider contextPathProvider = new ContextPathProvider(contextPath);

        assertThat(contextPathProvider.pathFor("/index"), equalTo("/index"));
    }
}