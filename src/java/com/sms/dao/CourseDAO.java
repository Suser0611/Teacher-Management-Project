package com.sms.dao;

import com.sms.model.Course;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseDAO {
    
    public List<Course> getCoursesByTeacherId(int teacherId) throws SQLException, ClassNotFoundException {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT * FROM Courses WHERE teacher_id = ? AND semester = ?";
        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, teacherId);
        pstmt.setString(2, "2026-2027-1");
        ResultSet rs = pstmt.executeQuery();
        
        while (rs.next()) {
            Course course = new Course();
            course.setCourseId(rs.getInt("course_id"));
            course.setCourseCode(rs.getString("course_code"));
            course.setCourseName(rs.getString("course_name"));
            course.setSchedule(rs.getString("schedule"));
            course.setClassroom(rs.getString("classroom"));
            course.setTeacherId(rs.getInt("teacher_id"));
            course.setCredits(rs.getInt("credits"));
            course.setSemester(rs.getString("semester"));
            course.setMaxStudents(rs.getInt("max_students"));
            course.setCurrentStudents(rs.getInt("current_students"));
            courses.add(course);
        }
        return courses;
    }
    
    public Course getCourseById(int courseId) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM Courses WHERE course_id = ?";
        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, courseId);
        ResultSet rs = pstmt.executeQuery();
        
        if (rs.next()) {
            Course course = new Course();
            course.setCourseId(rs.getInt("course_id"));
            course.setCourseCode(rs.getString("course_code"));
            course.setCourseName(rs.getString("course_name"));
            course.setSchedule(rs.getString("schedule"));
            course.setClassroom(rs.getString("classroom"));
            course.setTeacherId(rs.getInt("teacher_id"));
            course.setCredits(rs.getInt("credits"));
            course.setSemester(rs.getString("semester"));
            course.setMaxStudents(rs.getInt("max_students"));
            course.setCurrentStudents(rs.getInt("current_students"));
            return course;
        }
        return null;
    }
}