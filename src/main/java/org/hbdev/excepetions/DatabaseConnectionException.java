package org.hbdev.excepetions;

public class DatabaseConnectionException extends RuntimeException{
    public DatabaseConnectionException(String message){

        super(message);
    }
    public DatabaseConnectionException(){
        super("Connection failed, please check your folder.");
    }

}
