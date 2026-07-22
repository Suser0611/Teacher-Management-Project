package com.sms.model;

import java.sql.Date;
import java.sql.Time;

public class ScheduleRequest {
    private int requestId;
    private int teacherId;
    private int courseId;
    private String courseName;
    private String currentSchedule;
    private Date newDate;
    private Time newTimeSlot;
    private String classroom;
    private String reason;
    private String status;
    private Date requestDate;
    private String rejectionReason;
    
    // Constructors
    public ScheduleRequest() {}
    
    // Getters and Setters
    public int getRequestId() { return requestId; }
    public void setRequestId(int requestId) { this.requestId = requestId; }
    
    public int getTeacherId() { return teacherId; }
    public void setTeacherId(int teacherId) { this.teacherId = teacherId; }
    
    public int getCourseId() { return courseId; }
    public void setCourseId(int courseId) { this.courseId = courseId; }
    
    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }
    
    public String getCurrentSchedule() { return currentSchedule; }
    public void setCurrentSchedule(String currentSchedule) { this.currentSchedule = currentSchedule; }
    
    public Date getNewDate() { return newDate; }
    public void setNewDate(Date newDate) { this.newDate = newDate; }
    
    public Time getNewTimeSlot() { return newTimeSlot; }
    public void setNewTimeSlot(Time newTimeSlot) { this.newTimeSlot = newTimeSlot; }
    
    public String getClassroom() { return classroom; }
    public void setClassroom(String classroom) { this.classroom = classroom; }
    
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public Date getRequestDate() { return requestDate; }
    public void setRequestDate(Date requestDate) { this.requestDate = requestDate; }
    
    public String getRejectionReason() { return rejectionReason; }
    public void setRejectionReason(String rejectionReason) { this.rejectionReason = rejectionReason; }
}