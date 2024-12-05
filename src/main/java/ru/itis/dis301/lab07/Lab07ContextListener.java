package ru.itis.dis301.lab07;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import ru.itis.dis301.lab07.db.DBConnection;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@WebListener
public class Lab07ContextListener implements ServletContextListener {
    public void contextInitialized(ServletContextEvent sce) {
        DBConnection.getInstance().initDb();
        Map<String, Long> authenticationData = new HashMap<>();
        sce.getServletContext().setAttribute("AUTH_DATA", authenticationData);
    }

    public void contextDestroyed(ServletContextEvent sce) {
        // Закрываем подключение
    }
}
