package controller;

import dao.ProductDAOImpl;
import model.Product;
import service.ProductService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/products")
public class ProductServlet extends HttpServlet {

    private ProductService productService;

    @Override
    public void init() {
        ProductDAOImpl productDAO =
                new ProductDAOImpl(getServletContext());

        productService =
                new ProductService(productDAO);
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");

        HttpSession session =
                request.getSession(false);

        if (session == null ||
                session.getAttribute("user") == null) {

            response.sendRedirect(
                    request.getContextPath() + "/login");

            return;
        }

        List<Product> products =
                productService.getAllProducts();

        PrintWriter out = response.getWriter();

        out.println("""
                <html>
                <head>
                    <title>Shakthi Mart Products</title>
                </head>
                <body>

                <h1>Shakthi Mart - Products</h1>

                <h2>Add Product</h2>

                <form method="post"
                      action="products">

                    <label>Seller ID:</label>
                    <input type="number"
                           name="sellerId"
                           required>
                    <br><br>

                    <label>Product Name:</label>
                    <input type="text"
                           name="name"
                           required>
                    <br><br>

                    <label>Description:</label>
                    <input type="text"
                           name="description">
                    <br><br>

                    <label>Price:</label>
                    <input type="number"
                           name="price"
                           step="0.01"
                           required>
                    <br><br>

                    <label>Stock:</label>
                    <input type="number"
                           name="stockQty"
                           required>
                    <br><br>

                    <label>Category:</label>
                    <input type="text"
                           name="category">
                    <br><br>

                    <label>Image URL:</label>
                    <input type="text"
                           name="imageUrl">
                    <br><br>

                    <button type="submit">
                        Add Product
                    </button>

                </form>

                <hr>

                <h2>Available Products</h2>
                """);

        if (products.isEmpty()) {

            out.println("<p>No products available.</p>");

        } else {

            for (Product product : products) {

                out.println("<div>");
                out.println("<h3>"
                        + product.getName()
                        + "</h3>");

                out.println("<p>"
                        + product.getDescription()
                        + "</p>");

                out.println("<p>Price: ₹"
                        + product.getPrice()
                        + "</p>");

                out.println("<p>Stock: "
                        + product.getStockQty()
                        + "</p>");

                out.println("<p>Category: "
                        + product.getCategory()
                        + "</p>");

                out.println("<hr>");
                out.println("</div>");
            }
        }

        out.println("""
                </body>
                </html>
                """);
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
                    request.getContextPath() + "/login");

            return;
        }

        int sellerId = Integer.parseInt(
                request.getParameter("sellerId"));

        String name =
                request.getParameter("name");

        String description =
                request.getParameter("description");

        double price = Double.parseDouble(
                request.getParameter("price"));

        int stockQty = Integer.parseInt(
                request.getParameter("stockQty"));

        String category =
                request.getParameter("category");

        String imageUrl =
                request.getParameter("imageUrl");

        Product product = new Product(
                0,
                sellerId,
                name,
                description,
                price,
                stockQty,
                category,
                imageUrl
        );

        productService.addProduct(product);

        response.sendRedirect(
                request.getContextPath()
                        + "/products");
    }
}
