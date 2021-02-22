package studentmanagement.application.config;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@EnableWebFluxSecurity
public class SecurityConfiguration {

  @Bean
  public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {

    http
        .cors()
        .and()
        .csrf()
        .disable()
        .authorizeExchange()
        .pathMatchers(HttpMethod.OPTIONS).permitAll()
        .pathMatchers("/perform_logout").permitAll()
        .pathMatchers("/actuator/health").permitAll()
        .pathMatchers("/sessions/**").authenticated()
        .anyExchange()
        .permitAll();
    return http.build();
  }

}


