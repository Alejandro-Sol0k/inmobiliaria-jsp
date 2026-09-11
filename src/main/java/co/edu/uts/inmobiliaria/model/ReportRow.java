package co.edu.uts.inmobiliaria.model;

public final class ReportRow {
    private final String label;
    private final int total;

    public ReportRow(String label, int total) {
        this.label = label;
        this.total = total;
    }

    public String getLabel() { return label; }
    public int getTotal() { return total; }
}
