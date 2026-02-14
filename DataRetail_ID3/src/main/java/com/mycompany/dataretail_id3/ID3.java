/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.dataretail_id3;
import java.util.*;
/**
 *
 * @author darwi
 */
public class ID3 {
// Calcular Entropía (con impresión de pasos)
    public double calcularEntropia(List<Registro> datos) {
        if (datos.isEmpty()) return 0;

        Map<String, Integer> conteo = new HashMap<>();
        for (Registro r : datos) {
            conteo.put(r.getEtiqueta(), conteo.getOrDefault(r.getEtiqueta(), 0) + 1);
        }

        double entropia = 0;
        // Solo imprimimos detalle si es el conjunto total (para no saturar)
        // System.out.print("   -> Cálculo Entropía: ");
        
        for (int count : conteo.values()) {
            double p = (double) count / datos.size();
            entropia -= p * (Math.log(p) / Math.log(2));
        }
        // System.out.println(String.format("%.4f", entropia));
        return entropia;
    }

    // Calcular Ganancia (¡Aquí mostramos la magia para el profesor!)
    public double calcularGanancia(List<Registro> datos, String atributo) {
        double entropiaGeneral = calcularEntropia(datos);
        
        Map<String, List<Registro>> subconjuntos = new HashMap<>();
        for (Registro r : datos) {
            String val = r.getValor(atributo);
            subconjuntos.putIfAbsent(val, new ArrayList<>());
            subconjuntos.get(val).add(r);
        }

        double entropiaRestante = 0;
        System.out.println("   [Analizando Atributo: " + atributo + "]");
        
        for (Map.Entry<String, List<Registro>> entry : subconjuntos.entrySet()) {
            double prob = (double) entry.getValue().size() / datos.size();
            double entropiaSub = calcularEntropia(entry.getValue());
            entropiaRestante += prob * entropiaSub;
            
            System.out.println("     - Valor '" + entry.getKey() + "': " + entry.getValue().size() + " registros, Entropía: " + String.format("%.4f", entropiaSub));
        }

        double ganancia = entropiaGeneral - entropiaRestante;
        System.out.println("   => GANANCIA para " + atributo + ": " + String.format("%.4f", ganancia));
        System.out.println("------------------------------------------------");
        return ganancia;
    }

    public Nodo construirArbol(List<Registro> datos, List<String> atributosDisponibles) {
        // Caso Base 1: Todos misma etiqueta
        String primeraEtiqueta = datos.get(0).getEtiqueta();
        boolean todasIguales = datos.stream().allMatch(r -> r.getEtiqueta().equals(primeraEtiqueta));
        if (todasIguales) {
            Nodo hoja = new Nodo(primeraEtiqueta);
            hoja.esHoja = true;
            return hoja;
        }

        // Caso Base 2: Sin atributos
        if (atributosDisponibles.isEmpty()) {
            Nodo hoja = new Nodo(obtenerMayoritaria(datos));
            hoja.esHoja = true;
            return hoja;
        }

        System.out.println("\n*** BUSCANDO MEJOR NODO RAÍZ (Datos: " + datos.size() + ") ***");

        String mejorAtributo = null;
        double maxGanancia = -1;

        for (String atributo : atributosDisponibles) {
            double ganancia = calcularGanancia(datos, atributo);
            if (ganancia > maxGanancia) {
                maxGanancia = ganancia;
                mejorAtributo = atributo;
            }
        }

        if (mejorAtributo == null) {
             Nodo hoja = new Nodo(obtenerMayoritaria(datos));
             hoja.esHoja = true;
             return hoja;
        }

        System.out.println(">>> SELECCIONADO: " + mejorAtributo + " (Ganancia: " + String.format("%.4f", maxGanancia) + ")\n");

        Nodo nodoRaiz = new Nodo(mejorAtributo);
        List<String> nuevosAtributos = new ArrayList<>(atributosDisponibles);
        nuevosAtributos.remove(mejorAtributo);

        Map<String, List<Registro>> divisiones = new HashMap<>();
        for (Registro r : datos) {
            String val = r.getValor(mejorAtributo);
            divisiones.putIfAbsent(val, new ArrayList<>());
            divisiones.get(val).add(r);
        }

        for (Map.Entry<String, List<Registro>> entrada : divisiones.entrySet()) {
            nodoRaiz.agregarHijo(construirArbol(entrada.getValue(), nuevosAtributos), entrada.getKey());
        }

        return nodoRaiz;
    }

    private String obtenerMayoritaria(List<Registro> datos) {
        Map<String, Integer> conteo = new HashMap<>();
        for (Registro r : datos) conteo.put(r.getEtiqueta(), conteo.getOrDefault(r.getEtiqueta(), 0) + 1);
        return Collections.max(conteo.entrySet(), Map.Entry.comparingByValue()).getKey();
    }
}
