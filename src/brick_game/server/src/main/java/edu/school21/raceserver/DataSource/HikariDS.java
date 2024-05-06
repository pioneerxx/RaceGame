package edu.school21.raceserver.DataSource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class HikariDS {
    private Properties properties;
    private String url;
    private String username;
    private String password;
    private String driverName;
    public HikariDS() {
        Properties properties = new Properties();
        try (InputStream stream = this.getClass().getResourceAsStream("/db.properties")) {
            properties.load(stream);
            url = properties.getProperty("db.url");
            username = properties.getProperty("db.user");
            password = properties.getProperty("db.password");
            driverName = properties.getProperty("db.driver.name");
        } catch (IOException e) {

        }
    }
    public DataSource getDataSource() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(url);
        hikariConfig.setUsername(username);
        hikariConfig.setPassword(password);
        hikariConfig.setDriverClassName(driverName);
        return new HikariDataSource(hikariConfig);
    }
}
