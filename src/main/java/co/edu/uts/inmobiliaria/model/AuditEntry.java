package co.edu.uts.inmobiliaria.model;

import java.sql.Timestamp;

public final class AuditEntry {
    private final String actor;
    private final String action;
    private final String entity;
    private final String entityId;
    private final String detail;
    private final Timestamp createdAt;

    public AuditEntry(String actor, String action, String entity, String entityId,
            String detail, Timestamp createdAt) {
        this.actor = actor;
        this.action = action;
        this.entity = entity;
        this.entityId = entityId;
        this.detail = detail;
        this.createdAt = createdAt;
    }

    public String getActor() { return actor; }
    public String getAction() { return action; }
    public String getEntity() { return entity; }
    public String getEntityId() { return entityId; }
    public String getDetail() { return detail; }
    public Timestamp getCreatedAt() { return createdAt; }
}
