package controller;

import dao.CartDAO;
import dao.CartDAOImpl;
import dao.OrderDAO;
import dao.OrderDAOImpl;
import dao.OrderItemDAO;
import dao.OrderItemDAOImpl;
import model.CartItem;
import model.Order;
import model.OrderItem;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/checkout")
public class CheckoutServlet extends HttpServlet {

    private CartDAO cartDAO;
    private OrderDAO orderDAO;
    private OrderItemDAO orderItemDAO;

    @Override
    public void init() {

        cartDAO =
                new CartDAOImpl(getServletContext());

        orderDAO =
                new OrderDAOImpl(getServletContext());

        orderItemDAO =
                new OrderItemDAOImpl(getServletContext());
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

        List<CartItem> cartItems =
                cartDAO.findByBuyerId(userId);

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
                "<title>Shakthi Mart - Checkout</title>");

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
            }

            .header a {
                color: white;
                text-decoration: none;
                margin: 0 10px;
                font-weight: bold;
            }

            .container {
                width: 90%;
                max-width: 900px;
                margin: 30px auto;
            }

            .box {
                background: white;
                padding: 25px;
                border-radius: 12px;
                box-shadow: 0 3px 10px rgba(0,0,0,0.1);
                margin-bottom: 20px;
            }

            .item {
                padding: 15px 0;
                border-bottom: 1px solid #ddd;
            }

            .item h3 {
                color: #6a1b9a;
                margin-bottom: 5px;
            }

            .total {
                font-size: 24px;
                font-weight: bold;
                color: #2e7d32;
                text-align: right;
                margin-top: 20px;
            }

            .checkout-button {
                background: #6a1b9a;
                color: white;
                border: none;
                padding: 12px 25px;
                border-radius: 7px;
                cursor: pointer;
                font-size: 16px;
                font-weight: bold;
            }

            .checkout-button:hover {
                background: #4a148c;
            }

            .empty {
                text-align: center;
                padding: 40px;
            }

            .back {
                color: #6a1b9a;
                font-weight: bold;
                text-decoration: none;
            }
        """);

        out.println("</style>");
        out.println("</head>");

        out.println("<body>");

        // HEADER
        out.println("<div class='header'>");

        out.println(
                "<h1>Shakthi Mart 🛠️</h1>");

        out.println(
                "<p>Checkout 📦</p>");

        out.println(
                "<a href='" +
                request.getContextPath() +
                "/services'>Services</a>");

        out.println(
                "<a href='" +
                request.getContextPath() +
                "/cart'>Cart</a>");

        out.println("</div>");

        out.println("<div class='container'>");

        // EMPTY CART
        if (cartItems.isEmpty()) {

            out.println(
                    "<div class='box empty'>");

            out.println(
                    "<h2>Your cart is empty 🛒</h2>");

            out.println(
                    "<p>Please add a service before checkout.</p>");

            out.println(
                    "<a class='back' href='" +
                    request.getContextPath() +
                    "/services'>");

            out.println(
                    "Browse Services");

            out.println("</a>");

            out.println("</div>");

        } else {

            double grandTotal = 0;

            // ORDER SUMMARY
            out.println("<div class='box'>");

            out.println(
                    "<h2>📦 Order Summary</h2>");

            for (CartItem item : cartItems) {

                double itemTotal =
                        item.getPrice() *
                        item.getQuantity();

                grandTotal += itemTotal;

                out.println(
                        "<div class='item'>");

                out.println(
                        "<h3>" +
                        escapeHtml(
                                item.getServiceName()) +
                        "</h3>");

                out.println(
                        "<p>Price: ₹" +
                        String.format(
                                "%.2f",
                                item.getPrice()) +
                        "</p>");

                out.println(
                        "<p>Quantity: " +
                        item.getQuantity() +
                        "</p>");

                out.println(
                        "<p>Item Total: ₹" +
                        String.format(
                                "%.2f",
                                itemTotal) +
                        "</p>");

                out.println("</div>");
            }

            out.println(
                    "<div class='total'>" +
                    "Grand Total: ₹" +
                    String.format(
                            "%.2f",
                            grandTotal) +
                    "</div>");

            out.println("</div>");

            // PLACE ORDER
            out.println(
                    "<div class='box'>");

            out.println(
                    "<h2>Confirm Your Order</h2>");

            out.println(
                    "<p>Your order will be placed with " +
                    "status <b>PENDING</b>.</p>");

            out.println(
                    "<form method='post' " +
                    "action='" +
                    request.getContextPath() +
                    "/checkout'>");

            out.println(
                    "<button class='checkout-button' " +
                    "type='submit'>");

            out.println(
                    "Place Order 📦");

            out.println("</button>");

            out.println("</form>");

            out.println("</div>");
        }

        out.println("</div>");

        out.println("</body>");
        out.println("</html>");
    }

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

        Integer userId =
                (Integer) session.getAttribute(
                        "userId");

        if (userId == null) {

            response.sendRedirect(
                    request.getContextPath() +
                    "/login");

            return;
        }

        // Get cart
        List<CartItem> cartItems =
                cartDAO.findByBuyerId(userId);

        if (cartItems.isEmpty()) {

            response.sendRedirect(
                    request.getContextPath() +
                    "/cart");

            return;
        }

        // Calculate total
        double grandTotal = 0;

        for (CartItem item : cartItems) {

            grandTotal +=
                    item.getPrice() *
                    item.getQuantity();
        }

        // Create order
        Order order =
                new Order();

        order.setBuyerId(userId);
        order.setTotalAmount(grandTotal);
        order.setStatus("PENDING");

        int orderId =
                orderDAO.createOrder(order);

        // Save order items
        for (CartItem item : cartItems) {

            OrderItem orderItem =
                    new OrderItem();

            orderItem.setOrderId(orderId);
            orderItem.setServiceId(
                    item.getServiceId());
            orderItem.setQuantity(
                    item.getQuantity());
            orderItem.setPrice(
                    item.getPrice());

            orderItemDAO.save(orderItem);
        }

        // Clear cart after successful order
        cartDAO.clearCart(userId);

        // Redirect to confirmation
        response.sendRedirect(
                request.getContextPath() +
                "/checkout?success=true&orderId=" +
                orderId);
    }

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
