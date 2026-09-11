package co.edu.uts.inmobiliaria.model;

import java.sql.Timestamp;

public final class PropertyRequest {
    private final int id;
    private final int propertyId;
    private final String propertyTitle;
    private final String clientName;
    private final String operationType;
    private final String status;
    private final Timestamp createdAt;

    public PropertyRequest(int id, int propertyId, String propertyTitle, String clientName,
            String operationType, String status, Timestamp createdAt) {
        this.id = id;
        this.propertyId = propertyId;
        this.propertyTitle = propertyTitle;
        this.clientName = clientName;
        this.operationType = operationType;
        this.status = status;
        this.createdAt = createdAt;
    }

    public int getId() { return id; }
    public int getPropertyId() { return propertyId; }
    public String getPropertyTitle() { return propertyTitle; }
    public String getClientName() { return clientName; }
    public String getOperationType() { return operationType; }
    public String getStatus() { return status; }
    public Timestamp getCreatedAt() { return createdAt; }
}
