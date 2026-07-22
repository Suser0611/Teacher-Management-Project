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

@WebServlet("/profile")
public class ProfileServlet extends HttpServlet {
    
    private TeacherDAO teacherDAO = new TeacherDAO();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("teacher") == null) {
            response.sendRedirect("login");
            return;
        }
        
        try {
            Teacher teacher = (Teacher) session.getAttribute("teacher");
            request.setAttribute("teacher", teacher);
            request.getRequestDispatcher("/profile.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("home");
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("teacher") == null) {
            response.sendRedirect("login");
            return;
        }
        
        String action = request.getParameter("action");
        
        if ("update".equals(action)) {
            handleUpdateProfile(request, response, session);
        } else if ("cancel".equals(action)) {
            response.sendRedirect("profile");
        } else {
            response.sendRedirect("profile");
        }
    }
    
    private void handleUpdateProfile(HttpServletRequest request, HttpServletResponse response, HttpSession session) 
            throws ServletException, IOException {
        
        Teacher currentTeacher = (Teacher) session.getAttribute("teacher");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        
        boolean hasError = false;
        
        // Validate email
        if (ValidationUtil.isNullOrEmpty(email)) {
            request.setAttribute("emailError", "Email is required");
            hasError = true;
        } else if (!ValidationUtil.isValidEmail(email)) {
            request.setAttribute("emailError", "Invalid email format");
            hasError = true;
        }
        
        // Validate phone
        if (ValidationUtil.isNullOrEmpty(phone)) {
            request.setAttribute("phoneError", "Phone number is required");
            hasError = true;
        } else if (!ValidationUtil.isValidPhone(phone)) {
            request.setAttribute("phoneError", "Phone must contain 10-11 digits");
            hasError = true;
        }
        
        // Validate address length
        if (!ValidationUtil.isNullOrEmpty(address) && !ValidationUtil.isValidLength(address, 255)) {
            request.setAttribute("addressError", "Address must not exceed 255 characters");
            hasError = true;
        }
        
        // Security validation
        if (!ValidationUtil.isXssSafe(address) || !ValidationUtil.isSqlInjectionSafe(address)) {
            request.setAttribute("addressError", "Invalid characters in address");
            hasError = true;
        }
        
        if (hasError) {
            request.setAttribute("teacher", currentTeacher);
            request.getRequestDispatcher("/profile.jsp").forward(request, response);
            return;
        }
        
        try {
            Teacher updatedTeacher = new Teacher();
            updatedTeacher.setTeacherId(currentTeacher.getTeacherId());
            updatedTeacher.setFullName(currentTeacher.getFullName());
            updatedTeacher.setEmail(email);
            updatedTeacher.setPhone(phone);
            updatedTeacher.setAddress(address);
            updatedTeacher.setAvatar(currentTeacher.getAvatar());
            
            teacherDAO.updateTeacherProfile(updatedTeacher);
            
            // Update session
            currentTeacher.setEmail(email);
            currentTeacher.setPhone(phone);
            currentTeacher.setAddress(address);
            session.setAttribute("teacher", currentTeacher);
            
            request.setAttribute("successMessage", "Profile updated successfully!");
            request.setAttribute("teacher", currentTeacher);
            request.getRequestDispatcher("/profile.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Failed to update profile. Please try again.");
            request.setAttribute("teacher", currentTeacher);
            request.getRequestDispatcher("/profile.jsp").forward(request, response);
        }
    }
}