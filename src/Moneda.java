import java.util.Map;

public class Moneda {
    private String base_code;
    private Map<String, Double> conversion_rates;

    public String base_code() {
        return base_code;
    }

    public Map<String, Double> conversion_rates() {
        return conversion_rates;
    }
}
