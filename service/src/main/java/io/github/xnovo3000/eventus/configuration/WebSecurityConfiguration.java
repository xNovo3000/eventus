package io.github.xnovo3000.eventus.configuration;

import io.github.xnovo3000.eventus.api.repository.UserRepository;
import io.github.xnovo3000.eventus.security.JpaUserDetailsService;
import io.github.xnovo3000.eventus.util.ErrorInterceptor;
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
import org.springframework.security.web.authentication.switchuser.SwitchUserFilter;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

/**
 * Configuration for anything related to web security
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration {

    /**
     * Edits the current WebSecurityCustomizer ignoring static files that are public
     *
     * @return The built customizer
     */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        // Ignore HTTP authentication chain for these static files
        return web -> web.ignoring().requestMatchers("/favicon.ico");
    }

    /**
     * Create the security configuration
     *
     * @param http The base chain
     * @return The built chain
     * @throws Exception in case of misconfiguration
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Set authorization for endpoints
        http.authorizeHttpRequests((customizer) -> {
            customizer.requestMatchers(HttpMethod.GET, "/event/*").authenticated();
            customizer.requestMatchers(HttpMethod.POST, "/event/*/subscribe").authenticated();
            customizer.requestMatchers(HttpMethod.POST, "/event/*/unsubscribe").authenticated();
            customizer.requestMatchers(HttpMethod.POST, "/event/*/rate").authenticated();
            customizer.requestMatchers(HttpMethod.POST, "/event/*/approve").hasAuthority("EVENT_MANAGER");
            customizer.requestMatchers(HttpMethod.POST, "/event/*/reject").hasAuthority("EVENT_MANAGER");
            customizer.requestMatchers(HttpMethod.POST, "/event/*/remove_subscription").hasAuthority("EVENT_MANAGER");
            customizer.requestMatchers(HttpMethod.POST, "/event/propose").authenticated();
            // HistoryController
            customizer.requestMatchers(HttpMethod.GET, "/history").hasAuthority("EVENT_MANAGER");
            // HomeController
            customizer.requestMatchers(HttpMethod.GET, "/").authenticated();
            // RegisterController
            customizer.requestMatchers(HttpMethod.GET, "/register").permitAll();
            customizer.requestMatchers(HttpMethod.POST, "/register").permitAll();
            // ProfileController
            customizer.requestMatchers(HttpMethod.GET, "/profile").authenticated();
            customizer.requestMatchers(HttpMethod.POST, "/profile/change_password").authenticated();
            // ProposedController
            customizer.requestMatchers(HttpMethod.GET, "/proposed").hasAuthority("EVENT_MANAGER");
            customizer.requestMatchers(HttpMethod.POST, "/proposed/change_password").hasAuthority("EVENT_MANAGER");
            // UserController
            customizer.requestMatchers(HttpMethod.GET, "/user").hasAuthority("USER_MANAGER");
            customizer.requestMatchers(HttpMethod.POST, "/user/*/enable").hasAuthority("USER_MANAGER");
            customizer.requestMatchers(HttpMethod.POST, "/user/*/disable").hasAuthority("USER_MANAGER");
            customizer.requestMatchers(HttpMethod.POST, "/user/*/reset_password").hasAuthority("USER_MANAGER");
            customizer.requestMatchers(HttpMethod.POST, "/user/*/update_authorities").hasAuthority("USER_MANAGER");
            // Error manager
            customizer.requestMatchers("/error").permitAll();
            // Less privileges by default
            customizer.anyRequest().hasAuthority("unreachable");
        });
        // Set error interceptor
        http.addFilterAfter(new ErrorInterceptor(), SwitchUserFilter.class);
        // Set rememberMe cookie
        http.rememberMe((customizer) -> customizer.key("session"));
        // Enable login
        http.formLogin((customizer) -> {
            customizer.loginPage("/login");
            customizer.defaultSuccessUrl("/", true);
            customizer.permitAll();
        });
        // Enable logout
        http.logout((customizer) -> {
            customizer.deleteCookies("JSESSIONID");
            customizer.logoutSuccessUrl("/login");
            customizer.permitAll();
        });
        // Build chain
        return http.build();
    }

    /**
     * Override the UserDetailsService of Spring Security with a custom implementation.
     * In this case JpaUserDetailsService is returned
     *
     * @param userRepository The user repository
     * @return The UserDetailsService implementation
     */
    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return new JpaUserDetailsService(userRepository);
    }

    /**
     * Override the PasswordEncoder of Spring Security with a safer implementation.
     * In this case BCryptPasswordEncoder is returned
     *
     * @return The PasswordEncoder implementation
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Create the Hibernate validation system
     *
     * @return The MethodValidationPostProcessor
     */
    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        return new MethodValidationPostProcessor();
    }

}