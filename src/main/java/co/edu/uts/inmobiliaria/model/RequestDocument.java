package co.edu.uts.inmobiliaria.model;

public final class RequestDocument {
    private final int id;
    private final int requestId;
    private final String propertyTitle;
    private final String fileName;
    private final String fileUrl;
    private final String status;

    public RequestDocument(int id, int requestId, String propertyTitle, String fileName,
            String fileUrl, String status) {
        this.id = id;
        this.requestId = requestId;
        this.propertyTitle = propertyTitle;
        this.fileName = fileName;
        this.fileUrl = fileUrl;
        this.status = status;
    }

    public int getId() { return id; }
    public int getRequestId() { return requestId; }
    public String getPropertyTitle() { return propertyTitle; }
    public String getFileName() { return fileName; }
    public String getFileUrl() { return fileUrl; }
    public String getStatus() { return status; }
}
