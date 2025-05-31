package org.example.tasklist.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.Connection;

@Configuration
@RequiredArgsConstructor
public class DataSourceConfig {

    private final DataSource dataSource;

    public Connection getConnection() throws Exception {
        return dataSource.getConnection();
    }
}
