<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.sms.model.Teacher" %>
<%@ page import="com.sms.model.Course" %>
<%@ page import="java.util.List" %>
<%
    Teacher teacher = (Teacher) session.getAttribute("teacher");
    if (teacher == null) {
        response.sendRedirect("login");
        return;
    }
    List<Course> courses = (List<Course>) request.getAttribute("courses");
%>
<!DOCTYPE html>
<html>
<head>
    <title>Change Schedule - Schedule Management System</title>
    <link rel="stylesheet" href="css/style.css">
    <script src="js/script.js"></script>
</head>
<body>
    <div class="container">
        <header>
            <div class="header-content">
                <div class="logo">
                    <img src="images/logo.png" alt="System Logo" height="50">
                    <span>Schedule Management System</span>
                </div>
                <div class="user-info">
                    <span>Welcome, <%= teacher.getFullName() %></span>
                    <a href="logout" class="logout-btn">Logout</a>
                </div>
            </div>
        </header>
        
        <nav>
            <ul class="nav-menu">
                <li><a href="home">Home</a></li>
                <li><a href="schedule" class="active">Change Schedule</a></li>
                <li><a href="profile">Teacher Profile</a></li>
            </ul>
        </nav>
        
        <main>
            <h1>Change Schedule Request</h1>
            
            <% if (request.getAttribute("successMessage") != null) { %>
                <div class="success-message"><%= request.getAttribute("successMessage") %></div>
            <% } %>
            
            <% if (request.getAttribute("error") != null) { %>
                <div class="error-message"><%= request.getAttribute("error") %></div>
            <% } %>
            
            <form action="schedule" method="post" onsubmit="return validateScheduleForm()">
                <input type="hidden" name="action" value="submit">
                
                <div class="form-group">
                    <label for="courseId">Course *</label>
                    <select id="courseId" name="courseId">
                        <option value="">Select a course</option>
                        <% if (courses != null) { %>
                            <% for (Course course : courses) { %>
                                <option value="<%= course.getCourseId() %>" 
                                    <%= request.getParameter("courseId") != null && 
                                       request.getParameter("courseId").equals(String.valueOf(course.getCourseId())) ? "selected" : "" %>
                                    data-schedule="<%= course.getSchedule() %>">
                                    <%= course.getCourseCode() %> - <%= course.getCourseName() %>
                                </option>
                            <% } %>
                        <% } %>
                    </select>
                    <% if (request.getAttribute("courseError") != null) { %>
                        <span class="field-error"><%= request.getAttribute("courseError") %></span>
                    <% } %>
                </div>
                
                <div class="form-group">
                    <label for="currentSchedule">Current Schedule</label>
                    <input type="text" id="currentSchedule" name="currentSchedule" 
                           value="<%= request.getParameter("currentSchedule") != null ? 
                                   request.getParameter("currentSchedule") : "" %>" 
                           placeholder="Current schedule information" readonly>
                </div>
                
                <div class="form-group">
                    <label for="newDate">New Date *</label>
                    <input type="date" id="newDate" name="newDate" 
                           value="<%= request.getParameter("newDate") != null ? 
                                   request.getParameter("newDate") : "" %>">
                    <% if (request.getAttribute("dateError") != null) { %>
                        <span class="field-error"><%= request.getAttribute("dateError") %></span>
                    <% } %>
                </div>
                
                <div class="form-group">
                    <label for="timeSlot">Time Slot *</label>
                    <select id="timeSlot" name="timeSlot">
                        <option value="">Select time slot</option>
                        <option value="07:00" <%= "07:00".equals(request.getParameter("timeSlot")) ? "selected" : "" %>>07:00 - 08:30</option>
                        <option value="08:45" <%= "08:45".equals(request.getParameter("timeSlot")) ? "selected" : "" %>>08:45 - 10:15</option>
                        <option value="10:30" <%= "10:30".equals(request.getParameter("timeSlot")) ? "selected" : "" %>>10:30 - 12:00</option>
                        <option value="13:00" <%= "13:00".equals(request.getParameter("timeSlot")) ? "selected" : "" %>>13:00 - 14:30</option>
                        <option value="14:45" <%= "14:45".equals(request.getParameter("timeSlot")) ? "selected" : "" %>>14:45 - 16:15</option>
                        <option value="16:30" <%= "16:30".equals(request.getParameter("timeSlot")) ? "selected" : "" %>>16:30 - 18:00</option>
                    </select>
                    <% if (request.getAttribute("timeSlotError") != null) { %>
                        <span class="field-error"><%= request.getAttribute("timeSlotError") %></span>
                    <% } %>
                </div>
                
                <div class="form-group">
                    <label for="classroom">Classroom *</label>
                    <input type="text" id="classroom" name="classroom" 
                           value="<%= request.getParameter("classroom") != null ? 
                                   request.getParameter("classroom") : "" %>" 
                           placeholder="Enter classroom (e.g., A101)" maxlength="10">
                    <% if (request.getAttribute("classroomError") != null) { %>
                        <span class="field-error"><%= request.getAttribute("classroomError") %></span>
                    <% } %>
                </div>
                
                <div class="form-group">
                    <label for="reason">Reason for Change *</label>
                    <textarea id="reason" name="reason" rows="4" maxlength="500" 
                              placeholder="Please provide a detailed reason for the schedule change"><%= request.getParameter("reason") != null ? 
                                      request.getParameter("reason") : "" %></textarea>
                    <span class="char-count">0/500 characters</span>
                    <% if (request.getAttribute("reasonError") != null) { %>
                        <span class="field-error"><%= request.getAttribute("reasonError") %></span>
                    <% } %>
                </div>
                
                <div class="form-actions">
                    <button type="submit" class="btn btn-primary">Submit Request</button>
                    <button type="reset" class="btn btn-secondary" onclick="resetForm()">Reset</button>
                    <a href="schedule?action=cancel" class="btn btn-danger">Cancel</a>
                </div>
            </form>
        </main>
        
        <footer>
            <p>&copy; 2026 Schedule Management System. All rights reserved.</p>
        </footer>
    </div>
    
    <script>
        // Update current schedule when course selection changes
        document.getElementById('courseId').addEventListener('change', function() {
            var selectedOption = this.options[this.selectedIndex];
            var schedule = selectedOption.getAttribute('data-schedule');
            document.getElementById('currentSchedule').value = schedule || '';
        });
        
        // Character count for reason
        document.getElementById('reason').addEventListener('input', function() {
            var count = this.value.length;
            document.querySelector('.char-count').textContent = count + '/500 characters';
        });
        
        // Reset function
        function resetForm() {
            document.querySelector('form').reset();
            document.getElementById('currentSchedule').value = '';
            document.querySelector('.char-count').textContent = '0/500 characters';
        }
        
        function validateScheduleForm() {
            var courseId = document.getElementById('courseId').value;
            var newDate = document.getElementById('newDate').value;
            var timeSlot = document.getElementById('timeSlot').value;
            var classroom = document.getElementById('classroom').value.trim();
            var reason = document.getElementById('reason').value.trim();
            
            if (courseId === '') {
                alert('Please select a course');
                return false;
            }
            
            if (newDate === '') {
                alert('Please select a new date');
                return false;
            }
            
            // Check if date is in the past
            var today = new Date().toISOString().split('T')[0];
            if (newDate < today) {
                alert('Date cannot be in the past');
                return false;
            }
            
            if (timeSlot === '') {
                alert('Please select a time slot');
                return false;
            }
            
            if (classroom === '') {
                alert('Please enter a classroom');
                return false;
            }
            
            if (reason === '') {
                alert('Please provide a reason for the change');
                return false;
            }
            
            if (reason.length > 500) {
                alert('Reason must not exceed 500 characters');
                return false;
            }
            
            return true;
        }
    </script>
</body>
</html>