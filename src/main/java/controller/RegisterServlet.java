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
import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");

        response.getWriter().println("""
            <html>
            <head>
                <title>Shakthi Mart - Register</title>
            </head>
            <body>

                <h1>Shakthi Mart Registration</h1>

                <form method="post" action="register">

                    <label>Name:</label>
                    <input type="text" name="name" required>
                    <br><br>

                    <label>Email:</label>
                    <input type="email" name="email" required>
                    <br><br>

                    <label>Password:</label>
                    <input type="password" name="password" required>
                    <br><br>

                    <label>Role:</label>
                    <select name="role">
                        <option value="BUYER">Buyer</option>
                        <option value="SELLER">Seller</option>
                    </select>
                    <br><br>

                    <button type="submit">Register</button>

                </form>

                <p>
                    Already registered?
                    <a href="login">Login here</a>
                </p>

            </body>
            </html>
            """);
    }

    @Override
    protected void doPost(HttpServletRequest request,
                           HttpServletResponse response)
            throws ServletException, IOException {

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String role = request.getParameter("role");

        if (name == null || email == null || password == null
                || name.isBlank() || email.isBlank() || password.isBlank()) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "All required fields must be filled."
            );
            return;
        }

        // Hash the password before storing it in the database
        String hashedPassword = BCrypt.hashpw(
                password,
                BCrypt.gensalt()
        );

        User user = new User(
                0,
                name,
                email,
                hashedPassword,
                role
        );

        UserDAOImpl userDAO = new UserDAOImpl(getServletContext());
        UserService userService = new UserService(userDAO);

        userService.register(user);

        response.sendRedirect(
                request.getContextPath() + "/login"
        );
    }
}
