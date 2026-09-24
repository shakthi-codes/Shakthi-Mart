package controller;

import dao.UserDAO;
import dao.UserDAOImpl;
import model.User;
import service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        response.getWriter().println("""
            <!DOCTYPE html>
            <html>
            <head>
                <title>Login - Shakthi Mart</title>

                <style>

                    body {
                        font-family: Arial, sans-serif;
                        background: #f5f0ff;
                        display: flex;
                        justify-content: center;
                        align-items: center;
                        height: 100vh;
                        margin: 0;
                    }

                    .login-box {
                        background: white;
                        padding: 30px;
                        width: 350px;
                        border-radius: 15px;
                        box-shadow: 0 5px 20px rgba(0,0,0,0.15);
                    }

                    h1 {
                        text-align: center;
                        color: #6a1b9a;
                    }

                    input,
                    button {
                        width: 100%;
                        padding: 12px;
                        margin-top: 10px;
                        box-sizing: border-box;
                        border-radius: 8px;
                    }

                    input {
                        border: 1px solid #ccc;
                    }

                    button {
                        border: none;
                        background: #6a1b9a;
                        color: white;
                        font-size: 16px;
                        cursor: pointer;
                    }

                    button:hover {
                        background: #4a148c;
                    }

                    .register {
                        text-align: center;
                        margin-top: 15px;
                    }

                    a {
                        color: #6a1b9a;
                        text-decoration: none;
                        font-weight: bold;
                    }

                </style>
            </head>

            <body>

                <div class="login-box">

                    <h1>Shakthi Mart</h1>

                    <form method="post" action="login">

                        <input
                            type="email"
                            name="email"
                            placeholder="Enter Email"
                            required
                        >

                        <input
                            type="password"
                            name="password"
                            placeholder="Enter Password"
                            required
                        >

                        <button type="submit">
                            Login
                        </button>

                    </form>

                    <div class="register">
                        New user?
                        <a href="register">Register here</a>
                    </div>

                </div>

            </body>
            </html>
            """);
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        /*
         * Validate input
         */
        if (email == null ||
                password == null ||
                email.trim().isEmpty() ||
                password.trim().isEmpty()) {

            response.sendRedirect(
                    request.getContextPath() + "/login"
            );

            return;
        }

        /*
         * Normalize email
         */
        email = email.trim().toLowerCase();

        /*
         * Create DAO and service
         */
        UserDAO userDAO =
                new UserDAOImpl(getServletContext());

        UserService userService =
                new UserService(userDAO);

        /*
         * Find user by email
         */
        User user = userService.login(email);

        /*
         * DEBUG INFORMATION
         *
         * This will appear in the Tomcat terminal.
         */
        System.out.println(
                "LOGIN DEBUG - Email: " + email
        );

        System.out.println(
                "LOGIN DEBUG - User found: " +
                (user != null)
        );

        /*
         * Check password
         */
        boolean passwordCorrect = false;

        if (user != null &&
                user.getPassword() != null) {

            try {

                passwordCorrect =
                        BCrypt.checkpw(
                                password,
                                user.getPassword()
                        );

                System.out.println(
                        "LOGIN DEBUG - Password correct: " +
                        passwordCorrect
                );

            } catch (Exception e) {

                System.out.println(
                        "LOGIN DEBUG - BCrypt error: " +
                        e.getMessage()
                );

                passwordCorrect = false;
            }
        } else {

            System.out.println(
                    "LOGIN DEBUG - No user or password hash found."
            );
        }

        /*
         * Successful login
         */
        if (passwordCorrect) {

            HttpSession session =
                    request.getSession(true);

            /*
             * Regenerate session ID
             */
            request.changeSessionId();

            /*
             * 30 minutes session timeout
             */
            session.setMaxInactiveInterval(
                    30 * 60
            );

            /*
             * Store user information
             */
            session.setAttribute(
                    "user",
                    user.getEmail()
            );

            session.setAttribute(
                    "role",
                    user.getRole()
            );

            session.setAttribute(
                    "userId",
                    user.getId()
            );

            /*
             * Open Service Marketplace
             */
            response.sendRedirect(
                    request.getContextPath() +
                    "/services"
            );

        } else {

            /*
             * Login failed
             */
            response.setContentType(
                    "text/html;charset=UTF-8"
            );

            response.getWriter().println("""
                <!DOCTYPE html>

                <html>

                <head>

                    <title>
                        Login Failed - Shakthi Mart
                    </title>

                    <style>

                        body {
                            font-family: Arial, sans-serif;
                            text-align: center;
                            padding-top: 100px;
                            background: #f5f0ff;
                        }

                        .box {
                            background: white;
                            display: inline-block;
                            padding: 30px;
                            border-radius: 15px;
                            box-shadow:
                                0 5px 20px
                                rgba(0,0,0,0.15);
                        }

                        h2 {
                            color: #c62828;
                        }

                        a {
                            color: #6a1b9a;
                            font-weight: bold;
                            text-decoration: none;
                        }

                    </style>

                </head>

                <body>

                    <div class="box">

                        <h2>
                            Invalid Email or Password
                        </h2>

                        <p>
                            Please try again.
                        </p>

                        <a href="login">
                            Back to Login
                        </a>

                    </div>

                </body>

                </html>
                """);
        }
    }
}
