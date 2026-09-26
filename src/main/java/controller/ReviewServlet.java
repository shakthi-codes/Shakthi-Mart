package controller;

import dao.ReviewDAO;
import dao.ReviewDAOImpl;
import model.Review;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/review")
public class ReviewServlet extends HttpServlet {

    private ReviewDAO reviewDAO;

    @Override
    public void init() {
        reviewDAO = new ReviewDAOImpl(getServletContext());
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null) {
            response.sendRedirect(
                    request.getContextPath() + "/login"
            );
            return;
        }

        try {

            Object userIdObject =
                    session.getAttribute("userId");

            if (userIdObject == null) {
                response.sendRedirect(
                        request.getContextPath() + "/login"
                );
                return;
            }

            int userId;

            if (userIdObject instanceof Integer) {
                userId = (Integer) userIdObject;
            } else {
                userId = Integer.parseInt(
                        userIdObject.toString()
                );
            }

            int serviceId = Integer.parseInt(
                    request.getParameter("serviceId")
            );

            int rating = Integer.parseInt(
                    request.getParameter("rating")
            );

            String comment =
                    request.getParameter("comment");

            if (rating < 1 || rating > 5) {
                response.sendRedirect(
                        request.getContextPath()
                        + "/services?error=invalid-rating"
                );
                return;
            }

            if (comment != null) {
                comment = comment.trim();
            }

            Review review = new Review();

            review.setBuyerId(userId);
            review.setServiceId(serviceId);
            review.setRating(rating);
            review.setComment(comment);

            reviewDAO.save(review);

            response.sendRedirect(
                    request.getContextPath()
                    + "/services?review=success"
            );

        } catch (Exception e) {

            e.printStackTrace();

            response.sendRedirect(
                    request.getContextPath()
                    + "/services?error=review-failed"
            );
        }
    }
}
