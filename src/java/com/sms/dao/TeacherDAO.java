package com.sms.dao;

import com.sms.model.Teacher;
import java.sql.*;

public class TeacherDAO {
    
    public Teacher getTeacherByUsername(String username) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM Teachers WHERE username = ?";
        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, username);
        ResultSet rs = pstmt.executeQuery();
        
        if (rs.next()) {
            Teacher teacher = new Teacher();
            teacher.setTeacherId(rs.getInt("teacher_id"));
            teacher.setUsername(rs.getString("username"));
            teacher.setPassword(rs.getString("password"));
            teacher.setFullName(rs.getString("full_name"));
            teacher.setEmail(rs.getString("email"));
            teacher.setPhone(rs.getString("phone"));
            teacher.setAddress(rs.getString("address"));
            teacher.setRole(rs.getString("role"));
            teacher.setAvatar(rs.getString("avatar"));
            teacher.setActive(rs.getBoolean("is_active"));
            teacher.setFailedAttempts(rs.getInt("failed_attempts"));
            teacher.setLocked(rs.getBoolean("is_locked"));
            return teacher;
        }
        return null;
    }
    
    public boolean validateTeacherCredentials(String username, String password) throws SQLException, ClassNotFoundException {
        Teacher teacher = getTeacherByUsername(username);
        if (teacher != null && teacher.isActive() && !teacher.isLocked()) {
            return teacher.getPassword().equals(password);
        }
        return false;
    }
    
    public void updateFailedAttempts(String username, int attempts) throws SQLException, ClassNotFoundException {
        // SỬA: Sử dụng số 0 và 1 thay vì true/false cho SQL Server
        String sql = "UPDATE Teachers SET failed_attempts = ?, is_locked = ? WHERE username = ?";
        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, attempts);
        
        // SQL Server không hiểu true/false, phải dùng bit (0 hoặc 1)
        int lockedValue = (attempts >= 5) ? 1 : 0;
        pstmt.setInt(2, lockedValue);
        pstmt.setString(3, username);
        pstmt.executeUpdate();
    }
    
    public void resetFailedAttempts(String username) throws SQLException, ClassNotFoundException {
        // SỬA: Sử dụng số 0 và 1 thay vì true/false cho SQL Server
        String sql = "UPDATE Teachers SET failed_attempts = 0, is_locked = 0 WHERE username = ?";
        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, username);
        pstmt.executeUpdate();
    }
    
    public void updateTeacherProfile(Teacher teacher) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE Teachers SET full_name = ?, email = ?, phone = ?, address = ?, avatar = ? WHERE teacher_id = ?";
        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, teacher.getFullName());
        pstmt.setString(2, teacher.getEmail());
        pstmt.setString(3, teacher.getPhone());
        pstmt.setString(4, teacher.getAddress());
        pstmt.setString(5, teacher.getAvatar());
        pstmt.setInt(6, teacher.getTeacherId());
        pstmt.executeUpdate();
    }
    
    public Teacher getTeacherById(int teacherId) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM Teachers WHERE teacher_id = ?";
        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, teacherId);
        ResultSet rs = pstmt.executeQuery();
        
        if (rs.next()) {
            Teacher teacher = new Teacher();
            teacher.setTeacherId(rs.getInt("teacher_id"));
            teacher.setUsername(rs.getString("username"));
            teacher.setPassword(rs.getString("password"));
            teacher.setFullName(rs.getString("full_name"));
            teacher.setEmail(rs.getString("email"));
            teacher.setPhone(rs.getString("phone"));
            teacher.setAddress(rs.getString("address"));
            teacher.setRole(rs.getString("role"));
            teacher.setAvatar(rs.getString("avatar"));
            teacher.setActive(rs.getBoolean("is_active"));
            teacher.setLocked(rs.getBoolean("is_locked")); // Đọc từ database
            return teacher;
        }
        return null;
    }
}