package com.autovibe.dealership;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@ConditionalOnProperty(
        name = "app.bootstrap-users",
        havingValue = "true"
)
public class DataInitializer {

    @Bean
    CommandLineRunner initUsers(
            AppUserRepository appUserRepository,
            PasswordEncoder passwordEncoder,
            @Value("${app.admin.password}") String adminPassword,
            @Value("${app.sales.password}") String salesPassword) {

        return args -> {

            if (appUserRepository.findByUsername("admin").isEmpty()) {
                AppUser admin = new AppUser(
                        "admin",
                        passwordEncoder.encode(adminPassword),
                        "ADMIN"
                );

                appUserRepository.save(admin);
            }

            if (appUserRepository.findByUsername("sales").isEmpty()) {
                AppUser sales = new AppUser(
                        "sales",
                        passwordEncoder.encode(salesPassword),
                        "SALESPERSON"
                );

                appUserRepository.save(sales);
            }
        };
    }
}
