import java.util.HashMap;
import java.util.Map;

public class MonedaInfo {

    private static final Map<String, String> monedaMap = new HashMap<>();

    static {
        monedaMap.put("ARS", "Peso Argentino");
        monedaMap.put("BOB", "Boliviano");
        monedaMap.put("BRL", "Real Brasileño");
        monedaMap.put("CLP", "Peso Chileno");
        monedaMap.put("COP", "Peso Colombiano");
        monedaMap.put("CRC", "Colón Costarricense");
        monedaMap.put("DOP", "Peso Dominicano");
        monedaMap.put("MXN", "Peso Mexicano");
        monedaMap.put("PEN", "Sol Peruano");
        monedaMap.put("UYU", "Peso Uruguayo");
        monedaMap.put("VES", "Bolívar Venezolano");
        monedaMap.put("USD", "Dólar Estadounidense");
        monedaMap.put("EUR", "Euro");
        monedaMap.put("GBP", "Libra Esterlina");
        monedaMap.put("JPY", "Yen Japonés");
        monedaMap.put("CNY", "Yuan Chino");
        monedaMap.put("CAD", "Dólar Canadiense");
    }

    public static String obtenerNombreMoneda(String clave) {
        return monedaMap.getOrDefault(clave, "Moneda no traducida");
    }

    public static Map<String, String> obtenerMonedasValidas() {
        return monedaMap;
    }
}
