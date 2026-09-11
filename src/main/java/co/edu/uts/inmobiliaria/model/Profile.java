package co.edu.uts.inmobiliaria.model;

public final class Profile {
    private final int id;
    private final int userId;
    private final String names;
    private final String lastNames;
    private final String document;
    private final String phone;
    private final String address;
    private final String photoUrl;

    public Profile(int id, int userId, String names, String lastNames, String document,
            String phone, String address, String photoUrl) {
        this.id = id;
        this.userId = userId;
        this.names = names;
        this.lastNames = lastNames;
        this.document = document;
        this.phone = phone;
        this.address = address;
        this.photoUrl = photoUrl;
    }

    public int getId() { return id; }
    public int getUserId() { return userId; }
    public String getNames() { return names; }
    public String getLastNames() { return lastNames; }
    public String getDocument() { return document; }
    public String getPhone() { return phone; }
    public String getAddress() { return address; }
    public String getPhotoUrl() { return photoUrl; }
}
