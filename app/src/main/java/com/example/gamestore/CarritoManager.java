package com.example.gamestore;

import java.util.ArrayList;

public class CarritoManager {

    private static final ArrayList<String> productos = new ArrayList<>();
    private static int total = 0;

    public static void agregarProducto(String nombre, String precioTexto) {
        productos.add("• " + nombre + " - " + precioTexto);
        total += convertirPrecioANumero(precioTexto);
    }

    public static void vaciarCarrito() {
        productos.clear();
        total = 0;
    }

    public static int getCantidadProductos() {
        return productos.size();
    }

    public static int getTotal() {
        return total;
    }

    public static String getProductosTexto() {
        if (productos.isEmpty()) {
            return "No hay productos agregados";
        }

        StringBuilder resumen = new StringBuilder();

        for (String producto : productos) {
            resumen.append(producto).append("\n");
        }

        return resumen.toString().trim();
    }

    private static int convertirPrecioANumero(String precioTexto) {
        String precioLimpio = precioTexto.replace("$", "").replace(".", "").trim();
        return Integer.parseInt(precioLimpio);
    }
}