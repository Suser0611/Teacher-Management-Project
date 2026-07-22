package com.sms.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    
    // === CẬP NHẬT THEO CẤU HÌNH SQL SERVER CỦA BẠN ===
    // Cấu hình cho SQL Server 2022 với xác thực SQL
    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=ScheduleManagement;encrypt=true;trustServerCertificate=true";
    private static final String USER = "admin";
    private static final String PASSWORD = "123123"; // THAY ĐỔI PASSWORD CỦA BẠN
    
    private static Connection connection = null;
    
    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        if (connection == null || connection.isClosed()) {
            try {
                System.out.println("Loading SQL Server driver...");
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                System.out.println("Driver loaded successfully!");
                
                System.out.println("Connecting to database...");
                System.out.println("URL: " + URL);
                System.out.println("User: " + USER);
                
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Database connected successfully!");
                System.out.println("Database: " + connection.getCatalog());
                
            } catch (ClassNotFoundException e) {
                System.err.println("ERROR: SQL Server driver not found!");
                System.err.println("Please add sqljdbc42.jar to WEB-INF/lib");
                throw e;
            } catch (SQLException e) {
                System.err.println("ERROR: SQL Exception");
                System.err.println("Error Code: " + e.getErrorCode());
                System.err.println("SQL State: " + e.getSQLState());
                System.err.println("Message: " + e.getMessage());
                
                if (e.getErrorCode() == 18456) {
                    System.err.println("-> TIPS: Sai username hoặc password SQL Server!");
                    System.err.println("-> Kiểm tra USER = " + USER + " và PASSWORD");
                } else if (e.getMessage().contains("TCP/IP")) {
                    System.err.println("-> TIPS: Không thể kết nối đến SQL Server!");
                    System.err.println("-> Kiểm tra: SQL Server đã chạy chưa?");
                    System.err.println("-> Kiểm tra: Port 1433 có mở không?");
                    System.err.println("-> Kiểm tra: Firewall có chặn không?");
                }
                throw e;
            }
        }
        return connection;
    }
    
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Database connection closed.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    // Hàm test kết nối - Chạy riêng để kiểm tra
    public static void main(String[] args) {
        try {
            System.out.println("=== TESTING DATABASE CONNECTION ===");
            Connection conn = getConnection();
            System.out.println("SUCCESS! Connected to: " + conn.getCatalog());
            closeConnection();
        } catch (Exception e) {
            System.err.println("FAILED! Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}