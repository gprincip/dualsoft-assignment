package net.dualsoft.assignment.configuration;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
public class DataSourceConfig {

    private static final String PG_DRIVER = "org.postgresql.Driver";
	private static final String PG_USER_PASSWORD = "PG_PASSWORD";
	private static final String PG_USER_USERNAME = "PG_USERNAME";
	private static final String PG_MATCHES_DB_CONNECTION_URL = "PG_MATCHES_DB_CONNECTION_URL";

	@Bean(name = "dataSource")
    public HikariDataSource dataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class)
                .url(System.getenv(PG_MATCHES_DB_CONNECTION_URL))
                .username(System.getenv(PG_USER_USERNAME))
                .password(System.getenv(PG_USER_PASSWORD))
                .driverClassName(PG_DRIVER)
                .build();
    }
	
}
