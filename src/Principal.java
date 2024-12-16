import java.io.IOException;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class Principal {

    public static void main(String[] args) {
        Scanner lectura = new Scanner(System.in);
        ConsultaMonedaAPI consulta = new ConsultaMonedaAPI();
        GeneradorDeArchivo generadorDeArchivo = new GeneradorDeArchivo();
        List<String> historialDeBusquedas = new ArrayList<>();
        boolean monedasExtrasMostradas = false;

        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
        numberFormat.setMinimumFractionDigits(2);
        numberFormat.setMaximumFractionDigits(2);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        Map<String, String> todasLasMonedas = new TreeMap<>(MonedaInfo.obtenerMonedasValidas());

        printDivider();
        System.out.println("Monedas válidas para realizar conversiones (clave - nombre):");
        for (String clave : todasLasMonedas.keySet()) {
            System.out.printf("%s - %s\n", clave, todasLasMonedas.get(clave));
        }

        while (true) {
            if (!monedasExtrasMostradas) {
                printDivider();
                System.out.println("Escribe la clave de la moneda que deseas convertir ('mostrar' para ver todas las opciones o 'salir' para finalizar):");
            } else {
                printDivider();
                System.out.println("Escribe la clave de la moneda que deseas convertir o 'salir' para finalizar:");
            }

            var monedaBase = lectura.nextLine().toUpperCase();

            if (monedaBase.equalsIgnoreCase("salir")) {
                break;
            }

            if (!monedasExtrasMostradas && monedaBase.equalsIgnoreCase("mostrar")) {
                Map<String, String> monedasAdicionales = consulta.cargarMonedasDeLaAPI();
                if (monedasAdicionales != null) {
                    System.out.println("Todas las monedas disponibles:");
                    for (String clave : monedasAdicionales.keySet()) {
                        String nombreMoneda = MonedaInfo.obtenerNombreMoneda(clave);
                        if (nombreMoneda.equals("Moneda no traducida")) {
                            nombreMoneda = monedasAdicionales.get(clave);
                        }
                        System.out.printf("%s - %s\n", clave, nombreMoneda);
                    }
                    todasLasMonedas.putAll(monedasAdicionales);
                    monedasExtrasMostradas = true;
                } else {
                    System.out.println("No se pudieron cargar monedas adicionales.");
                }
                continue;
            }

            if (!todasLasMonedas.containsKey(monedaBase)) {
                System.out.println("Moneda no válida. Intente de nuevo.");
                continue;
            }

            try {
                Moneda moneda = consulta.buscaMoneda(monedaBase);
                Map<String, Double> conversionRates = moneda.conversion_rates();

                System.out.println("Escribe la clave de la moneda a la que deseas convertir:");
                var monedaDestino = lectura.nextLine().toUpperCase();

                if (!conversionRates.containsKey(monedaDestino)) {
                    System.out.println("Moneda no encontrada o no permitida.");
                    continue;
                }

                double tasa = conversionRates.get(monedaDestino);

                System.out.println("Escribe la cantidad que deseas convertir:");
                double cantidad = lectura.nextDouble();
                lectura.nextLine();

                double resultado = cantidad * tasa;

                String nombreMonedaBase = todasLasMonedas.get(monedaBase);
                String nombreMonedaDestino = todasLasMonedas.get(monedaDestino);

                String marcaDeTiempo = LocalDateTime.now().format(formatter);

                System.out.printf("La cantidad de %s %s (%s) equivale a %s %s (%s)\n",
                        numberFormat.format(cantidad), monedaBase, nombreMonedaBase,
                        numberFormat.format(resultado), monedaDestino, nombreMonedaDestino);
                printDivider();

                String busqueda = String.format("[%s] %s %s (%s) -> %s %s (%s)",
                        marcaDeTiempo, numberFormat.format(cantidad), monedaBase, nombreMonedaBase,
                        numberFormat.format(resultado), monedaDestino, nombreMonedaDestino);
                historialDeBusquedas.add(busqueda);

            } catch (Exception e) {
                System.out.println("Error en la conversión: " + e.getMessage());
            }
        }

        printDivider();
        System.out.println("\nHistorial de conversiones realizadas:");
        for (String busqueda : historialDeBusquedas) {
            System.out.println(busqueda);
        }

        try {
            generadorDeArchivo.guardarJsonHistorial(historialDeBusquedas, "historial_busquedas.json");
        } catch (IOException e) {
            System.out.println("Error al guardar el historial en archivo JSON: " + e.getMessage());
        }
        printDivider();
        System.out.println("¡Gracias por usar nuestro conversor de monedas!");
        System.out.println("Los datos de las tasas de cambio fueron proporcionados por ExchangeRate-API.");
        System.out.println("Para más información, puedes visitar: https://www.exchangerate-api.com/");
        printDivider();
    }

    public static void printDivider() {
        System.out.println("------------------------------------------------------------------------------");
    }
}
