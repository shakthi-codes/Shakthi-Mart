package controller;

import dao.CartDAO;
import dao.CartDAOImpl;
import dao.ServiceDAO;
import dao.ServiceDAOImpl;
import dao.UserDAO;
import dao.UserDAOImpl;
import dao.ReviewDAO;
import dao.ReviewDAOImpl;

import model.Service;
import model.User;
import model.Review;

import service.ServiceManager;

import util.DBUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.ArrayList;
import java.util.List;

@WebServlet("/services")
public class ServiceServlet extends HttpServlet {

    private ServiceManager serviceManager;
    private CartDAO cartDAO;
    private ReviewDAO reviewDAO;

    @Override
    public void init() {

        ServiceDAO serviceDAO =
                new ServiceDAOImpl(getServletContext());

        serviceManager =
                new ServiceManager(serviceDAO);

        cartDAO =
                new CartDAOImpl(getServletContext());

        reviewDAO =
                new ReviewDAOImpl(getServletContext());
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session =
                request.getSession(false);

        if (session == null ||
                session.getAttribute("user") == null) {

            response.sendRedirect(
                    request.getContextPath() + "/login");

            return;
        }

        UserDAO userDAO =
                new UserDAOImpl(getServletContext());

        List<Service> allServices =
                serviceManager.getAllServices();

        /*
         * SEARCH + CATEGORY FILTER
         */

        String search =
                request.getParameter("search");

        String category =
                request.getParameter("category");

        if (search == null) {
            search = "";
        }

        if (category == null) {
            category = "All";
        }

        search = search.trim();

        List<Service> services =
                new ArrayList<>();

        for (Service service : allServices) {

            boolean searchMatch = true;
            boolean categoryMatch = true;

            if (!search.isEmpty()) {

                String name =
                        service.getName() == null
                                ? ""
                                : service.getName();

                String description =
                        service.getDescription() == null
                                ? ""
                                : service.getDescription();

                String serviceCategory =
                        service.getCategory() == null
                                ? ""
                                : service.getCategory();

                String keyword =
                        search.toLowerCase();

                searchMatch =
                        name.toLowerCase()
                                .contains(keyword)
                        ||
                        description.toLowerCase()
                                .contains(keyword)
                        ||
                        serviceCategory.toLowerCase()
                                .contains(keyword);
            }

            if (!"All".equalsIgnoreCase(category)) {

                String serviceCategory =
                        service.getCategory() == null
                                ? ""
                                : service.getCategory();

                categoryMatch =
                        serviceCategory
                                .equalsIgnoreCase(category);
            }

            if (searchMatch && categoryMatch) {
                services.add(service);
            }
        }

        String message =
                request.getParameter("message");

        String reviewMessage =
                request.getParameter("review");

        String reviewError =
                request.getParameter("error");

        response.setContentType(
                "text/html;charset=UTF-8");

        PrintWriter out =
                response.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html lang='en'>");

        out.println("<head>");

        out.println("<meta charset='UTF-8'>");

        out.println(
                "<meta name='viewport' " +
                "content='width=device-width, initial-scale=1.0'>");

        out.println(
                "<title>Shakthi Mart - Services</title>");

        out.println("<style>");

        out.println("""
            * {
                box-sizing: border-box;
            }

            body {
                font-family: Arial, sans-serif;
                margin: 0;
                background: #f5f3ff;
                color: #222;
            }

            .header {
                background: linear-gradient(
                    135deg,
                    #6a1b9a,
                    #8e24aa
                );
                color: white;
                padding: 25px;
                text-align: center;
            }

            .header h1 {
                margin: 0;
                font-size: 34px;
            }

            .header p {
                margin: 8px 0;
            }

            .nav {
                margin-top: 15px;
            }

            .nav a {
                color: white;
                text-decoration: none;
                font-weight: bold;
                margin: 0 10px;
            }

            .container {
                width: 92%;
                max-width: 1200px;
                margin: 25px auto;
            }

            .message {
                background: #e8f5e9;
                color: #2e7d32;
                padding: 14px;
                border-radius: 8px;
                margin-bottom: 20px;
                font-weight: bold;
            }

            .review-success {
                background: #e8f5e9;
                color: #2e7d32;
                padding: 14px;
                border-radius: 8px;
                margin-bottom: 20px;
                font-weight: bold;
            }

            .review-error {
                background: #ffebee;
                color: #c62828;
                padding: 14px;
                border-radius: 8px;
                margin-bottom: 20px;
                font-weight: bold;
            }

            .search-box {
                background: white;
                padding: 22px;
                border-radius: 12px;
                box-shadow:
                    0 3px 10px rgba(0,0,0,0.1);
                margin-bottom: 25px;
            }

            .search-box h2 {
                color: #6a1b9a;
                margin-top: 0;
            }

            .search-row {
                display: grid;
                grid-template-columns:
                    2fr 1fr auto;
                gap: 12px;
                align-items: center;
            }

            .search-row input,
            .search-row select {
                width: 100%;
                padding: 12px;
                border: 1px solid #ccc;
                border-radius: 7px;
                font-size: 15px;
            }

            .search-button {
                background: #6a1b9a;
                color: white;
                border: none;
                padding: 12px 22px;
                border-radius: 7px;
                cursor: pointer;
                font-weight: bold;
            }

            .search-button:hover {
                background: #4a148c;
            }

            .clear-button {
                display: inline-block;
                margin-left: 8px;
                padding: 11px 18px;
                border-radius: 7px;
                background: #eeeeee;
                color: #333;
                text-decoration: none;
                font-weight: bold;
            }

            .categories {
                display: flex;
                flex-wrap: wrap;
                gap: 10px;
                margin-top: 18px;
            }

            .category-chip {
                text-decoration: none;
                padding: 9px 15px;
                border-radius: 20px;
                background: #ede7f6;
                color: #6a1b9a;
                font-weight: bold;
                border: 1px solid #d1c4e9;
            }

            .category-chip:hover {
                background: #d1c4e9;
            }

            .offer-box {
                background: white;
                padding: 25px;
                border-radius: 12px;
                box-shadow:
                    0 3px 10px rgba(0,0,0,0.1);
                margin-bottom: 30px;
            }

            .offer-box h2 {
                color: #6a1b9a;
                margin-top: 0;
            }

            input,
            textarea,
            select {
                width: 100%;
                padding: 10px;
                margin: 7px 0 15px;
                border: 1px solid #ccc;
                border-radius: 6px;
            }

            button {
                background: #6a1b9a;
                color: white;
                border: none;
                padding: 10px 16px;
                border-radius: 6px;
                cursor: pointer;
                margin: 4px;
            }

            button:hover {
                background: #4a148c;
            }

            .services-title {
                display: flex;
                justify-content: space-between;
                align-items: center;
                margin-bottom: 15px;
            }

            .services-title h2 {
                color: #4a148c;
            }

            .result-count {
                color: #666;
                font-weight: bold;
            }

            .services-grid {
                display: grid;
                grid-template-columns:
                    repeat(auto-fit, minmax(300px, 1fr));
                gap: 20px;
            }

            .card {
                background: white;
                padding: 20px;
                border-radius: 14px;
                box-shadow:
                    0 3px 10px rgba(0,0,0,0.1);
                transition:
                    transform 0.2s,
                    box-shadow 0.2s;
            }

            .card:hover {
                transform: translateY(-4px);
                box-shadow:
                    0 7px 18px rgba(0,0,0,0.15);
            }

            .card h3 {
                color: #6a1b9a;
                margin-top: 0;
                font-size: 21px;
            }

            .price {
                font-size: 23px;
                font-weight: bold;
                color: #2e7d32;
            }

            .category {
                display: inline-block;
                background: #ede7f6;
                color: #6a1b9a;
                padding: 6px 11px;
                border-radius: 15px;
                font-size: 13px;
                font-weight: bold;
            }

            .creator {
                font-weight: bold;
                color: #555;
            }

            .description {
                min-height: 55px;
                color: #555;
                line-height: 1.5;
            }

            .score {
                background: #fff8e1;
                padding: 8px;
                border-radius: 7px;
                display: inline-block;
                font-weight: bold;
            }

            .rating-box {
                background: #fff8e1;
                padding: 12px;
                border-radius: 8px;
                margin-top: 12px;
            }

            .rating-stars {
                color: #f9a825;
                font-size: 22px;
                letter-spacing: 2px;
            }

            .review-section {
                margin-top: 18px;
                padding-top: 15px;
                border-top: 1px solid #ddd;
            }

            .review-section h4 {
                color: #6a1b9a;
                margin-bottom: 8px;
            }

            .review-form {
                background: #faf7ff;
                padding: 12px;
                border-radius: 8px;
                margin-top: 10px;
            }

            .review-form select,
            .review-form textarea {
                margin-bottom: 8px;
            }

            .review-item {
                background: #f5f3ff;
                padding: 10px;
                border-radius: 8px;
                margin-top: 8px;
            }

            .review-item .stars {
                color: #f9a825;
                font-size: 18px;
            }

            .review-item .comment {
                color: #555;
                margin-top: 5px;
                line-height: 1.4;
            }

            .actions {
                margin-top: 15px;
            }

            .empty {
                background: white;
                padding: 40px;
                text-align: center;
                border-radius: 12px;
                box-shadow:
                    0 3px 10px rgba(0,0,0,0.1);
            }

            .empty h3 {
                color: #6a1b9a;
            }

            @media (max-width: 700px) {

                .search-row {
                    grid-template-columns: 1fr;
                }

                .services-title {
                    flex-direction: column;
                    align-items: flex-start;
                }

                .card form {
                    display: block !important;
                    margin-bottom: 5px;
                }

                .card button {
                    width: 100%;
                }
            }
        """);

        out.println("</style>");
        out.println("</head>");

        out.println("<body>");

        /*
         * HEADER
         */

        out.println("<div class='header'>");

        out.println(
                "<h1>Shakthi Mart 🛠️</h1>");

        out.println(
                "<p>Service Marketplace • " +
                "Connect Skills with Real Needs</p>");

        out.println("<div class='nav'>");

        out.println(
                "<a href='" +
                request.getContextPath() +
                "/services'>🏠 Services</a>");

        out.println(
                "<a href='" +
                request.getContextPath() +
                "/cart'>🛒 Cart</a>");

        out.println(
                "<a href='" +
                request.getContextPath() +
                "/logout'>Logout</a>");

        out.println("</div>");

        out.println("</div>");

        out.println("<div class='container'>");

        /*
         * MESSAGE
         */

        if (message != null) {

            out.println("<div class='message'>");

            if ("added".equals(message)) {

                out.println(
                        "Service added successfully! 🎉");

            } else if ("requested".equals(message)) {

                out.println(
                        "Service requested successfully! 📦");

            } else if ("wishlist-added".equals(message)) {

                out.println(
                        "Service added to wishlist! 💜");

            } else if ("wishlist-removed".equals(message)) {

                out.println(
                        "Service removed from wishlist.");

            } else if ("cart-added".equals(message)) {

                out.println(
                        "Service added to cart successfully! 🛒");

            } else if ("error".equals(message)) {

                out.println(
                        "Something went wrong.");
            }

            out.println("</div>");
        }

        /*
         * REVIEW MESSAGE
         */

        if ("success".equals(reviewMessage)) {

            out.println(
                    "<div class='review-success'>" +
                    "⭐ Thank you! Your rating and feedback " +
                    "were added successfully!" +
                    "</div>");
        }

        if ("invalid-rating".equals(reviewError)) {

            out.println(
                    "<div class='review-error'>" +
                    "Please select a valid rating from 1 to 5." +
                    "</div>");
        }

        if ("review-failed".equals(reviewError)) {

            out.println(
                    "<div class='review-error'>" +
                    "Unable to save your feedback. " +
                    "Please try again." +
                    "</div>");
        }

        /*
         * SEARCH + FILTER
         */

        out.println("<div class='search-box'>");

        out.println(
                "<h2>🔍 Find the Perfect Service</h2>");

        out.println(
                "<form method='get' " +
                "action='" +
                request.getContextPath() +
                "/services'>");

        out.println("<div class='search-row'>");

        out.println(
                "<input type='text' " +
                "name='search' " +
                "placeholder='Search services, skills or categories...' " +
                "value='" +
                escapeHtml(search) +
                "'>");

        out.println(
                "<select name='category'>");

        out.println(
                "<option value='All'>📂 All Categories</option>");

        String[] categories = {
            "Technology",
            "Design",
            "Education",
            "Writing",
            "Digital Marketing",
            "Media",
            "Business",
            "Data Analysis"
        };

        for (String cat : categories) {

            if (cat.equalsIgnoreCase(category)) {

                out.println(
                        "<option value='" +
                        escapeHtml(cat) +
                        "' selected>" +
                        escapeHtml(cat) +
                        "</option>");

            } else {

                out.println(
                        "<option value='" +
                        escapeHtml(cat) +
                        "'>" +
                        escapeHtml(cat) +
                        "</option>");
            }
        }

        out.println("</select>");

        out.println(
                "<button class='search-button' " +
                "type='submit'>" +
                "Search 🔎</button>");

        out.println("</div>");

        out.println("</form>");

        /*
         * CATEGORY CHIPS
         */

        out.println(
                "<div class='categories'>");

        out.println(
                "<a class='category-chip' " +
                "href='" +
                request.getContextPath() +
                "/services'>✨ All</a>");

        for (String cat : categories) {

            out.println(
                    "<a class='category-chip' " +
                    "href='" +
                    request.getContextPath() +
                    "/services?category=" +
                    escapeHtml(cat) +
                    "'>" +
                    getCategoryEmoji(cat) +
                    " " +
                    escapeHtml(cat) +
                    "</a>");
        }

        out.println("</div>");

        out.println("</div>");

        /*
         * OFFER SERVICE
         */

        out.println("<div class='offer-box'>");

        out.println(
                "<h2>➕ Offer Your Service</h2>");

        out.println(
                "<p>Have a skill? Share it with " +
                "other students on Shakthi Mart.</p>");

        out.println(
                "<form method='post' action='" +
                request.getContextPath() +
                "/services'>");

        out.println(
                "<input type='hidden' " +
                "name='action' value='add'>");

        out.println("<label>Service Name</label>");

        out.println(
                "<input type='text' " +
                "name='name' " +
                "placeholder='Example: Python Programming' " +
                "required>");

        out.println("<label>Description</label>");

        out.println(
                "<textarea name='description' " +
                "rows='4' " +
                "placeholder='Describe your service...' " +
                "required></textarea>");

        out.println("<label>Price (₹)</label>");

        out.println(
                "<input type='number' " +
                "name='price' " +
                "step='0.01' " +
                "min='0' " +
                "placeholder='500' " +
                "required>");

        out.println("<label>Category</label>");

        out.println(
                "<select name='category' required>");

        out.println(
                "<option value=''>" +
                "Select Category</option>");

        for (String cat : categories) {

            out.println(
                    "<option value='" +
                    escapeHtml(cat) +
                    "'>" +
                    getCategoryEmoji(cat) +
                    " " +
                    escapeHtml(cat) +
                    "</option>");
        }

        out.println("</select>");

        out.println(
                "<button type='submit'>" +
                "Offer Service 🚀</button>");

        out.println("</form>");

        out.println("</div>");

        /*
         * AVAILABLE SERVICES TITLE
         */

        out.println("<div class='services-title'>");

        out.println(
                "<h2>🛍️ Available Services</h2>");

        out.println(
                "<span class='result-count'>" +
                services.size() +
                " service(s) found</span>");

        out.println("</div>");

        /*
         * SERVICE CARDS
         */

        if (services.isEmpty()) {

            out.println("<div class='empty'>");

            out.println(
                    "<h3>😕 No services found</h3>");

            out.println(
                    "<p>Try another search or category.</p>");

            out.println(
                    "<a class='clear-button' " +
                    "href='" +
                    request.getContextPath() +
                    "/services'>" +
                    "View All Services</a>");

            out.println("</div>");

        } else {

            out.println(
                    "<div class='services-grid'>");

            Integer userId =
                    (Integer) session.getAttribute(
                            "userId");

            for (Service service : services) {

                User creator =
                        userDAO.findById(
                                service.getCreatorId());

                String creatorName =
                        "Unknown";

                if (creator != null) {

                    creatorName =
                            creator.getName();
                }

                int wishlistCount =
                        getWishlistCount(
                                service.getId());

                boolean wishlisted = false;

                if (userId != null) {

                    wishlisted =
                            isWishlisted(
                                    userId,
                                    service.getId());
                }

                /*
                 * REVIEW DATA
                 */

                double averageRating =
                        reviewDAO.getAverageRating(
                                service.getId());

                List<Review> reviews =
                        reviewDAO.findByServiceId(
                                service.getId());

                out.println(
                        "<div class='card'>");

                /*
                 * SERVICE NAME
                 */

                out.println(
                        "<h3>" +
                        getCategoryEmoji(
                                service.getCategory()) +
                        " " +
                        escapeHtml(
                                service.getName()) +
                        "</h3>");

                /*
                 * CREATOR
                 */

                out.println(
                        "<p class='creator'>" +
                        "👤 Created by: " +
                        escapeHtml(
                                creatorName) +
                        "</p>");

                /*
                 * CATEGORY
                 */

                out.println(
                        "<span class='category'>" +
                        escapeHtml(
                                service.getCategory()) +
                        "</span>");

                /*
                 * DESCRIPTION
                 */

                out.println(
                        "<p class='description'>" +
                        escapeHtml(
                                service.getDescription()) +
                        "</p>");

                /*
                 * PRICE
                 */

                out.println(
                        "<p class='price'>₹" +
                        String.format(
                                "%.2f",
                                service.getPrice()) +
                        "</p>");

                /*
                 * RATING
                 */

                out.println(
                        "<div class='rating-box'>");

                out.println(
                        "<strong>⭐ Rating</strong>");

                if (averageRating > 0) {

                    out.println(
                            "<div class='rating-stars'>" +
                            getStars(averageRating) +
                            "</div>");

                    out.println(
                            "<strong>" +
                            String.format(
                                    "%.1f",
                                    averageRating) +
                            " / 5</strong>");

                    out.println(
                            " (" +
                            reviews.size() +
                            " review(s))");

                } else {

                    out.println(
                            "<p>No ratings yet. " +
                            "Be the first to review! ⭐</p>");
                }

                out.println("</div>");

                /*
                 * WISHLIST SCORE
                 */

                out.println(
                        "<p class='score'>" +
                        "❤️ Wishlist Score: " +
                        wishlistCount +
                        "</p>");

                out.println(
                        "<div class='actions'>");

                /*
                 * WISHLIST
                 */

                out.println(
                        "<form method='post' " +
                        "action='" +
                        request.getContextPath() +
                        "/services' " +
                        "style='display:inline;'>");

                out.println(
                        "<input type='hidden' " +
                        "name='serviceId' value='" +
                        service.getId() +
                        "'>");

                if (wishlisted) {

                    out.println(
                            "<input type='hidden' " +
                            "name='action' " +
                            "value='removeWishlist'>");

                    out.println(
                            "<button type='submit'>" +
                            "💜 Remove Wishlist" +
                            "</button>");

                } else {

                    out.println(
                            "<input type='hidden' " +
                            "name='action' " +
                            "value='addWishlist'>");

                    out.println(
                            "<button type='submit'>" +
                            "♡ Wishlist" +
                            "</button>");
                }

                out.println("</form>");

                /*
                 * CART
                 */

                out.println(
                        "<form method='post' " +
                        "action='" +
                        request.getContextPath() +
                        "/services' " +
                        "style='display:inline;'>");

                out.println(
                        "<input type='hidden' " +
                        "name='action' " +
                        "value='addCart'>");

                out.println(
                        "<input type='hidden' " +
                        "name='serviceId' value='" +
                        service.getId() +
                        "'>");

                out.println(
                        "<button type='submit'>" +
                        "🛒 Add to Cart" +
                        "</button>");

                out.println("</form>");

                /*
                 * REQUEST SERVICE
                 */

                out.println(
                        "<form method='post' " +
                        "action='" +
                        request.getContextPath() +
                        "/services' " +
                        "style='display:inline;'>");

                out.println(
                        "<input type='hidden' " +
                        "name='action' " +
                        "value='request'>");

                out.println(
                        "<input type='hidden' " +
                        "name='serviceId' value='" +
                        service.getId() +
                        "'>");

                out.println(
                        "<button type='submit'>" +
                        "📦 Request Service" +
                        "</button>");

                out.println("</form>");

                out.println("</div>");

                /*
                 * REVIEW FORM
                 */

                out.println(
                        "<div class='review-section'>");

                out.println(
                        "<h4>⭐ Give Your Feedback</h4>");

                out.println(
                        "<form method='post' " +
                        "action='" +
                        request.getContextPath() +
                        "/review' " +
                        "class='review-form'>");

                out.println(
                        "<input type='hidden' " +
                        "name='serviceId' value='" +
                        service.getId() +
                        "'>");

                out.println("<label>Rating</label>");

                out.println(
                        "<select name='rating' required>");

                out.println(
                        "<option value=''>" +
                        "Select rating</option>");

                out.println(
                        "<option value='5'>⭐⭐⭐⭐⭐ Excellent</option>");

                out.println(
                        "<option value='4'>⭐⭐⭐⭐ Very Good</option>");

                out.println(
                        "<option value='3'>⭐⭐⭐ Good</option>");

                out.println(
                        "<option value='2'>⭐⭐ Fair</option>");

                out.println(
                        "<option value='1'>⭐ Poor</option>");

                out.println("</select>");

                out.println(
                        "<textarea name='comment' " +
                        "rows='3' " +
                        "placeholder='Write your feedback...'></textarea>");

                out.println(
                        "<button type='submit'>" +
                        "⭐ Submit Review</button>");

                out.println("</form>");

                /*
                 * EXISTING REVIEWS
                 */

                if (!reviews.isEmpty()) {

                    out.println(
                            "<h4>💬 Customer Feedback</h4>");

                    for (Review review : reviews) {

                        out.println(
                                "<div class='review-item'>");

                        out.println(
                                "<div class='stars'>" +
                                getStarString(
                                        review.getRating()) +
                                "</div>");

                        if (review.getComment() != null &&
                                !review.getComment()
                                        .trim()
                                        .isEmpty()) {

                            out.println(
                                    "<div class='comment'>" +
                                    "💬 " +
                                    escapeHtml(
                                            review.getComment()) +
                                    "</div>");
                        }

                        out.println("</div>");
                    }
                }

                out.println("</div>");

                out.println("</div>");
            }

            out.println("</div>");
        }

        out.println("</div>");

        out.println("</body>");
        out.println("</html>");
    }

