package controller;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@WebServlet("/home")
public class HomeServlet extends HttpServlet {
    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String contextPath =
                request.getContextPath();
        response.getWriter().println(
                "<!DOCTYPE html>" +
                "<html lang='en'>" +
                "<head>" +
                "<meta charset='UTF-8'>" +
                "<meta name='viewport' " +
                "content='width=device-width, initial-scale=1.0'>" +
                "<title>Shakthi Mart</title>" +
                "<style>" +
                "body{font-family:Arial,sans-serif;" +
                "margin:40px;background:#f5f3ff;color:#222;}" +
                ".box{max-width:700px;margin:auto;" +
                "background:white;padding:30px;" +
                "border-radius:15px;" +
                "box-shadow:0 4px 15px rgba(0,0,0,.12);}" +
                "button{padding:10px 16px;" +
                "background:#6a1b9a;color:white;" +
                "border:0;border-radius:7px;cursor:pointer;}" +
                "#chatPanel{margin-top:20px;}" +
                "#chatMessages{padding:10px;" +
                "background:#f5f5f5;margin-bottom:10px;" +
                "min-height:50px;}" +
                "input{padding:10px;width:70%;" +
                "box-sizing:border-box;}" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class='box'>" +
                "<h1>Welcome to Shakthi Mart!</h1>" +
                "<p>Shakthi Mart Assistant is ready.</p>" +
                "<button id='chatButton'>?? Chat</button>" +
                "<div id='chatPanel' style='display:none;'>" +
                "<h2>Shakthi Mart Assistant</h2>" +
                "<div id='chatMessages'></div>" +
                "<input type='text' id='chatInput' " +
                "placeholder='Ask about services...'>" +
                "<button id='sendButton'>Send</button>" +
                "</div>" +
                "</div>" +
                "<script>" +
                "const chatButton=" +
                "document.getElementById('chatButton');" +
                "const chatPanel=" +
                "document.getElementById('chatPanel');" +
                "const sendButton=" +
                "document.getElementById('sendButton');" +
                "const chatInput=" +
                "document.getElementById('chatInput');" +
                "const chatMessages=" +
                "document.getElementById('chatMessages');" +
                "chatButton.addEventListener('click',function(){" +
                "chatPanel.style.display=" +
                "chatPanel.style.display==='none'?" +
                "'block':'none';" +
                "});" +
                "sendButton.addEventListener('click',async function(){" +
                "const message=chatInput.value.trim();" +
                "if(!message)return;" +
                "chatMessages.innerHTML+=" +
                "'<p><b>You:</b> '+" +
                "message.replace(/</g,'&lt;')+" +
                "'</p>';" +
                "try{" +
                "const response=await fetch('" +
                contextPath +
                "/api/chat',{method:'POST'," +
                "headers:{'Content-Type':" +
                "'application/x-www-form-urlencoded'}," +
                "body:'message='+encodeURIComponent(message)" +
                "});" +
                "const data=await response.json();" +
                "chatMessages.innerHTML+=" +
                "'<p><b>Assistant:</b> '+" +
                "(data.success?data.reply:data.error)+" +
                "'</p>';" +
                "}catch(error){" +
                "chatMessages.innerHTML+=" +
                "'<p><b>Error:</b> Unable to contact assistant.</p>';" +
                "}" +
                "chatInput.value='';" +
                "});" +
                "</script>" +
                "</body>" +
                "</html>"
        );
    }
}
