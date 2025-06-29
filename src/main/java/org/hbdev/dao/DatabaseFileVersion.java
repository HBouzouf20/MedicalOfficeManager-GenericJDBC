package org.hbdev.dao;

import java.io.File;

import org.hbdev.excepetions.DatabaseConnectionException;


public class DatabaseFileVersion {
    private File database;

    public DatabaseFileVersion() {
        File cnx = new File("database"); //folder database
        if (!cnx.exists() && !cnx.canRead()) {
            throw new DatabaseConnectionException();//Exist function
        }
        database = cnx;
        System.out.println("Connextion established ...");
        
    }
    public File getDatabase() {
        return database;
    }
    
}
