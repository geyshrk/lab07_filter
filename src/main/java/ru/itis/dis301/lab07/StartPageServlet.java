package ru.itis.dis301.lab07;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/startpage")
public class StartPageServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        try{
            request.getRequestDispatcher("start.ftl").forward(request, response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ServletException e) {
            throw new RuntimeException(e);
        }


    }
}
