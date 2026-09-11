package co.edu.uts.inmobiliaria.model;

public final class CatalogItem {
    private final int id;
    private final String name;

    public CatalogItem(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() { return id; }
    public String getName() { return name; }
}
