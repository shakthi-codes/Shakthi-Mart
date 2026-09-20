package controller;

import dao.UserDAOImpl;
import model.User;
import service.UserService;

import org.mindrot.jbcrypt.BCrypt;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");

        response.getWriter().println("""
            <html>
            <head>
                <title>Shakthi Mart - Login</title>
            </head>
            <body>

                <h1>Shakthi Mart Login</h1>

                <form method="post" action="login">

                    <label>Email:</label>
                    <input type="email" name="email" required>
                    <br><br>

                    <label>Password:</label>
                    <input type="password" name="password" required>
                    <br><br>

                    <button type="submit">Login</button>

                </form>

                <p>
                    New user?
                    <a href="register">Register here</a>
                </p>

            </body>
            </html>
            """);
    }

    @Override
    protected void doPost(HttpServletRequest request,
                           HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        if (email == null || password == null
                || email.isBlank() || password.isBlank()) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Email and password are required."
            );
            return;
        }

        UserDAOImpl userDAO = new UserDAOImpl(getServletContext());
        UserService userService = new UserService(userDAO);

        User user = userService.login(email);

        // Check whether user exists and password is correct
        if (user != null && BCrypt.checkpw(password, user.getPassword())) {

            HttpSession session = request.getSession(true);

            // Protect against session fixation
            request.changeSessionId();

            // 30 minutes session timeout
            session.setMaxInactiveInterval(30 * 60);

            session.setAttribute("user", user.getEmail());
            session.setAttribute("role", user.getRole());

            response.sendRedirect(
                    request.getContextPath() + "/home"
            );

        } else {

            response.setContentType("text/html");

            response.getWriter().println("""
                <h2>Invalid email or password</h2>
                <p>Please try again.</p>
                <a href="login">Back to Login</a>
                """);
        }
    }
}