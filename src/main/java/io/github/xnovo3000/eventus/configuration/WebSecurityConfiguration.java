package io.github.xnovo3000.eventus.configuration;

import io.github.xnovo3000.eventus.repository.UserRepository;
import io.github.xnovo3000.eventus.security.JpaUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration {

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        // Ignore HTTP authentication chain for these static files
        return web -> web.ignoring().requestMatchers("/favicon.ico");
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Set authorization for endpoints
        http.authorizeHttpRequests()
                // ActionController
                .requestMatchers(HttpMethod.POST, "/action/propose_event").authenticated()
                .requestMatchers(HttpMethod.POST, "/action/participate").authenticated()
                .requestMatchers(HttpMethod.POST, "/action/dont_participate").authenticated()
                .requestMatchers(HttpMethod.POST, "/action/approve_event").hasAuthority("EVENT_MANAGER")
                .requestMatchers(HttpMethod.POST, "/action/reject_event").hasAuthority("EVENT_MANAGER")
                // EventController
                .requestMatchers(HttpMethod.GET, "/event/*").authenticated()
                // HomeController
                .requestMatchers(HttpMethod.GET, "/").authenticated()
                // RegisterController
                .requestMatchers(HttpMethod.GET, "/register").permitAll()
                .requestMatchers(HttpMethod.POST, "/register").permitAll()
                // ProposedEventsController
                .requestMatchers(HttpMethod.GET, "/proposed_events").hasAuthority("EVENT_MANAGER")
                // Less privileges by default
                .anyRequest().hasAuthority("unreachable");
        // Enable login
        http.formLogin().loginPage("/login").defaultSuccessUrl("/", true).permitAll();
        // Enable logout
        http.logout().permitAll();
        // Build chain
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return new JpaUserDetailsService(userRepository);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        return new MethodValidationPostProcessor();
    }

}