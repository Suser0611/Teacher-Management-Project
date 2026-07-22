package com.sms.servlet;

import com.sms.dao.TeacherDAO;
import com.sms.model.Teacher;
import com.sms.util.ValidationUtil;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    
    private TeacherDAO teacherDAO = new TeacherDAO();
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        try {
            System.out.println("=== LOGIN ATTEMPT ===");
            System.out.println("Username: " + username);
            
            // Security checks
            if (username != null && !ValidationUtil.isXssSafe(username)) {
                request.setAttribute("error", "Invalid input detected");
                request.getRequestDispatcher("/login.jsp").forward(request, response);
                return;
            }
            
            // Check for empty fields
            if (username == null || username.trim().isEmpty()) {
                request.setAttribute("usernameError", "Username is required");
                request.getRequestDispatcher("/login.jsp").forward(request, response);
                return;
            }
            
            if (password == null || password.isEmpty()) {
                request.setAttribute("passwordError", "Password is required");
                request.getRequestDispatcher("/login.jsp").forward(request, response);
                return;
            }
            
            // Trim spaces from username
            username = username.trim();
            
            Teacher teacher = teacherDAO.getTeacherByUsername(username);
            
            if (teacher == null || !teacher.isActive()) {
                System.out.println("Teacher not found or inactive");
                request.setAttribute("error", "Invalid username or password");
                request.getRequestDispatcher("/login.jsp").forward(request, response);
                return;
            }
            
            System.out.println("Teacher found: " + teacher.getFullName());
            System.out.println("Is locked: " + teacher.isLocked());
            System.out.println("Password match: " + teacher.getPassword().equals(password));
            
            // Check if account is locked
            if (teacher.isLocked()) {
                System.out.println("Account is locked");
                request.setAttribute("error", "Account is locked due to multiple failed attempts");
                request.getRequestDispatcher("/login.jsp").forward(request, response);
                return;
            }
            
            // Validate password (case-sensitive)
            if (teacher.getPassword().equals(password)) {
                // Successful login
                System.out.println("Login successful!");
                teacherDAO.resetFailedAttempts(username);
                HttpSession session = request.getSession();
                session.setAttribute("teacher", teacher);
                session.setMaxInactiveInterval(30 * 60);
                response.sendRedirect("home");
            } else {
                // Failed attempt
                int attempts = teacher.getFailedAttempts() + 1;
                System.out.println("Login failed. Attempts: " + attempts);
                teacherDAO.updateFailedAttempts(username, attempts);
                request.setAttribute("error", "Invalid username or password");
                request.getRequestDispatcher("/login.jsp").forward(request, response);
            }
            
        } catch (Exception e) {
            System.err.println("=== ERROR ===");
            e.printStackTrace();
            request.setAttribute("error", "System error: " + e.getMessage());
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("teacher") != null) {
            response.sendRedirect("home");
        } else {
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }
}