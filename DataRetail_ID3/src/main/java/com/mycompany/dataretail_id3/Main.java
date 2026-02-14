/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.dataretail_id3;
import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.awt.Dimension;

public class Main {

    public static void main(String[] args) {
        // Cargar datos (Asegúrate de tener tu datos.txt listo)
        String nombreArchivo = "datos.txt"; 
        List<Registro> datos = cargarDatosDesdeArchivo(nombreArchivo);
        
        if (datos.isEmpty()) {
            System.err.println("No se encontraron datos. Usando datos de prueba por defecto...");
            datos = cargarDatosDefault(); // Fallback si falla el archivo
        }

        List<String> atributos = new ArrayList<>(Arrays.asList("VentasPrevias", "Precio", "Temporada"));

        ID3 id3 = new ID3();
        Nodo arbol = id3.construirArbol(datos, atributos);

        // ABRIR VENTANA MEJORADA
        if (arbol != null) {
            SwingUtilities.invokeLater(() -> {
                try {
                    // Intentar poner estilo nativo del sistema operativo
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) { }

                JFrame ventana = new JFrame("Árbol de Decisión ID3 - DataRetail Analytics");
                ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                ventana.setSize(1200, 800);
                
                // Usar el nuevo Lienzo con Scroll
                LienzoArbol lienzo = new LienzoArbol(arbol);
                
                // El JScrollPane permite movernos si el árbol es gigante
                JScrollPane scrollPane = new JScrollPane(lienzo);
                scrollPane.getVerticalScrollBar().setUnitIncrement(16); // Scroll más rápido
                scrollPane.getHorizontalScrollBar().setUnitIncrement(16);
                
                ventana.add(scrollPane);
                ventana.setLocationRelativeTo(null);
                ventana.setVisible(true);
            });
        }
    }
    
    // Método auxiliar por si falla el TXT
    private static List<Registro> cargarDatosDefault() {
         List<Registro> lista = new ArrayList<>();
         lista.add(crearReg("Alta", "Alto", "Invierno", "NO"));
         lista.add(crearReg("Baja", "Bajo", "Verano", "SI"));
         return lista;
    }

    private static Registro crearReg(String ventas, String precio, String temp, String target) {
        Registro r = new Registro(target);
        r.agregarAtributo("VentasPrevias", ventas);
        r.agregarAtributo("Precio", precio);
        r.agregarAtributo("Temporada", temp);
        return r;
    }

    private static List<Registro> cargarDatosDesdeArchivo(String ruta) {
        List<Registro> lista = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            String linea;
            boolean primeraLinea = true;
            while ((linea = br.readLine()) != null) {
                if (primeraLinea) { primeraLinea = false; continue; }
                String[] partes = linea.split(",");
                if (partes.length >= 4) {
                    Registro r = new Registro(partes[3].trim());
                    r.agregarAtributo("VentasPrevias", partes[0].trim());
                    r.agregarAtributo("Precio", partes[1].trim());
                    r.agregarAtributo("Temporada", partes[2].trim());
                    lista.add(r);
                }
            }
        } catch (IOException e) {
            System.err.println("Advertencia: " + e.getMessage());
        }
        return lista;
    }

}

