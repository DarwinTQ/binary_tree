/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.dataretail_id3;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author darwi
 */
public class Nodo {
    public String nombre; 
    public List<Nodo> hijos;
    public String valorRama; 
    public boolean esHoja;

    public Nodo(String nombre) {
        this.nombre = nombre;
        this.hijos = new ArrayList<>();
        this.esHoja = false;
    }

    public void agregarHijo(Nodo hijo, String valorRama) {
        hijo.valorRama = valorRama;
        this.hijos.add(hijo);
    }

    public void imprimirPreOrden(String prefijo) {
        System.out.println(prefijo + (valorRama != null ? "[" + valorRama + "] -> " : "") + nombre);
        for (Nodo hijo : hijos) {
            hijo.imprimirPreOrden(prefijo + "  ");
        }
    }

    public void imprimirPostOrden() {
        for (Nodo hijo : hijos) {
            hijo.imprimirPostOrden();
        }
        System.out.println((valorRama != null ? "[" + valorRama + "] -> " : "") + nombre);
    }
}
