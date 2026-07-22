package com.sms.servlet;

import com.sms.dao.TeacherDAO;
import com.sms.model.Teacher;
import java.io.File;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

@WebServlet("/upload-avatar")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 1,
    maxFileSize = 1024 * 1024 * 5,
    maxRequestSize = 1024 * 1024 * 10
)
public class UploadAvatarServlet extends HttpServlet {
    
    private TeacherDAO teacherDAO = new TeacherDAO();
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("teacher") == null) {
            response.sendRedirect("login");
            return;
        }
        
        Teacher teacher = (Teacher) session.getAttribute("teacher");
        
        try {
            Part filePart = request.getPart("avatar");
            String fileName = filePart.getSubmittedFileName();
            
            // Validate file type
            if (!fileName.toLowerCase().endsWith(".jpg") && 
                !fileName.toLowerCase().endsWith(".jpeg") && 
                !fileName.toLowerCase().endsWith(".png")) {
                request.setAttribute("error", "Only JPG, JPEG, and PNG files are allowed");
                request.getRequestDispatcher("/profile.jsp").forward(request, response);
                return;
            }
            
            // Validate file size (already handled by maxFileSize)
            
            // Save file
            String uploadPath = getServletContext().getRealPath("") + File.separator + "uploads";
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }
            
            String newFileName = teacher.getUsername() + "_" + System.currentTimeMillis() + 
                               fileName.substring(fileName.lastIndexOf("."));
            String filePath = uploadPath + File.separator + newFileName;
            filePart.write(filePath);
            
            // Update database
            teacher.setAvatar("uploads/" + newFileName);
            teacherDAO.updateTeacherProfile(teacher);
            
            // Update session
            session.setAttribute("teacher", teacher);
            
            request.setAttribute("successMessage", "Avatar uploaded successfully!");
            request.setAttribute("teacher", teacher);
            request.getRequestDispatcher("/profile.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Failed to upload avatar. Please try again.");
            request.setAttribute("teacher", teacher);
            request.getRequestDispatcher("/profile.jsp").forward(request, response);
        }
    }
}