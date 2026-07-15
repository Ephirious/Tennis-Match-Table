package com.ephirious.config;


import jakarta.persistence.EntityManagerFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.context.annotation.Bean;
import static com.ephirious.util.EnvironmentVariableConfig.getEnvVariableOrThrow;

@org.springframework.context.annotation.Configuration
public class RootConfig {
    private static final String DB_PROTOCOL = getEnvVariableOrThrow("DATABASE_PROTOCOL");
    private static final String DB_SERVER = getEnvVariableOrThrow("DATABASE_SERVER");
    private static final String DB_PORT = getEnvVariableOrThrow("DATABASE_PORT");
    private static final String DB_NAME = getEnvVariableOrThrow("DATABASE_NAME");
    private static final String DB_USER = getEnvVariableOrThrow("DATABASE_USER");
    private static final String DB_PASSWORD = getEnvVariableOrThrow("DATABASE_PASSWORD");
    private static final String DB_DRIVER = getEnvVariableOrThrow("DATABASE_DRIVER");

    private static final String DB_CP_PROVIDER = "org.hibernate.hikaricp.internal.HikariCPConnectionProvider";
    private static final String CP_POOL_SIZE = "10";
    private static final String CP_POOL_IDLE_TIMEOUT = "60000";

    @Bean
    EntityManagerFactory entityManagerFactory() {
        Configuration configuration = new Configuration();

        configureDatabase(configuration);
        configureConnectionPool(configuration);

        configuration.addPackage("com.ephirious.model.entity");

        return configuration.buildSessionFactory();
    }

    private String constructDbUrl() {
        return "%s://%s:%s/%s".formatted(
                DB_PROTOCOL,
                DB_SERVER,
                DB_PORT,
                DB_NAME
        );
    }

    private void configureDatabase(Configuration configuration) {
        configuration.setProperty("hibernate.connection.url", constructDbUrl());
        configuration.setProperty("hibernate.connection.username", DB_USER);
        configuration.setProperty("hibernate.connection.password", DB_PASSWORD);
        configuration.setProperty("hibernate.connection.driver_class", DB_DRIVER);
    }

    private void configureConnectionPool(Configuration configuration) {
        configuration.setProperty("hibernate.connection.provider_class", DB_CP_PROVIDER);
        configuration.setProperty("hibernate.hikari.maximumPoolSize", CP_POOL_SIZE);
        configuration.setProperty("hibernate.hikari.idleTimeout", CP_POOL_IDLE_TIMEOUT);
        configuration.setProperty("hibernate.hikari.dataSource.url", constructDbUrl());
        configuration.setProperty("hibernate.hikari.dataSource.user", DB_USER);
        configuration.setProperty("hibernate.hikari.dataSource.password", DB_PASSWORD);
    }
}
