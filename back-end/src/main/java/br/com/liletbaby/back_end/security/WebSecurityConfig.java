package br.com.liletbaby.back_end.security;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.config.annotation.web.configurers.oauth2.client.OAuth2LoginConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.CorsFilter;

/**
 * WebSecurityConfig class.
 *
 * @author Wender Couto
 * @since 0.0.1-SNAPSHOT
 */

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired
    private SecurityFilter securityFilter;

    @Autowired
    private CorsFilter corsFilter;

    @Autowired
    GoogleAuthSuccessHandler googleAuthSuccessHandler;

    private final String OAUTH2_METHOD = "GET";
    private final String OAUTH2_ENDPOINT = "/auth/OAuth2/google";

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(new Customizer<>() {
                    @Override
                    public void customize(SessionManagementConfigurer<HttpSecurity> sessionManagement) {
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                    }
                }).authorizeHttpRequests(new Customizer<>() {
                    @Override
                    public void customize(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authMatchers) {
                        authMatchers.requestMatchers(HttpMethod.GET, "/home").permitAll();
                        authMatchers.requestMatchers(HttpMethod.POST, "/auth/login").permitAll();
                        authMatchers.requestMatchers(HttpMethod.GET, OAUTH2_ENDPOINT).permitAll();
                        authMatchers.requestMatchers(HttpMethod.POST, "/auth/register").permitAll();
                        authMatchers.requestMatchers(HttpMethod.PUT, "/auth/user/update").authenticated();
                        authMatchers.anyRequest().authenticated();
                    }
                }).oauth2Login(new Customizer<>() {
                    @Override
                    public void customize(OAuth2LoginConfigurer<HttpSecurity> httpSecurityOAuth2LoginConfigurer) {
                        httpSecurityOAuth2LoginConfigurer.loginPage("/auth/OAuth2/google");
                        httpSecurityOAuth2LoginConfigurer.successHandler(googleAuthSuccessHandler);
                    }
                }).exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint((request, response, authException) -> { // Limitando a uma ou mais Rotas para OAUTH2
                            if (OAUTH2_METHOD.equals(request.getMethod()) && OAUTH2_ENDPOINT.equals(request.getServletPath())) {
                                response.sendRedirect("/login/oauth2/code/google");
                            } else {
                                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                            }
                        })
                ).addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}