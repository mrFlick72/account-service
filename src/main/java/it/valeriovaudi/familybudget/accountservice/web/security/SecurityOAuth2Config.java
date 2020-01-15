package it.valeriovaudi.familybudget.accountservice.web.security;

import it.valeriovaudi.vauthenticator.security.clientsecuritystarter.user.VAuthenticatorOAuth2User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcReactiveOAuth2UserService;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.userinfo.CustomUserTypesOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.ReactiveOAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.logout.RedirectServerLogoutSuccessHandler;
import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

@EnableWebFluxSecurity
public class SecurityOAuth2Config {

    @Value("${spring.security.oauth2.client.registration.client.client-id}")
    private String familyBudgetClientRegistrationId;

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
    public VAuthenticatorReactiveOidcUserService vAuthenticatorOidcUserService() {
        return new VAuthenticatorReactiveOidcUserService(
                Map.of(familyBudgetClientRegistrationId, VAuthenticatorOAuth2User.class),
                new OidcReactiveOAuth2UserService()
        );
    }

}

class VAuthenticatorReactiveOidcUserService implements ReactiveOAuth2UserService<OidcUserRequest, OidcUser> {
    private final Map<String, Class<? extends OAuth2User>> customUserTypesMapping;
    private final OidcReactiveOAuth2UserService delegate;

    public VAuthenticatorReactiveOidcUserService(Map<String, Class<? extends OAuth2User>> customUserTypesMapping,
                                                 OidcReactiveOAuth2UserService delegate) {
        this.customUserTypesMapping = customUserTypesMapping;
        this.delegate = delegate;
    }

    public Mono<OidcUser> loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        CustomUserTypesOAuth2UserService customUserTypesOAuth2UserService = new CustomUserTypesOAuth2UserService(this.customUserTypesMapping);

        Mono<OAuth2User> oAuth2User = Mono.fromCallable(() -> customUserTypesOAuth2UserService.loadUser(userRequest));
        Mono<OidcUser> delegate = this.delegate.loadUser(userRequest);
        return Mono.zip(delegate, oAuth2User, (oidcUser, oAuth2User1) -> {
            Collection<? extends GrantedAuthority> mappedAuthorities = oAuth2User1.getAuthorities().stream().map((authority) -> new OidcUserAuthority(authority.getAuthority(), oidcUser.getIdToken(), oidcUser.getUserInfo())).collect(Collectors.toList());
            return new DefaultOidcUser(mappedAuthorities, oidcUser.getIdToken(), oidcUser.getUserInfo());
        });
    }
}
