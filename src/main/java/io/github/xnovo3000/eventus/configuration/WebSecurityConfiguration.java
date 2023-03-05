package io.github.xnovo3000.eventus.configuration;

import io.github.xnovo3000.eventus.mvc.repository.UserRepository;
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
                // EventController
                .requestMatchers(HttpMethod.GET, "/event/*").authenticated()
                .requestMatchers(HttpMethod.POST, "/event/*/subscribe").authenticated()
                .requestMatchers(HttpMethod.POST, "/event/*/unsubscribe").authenticated()
                .requestMatchers(HttpMethod.POST, "/event/*/rate").authenticated()
                // HomeController
                .requestMatchers(HttpMethod.GET, "/").authenticated()
                .requestMatchers(HttpMethod.POST, "/propose").authenticated()
                // RegisterController
                .requestMatchers(HttpMethod.GET, "/register").permitAll()
                .requestMatchers(HttpMethod.POST, "/register").permitAll()
                // ProposedController
                .requestMatchers(HttpMethod.GET, "/proposed").hasAuthority("EVENT_MANAGER")
                .requestMatchers(HttpMethod.POST, "/proposed/approve").hasAuthority("EVENT_MANAGER")
                .requestMatchers(HttpMethod.POST, "/proposed/reject").hasAuthority("EVENT_MANAGER")
                // TODO: UserController
                .requestMatchers(HttpMethod.GET, "/user").hasAuthority("USER_MANAGER")
                // Error manager
                .requestMatchers("/error").permitAll()
                // Less privileges by default
                .anyRequest().hasAuthority("unreachable");
        // Set rememberMe cookie
        http.rememberMe()
                .key("session");
        // Enable login
        http.formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/", true)
                .permitAll();
        // Enable logout
        http.logout()
                .deleteCookies("JSESSIONID")
                .logoutSuccessUrl("/login")
                .permitAll();
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