package org.hbdev.models.database;

import lombok.extern.java.Log;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
@Log
public class DatabaseConfig {
    private static final String CONFIG_FILE = "config/database.conf";
    private static String dbUrl;
    private static String dbUser;
    private static String dbPassword;
    private static String dbDriver;

    static {
        loadConfiguration();
        try {
            log.info("Loading MySQL JDBC Driver ...");
            Class.forName(dbDriver); // Load MySQL driver
            log.info("MySQL JDBC Driver loaded successfully");
        } catch (ClassNotFoundException e) {
            log.warning("MySQL JDBC Driver not found.");
            e.printStackTrace();
        }
    }

    private static void loadConfiguration() {
        Properties props = new Properties();

        try (InputStream input = DatabaseConfig.class
                .getClassLoader()
                .getResourceAsStream(CONFIG_FILE)) {

            if (input == null) {
                throw new RuntimeException("Configuration file not found at: " + CONFIG_FILE);
            }

            props.load(input);
            dbUrl = props.getProperty("db.url");
            dbUser = props.getProperty("db.username");
            dbPassword = props.getProperty("db.password");
            dbDriver = props.getProperty("db.driver");

        } catch (Exception e) {
            System.err.println("Error loading DB configuration.");
            e.printStackTrace();
        }
    }
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    }
}
