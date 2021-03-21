package it.valeriovaudi.familybudget.accountservice.config;

import it.valeriovaudi.vauthenticator.security.clientsecuritystarter.user.VAuthenticatorOAuth2User;
import it.valeriovaudi.vauthenticator.security.clientsecuritystarter.user.VAuthenticatorReactiveOidcUserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcReactiveOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.CustomUserTypesOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.ReactiveOAuth2UserService;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.logout.*;
import org.springframework.security.web.server.header.ClearSiteDataServerHttpHeadersWriter;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;

import java.net.URI;
import java.util.Map;

import static org.springframework.security.web.server.header.ClearSiteDataServerHttpHeadersWriter.Directive.CACHE;
import static org.springframework.security.web.server.header.ClearSiteDataServerHttpHeadersWriter.Directive.COOKIES;

@EnableWebFluxSecurity
public class SecurityOAuth2Config {

    @Value("${granted-role.account-service}")
    private String grantedRole;

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        return http.csrf().disable()
                .authorizeExchange()
                .and()
                .authorizeExchange().pathMatchers("/user-info", "/actuator/**", "/oidc_logout.html").permitAll()
                .and()
                .authorizeExchange().anyExchange().hasAnyRole(grantedRole)
                .and()
                .oauth2ResourceServer().jwt()
                .and()
                .and()
                .oauth2Login()
                .and()
                .logout().logoutHandler(serverLogoutHandler())
                .logoutSuccessHandler(logoutSuccessHandler())
                .requiresLogout(ServerWebExchangeMatchers.pathMatchers(HttpMethod.GET, "/singout"))
                .and()
                .build();
    }

    private ServerLogoutHandler serverLogoutHandler() {
        ServerLogoutHandler securityContext = new SecurityContextServerLogoutHandler();
        ClearSiteDataServerHttpHeadersWriter writer = new ClearSiteDataServerHttpHeadersWriter(CACHE, COOKIES);
        ServerLogoutHandler clearSiteData = new HeaderWriterServerLogoutHandler(writer);
        return new DelegatingServerLogoutHandler(securityContext, clearSiteData);
    }

    private ServerLogoutSuccessHandler logoutSuccessHandler() {
        RedirectServerLogoutSuccessHandler successHandler = new RedirectServerLogoutSuccessHandler();
        successHandler.setLogoutSuccessUrl(URI.create("/site/index.html"));
        return successHandler;
    }

    @Bean
    public ReactiveOAuth2UserService vAuthenticatorOidcUserService(@Value("${vauthenticator.client.registrationId}") String registrationId) {
        return new VAuthenticatorReactiveOidcUserService(new OidcReactiveOAuth2UserService(),
                new CustomUserTypesOAuth2UserService(Map.of(registrationId, VAuthenticatorOAuth2User.class))
        );
    }

}