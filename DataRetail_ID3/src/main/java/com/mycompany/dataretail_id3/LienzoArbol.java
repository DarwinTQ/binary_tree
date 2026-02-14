/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.dataretail_id3;
import javax.swing.JPanel;
import java.awt.*;
import java.awt.geom.CubicCurve2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LienzoArbol extends JPanel {
    private Nodo raiz;
    private Map<Nodo, Point> posiciones;
    private final int ANCHO_NODO = 120;
    private final int ALTO_NODO = 50;
    private final int ESPACIO_VERTICAL = 100;
    private final int ESPACIO_HORIZONTAL = 30;

    public LienzoArbol(Nodo raiz) {
        this.raiz = raiz;
        this.posiciones = new HashMap<>();
        this.setBackground(new Color(245, 247, 250)); // Fondo gris muy suave (tipo moderno)
        
        // Calcular tamaño necesario
        calcularPosiciones();
    }

    // Algoritmo para centrar padres sobre sus hijos
    private void calcularPosiciones() {
        posiciones.clear();
        if (raiz == null) return;

        // 1. Obtener todas las hojas para distribuirlas horizontalmente
        List<Nodo> hojas = new ArrayList<>();
        obtenerHojas(raiz, hojas);

        // 2. Asignar X a las hojas
        int xActual = 50;
        for (Nodo hoja : hojas) {
            // Guardamos temporalmente la X en el mapa (la Y se calcula por profundidad)
            posiciones.put(hoja, new Point(xActual, 0)); 
            xActual += ANCHO_NODO + ESPACIO_HORIZONTAL;
        }

        // 3. Asignar posiciones recursivamente (Post-Orden: hijos primero, luego padres)
        calcularPosicionesRecursivo(raiz, 0);

        // 4. Ajustar el tamaño del panel para que aparezcan las barras de scroll
        int maxAncho = xActual + 50;
        int maxAlto = obtenerProfundidad(raiz) * ESPACIO_VERTICAL + 150;
        setPreferredSize(new Dimension(maxAncho, maxAlto));
        revalidate();
    }

    private void obtenerHojas(Nodo nodo, List<Nodo> hojas) {
        if (nodo.hijos.isEmpty()) {
            hojas.add(nodo);
        } else {
            for (Nodo hijo : nodo.hijos) obtenerHojas(hijo, hojas);
        }
    }

    private int obtenerProfundidad(Nodo nodo) {
        if (nodo.hijos.isEmpty()) return 1;
        int max = 0;
        for (Nodo h : nodo.hijos) max = Math.max(max, obtenerProfundidad(h));
        return 1 + max;
    }

    // Retorna la coordenada X calculada para este nodo
    private int calcularPosicionesRecursivo(Nodo nodo, int nivel) {
        // Si ya tiene posición (es hoja y ya la setea el paso 2), solo actualizamos Y
        if (nodo.hijos.isEmpty()) {
            Point p = posiciones.get(nodo);
            p.y = (nivel * ESPACIO_VERTICAL) + 50;
            return p.x;
        }

        // Si es rama, su X es el promedio de las X de sus hijos
        int sumaX = 0;
        for (Nodo hijo : nodo.hijos) {
            sumaX += calcularPosicionesRecursivo(hijo, nivel + 1);
        }
        int promedioX = sumaX / nodo.hijos.size();
        
        posiciones.put(nodo, new Point(promedioX, (nivel * ESPACIO_VERTICAL) + 50));
        return promedioX;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (raiz == null) return;

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // Dibujar conexiones primero (para que queden detrás de los nodos)
        dibujarConexiones(g2d, raiz);
        // Dibujar nodos después
        dibujarNodos(g2d, raiz);
    }

    private void dibujarConexiones(Graphics2D g2, Nodo nodo) {
        Point pPadre = posiciones.get(nodo);
        
        for (Nodo hijo : nodo.hijos) {
            Point pHijo = posiciones.get(hijo);
            
            // Dibujar curva suave (CubicCurve)
            CubicCurve2D curva = new CubicCurve2D.Float(
                    pPadre.x + ANCHO_NODO/2, pPadre.y + ALTO_NODO, // Inicio (centro abajo padre)
                    pPadre.x + ANCHO_NODO/2, pPadre.y + ALTO_NODO + 40, // Control 1
                    pHijo.x + ANCHO_NODO/2, pHijo.y - 40, // Control 2
                    pHijo.x + ANCHO_NODO/2, pHijo.y // Fin (centro arriba hijo)
            );
            
            g2.setColor(new Color(150, 150, 150));
            g2.setStroke(new BasicStroke(2));
            g2.draw(curva);

            // Dibujar etiqueta de la rama (pequeño recuadro blanco con texto)
            if (hijo.valorRama != null) {
                // Calcular punto medio de la curva (aproximado)
                int midX = (pPadre.x + pHijo.x + ANCHO_NODO) / 2;
                int midY = (pPadre.y + pHijo.y + ALTO_NODO) / 2;
                
                String texto = hijo.valorRama;
                FontMetrics fm = g2.getFontMetrics();
                int textW = fm.stringWidth(texto) + 10;
                
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(midX - textW/2, midY - 10, textW, 20, 5, 5);
                g2.setColor(new Color(100, 100, 100)); // Gris oscuro para borde etiqueta
                g2.drawRoundRect(midX - textW/2, midY - 10, textW, 20, 5, 5);
                
                g2.setColor(new Color(0, 102, 204)); // Azul texto
                g2.setFont(new Font("Segoe UI", Font.BOLD, 11));
                g2.drawString(texto, midX - textW/2 + 5, midY + 4);
            }

            dibujarConexiones(g2, hijo);
        }
    }

    private void dibujarNodos(Graphics2D g2, Nodo nodo) {
        Point p = posiciones.get(nodo);
        
        // Sombra
        g2.setColor(new Color(0, 0, 0, 30));
        g2.fillRoundRect(p.x + 3, p.y + 3, ANCHO_NODO, ALTO_NODO, 15, 15);

        // Color de fondo según si es Hoja o Rama
        GradientPaint gradiente;
        if (nodo.esHoja) {
            // Verde suave para resultados (SI/NO)
            Color inicio = nodo.nombre.equalsIgnoreCase("SI") ? new Color(168, 230, 207) : new Color(255, 179, 179);
            Color fin = nodo.nombre.equalsIgnoreCase("SI") ? new Color(220, 237, 193) : new Color(255, 204, 204);
            gradiente = new GradientPaint(p.x, p.y, inicio, p.x, p.y + ALTO_NODO, fin);
        } else {
            // Azul/Celeste profesional para atributos
            gradiente = new GradientPaint(p.x, p.y, new Color(224, 247, 250), p.x, p.y + ALTO_NODO, new Color(178, 235, 242));
        }
        
        g2.setPaint(gradiente);
        g2.fillRoundRect(p.x, p.y, ANCHO_NODO, ALTO_NODO, 15, 15);

        // Borde
        g2.setColor(new Color(100, 110, 120));
        g2.setStroke(new BasicStroke(1));
        g2.drawRoundRect(p.x, p.y, ANCHO_NODO, ALTO_NODO, 15, 15);

        // Texto
        String texto = nodo.nombre;
        g2.setFont(new Font("Segoe UI", Font.BOLD, 13));
        FontMetrics fm = g2.getFontMetrics();
        int textW = fm.stringWidth(texto);
        
        g2.setColor(new Color(50, 50, 50));
        g2.drawString(texto, p.x + (ANCHO_NODO - textW)/2, p.y + (ALTO_NODO + 5)/2);

        for (Nodo hijo : nodo.hijos) dibujarNodos(g2, hijo);
    }
}