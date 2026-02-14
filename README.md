# DataRetail Analytics - Sistema de Árboles de Decisión ID3

## 📋 Descripción
Este proyecto es la solución al Trabajo Final del curso **"Artificial Intelligence With Machine Learning In Java"**. 

El sistema implementa el algoritmo **ID3 (Iterative Dichotomiser 3)** para generar un árbol de decisión basado en datos históricos de ventas. Su objetivo es ayudar a la empresa *DataRetail Analytics* a decidir automáticamente si un producto debe ser promocionado o no, maximizando las ventas y reduciendo el sobrestock.

## 🚀 Características Principales
* **Algoritmo ID3 Puro:** Implementación desde cero en Java (sin librerías externas de ML).
* **Cálculos Visibles:** Muestra en consola el cálculo detallado de **Entropía** y **Ganancia de Información** para cada atributo.
* **Visualización Gráfica (GUI):** Dibuja el árbol de decisión generado utilizando **Java Swing**, con un diseño moderno, nodos coloreados y conexiones curvas.
* **Carga de Datos Dinámica:** Lee los registros desde un archivo `datos.txt` (CSV) permitiendo analizar grandes volúmenes de datos (probado con 100+ registros).
* **Validación Estructural:** Incluye recorridos en **Pre-Orden** y **Post-Orden** para verificar la lógica del árbol.

## 🛠️ Tecnologías Utilizadas
* **Lenguaje:** Java (JDK 8 o superior).
* **IDE Recomendado:** Apache NetBeans.
* **Interfaz:** Java Swing & AWT (Graphics2D).
* **Gestión de Archivos:** E/S estándar de Java (FileReader/BufferedReader).

## 📂 Estructura del Proyecto
El código está organizado en las siguientes clases:

1.  **`Main.java`**: Punto de entrada. Carga el archivo `.txt`, ejecuta el algoritmo y lanza la ventana gráfica.
2.  **`ID3.java`**: El "cerebro" matemático. Contiene la lógica de entropía, ganancia y la recursividad para construir el árbol.
3.  **`Nodo.java`**: Estructura de datos que representa cada punto de decisión (o hoja) del árbol.
4.  **`Registro.java`**: Modelo de datos para representar cada fila del historial de ventas.
5.  **`LienzoArbol.java`**: Componente gráfico personalizado que dibuja el árbol visualmente.

## ⚙️ Cómo Ejecutar
1.  Abre el proyecto en **NetBeans**.
2.  Asegúrate de que el archivo **`datos.txt`** esté en la carpeta raíz del proyecto (al mismo nivel que la carpeta `src` o el `pom.xml`), NO dentro de `src`.
3.  Ejecuta la clase **`Main.java`**.
4.  Observa la consola para ver los cálculos matemáticos y la estructura en texto.
5.  Se abrirá automáticamente una ventana con el gráfico del árbol de decisión.

## 📊 Formato del Archivo `datos.txt`
El archivo debe seguir este formato CSV simple (sin espacios extra después de las comas):

```text
VentasPrevias,Precio,Temporada,Promocionar
Alta,Alto,Invierno,NO
Media,Bajo,Verano,SI
...
