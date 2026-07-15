package com.ephirious.util;

import lombok.experimental.UtilityClass;

import java.util.Optional;

@UtilityClass
public class EnvironmentVariableConfig {
    public static String getEnvVariableOrThrow(String varName) {
        return Optional.ofNullable(System.getenv(varName))
                .orElseThrow(() -> new IllegalStateException(
                        "Not found environment variable by name '%s'".formatted(varName))
                );
    }
}
