async function sendChatMessage(message, chatMessages) {
    const response = await fetch("api/chat", {
        method: "POST",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded"
        },
        body: "message=" + encodeURIComponent(message)
    });
    const data = await response.json();
    const reply = document.createElement("p");
    reply.textContent = data.success
        ? "Assistant: " + data.reply
        : "Error: " + data.error;
    chatMessages.appendChild(reply);
}
