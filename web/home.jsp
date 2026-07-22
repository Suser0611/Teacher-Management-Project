<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.sms.model.Teacher" %>
<%
    Teacher teacher = (Teacher) session.getAttribute("teacher");
    if (teacher == null) {
        response.sendRedirect("login");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <title>Home - Schedule Management System</title>
    <link rel="stylesheet" href="css/style.css">
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
                    <span>ID: <%= teacher.getTeacherId() %></span>
                    <span>Role: <%= teacher.getRole() %></span>
                    <a href="logout" class="logout-btn">Logout</a>
                </div>
            </div>
        </header>
        
        <nav>
            <ul class="nav-menu">
                <li><a href="home" class="active">Home</a></li>
                <li><a href="schedule">Change Schedule</a></li>
                <li><a href="profile">Teacher Profile</a></li>
            </ul>
        </nav>
        
        <main>
            <h1>Welcome to Schedule Management System</h1>
            <p>Hello <%= teacher.getFullName() %>, you are successfully logged in.</p>
            
            <div class="dashboard-cards">
                <div class="card">
                    <h3>Quick Actions</h3>
                    <ul>
                        <li><a href="schedule">Request Schedule Change</a></li>
                        <li><a href="profile">Update Profile</a></li>
                        <li><a href="logout">Logout</a></li>
                    </ul>
                </div>
                <div class="card">
                    <h3>Account Information</h3>
                    <p><strong>Username:</strong> <%= teacher.getUsername() %></p>
                    <p><strong>Email:</strong> <%= teacher.getEmail() %></p>
                    <p><strong>Phone:</strong> <%= teacher.getPhone() %></p>
                </div>
            </div>
        </main>
        
        <footer>
            <p>&copy; 2026 Schedule Management System. All rights reserved.</p>
        </footer>
    </div>
    
    <script>
        // Page load performance measurement
        window.onload = function() {
            console.log("Home page loaded successfully");
        };
    </script>
</body>
</html>