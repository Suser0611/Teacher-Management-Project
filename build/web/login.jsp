<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Teacher Login - Schedule Management System</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <div class="login-container">
        <div class="login-box">
            <h2>Schedule Management System</h2>
            <h3>Teacher Login</h3>
            
            <% if (request.getAttribute("error") != null) { %>
                <div class="error-message"><%= request.getAttribute("error") %></div>
            <% } %>
            
            <form action="login" method="post" onsubmit="return validateLoginForm()">
                <div class="form-group">
                    <label for="username">Username:</label>
                    <input type="text" id="username" name="username" 
                           value="<%= request.getParameter("username") != null ? request.getParameter("username") : "" %>" 
                           placeholder="Enter username">
                    <% if (request.getAttribute("usernameError") != null) { %>
                        <span class="field-error"><%= request.getAttribute("usernameError") %></span>
                    <% } %>
                </div>
                
                <div class="form-group">
                    <label for="password">Password:</label>
                    <input type="password" id="password" name="password" placeholder="Enter password">
                    <% if (request.getAttribute("passwordError") != null) { %>
                        <span class="field-error"><%= request.getAttribute("passwordError") %></span>
                    <% } %>
                </div>
                
                <button type="submit" class="login-btn">Login</button>
            </form>
            
            <div class="login-info">
                <p>Demo Account: teacher01 / Teacher@123</p>
            </div>
        </div>
    </div>
    
    <script>
        function validateLoginForm() {
            var username = document.getElementById('username').value.trim();
            var password = document.getElementById('password').value;
            
            if (username === '') {
                alert('Username is required');
                return false;
            }
            
            if (password === '') {
                alert('Password is required');
                return false;
            }
            
            return true;
        }
    </script>
</body>
</html>