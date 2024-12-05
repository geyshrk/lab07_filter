package ru.itis.dis301.lab07;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.itis.dis301.lab07.db.DBConnection;

import java.io.IOException;
import java.sql.*;
import java.util.Map;
import java.util.Random;

@WebServlet("/usercheck")
public class UserCheckServlet extends HttpServlet {
    private static final String USER_NAME = "username";
    private static final String PASSWORD_NAME = "password";
    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        Connection connection = DBConnection.getConnection();
        try{
            String password_value = request.getParameter(PASSWORD_NAME);
            String user_value = request.getParameter(USER_NAME);
            System.out.println(user_value + " " + password_value);
            PreparedStatement statement = connection
                    .prepareStatement("SELECT password FROM login_users WHERE username = ?");

            statement.setString(1, user_value);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            String password = resultSet.getString("password");
            if (password != null && password_value.equals(password)) {
                Long value = new Random().nextLong();
                response.addCookie(new Cookie("LOGIN_ID", value.toString()));
                response.addCookie(new Cookie("LOGIN", user_value));
                Map<String, Long> authenticationData =
                        (Map<String, Long>) request.getServletContext().getAttribute("AUTH_DATA");
                authenticationData.put(user_value, value);
                request.getRequestDispatcher("index.ftl").forward(request, response);
            } else {
                request.getRequestDispatcher("login.ftl").forward(request, response);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ServletException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        DBConnection.releaseConnection(connection);


    }

}
