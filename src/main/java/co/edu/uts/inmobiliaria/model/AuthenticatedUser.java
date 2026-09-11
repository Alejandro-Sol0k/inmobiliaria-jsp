package co.edu.uts.inmobiliaria.model;

public final class AuthenticatedUser {
    private final int id;
    private final String email;
    private final String role;

    public AuthenticatedUser(int id, String email, String role) {
        this.id = id;
        this.email = email;
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }
}
