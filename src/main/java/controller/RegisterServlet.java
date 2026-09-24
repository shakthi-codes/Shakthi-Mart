package controller;

import dao.UserDAO;
import dao.UserDAOImpl;
import model.User;
import org.mindrot.jbcrypt.BCrypt;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private UserDAO userDAO;

    @Override
    public void init() {
        userDAO = new UserDAOImpl(getServletContext());
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out = response.getWriter();

        String contextPath = request.getContextPath();

        String html = """
            <!DOCTYPE html>
            <html lang="en">

            <head>

                <meta charset="UTF-8">

                <meta name="viewport"
                      content="width=device-width, initial-scale=1.0">

                <title>Shakthi Mart - Register</title>

                <style>

                    * {
                        box-sizing: border-box;
                        margin: 0;
                        padding: 0;
                    }

                    body {
                        font-family: Arial, sans-serif;
                        min-height: 100vh;
                        display: flex;
                        justify-content: center;
                        align-items: center;

                        background: linear-gradient(
                            135deg,
                            #4a148c,
                            #6a1b9a,
                            #8e24aa
                        );

                        padding: 20px;
                    }

                    .register-container {
                        width: 100%;
                        max-width: 450px;
                    }

                    .brand {
                        text-align: center;
                        color: white;
                        margin-bottom: 20px;
                    }

                    .brand h1 {
                        font-size: 32px;
                        margin-bottom: 8px;
                    }

                    .brand p {
                        font-size: 14px;
                        opacity: 0.9;
                    }

                    .register-card {
                        background: white;
                        border-radius: 20px;
                        padding: 35px;

                        box-shadow:
                            0 15px 40px
                            rgba(0, 0, 0, 0.25);
                    }

                    .register-card h2 {
                        text-align: center;
                        color: #4a148c;
                        margin-bottom: 8px;
                        font-size: 27px;
                    }

                    .subtitle {
                        text-align: center;
                        color: #777;
                        margin-bottom: 25px;
                        font-size: 14px;
                    }

                    .form-group {
                        margin-bottom: 18px;
                    }

                    .form-group label {
                        display: block;
                        margin-bottom: 7px;
                        color: #333;
                        font-weight: bold;
                        font-size: 14px;
                    }

                    .form-group input,
                    .form-group select {
                        width: 100%;
                        padding: 13px 14px;

                        border: 1px solid #ddd;
                        border-radius: 10px;

                        font-size: 15px;
                        outline: none;

                        transition: 0.3s;

                        background: #fafafa;
                    }

                    .form-group input:focus,
                    .form-group select:focus {

                        border-color: #7b1fa2;

                        box-shadow:
                            0 0 0 3px
                            rgba(123, 31, 162, 0.12);

                        background: white;
                    }

                    .register-button {
                        width: 100%;
                        padding: 14px;

                        border: none;
                        border-radius: 10px;

                        background: linear-gradient(
                            135deg,
                            #6a1b9a,
                            #8e24aa
                        );

                        color: white;

                        font-size: 16px;
                        font-weight: bold;

                        cursor: pointer;

                        margin-top: 8px;

                        transition: 0.3s;
                    }

                    .register-button:hover {

                        transform: translateY(-2px);

                        box-shadow:
                            0 6px 15px
                            rgba(106, 27, 154, 0.3);
                    }

                    .login-link {
                        text-align: center;
                        margin-top: 22px;
                        color: #666;
                        font-size: 14px;
                    }

                    .login-link a {
                        color: #6a1b9a;
                        font-weight: bold;
                        text-decoration: none;
                    }

                    .login-link a:hover {
                        text-decoration: underline;
                    }

                    .back-home {
                        display: block;
                        text-align: center;
                        margin-top: 15px;

                        color: white;
                        text-decoration: none;

                        font-size: 14px;
                    }

                    .back-home:hover {
                        text-decoration: underline;
                    }

                    @media (max-width: 500px) {

                        .register-card {
                            padding: 25px 20px;
                        }

                        .brand h1 {
                            font-size: 27px;
                        }
                    }

                </style>

            </head>

            <body>

                <div class="register-container">

                    <div class="brand">

                        <h1>🛠️ Shakthi Mart</h1>

                        <p>
                            Learn • Share • Earn
                        </p>

                    </div>

                    <div class="register-card">

                        <h2>
                            Create Account
                        </h2>

                        <p class="subtitle">
                            Join Shakthi Mart and explore amazing services
                        </p>

                        <form
                            method="post"
                            action="%s/register">

                            <div class="form-group">

                                <label for="name">
                                    👤 Full Name
                                </label>

                                <input
                                    type="text"
                                    id="name"
                                    name="name"
                                    placeholder="Enter your name"
                                    required>

                            </div>

                            <div class="form-group">

                                <label for="email">
                                    📧 Email Address
                                </label>

                                <input
                                    type="email"
                                    id="email"
                                    name="email"
                                    placeholder="Enter your email"
                                    required>

                            </div>

                            <div class="form-group">

                                <label for="password">
                                    🔐 Password
                                </label>

                                <input
                                    type="password"
                                    id="password"
                                    name="password"
                                    placeholder="Create a password"
                                    required>

                            </div>

                            <div class="form-group">

                                <label for="role">
                                    🎯 Account Type
                                </label>

                                <select
                                    id="role"
                                    name="role"
                                    required>

                                    <option value="">
                                        Select your role
                                    </option>

                                    <option value="BUYER">
                                        Buyer
                                    </option>

                                    <option value="SELLER">
                                        Seller
                                    </option>

                                </select>

                            </div>

                            <button
                                type="submit"
                                class="register-button">

                                Create Account ✨

                            </button>

                        </form>

                        <div class="login-link">

                            Already have an account?

                            <a href="%s/login">
                                Login here
                            </a>

                        </div>

                    </div>

                    <a
                        class="back-home"
                        href="%s/services">

                        ← Back to Shakthi Mart

                    </a>

                </div>

            </body>

            </html>
            """.replace("%s", contextPath);

        out.println(html);
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String name =
                request.getParameter("name");

        String email =
                request.getParameter("email");

        String password =
                request.getParameter("password");

        String role =
                request.getParameter("role");

        // Normalize input

        if (name != null) {
            name = name.trim();
        }

        if (email != null) {
            email = email.trim().toLowerCase();
        }

        if (role != null) {
            role = role.trim().toUpperCase();
        }

        // Validate input

        if (name == null ||
                email == null ||
                password == null ||
                role == null ||
                name.isBlank() ||
                email.isBlank() ||
                password.isBlank() ||
                role.isBlank()) {

            response.sendRedirect(
                    request.getContextPath() +
                    "/register"
            );

            return;
        }

        // Validate role

        if (!role.equals("BUYER") &&
                !role.equals("SELLER")) {

            response.sendRedirect(
                    request.getContextPath() +
                    "/register"
            );

            return;
        }

        // Hash password using BCrypt

        String hashedPassword =
                BCrypt.hashpw(
                        password,
                        BCrypt.gensalt()
                );

        // Create User object

        User user = new User();

        user.setName(name);
        user.setEmail(email);
        user.setPassword(hashedPassword);
        user.setRole(role);

        // Save user

        try {

            userDAO.save(user);

            System.out.println(
                    "REGISTER DEBUG - Saved email: " +
                    user.getEmail()
            );

            User checkUser =
                    userDAO.findByEmail(
                            user.getEmail()
                    );

            System.out.println(
                    "REGISTER DEBUG - User found after save: " +
                    (checkUser != null)
            );

            // Registration successful

            response.sendRedirect(
                    request.getContextPath() +
                    "/login?registered=true"
            );

        } catch (Exception e) {

            e.printStackTrace();

            response.setContentType(
                    "text/html;charset=UTF-8"
            );

            PrintWriter out =
                    response.getWriter();

            String contextPath =
                    request.getContextPath();

            String errorPage = """
                <!DOCTYPE html>

                <html>

                <head>

                    <title>
                        Registration Error
                    </title>

                    <style>

                        body {
                            font-family: Arial, sans-serif;
                            background: #f5f3ff;
                            text-align: center;
                            padding: 80px 20px;
                        }

                        .box {
                            max-width: 500px;
                            margin: auto;
                            background: white;
                            padding: 30px;
                            border-radius: 15px;

                            box-shadow:
                                0 5px 20px
                                rgba(0,0,0,0.1);
                        }

                        h2 {
                            color: #6a1b9a;
                        }

                        a {
                            display: inline-block;
                            margin-top: 20px;
                            color: white;
                            background: #6a1b9a;
                            padding: 12px 20px;
                            border-radius: 8px;
                            text-decoration: none;
                        }

                    </style>

                </head>

                <body>

                    <div class="box">

                        <h2>
                            Registration Failed
                        </h2>

                        <p>
                            This email may already be registered.
                            Please try another email.
                        </p>

                        <a href="%s/register">
                            Try Again
                        </a>

                    </div>

                </body>

                </html>
                """.replace("%s", contextPath);

            out.println(errorPage);
        }
    }
}
