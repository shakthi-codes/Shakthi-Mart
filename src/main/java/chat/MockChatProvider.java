package chat;
public class MockChatProvider implements ChatProvider {
    @Override
    public String getReply(String message) {
        if (message == null || message.trim().isEmpty()) {
            return "Please enter a question.";
        }
        String question = message.toLowerCase();
        if (question.contains("price")) {
            return "You can check the price on each service listing.";
        }
        if (question.contains("cart")) {
            return "You can add services to your cart and review the total before checkout.";
        }
        if (question.contains("checkout")) {
            return "You can complete your order through the checkout page.";
        }
        if (question.contains("login")) {
            return "Please log in using your registered account.";
        }
        if (question.contains("service")) {
            return "You can browse available services from the service listing.";
        }
        return "I can help with Shakthi Mart services, prices, cart, login, and checkout.";
    }
}
