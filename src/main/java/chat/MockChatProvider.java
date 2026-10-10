package chat;
public class MockChatProvider implements ChatProvider {
    @Override
    public String getReply(String message) {
        if (message == null || message.trim().isEmpty()) {
            return "Please enter a question.";
        }
        String question = message.toLowerCase().trim();
        // General navigation
        if (question.contains("price")
                || question.contains("cost")
                || question.contains("rate")) {
            return "You can check the price of every service on its service card. Shakthi Mart offers services starting from affordable student-friendly prices.";
        }
        if (question.contains("cart")) {
            return "You can add any service to your cart and review the total before checkout.";
        }
        if (question.contains("checkout")
                || question.contains("payment")) {
            return "After adding services to your cart, open the Cart and continue to Checkout to complete your order.";
        }
        if (question.contains("login")
                || question.contains("sign in")) {
            return "Please log in using your registered Shakthi Mart account. New users can create an account using Register.";
        }
        // Technology
        if (question.contains("python")) {
            return "Yes! Python Programming is available on Shakthi Mart. It covers Python basics, problem solving and beginner programming assistance.";
        }
        if (question.contains("java")) {
            return "Yes! Java Programming is available. It includes OOP, collections and basic Java project support.";
        }
        if (question.contains("web development")
                || question.contains("website")
                || question.contains("web design")) {
            return "Web Development is available. You can request help with modern responsive websites using HTML, CSS and JavaScript.";
        }
        if (question.contains("programming")
                || question.contains("coding")
                || question.contains("technology")) {
            return "Our Technology services include Web Development, Java Programming and Python Programming.";
        }
        // Design
        if (question.contains("graphic design")
                || question.contains("poster")
                || question.contains("branding")) {
            return "Graphic Design is available for posters, social media designs and simple branding materials.";
        }
        if (question.contains("ui")
                || question.contains("ux")
                || question.contains("user interface")) {
            return "UI/UX Design is available for clean and user-friendly mobile and website interfaces.";
        }
        if (question.contains("resume")
                || question.contains("cv")) {
            return "Resume Design is available for clean and professional resume formatting for students and job seekers.";
        }
        if (question.contains("design")) {
            return "Our Design services include Graphic Design, UI/UX Design and Resume Design.";
        }
        // Education
        if (question.contains("tutor")
                || question.contains("tutoring")
                || question.contains("education")
                || question.contains("study")
                || question.contains("academic")) {
            return "Academic Tutoring is available for school and college subjects, with online learning support and clear explanations.";
        }
        // Writing
        if (question.contains("writing")
                || question.contains("writer")
                || question.contains("article")
                || question.contains("content")
                || question.contains("caption")) {
            return "Content Writing is available for articles, website content and social media captions.";
        }
        // Digital Marketing
        if (question.contains("digital marketing")
                || question.contains("marketing")
                || question.contains("social media marketing")
                || question.contains("promotion")) {
            return "Digital Marketing is available for social media marketing, content planning and basic online promotion.";
        }
        // Media
        if (question.contains("video")
                || question.contains("video editing")
                || question.contains("reels")
                || question.contains("media")) {
            return "Video Editing is available for short videos, reels and presentations.";
        }
        // Business
        if (question.contains("business")
                || question.contains("presentation")
                || question.contains("powerpoint")
                || question.contains("ppt")) {
            return "Business Presentation service is available for professional PowerPoint presentations for projects and business ideas.";
        }
        // Data Analysis
        if (question.contains("data analysis")
                || question.contains("data")
                || question.contains("analytics")
                || question.contains("visualization")
                || question.contains("data cleaning")) {
            return "Data Analysis is available for basic data cleaning, analysis and visualization using Python.";
        }
        // Service booking/request
        if (question.contains("book")
                || question.contains("request")
                || question.contains("hire")
                || question.contains("need a service")
                || question.contains("want a service")) {
            return "You can book a service by opening the Services page, choosing the service you need, and clicking Request Service.";
        }
        // Category overview
        if (question.contains("what services")
                || question.contains("available services")
                || question.contains("services available")
                || question.equals("services")
                || question.contains("categories")) {
            return "Shakthi Mart currently offers Technology, Design, Education, Writing, Digital Marketing, Media, Business and Data Analysis services.";
        }
        // Help
        if (question.contains("hello")
                || question.contains("hi")
                || question.contains("help")) {
            return "Hi! I am the Shakthi Mart Assistant. I can help you find services, prices, Technology, Design, Education, Writing, Digital Marketing, Media, Business and Data Analysis services.";
        }
        return "I can help you find Shakthi Mart services such as Python, Java, Web Development, Graphic Design, UI/UX, Tutoring, Writing, Digital Marketing, Video Editing, Business Presentations and Data Analysis. What service are you looking for?";
    }
}
