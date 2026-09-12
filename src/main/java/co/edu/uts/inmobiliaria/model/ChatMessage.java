package co.edu.uts.inmobiliaria.model;

import java.sql.Timestamp;

public final class ChatMessage {
    private final String senderName;
    private final boolean fromCurrentUser;
    private final String text;
    private final Timestamp sentAt;

    public ChatMessage(String senderName, boolean fromCurrentUser, String text, Timestamp sentAt) {
        this.senderName = senderName;
        this.fromCurrentUser = fromCurrentUser;
        this.text = text;
        this.sentAt = sentAt;
    }

    public String getSenderName() { return senderName; }
    public boolean isFromCurrentUser() { return fromCurrentUser; }
    public String getText() { return text; }
    public Timestamp getSentAt() { return sentAt; }
}
