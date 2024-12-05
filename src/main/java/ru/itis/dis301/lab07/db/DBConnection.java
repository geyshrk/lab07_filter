package ru.itis.dis301.lab07.db;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;

public class DBConnection {
    private static DBConnection instance;
    private static int MAX_SIZE;
    private static Queue<Connection> connectionQueue;
    private static Set<Connection> activeConnections;
    private static final Object dbConnectionMonitor = new Object();
    private DBConnection(){};
    public static DBConnection getInstance(){
        if (instance == null) {
            synchronized (DBConnection.class) {
                if (instance == null) {
                    instance = new DBConnection();
                }
            }
        }
        return instance;
    }
    public void initDb() {
        Properties properties = new Properties();

        try {
            properties.load(Thread.currentThread()
                    .getContextClassLoader()
                    .getResourceAsStream("db.properties"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        MAX_SIZE = Integer.parseInt(properties.getProperty("database.max-pool-size"));
        String url = properties.getProperty("database.url");
        String user = properties.getProperty("database.username");
        String password = properties.getProperty("database.password");

        connectionQueue = new ArrayDeque<>();
        activeConnections = new HashSet<>();

        try {
            Class.forName(properties.getProperty("database.driver-name"));
            for (int i = 0; i < MAX_SIZE; ++i){
                connectionQueue.add(DriverManager.getConnection(url, user, password));
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static synchronized Connection getConnection() {
        try {
            while (connectionQueue.isEmpty())
                dbConnectionMonitor.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Connection connection = connectionQueue.poll();
        activeConnections.add(connection);
        return connection;
    }

    public static synchronized void releaseConnection(Connection connection) {
        activeConnections.remove(connection);
        connectionQueue.add(connection);
        synchronized (dbConnectionMonitor) {
            dbConnectionMonitor.notifyAll();
        }
    }
    public static void closeAllConnections(){

            try {
                for(Connection connection : activeConnections) {
                    connection.close();
                }
                for(Connection connection : connectionQueue) {
                    connection.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
}
