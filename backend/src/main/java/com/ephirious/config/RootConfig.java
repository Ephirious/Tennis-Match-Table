package com.ephirious.config;


import com.ephirious.entity.MatchJpaEntity;
import com.ephirious.entity.PlayerJpaEntity;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import static com.ephirious.config.DatabaseConfig.configureConnectionPool;
import static com.ephirious.config.DatabaseConfig.configureDatabase;

@org.springframework.context.annotation.Configuration
@ComponentScan(basePackages = {
        "com.ephirious.repository",
        "com.ephirious.mapper",
        "com.ephirious.service",
        "com.ephirious.transaction"
})
public class RootConfig {
    @Bean
    EntityManagerFactory entityManagerFactory() {
        Configuration configuration = new Configuration();

        configureDatabase(configuration);
        configureConnectionPool(configuration);

        configuration.addAnnotatedClass(PlayerJpaEntity.class);
        configuration.addAnnotatedClass(MatchJpaEntity.class);


        return configuration.buildSessionFactory();
    }
}
