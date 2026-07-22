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
    <title>Teacher Profile - Schedule Management System</title>
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
                    <a href="logout" class="logout-btn">Logout</a>
                </div>
            </div>
        </header>
        
        <nav>
            <ul class="nav-menu">
                <li><a href="home">Home</a></li>
                <li><a href="schedule">Change Schedule</a></li>
                <li><a href="profile" class="active">Teacher Profile</a></li>
            </ul>
        </nav>
        
        <main>
            <h1>Teacher Profile</h1>
            
            <% if (request.getAttribute("successMessage") != null) { %>
                <div class="success-message"><%= request.getAttribute("successMessage") %></div>
            <% } %>
            
            <% if (request.getAttribute("error") != null) { %>
                <div class="error-message"><%= request.getAttribute("error") %></div>
            <% } %>
            
            <div class="profile-container">
                <div class="profile-sidebar">
                    <div class="avatar-container">
                        <img src="<%= teacher.getAvatar() != null ? teacher.getAvatar() : "images/default-avatar.png" %>" 
                             alt="Profile Photo" class="profile-avatar" id="avatarPreview">
                    </div>
                    
                    <div class="avatar-upload">
                        <form action="upload-avatar" method="post" enctype="multipart/form-data">
                            <input type="file" name="avatar" id="avatarFile" accept=".jpg,.jpeg,.png" 
                                   onchange="previewAvatar(event)">
                            <button type="submit" class="btn btn-primary">Upload Avatar</button>
                        </form>
                    </div>
                    
                    <div class="profile-info">
                        <p><strong>Teacher ID:</strong> <%= teacher.getTeacherId() %></p>
                        <p><strong>Username:</strong> <%= teacher.getUsername() %></p>
                        <p><strong>Full Name:</strong> <%= teacher.getFullName() %></p>
                        <p><strong>Role:</strong> <%= teacher.getRole() %></p>
                    </div>
                </div>
                
                <div class="profile-main">
                    <form action="profile" method="post">
                        <input type="hidden" name="action" value="update">
                        
                        <div class="form-group">
                            <label for="email">Email *</label>
                            <input type="email" id="email" name="email" 
                                   value="<%= teacher.getEmail() != null ? teacher.getEmail() : "" %>" 
                                   placeholder="Enter email address">
                            <% if (request.getAttribute("emailError") != null) { %>
                                <span class="field-error"><%= request.getAttribute("emailError") %></span>
                            <% } %>
                        </div>
                        
                        <div class="form-group">
                            <label for="phone">Phone Number *</label>
                            <input type="tel" id="phone" name="phone" 
                                   value="<%= teacher.getPhone() != null ? teacher.getPhone() : "" %>" 
                                   placeholder="Enter phone number">
                            <% if (request.getAttribute("phoneError") != null) { %>
                                <span class="field-error"><%= request.getAttribute("phoneError") %></span>
                            <% } %>
                        </div>
                        
                        <div class="form-group">
                            <label for="address">Address</label>
                            <textarea id="address" name="address" rows="3" 
                                      placeholder="Enter address" maxlength="255"><%= teacher.getAddress() != null ? 
                                              teacher.getAddress() : "" %></textarea>
                            <% if (request.getAttribute("addressError") != null) { %>
                                <span class="field-error"><%= request.getAttribute("addressError") %></span>
                            <% } %>
                        </div>
                        
                        <div class="form-actions">
                            <button type="submit" class="btn btn-primary">Save Changes</button>
                            <a href="profile?action=cancel" class="btn btn-secondary">Cancel</a>
                        </div>
                    </form>
                </div>
            </div>
        </main>
        
        <footer>
            <p>&copy; 2026 Schedule Management System. All rights reserved.</p>
        </footer>
    </div>
    
    <script>
        function previewAvatar(event) {
            var reader = new FileReader();
            reader.onload = function() {
                var output = document.getElementById('avatarPreview');
                output.src = reader.result;
            };
            reader.readAsDataURL(event.target.files[0]);
        }
        
        function validateFileSize() {
            var fileInput = document.getElementById('avatarFile');
            var fileSize = fileInput.files[0].size;
            var maxSize = 5 * 1024 * 1024; // 5MB
            
            if (fileSize > maxSize) {
                alert('File size exceeds 5MB limit');
                fileInput.value = '';
                return false;
            }
            return true;
        }
    </script>
</body>
</html>