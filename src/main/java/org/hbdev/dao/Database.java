package org.hbdev.dao;

import lombok.Getter;
import lombok.extern.java.Log;
import org.hbdev.models.database.DatabaseConfig;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Log
public class Database {
    @Getter
    private Connection connection;
    private static Database INSTANCE;

    private Database(){
        try {
            connection = DatabaseConfig.getConnection();
            log.info("Connection established");

        } catch (SQLException e) {
            if(e.getMessage().contains("Access denied for user") || e.getMessage().contains("Unknown database")) {
                log.warning("Wrong credentials");
            }

            throw new RuntimeException(e);
        }
    }

    public static Database getInstance(){
        if(INSTANCE == null){
            INSTANCE = new Database();
        }
        return INSTANCE;
    }
}
