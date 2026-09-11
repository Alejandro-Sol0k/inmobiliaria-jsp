package co.edu.uts.inmobiliaria.model;

import java.sql.Timestamp;

public final class Appointment {
    private final int id;
    private final int propertyId;
    private final String propertyTitle;
    private final String clientName;
    private final Timestamp dateTime;
    private final String status;
    private final String observations;

    public Appointment(int id, int propertyId, String propertyTitle, String clientName,
            Timestamp dateTime, String status, String observations) {
        this.id = id;
        this.propertyId = propertyId;
        this.propertyTitle = propertyTitle;
        this.clientName = clientName;
        this.dateTime = dateTime;
        this.status = status;
        this.observations = observations;
    }

    public int getId() { return id; }
    public int getPropertyId() { return propertyId; }
    public String getPropertyTitle() { return propertyTitle; }
    public String getClientName() { return clientName; }
    public Timestamp getDateTime() { return dateTime; }
    public String getStatus() { return status; }
    public String getObservations() { return observations; }
}
