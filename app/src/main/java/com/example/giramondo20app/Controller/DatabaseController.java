package com.example.giramondo20app.Controller;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;



public class DatabaseController {
    private static final String Url = "jdbc:mysql://192.168.42.130:3306/database_giramondo";
    private static final String users = "admin";
    private static final String pass = "ninfadora97";
    private static Connection Connection;

    public static void connect() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection = DriverManager.getConnection(Url, users, pass);
        } catch (Exception e) {
            System.out.println(e + "\nTentativo di connessione fallito.");
        }
    }

    public static Connection getConnection() {
        return Connection;
    }

    public static void disconnect() {
        try {
            Connection.close();
        } catch (SQLException e) {
            System.out.println(e + "\nErrore durante la disconnessione");
        }
    }
}
