package com.conversor;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ConversorDeMonedas {
    private static final String API_KEY = "dbab4278ee6dd62b27be4ad1";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int opcion;

        do {
            System.out.println("***************************************************");
            System.out.println("Sea Binvenido/a al Conversor de Moneda =]");
            System.out.println();
            System.out.println("1) Dólar => Peso argentino");
            System.out.println("2) Peso argentino => Dólar");
            System.out.println("3) Dólar => Real brasileño");
            System.out.println("4) Real brasileño => Dólar");
            System.out.println("5) Dólar => Peso colombiano");
            System.out.println("6) Peso colombiano => Dólar");
            System.out.println("7) Salir");
//            System.out.print("Elija una opción válida: ");
//            opcion = scanner.nextInt();
            // Leer opción con validación robusta
            while (true) {
                System.out.print("Elija una opción válida: ");

                if (scanner.hasNextInt()) {
                    opcion = scanner.nextInt();

                    if (opcion >= 1 && opcion <= 7) {
                        break; // opción válida
                    } else {
                        System.out.println("Opción inválida. Intente nuevamente.\n");
                    }
                } else {
                    // Consumir entrada no numérica
                    String invalido = scanner.next();
                    System.out.println("Entrada no válida: \"" + invalido + "\". Por favor ingrese un número del 1 al 7.\n");
                }
            }

            String from = "", to = "";

            switch (opcion) {
                case 1 -> { from = "USD"; to = "ARS"; }
                case 2 -> { from = "ARS"; to = "USD"; }
                case 3 -> { from = "USD"; to = "BRL"; }
                case 4 -> { from = "BRL"; to = "USD"; }
                case 5 -> { from = "USD"; to = "COP"; }
                case 6 -> { from = "COP"; to = "USD"; }
                case 7 -> {
                    System.out.println("Gracias por usar el conversor. ¡Hasta luego!");
                    continue;
                }
  //              default -> {
  //                  System.out.println("Opción inválida. Intente nuevamente.");
  //                  continue;
  //              }
            }

 //         System.out.print("\nIngrese el valor que deseas convertir: ");
 //         double cantidad = scanner.nextDouble();
 //         Validación robusta del ingreso de cantidad
            double cantidad;
            while (true) {
                System.out.print("\nIngrese el valor que deseas convertir: ");

                if (scanner.hasNextDouble()) {
                    cantidad = scanner.nextDouble();
                    break;
                } else {
                    String invalido = scanner.next();
                    System.out.println("Entrada no válida: \"" + invalido + "\". Por favor ingrese una cantidad válida.\n");
                }
            }

            double resultado = obtenerConversion(from, to, cantidad);
            if (resultado >= 0) {
                System.out.printf("\nEl valor %.2f [%s] corresponde al valor final de => %.2f [%s]\n",
                        cantidad, from, resultado, to);
            } else {
                System.out.println("No se pudo realizar la conversión.");
            }

        } while (opcion != 7);

        scanner.close();
    }

    public static double obtenerConversion(String from, String to, double amount) {
        String urlStr = String.format("https://v6.exchangerate-api.com/v6/%s/pair/%s/%s/%.2f",
                API_KEY, from, to, amount);

        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder respuesta = new StringBuilder();
            String linea;

            while ((linea = reader.readLine()) != null) {
                respuesta.append(linea);
            }
            reader.close();

            //  Parseo usando Gson
            JsonObject json = JsonParser.parseString(respuesta.toString()).getAsJsonObject();

            if (json.get("result").getAsString().equals("success")) {
                return json.get("conversion_result").getAsDouble();
            } else {
                System.out.println("Error en la respuesta de la API: " + json.get("error-type").getAsString());
                return -1;
            }

        } catch (Exception e) {
            System.out.println("Error al conectar con la API: " + e.getMessage());
            return -1;
        }
    }
}