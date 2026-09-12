package co.edu.uts.inmobiliaria.model;

import java.sql.Timestamp;

public final class ChatConversation {
    private final int id;
    private final String clientName;
    private final String agencyName;
    private final String propertyTitle;
    private final String subject;
    private final String status;
    private final Timestamp createdAt;
    private final Timestamp closedAt;

    public ChatConversation(int id, String clientName, String agencyName, String propertyTitle,
            String subject, String status, Timestamp createdAt, Timestamp closedAt) {
        this.id = id;
        this.clientName = clientName;
        this.agencyName = agencyName;
        this.propertyTitle = propertyTitle;
        this.subject = subject;
        this.status = status;
        this.createdAt = createdAt;
        this.closedAt = closedAt;
    }

    public int getId() { return id; }
    public String getClientName() { return clientName; }
    public String getAgencyName() { return agencyName; }
    public String getPropertyTitle() { return propertyTitle; }
    public String getSubject() { return subject; }
    public String getStatus() { return status; }
    public Timestamp getCreatedAt() { return createdAt; }
    public Timestamp getClosedAt() { return closedAt; }
}
