import java.io.FileWriter;
import java.io.IOException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GeneradorDeArchivo {

    public void guardarJsonHistorial(Object historial, String nombreArchivo) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        FileWriter escritura = new FileWriter(nombreArchivo);
        escritura.write(gson.toJson(historial));
        escritura.close();
    }
}
