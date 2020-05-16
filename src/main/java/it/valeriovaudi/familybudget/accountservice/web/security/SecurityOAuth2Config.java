package it.valeriovaudi.familybudget.accountservice.web.security;

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
import org.springframework.security.web.server.authentication.logout.RedirectServerLogoutSuccessHandler;
import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;

import java.net.URI;
import java.util.Map;

@EnableWebFluxSecurity
public class SecurityOAuth2Config {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        return http.csrf().disable()
                .authorizeExchange()
                .and()
                .authorizeExchange().pathMatchers("/actuator/**", "/oidc_logout.html").permitAll()
                .and()
                .authorizeExchange().anyExchange().authenticated()
                .and()
                .oauth2ResourceServer().jwt()
                .and()
                .and()
                .oauth2Login()
                .and()

                .logout()
                .logoutSuccessHandler(logoutSuccessHandler())
                .requiresLogout(ServerWebExchangeMatchers.pathMatchers(HttpMethod.GET, "/singout"))
                .and()
                .build();
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