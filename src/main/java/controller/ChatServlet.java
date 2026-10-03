package controller;
import chat.ChatProvider;
import chat.MockChatProvider;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@WebServlet("/api/chat")
public class ChatServlet extends HttpServlet {
    private static final int MAX_MESSAGE_LENGTH = 500;
    private final ChatProvider chatProvider = new MockChatProvider();
    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String message = request.getParameter("message");
        if (message == null || message.trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(
                    "{\"success\":false,\"error\":\"Message is required.\"}");
            return;
        }
        if (message.length() > MAX_MESSAGE_LENGTH) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(
                    "{\"success\":false,\"error\":\"Message is too long.\"}");
            return;
        }
        String reply = chatProvider.getReply(message);
        response.getWriter().write(
                "{\"success\":true,\"reply\":\""
                        + escapeJson(reply)
                        + "\"}");
    }
    private String escapeJson(String value) {
        return value.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r");
    }
}
