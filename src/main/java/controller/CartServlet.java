package controller;

import dao.CartDAO;
import dao.CartDAOImpl;
import model.CartItem;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/cart")
public class CartServlet extends HttpServlet {

    private CartDAO cartDAO;

    @Override
    public void init() {
        cartDAO = new CartDAOImpl(getServletContext());
    }

    // =========================
    // DISPLAY CART
    // =========================
    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session =
                request.getSession(false);

        // Check login
        if (session == null ||
                session.getAttribute("user") == null) {

            response.sendRedirect(
                    request.getContextPath() + "/login");

            return;
        }

        Integer userId =
                (Integer) session.getAttribute("userId");

        if (userId == null) {

            response.sendRedirect(
                    request.getContextPath() + "/login");

            return;
        }

        // Load cart from database
        List<CartItem> cartItems =
                cartDAO.findByBuyerId(userId);

        response.setContentType(
                "text/html;charset=UTF-8");

        PrintWriter out =
                response.getWriter();

        // =========================
        // HTML
        // =========================

        out.println("<!DOCTYPE html>");
        out.println("<html lang='en'>");

        out.println("<head>");

        out.println("<meta charset='UTF-8'>");

        out.println(
                "<meta name='viewport' " +
                "content='width=device-width, initial-scale=1.0'>");

        out.println(
                "<title>Shakthi Mart - Cart</title>");

        // =========================
        // CSS
        // =========================

        out.println("<style>");

        out.println("""
            body {
                font-family: Arial, sans-serif;
                margin: 0;
                background: #f5f3ff;
                color: #222;
            }

            .header {
                background: linear-gradient(135deg, #6a1b9a, #8e24aa);
                color: white;
                padding: 25px;
                text-align: center;
            }

            .header h1 {
                margin: 0;
                font-size: 32px;
            }

            .header p {
                margin: 8px 0 15px;
            }

            .header a {
                color: white;
                text-decoration: none;
                margin: 0 10px;
                font-weight: bold;
            }

            .header a:hover {
                text-decoration: underline;
            }

            .container {
                width: 90%;
                max-width: 1000px;
                margin: 30px auto;
            }

            .cart-box {
                background: white;
                padding: 25px;
                border-radius: 12px;
                box-shadow: 0 3px 10px rgba(0,0,0,0.1);
            }

            .cart-box > h2 {
                color: #6a1b9a;
                margin-top: 0;
            }

            .cart-item {
                display: flex;
                justify-content: space-between;
                align-items: center;
                gap: 20px;
                padding: 20px 0;
                border-bottom: 1px solid #ddd;
            }

            .item-info {
                flex: 1;
            }

            .item-info h3 {
                margin: 0 0 8px;
                color: #6a1b9a;
            }

            .item-info p {
                margin: 5px 0;
            }

            .price {
                font-size: 18px;
                font-weight: bold;
                color: #2e7d32;
            }

            .quantity {
                font-weight: bold;
            }

            .item-total {
                font-size: 18px;
                font-weight: bold;
            }

            button {
                border: none;
                padding: 10px 16px;
                border-radius: 6px;
                cursor: pointer;
                font-weight: bold;
                color: white;
            }

            .remove-button {
                background: #c62828;
            }

            .remove-button:hover {
                background: #8e0000;
            }

            .clear-button {
                background: #757575;
            }

            .clear-button:hover {
                background: #424242;
            }

            .checkout-button {
                background: #2e7d32;
                padding: 12px 22px;
                font-size: 16px;
            }

            .checkout-button:hover {
                background: #1b5e20;
            }

            .summary {
                margin-top: 25px;
                text-align: right;
                border-top: 2px solid #eee;
                padding-top: 20px;
            }

            .grand-total {
                font-size: 24px;
                font-weight: bold;
                color: #2e7d32;
                margin-bottom: 20px;
            }

            .checkout-area {
                margin-top: 15px;
            }

            .empty {
                text-align: center;
                padding: 50px 20px;
            }

            .empty h2 {
                color: #6a1b9a;
            }

            .back-button {
                display: inline-block;
                margin-top: 15px;
                background: #6a1b9a;
                color: white;
                padding: 10px 16px;
                border-radius: 6px;
                text-decoration: none;
            }

            .back-button:hover {
                background: #4a148c;
            }

            @media (max-width: 650px) {

                .cart-item {
                    flex-direction: column;
                    align-items: flex-start;
                }

                .summary {
                    text-align: left;
                }
            }
        """);

        out.println("</style>");

        out.println("</head>");

        // =========================
        // BODY
        // =========================

        out.println("<body>");

        // HEADER
        out.println("<div class='header'>");

        out.println(
                "<h1>Shakthi Mart 🛠️</h1>");

        out.println(
                "<p>Your Shopping Cart 🛒</p>");

        out.println(
                "<a href='" +
                request.getContextPath() +
                "/services'>Services</a>");

        out.println(
                "<a href='" +
                request.getContextPath() +
                "/cart'>Cart</a>");

        out.println(
                "<a href='" +
                request.getContextPath() +
                "/logout'>Logout</a>");

