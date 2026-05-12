import java.util.*;

public class Comanda {
    private String id, mesa, alergias;
    private double total;
    private Map<String, Integer> platos;

    public Comanda() {}

    public Comanda(String id, String mesa, Map<String, Integer> platos,
                   String alergias, double total) {
        this.id = id;
        this.mesa = mesa;
        this.platos = platos;
        this.alergias = alergias;
        this.total = total;
    }

    public String getId() { return id; }
    public String getMesa() { return mesa; }
    public Map<String, Integer> getPlatos() { return platos; }
    public String getAlergias() { return alergias; }
    public double getTotal() { return total; }
}