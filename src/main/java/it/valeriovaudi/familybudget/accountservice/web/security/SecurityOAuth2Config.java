package it.valeriovaudi.familybudget.accountservice.web.security;

//import it.valeriovaudi.vauthenticator.security.clientsecuritystarter.user.VAuthenticatorOAuth2User;
//import it.valeriovaudi.vauthenticator.security.clientsecuritystarter.user.VAuthenticatorOidcUserService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcReactiveOAuth2UserService;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.logout.RedirectServerLogoutSuccessHandler;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;

import java.net.URI;

@EnableWebFluxSecurity
public class SecurityOAuth2Config {

    @Value("${spring.security.oauth2.client.registration.client.client-id}")
    private String familyBudgetClientRegistrationId;

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        RedirectServerLogoutSuccessHandler logoutSuccessHandler = new RedirectServerLogoutSuccessHandler();
        logoutSuccessHandler.setLogoutSuccessUrl(URI.create("/account/site/index.html"));

        return http.csrf().disable()
                .authorizeExchange()
                .and()
                .authorizeExchange().pathMatchers("/actuator/**", "/oidc_logout.html").permitAll()
                .and()
                .authorizeExchange().anyExchange().authenticated()
                .and()
                .oauth2ResourceServer().jwt()
                .and().and()
                .oauth2Login()
                .and()

                .logout()
                .logoutUrl("/account/logout")
                .requiresLogout(ServerWebExchangeMatchers.pathMatchers(HttpMethod.GET, "/account/logout"))
                .and()

//                .authenticationConverter(new OidcReactiveOAuth2UserService())
                .build();
//                .defaultSuccessUrl("/site/index.html")
//                .userInfoEndpoint()
//                .oidcUserService(vAuthenticatorOidcUserService());
    }

    @Bean
    public OidcReactiveOAuth2UserService oidcReactiveOAuth2UserService() {
        return new OidcReactiveOAuth2UserService();
    }

/*    public VAuthenticatorOidcUserService vAuthenticatorOidcUserService() {
        return new VAuthenticatorOidcUserService(
                Map.of(familyBudgetClientRegistrationId, VAuthenticatorOAuth2User.class),
                new OidcUserService()
        );
    }*/

}