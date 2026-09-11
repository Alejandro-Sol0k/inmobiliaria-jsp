package co.edu.uts.inmobiliaria.model;

import java.math.BigDecimal;

public final class Property {
    private final int id;
    private final String title;
    private final String city;
    private final String type;
    private final String operation;
    private final BigDecimal price;
    private final int bedrooms;
    private final int bathrooms;
    private final BigDecimal area;
    private final String imageUrl;

    public Property(int id, String title, String city, String type, String operation,
            BigDecimal price, int bedrooms, int bathrooms, BigDecimal area, String imageUrl) {
        this.id = id;
        this.title = title;
        this.city = city;
        this.type = type;
        this.operation = operation;
        this.price = price;
        this.bedrooms = bedrooms;
        this.bathrooms = bathrooms;
        this.area = area;
        this.imageUrl = imageUrl;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getCity() { return city; }
    public String getType() { return type; }
    public String getOperation() { return operation; }
    public BigDecimal getPrice() { return price; }
    public int getBedrooms() { return bedrooms; }
    public int getBathrooms() { return bathrooms; }
    public BigDecimal getArea() { return area; }
    public String getImageUrl() { return imageUrl; }
}
