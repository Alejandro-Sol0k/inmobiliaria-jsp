package co.edu.uts.inmobiliaria.model;

public final class AdminUser {
    private final int id;
    private final String email;
    private final String name;
    private final boolean active;
    private final String roles;

    public AdminUser(int id, String email, String name, boolean active, String roles) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.active = active;
        this.roles = roles;
    }

    public int getId() { return id; }
    public String getEmail() { return email; }
    public String getName() { return name; }
    public boolean isActive() { return active; }
    public String getRoles() { return roles; }
}
