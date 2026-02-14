/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.dataretail_id3;
import java.util.HashMap;
import java.util.Map;
/**
 *
 * @author darwi
 */
public class Registro {
    private Map<String, String> atributos;
    private String etiqueta;

    public Registro(String etiqueta) {
        this.atributos = new HashMap<>();
        this.etiqueta = etiqueta;
    }

    public void agregarAtributo(String nombre, String valor) {
        atributos.put(nombre, valor);
    }

    public String getValor(String nombreAtributo) {
        return atributos.get(nombreAtributo);
    }

    public String getEtiqueta() {
        return etiqueta;
    }
}
