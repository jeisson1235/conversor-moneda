import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.TreeMap;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;

public class ConsultaMonedaAPI {

    public Moneda buscaMoneda(String claveDeMoneda) {
        URI direccion = URI.create("https://v6.exchangerate-api.com/v6/c85853dc21ff8035ba17d8a2/latest/" + claveDeMoneda);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(direccion).build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return new Gson().fromJson(response.body(), Moneda.class);
        } catch (Exception e) {
            throw new RuntimeException("No se encontró esa moneda!");
        }
    }

    public Map<String, String> cargarMonedasDeLaAPI() {
        URI direccion = URI.create("https://v6.exchangerate-api.com/v6/c85853dc21ff8035ba17d8a2/codes");
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(direccion).build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            JsonObject jsonResponse = new Gson().fromJson(response.body(), JsonObject.class);
            JsonArray supportedCodes = jsonResponse.getAsJsonArray("supported_codes");

            Map<String, String> monedas = new TreeMap<>();
            for (int i = 0; i < supportedCodes.size(); i++) {
                JsonArray codePair = supportedCodes.get(i).getAsJsonArray();
                String clave = codePair.get(0).getAsString();
                String nombre = codePair.get(1).getAsString();
                monedas.put(clave, nombre);
            }

            return monedas;
        } catch (Exception e) {
            System.out.println("Error al cargar las monedas adicionales: " + e.getMessage());
            return null;
        }
    }
}
