package it.valeriovaudi.familybudget.accountservice.web.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
public class SecurityOAuth2Config extends WebSecurityConfigurerAdapter {

    @Value("${spring.security.oauth2.client.registration.client.client-id}")
    private String familyBudgetClientRegistrationId;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .and()
                .authorizeRequests().mvcMatchers("/actuator/**", "/oidc_logout.html").permitAll()
                .and()
                .authorizeRequests().anyRequest().authenticated()
                .and()
                .oauth2ResourceServer().jwt()
                .and().and()
                .oauth2Login().defaultSuccessUrl("/site/index.html")
                .userInfoEndpoint()
                .customUserType(VAuthenticatorOAuth2User.class, familyBudgetClientRegistrationId);
    }

}