    /*
     * POST
     */

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session =
                request.getSession(false);

        if (session == null ||
                session.getAttribute("user") == null) {

            response.sendRedirect(
                    request.getContextPath() +
                    "/login");

            return;
        }

        String action =
                request.getParameter("action");

        if ("add".equals(action)) {

            addService(request, session);

            response.sendRedirect(
                    request.getContextPath() +
                    "/services?message=added");

        } else if ("addWishlist".equals(action)) {

            addWishlist(request, session);

            response.sendRedirect(
                    request.getContextPath() +
                    "/services?message=wishlist-added");

        } else if ("removeWishlist".equals(action)) {

            removeWishlist(request, session);

            response.sendRedirect(
                    request.getContextPath() +
                    "/services?message=wishlist-removed");

        } else if ("addCart".equals(action)) {

            addCart(request, session);

            response.sendRedirect(
                    request.getContextPath() +
                    "/services?message=cart-added");

        } else if ("request".equals(action)) {

            handleServiceRequest(
                    request,
                    session);

            response.sendRedirect(
                    request.getContextPath() +
                    "/services?message=requested");

        } else {

            response.sendRedirect(
                    request.getContextPath() +
                    "/services");
        }
    }

    /*
     * ADD SERVICE
     */

    private void addService(
            HttpServletRequest request,
            HttpSession session) {

        Integer userId =
                (Integer) session.getAttribute(
                        "userId");

        if (userId == null) {
            return;
        }

        String name =
                request.getParameter("name");

        String description =
                request.getParameter(
                        "description");

        String category =
                request.getParameter(
                        "category");

        double price =
                Double.parseDouble(
                        request.getParameter(
                                "price"));

        Service service =
                new Service();

        service.setCreatorId(userId);
        service.setName(name);
        service.setDescription(description);
        service.setPrice(price);
        service.setCategory(category);

        serviceManager.addService(service);
    }

    /*
     * ADD CART
     */

    private void addCart(
            HttpServletRequest request,
            HttpSession session) {

        Integer userId =
                (Integer) session.getAttribute(
                        "userId");

        if (userId == null) {
            return;
        }

        String serviceIdParameter =
                request.getParameter(
                        "serviceId");

        if (serviceIdParameter == null) {
            return;
        }

        int serviceId =
                Integer.parseInt(
                        serviceIdParameter);

        cartDAO.addToCart(
                userId,
                serviceId);
    }

    /*
     * ADD WISHLIST
     */

    private void addWishlist(
            HttpServletRequest request,
            HttpSession session) {

        Integer userId =
                (Integer) session.getAttribute(
                        "userId");

        if (userId == null) {
            return;
        }

        int serviceId =
                Integer.parseInt(
                        request.getParameter(
                                "serviceId"));

        String sql =
                "INSERT INTO wishlist_items " +
                "(buyer_id, service_id) " +
                "VALUES (?, ?)";

        try (
                Connection connection =
                        DBUtil.getConnection(
                                getServletContext());

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(1, userId);
            statement.setInt(2, serviceId);
            statement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * REMOVE WISHLIST
     */

    private void removeWishlist(
            HttpServletRequest request,
            HttpSession session) {

        Integer userId =
                (Integer) session.getAttribute(
                        "userId");

        if (userId == null) {
            return;
        }

        int serviceId =
                Integer.parseInt(
                        request.getParameter(
                                "serviceId"));

        String sql =
                "DELETE FROM wishlist_items " +
                "WHERE buyer_id = ? " +
                "AND service_id = ?";

        try (
                Connection connection =
                        DBUtil.getConnection(
                                getServletContext());

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(1, userId);
            statement.setInt(2, serviceId);

            statement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * CHECK WISHLIST
     */

    private boolean isWishlisted(
            int userId,
            int serviceId) {

        String sql =
                "SELECT 1 " +
                "FROM wishlist_items " +
                "WHERE buyer_id = ? " +
                "AND service_id = ?";

        try (
                Connection connection =
                        DBUtil.getConnection(
                                getServletContext());

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(1, userId);
            statement.setInt(2, serviceId);

            try (
                    ResultSet resultSet =
                            statement.executeQuery()
            ) {

                return resultSet.next();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    /*
     * WISHLIST COUNT
     */

    private int getWishlistCount(
            int serviceId) {

        String sql =
                "SELECT COUNT(*) " +
                "FROM wishlist_items " +
                "WHERE service_id = ?";

        try (
                Connection connection =
                        DBUtil.getConnection(
                                getServletContext());

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(1, serviceId);

            try (
                    ResultSet resultSet =
                            statement.executeQuery()
            ) {

                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    /*
     * REQUEST SERVICE
     */

    private void handleServiceRequest(
            HttpServletRequest request,
            HttpSession session) {

        Integer buyerId =
                (Integer) session.getAttribute(
                        "userId");

        if (buyerId == null) {
            return;
        }

        int serviceId =
                Integer.parseInt(
                        request.getParameter(
                                "serviceId"));

        String serviceSql =
                "SELECT price " +
                "FROM services " +
                "WHERE id = ?";

        String orderSql =
                "INSERT INTO orders " +
                "(buyer_id, total_amount, status) " +
                "VALUES (?, ?, 'PENDING')";

        String itemSql =
                "INSERT INTO order_items " +
                "(order_id, service_id, quantity, price) " +
                "VALUES (?, ?, ?, ?)";

        try (
                Connection connection =
                        DBUtil.getConnection(
                                getServletContext())
        ) {

            connection.setAutoCommit(false);

            double price;

            try (
                    PreparedStatement statement =
                            connection.prepareStatement(
                                    serviceSql)
            ) {

                statement.setInt(1, serviceId);

                try (
                        ResultSet resultSet =
                                statement.executeQuery()
                ) {

                    if (!resultSet.next()) {

                        connection.rollback();
                        return;
                    }

                    price =
                            resultSet.getDouble(
                                    "price");
                }
            }

            int orderId;

            try (
                    PreparedStatement statement =
                            connection.prepareStatement(
                                    orderSql,
                                    java.sql.Statement
                                            .RETURN_GENERATED_KEYS)
            ) {

                statement.setInt(1, buyerId);
                statement.setDouble(2, price);

                statement.executeUpdate();

                try (
                        ResultSet keys =
                                statement.getGeneratedKeys()
                ) {

                    if (!keys.next()) {

                        connection.rollback();
                        return;
                    }

                    orderId =
                            keys.getInt(1);
                }
            }

            try (
                    PreparedStatement statement =
                            connection.prepareStatement(
                                    itemSql)
            ) {

                statement.setInt(1, orderId);
                statement.setInt(2, serviceId);
                statement.setInt(3, 1);
                statement.setDouble(4, price);

                statement.executeUpdate();
            }

            connection.commit();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * CATEGORY EMOJI
     */

    private String getCategoryEmoji(
            String category) {

        if (category == null) {
            return "🛠️";
        }

        switch (category.toLowerCase()) {

            case "technology":
                return "💻";

            case "design":
                return "🎨";

            case "education":
                return "📚";

            case "writing":
                return "✍️";

            case "digital marketing":
                return "📱";

            case "media":
                return "🎬";

            case "business":
                return "💼";

            case "data analysis":
                return "📊";

            default:
                return "🛠️";
        }
    }

    /*
     * AVERAGE RATING STARS
     */

    private String getStars(
            double rating) {

        int rounded =
                (int) Math.round(rating);

        StringBuilder stars =
                new StringBuilder();

        for (int i = 1; i <= 5; i++) {

            if (i <= rounded) {
                stars.append("★");
            } else {
                stars.append("☆");
            }
        }

        return stars.toString();
    }

    /*
     * REVIEW STARS
     */

    private String getStarString(
            int rating) {

        StringBuilder stars =
                new StringBuilder();

        for (int i = 1; i <= 5; i++) {

            if (i <= rating) {
                stars.append("★");
            } else {
                stars.append("☆");
            }
        }

        return stars.toString();
    }

    /*
     * HTML ESCAPE
     */

    private String escapeHtml(
            String value) {

        if (value == null) {
            return "";
        }

        return value
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}
