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
        http.authorizeHttpRequests()
                // EventController
                .requestMatchers(HttpMethod.GET, "/event/*").authenticated()
                .requestMatchers(HttpMethod.POST, "/event/*/subscribe").authenticated()
                .requestMatchers(HttpMethod.POST, "/event/*/unsubscribe").authenticated()
                .requestMatchers(HttpMethod.POST, "/event/*/rate").authenticated()
                .requestMatchers(HttpMethod.POST, "/event/*/approve").hasAuthority("EVENT_MANAGER")
                .requestMatchers(HttpMethod.POST, "/event/*/reject").hasAuthority("EVENT_MANAGER")
                .requestMatchers(HttpMethod.POST, "/event/*/remove_subscription").hasAuthority("EVENT_MANAGER")
                .requestMatchers(HttpMethod.POST, "/event/propose").authenticated()
                // HistoryController
                .requestMatchers(HttpMethod.GET, "/history").hasAuthority("EVENT_MANAGER")
                // HomeController
                .requestMatchers(HttpMethod.GET, "/").authenticated()
                // RegisterController
                .requestMatchers(HttpMethod.GET, "/register").permitAll()
                .requestMatchers(HttpMethod.POST, "/register").permitAll()
                // ProfileController
                .requestMatchers(HttpMethod.GET, "/profile").authenticated()
                .requestMatchers(HttpMethod.POST, "/profile/change_password").authenticated()
                // ProposedController
                .requestMatchers(HttpMethod.GET, "/proposed").hasAuthority("EVENT_MANAGER")
                .requestMatchers(HttpMethod.POST, "/proposed/change_password").hasAuthority("EVENT_MANAGER")
                // UserController
                .requestMatchers(HttpMethod.GET, "/user").hasAuthority("USER_MANAGER")
                .requestMatchers(HttpMethod.POST, "/user/*/enable").hasAuthority("USER_MANAGER")
                .requestMatchers(HttpMethod.POST, "/user/*/disable").hasAuthority("USER_MANAGER")
                .requestMatchers(HttpMethod.POST, "/user/*/reset_password").hasAuthority("USER_MANAGER")
                .requestMatchers(HttpMethod.POST, "/user/*/update_authorities").hasAuthority("USER_MANAGER")
                // Error manager
                .requestMatchers("/error").permitAll()
                // Less privileges by default
                .anyRequest().hasAuthority("unreachable");
        // Set error interceptor
        http.addFilterAfter(new ErrorInterceptor(), SwitchUserFilter.class);
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

    /**
     * Override the UserDetailsService of Spring Security with a custom implementation
     *
     * @param userRepository The user repository
     * @return The UserDetailsService implementation
     */
    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return new JpaUserDetailsService(userRepository);
    }

    /**
     * Override the PasswordEncoder of Spring Security with a custom implementation
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