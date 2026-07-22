package com.sms.dao;

import com.sms.model.ScheduleRequest;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ScheduleRequestDAO {
    
    public boolean createScheduleRequest(ScheduleRequest request) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO ScheduleRequests (teacher_id, course_id, current_schedule, new_date, " +
                    "new_time_slot, classroom, reason, status, request_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?, GETDATE())";
        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, request.getTeacherId());
        pstmt.setInt(2, request.getCourseId());
        pstmt.setString(3, request.getCurrentSchedule());
        pstmt.setDate(4, request.getNewDate());
        pstmt.setTime(5, request.getNewTimeSlot());
        pstmt.setString(6, request.getClassroom());
        pstmt.setString(7, request.getReason());
        pstmt.setString(8, "Pending");
        
        int result = pstmt.executeUpdate();
        return result > 0;
    }
    
    public boolean checkClassroomConflict(String classroom, Date date, Time timeSlot) throws SQLException, ClassNotFoundException {
        // SỬA: Sử dụng 'Rejected' thay vì 'Rejected' với dấu nháy đơn đúng
        String sql = "SELECT COUNT(*) FROM ScheduleRequests WHERE classroom = ? AND new_date = ? AND new_time_slot = ? AND status != ?";
        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, classroom);
        pstmt.setDate(2, date);
        pstmt.setTime(3, timeSlot);
        pstmt.setString(4, "Rejected");
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            return rs.getInt(1) > 0;
        }
        return false;
    }
    
    public boolean checkTeacherConflict(int teacherId, Date date, Time timeSlot) throws SQLException, ClassNotFoundException {
        // Kiểm tra xem giáo viên có lớp học nào trùng thời gian không
        String sql = "SELECT COUNT(*) FROM Courses WHERE teacher_id = ? AND schedule LIKE ?";
        // Lưu ý: Cần điều chỉnh logic kiểm tra conflict theo cấu trúc dữ liệu thực tế
        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, teacherId);
        // Tạm thời check đơn giản
        pstmt.setString(2, "%" + timeSlot.toString().substring(0, 5) + "%");
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            return rs.getInt(1) > 0;
        }
        return false;
    }
    
    public List<ScheduleRequest> getRequestsByTeacherId(int teacherId) throws SQLException, ClassNotFoundException {
        List<ScheduleRequest> requests = new ArrayList<>();
        String sql = "SELECT sr.*, c.course_name FROM ScheduleRequests sr " +
                    "JOIN Courses c ON sr.course_id = c.course_id " +
                    "WHERE sr.teacher_id = ? ORDER BY sr.request_date DESC";
        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, teacherId);
        ResultSet rs = pstmt.executeQuery();
        
        while (rs.next()) {
            ScheduleRequest request = new ScheduleRequest();
            request.setRequestId(rs.getInt("request_id"));
            request.setTeacherId(rs.getInt("teacher_id"));
            request.setCourseId(rs.getInt("course_id"));
            request.setCourseName(rs.getString("course_name"));
            request.setCurrentSchedule(rs.getString("current_schedule"));
            request.setNewDate(rs.getDate("new_date"));
            request.setNewTimeSlot(rs.getTime("new_time_slot"));
            request.setClassroom(rs.getString("classroom"));
            request.setReason(rs.getString("reason"));
            request.setStatus(rs.getString("status"));
            request.setRequestDate(rs.getDate("request_date"));
            requests.add(request);
        }
        return requests;
    }
}