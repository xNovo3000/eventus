package io.github.xnovo3000.eventus.configuration;

import io.github.xnovo3000.eventus.repository.UserRepository;
import io.github.xnovo3000.eventus.util.FirstBootApplicationRunner;
import org.modelmapper.ModelMapper;
import org.modelmapper.Provider;
import org.modelmapper.spring.SpringIntegration;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EntityScan(basePackages = "io.github.xnovo3000.eventus.entity")
@EnableJpaRepositories(basePackages = "io.github.xnovo3000.eventus.repository")
public class AppConfiguration {

    @Bean
    public ApplicationRunner applicationRunner(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            @Value("${io.github.xnovo3000.eventus.admin-password}") String adminPassword
    ) {
        return new FirstBootApplicationRunner(userRepository, passwordEncoder, adminPassword);
    }

    @Bean
    public ModelMapper modelMapper(BeanFactory beanFactory) {
        ModelMapper modelMapper = new ModelMapper();
        Provider<?> provider = SpringIntegration.fromSpring(beanFactory);
        modelMapper.getConfiguration().setProvider(provider);
        return modelMapper;
    }

}