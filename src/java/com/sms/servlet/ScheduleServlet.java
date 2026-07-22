package com.sms.servlet;

import com.sms.dao.CourseDAO;
import com.sms.dao.ScheduleRequestDAO;
import com.sms.model.Course;
import com.sms.model.ScheduleRequest;
import com.sms.model.Teacher;
import com.sms.util.ValidationUtil;
import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/schedule")
public class ScheduleServlet extends HttpServlet {
    
    private ScheduleRequestDAO requestDAO = new ScheduleRequestDAO();
    private CourseDAO courseDAO = new CourseDAO();
    
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
            List<Course> courses = courseDAO.getCoursesByTeacherId(teacher.getTeacherId());
            request.setAttribute("courses", courses);
            request.getRequestDispatcher("/change-schedule.jsp").forward(request, response);
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
        
        if ("submit".equals(action)) {
            handleSubmitRequest(request, response, session);
        } else if ("cancel".equals(action)) {
            response.sendRedirect("schedule");
        } else {
            response.sendRedirect("schedule");
        }
    }
    
    private void handleSubmitRequest(HttpServletRequest request, HttpServletResponse response, HttpSession session) 
            throws ServletException, IOException {
        
        Teacher teacher = (Teacher) session.getAttribute("teacher");
        String courseIdStr = request.getParameter("courseId");
        String currentSchedule = request.getParameter("currentSchedule");
        String newDateStr = request.getParameter("newDate");
        String timeSlotStr = request.getParameter("timeSlot");
        String classroom = request.getParameter("classroom");
        String reason = request.getParameter("reason");
        
        // Validation checks
        boolean hasError = false;
        
        // Check empty fields
        if (ValidationUtil.isNullOrEmpty(courseIdStr)) {
            request.setAttribute("courseError", "Course is required");
            hasError = true;
        }
        
        if (ValidationUtil.isNullOrEmpty(newDateStr)) {
            request.setAttribute("dateError", "New date is required");
            hasError = true;
        } else if (ValidationUtil.isValidDate(newDateStr)) {
            Date newDate = Date.valueOf(newDateStr);
            if (ValidationUtil.isPastDate(newDate)) {
                request.setAttribute("dateError", "Date cannot be in the past");
                hasError = true;
            }
        }
        
        if (ValidationUtil.isNullOrEmpty(timeSlotStr)) {
            request.setAttribute("timeSlotError", "Time slot is required");
            hasError = true;
        }
        
        if (ValidationUtil.isNullOrEmpty(classroom)) {
            request.setAttribute("classroomError", "Classroom is required");
            hasError = true;
        }
        
        if (ValidationUtil.isNullOrEmpty(reason)) {
            request.setAttribute("reasonError", "Reason is required");
            hasError = true;
        } else if (!ValidationUtil.isValidLength(reason, 500)) {
            request.setAttribute("reasonError", "Reason must not exceed 500 characters");
            hasError = true;
        } else if (!ValidationUtil.isXssSafe(reason) || !ValidationUtil.isSqlInjectionSafe(reason)) {
            request.setAttribute("reasonError", "Invalid characters in reason");
            hasError = true;
        }
        
        if (hasError) {
            try {
                List<Course> courses = courseDAO.getCoursesByTeacherId(teacher.getTeacherId());
                request.setAttribute("courses", courses);
            } catch (Exception e) {
                e.printStackTrace();
            }
            request.getRequestDispatcher("/change-schedule.jsp").forward(request, response);
            return;
        }
        
        try {
            int courseId = Integer.parseInt(courseIdStr);
            Date newDate = Date.valueOf(newDateStr);
            Time timeSlot = Time.valueOf(timeSlotStr + ":00");
            
            // Check for conflicts
            boolean hasConflict = false;
            
            // Check classroom conflict
            if (requestDAO.checkClassroomConflict(classroom, newDate, timeSlot)) {
                request.setAttribute("classroomError", "Classroom is already occupied at this time");
                hasConflict = true;
            }
            
            // Check teacher conflict (simplified)
            if (requestDAO.checkTeacherConflict(teacher.getTeacherId(), newDate, timeSlot)) {
                request.setAttribute("error", "You already have another class at this time");
                hasConflict = true;
            }
            
            if (hasConflict) {
                List<Course> courses = courseDAO.getCoursesByTeacherId(teacher.getTeacherId());
                request.setAttribute("courses", courses);
                request.getRequestDispatcher("/change-schedule.jsp").forward(request, response);
                return;
            }
            
            // Create schedule request
            ScheduleRequest scheduleRequest = new ScheduleRequest();
            scheduleRequest.setTeacherId(teacher.getTeacherId());
            scheduleRequest.setCourseId(courseId);
            scheduleRequest.setCurrentSchedule(currentSchedule);
            scheduleRequest.setNewDate(newDate);
            scheduleRequest.setNewTimeSlot(timeSlot);
            scheduleRequest.setClassroom(classroom);
            scheduleRequest.setReason(reason);
            
            boolean success = requestDAO.createScheduleRequest(scheduleRequest);
            
            if (success) {
                request.setAttribute("successMessage", "Schedule change request submitted successfully!");
                request.setAttribute("notification", "Your request has been sent for approval.");
            } else {
                request.setAttribute("error", "Failed to submit request. Please try again.");
            }
            
            List<Course> courses = courseDAO.getCoursesByTeacherId(teacher.getTeacherId());
            request.setAttribute("courses", courses);
            request.getRequestDispatcher("/change-schedule.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "System error. Please try again.");
            try {
                List<Course> courses = courseDAO.getCoursesByTeacherId(teacher.getTeacherId());
                request.setAttribute("courses", courses);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            request.getRequestDispatcher("/change-schedule.jsp").forward(request, response);
        }
    }
}