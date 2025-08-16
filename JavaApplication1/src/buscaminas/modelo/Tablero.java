/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package buscaminas.modelo;

import java.util.Random;

/**
 *
 * @author Alonso
 */
public class Tablero {
    private Casilla[][] casillas;
    private int tamaño;
    private int cantidadMinas;

    public Tablero(int tamaño) {
        this.tamaño = tamaño;
        this.cantidadMinas = 2 * tamaño;
        casillas = new Casilla[tamaño][tamaño];
        inicializarCasillas();
        colocarMinasAleatoriamente();
        calcularMinasCerca();
    }

    private void inicializarCasillas() {
        for (int i = 0; i < tamaño; i++) {
            for (int j = 0; j < tamaño; j++) {
                casillas[i][j] = new Casilla();
            }
        }
    }

    private void colocarMinasAleatoriamente() {
           Random random = new Random();
        int minasColocadas = 0;

        while (minasColocadas < cantidadMinas) {
            int fila = random.nextInt(tamaño);
            int columna = random.nextInt(tamaño);

            if (!casillas[fila][columna].tieneMina()) {
                casillas[fila][columna].setMina(true);
                minasColocadas++;
            }
        }
    }

    private void calcularMinasCerca() {
        for (int fila = 0; fila < tamaño; fila++) {
            for (int col = 0; col < tamaño; col++) {
                if (!casillas[fila][col].tieneMina()) {
                    int minasCercanas = contarMinasVecinas(fila, col);
                    casillas[fila][col].setMinasCerca(minasCercanas);
                }
            }
        }
    }

    private int contarMinasVecinas(int fila, int col) {
        int contador = 0;

        for (int i = fila - 1; i <= fila + 1; i++) {
            for (int j = col - 1; j <= col + 1; j++) {
                if (i >= 0 && i < tamaño && j >= 0 && j < tamaño) {
                    if (casillas[i][j].tieneMina()) {
                        contador++;
                    }
                }
            }
        }

        return contador;
    }

    // Getters
    public Casilla getCasilla(int fila, int col) {
        return casillas[fila][col];
    }

    public int getTamaño() {
        return tamaño;
    }

    public int getCantidadMinas() {
        return cantidadMinas;
    }
}