        out.println("</div>");

        // MAIN CONTAINER
        out.println("<div class='container'>");

        out.println("<div class='cart-box'>");

        out.println("<h2>🛒 My Cart</h2>");

        // =========================
        // EMPTY CART
        // =========================

        if (cartItems.isEmpty()) {

            out.println("<div class='empty'>");

            out.println(
                    "<h2>Your cart is empty 🛒</h2>");

            out.println(
                    "<p>Add a service from the Services page.</p>");

            out.println(
                    "<a class='back-button' href='" +
                    request.getContextPath() +
                    "/services'>");

            out.println("Browse Services");

            out.println("</a>");

            out.println("</div>");

        }

        // =========================
        // CART WITH ITEMS
        // =========================

        else {

            double grandTotal = 0;

            for (CartItem item : cartItems) {

                double itemTotal =
                        item.getPrice() *
                        item.getQuantity();

                grandTotal += itemTotal;

                // ITEM
                out.println(
                        "<div class='cart-item'>");

                // ITEM INFORMATION
                out.println(
                        "<div class='item-info'>");

                out.println(
                        "<h3>" +
                        escapeHtml(
                                item.getServiceName()) +
                        "</h3>");

                out.println(
                        "<p class='price'>₹" +
                        String.format(
                                "%.2f",
                                item.getPrice()) +
                        " per service</p>");

                out.println(
                        "<p class='quantity'>" +
                        "Quantity: " +
                        item.getQuantity() +
                        "</p>");

                out.println(
                        "<p class='item-total'>" +
                        "Total: ₹" +
                        String.format(
                                "%.2f",
                                itemTotal) +
                        "</p>");

                out.println("</div>");

                // REMOVE FORM
                out.println(
                        "<form method='post' " +
                        "action='" +
                        request.getContextPath() +
                        "/cart'>");

                out.println(
                        "<input type='hidden' " +
                        "name='serviceId' value='" +
                        item.getServiceId() +
                        "'>");

                out.println(
                        "<input type='hidden' " +
                        "name='action' value='remove'>");

                out.println(
                        "<button class='remove-button' " +
                        "type='submit'>");

                out.println("Remove");

                out.println("</button>");

                out.println("</form>");

                out.println("</div>");
            }

            // =========================
            // SUMMARY
            // =========================

            out.println("<div class='summary'>");

            out.println("<p>Cart Total</p>");

            out.println(
                    "<div class='grand-total'>₹" +
                    String.format(
                            "%.2f",
                            grandTotal) +
                    "</div>");

            // =========================
            // CHECKOUT BUTTON
            // =========================

            out.println(
                    "<div class='checkout-area'>");

            out.println(
                    "<form method='get' " +
                    "action='" +
                    request.getContextPath() +
                    "/checkout'>");

            out.println(
                    "<button class='checkout-button' " +
                    "type='submit'>");

            out.println(
                    "Proceed to Checkout 📦");

            out.println("</button>");

            out.println("</form>");

            out.println("</div>");

            // =========================
            // CLEAR CART
            // =========================

            out.println(
                    "<div style='margin-top:15px;'>");

            out.println(
                    "<form method='post' " +
                    "action='" +
                    request.getContextPath() +
                    "/cart'>");

            out.println(
                    "<input type='hidden' " +
                    "name='action' value='clear'>");

            out.println(
                    "<button class='clear-button' " +
                    "type='submit'>");

            out.println("Clear Cart");

            out.println("</button>");

            out.println("</form>");

            out.println("</div>");

            out.println("</div>");
        }

        // CLOSE CONTAINERS
        out.println("</div>");

        out.println("</div>");

        out.println("</body>");

        out.println("</html>");
    }

    // =========================
    // REMOVE / CLEAR CART
    // =========================

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session =
                request.getSession(false);

        // Check login
        if (session == null ||
                session.getAttribute("user") == null) {

            response.sendRedirect(
                    request.getContextPath() +
                    "/login");

            return;
        }

        Integer userId =
                (Integer) session.getAttribute(
                        "userId");

        if (userId == null) {

            response.sendRedirect(
                    request.getContextPath() +
                    "/login");

            return;
        }

        String action =
                request.getParameter("action");

        // =========================
        // REMOVE ITEM
        // =========================

        if ("remove".equals(action)) {

            String serviceIdParameter =
                    request.getParameter("serviceId");

            if (serviceIdParameter != null) {

                try {

                    int serviceId =
                            Integer.parseInt(
                                    serviceIdParameter);

                    cartDAO.removeFromCart(
                            userId,
                            serviceId);

                } catch (NumberFormatException e) {

                    response.sendError(
                            HttpServletResponse.SC_BAD_REQUEST,
                            "Invalid service ID");

                    return;
                }
            }
        }

        // =========================
        // CLEAR CART
        // =========================

        else if ("clear".equals(action)) {

            cartDAO.clearCart(userId);
        }

        // Return to cart
        response.sendRedirect(
                request.getContextPath() +
                "/cart");
    }

    // =========================
    // HTML ESCAPE
    // =========================

    private String escapeHtml(String value) {

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
