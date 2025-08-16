/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package buscaminas.modelo;

/**
 *
 * @author Alonso
 */
public class Juego {
    private Tablero tablero;
    private boolean juegoTerminado;
    private boolean jugadorPerdio;
    private int marcadasCorrectamente;

    public Juego(int tamaño) {
        this.tablero = new Tablero(tamaño);
        this.juegoTerminado = false;
        this.jugadorPerdio = false;
        this.marcadasCorrectamente = 0;
    }

    public Tablero getTablero() {
        return tablero;
    }

    public boolean estaTerminado() {
        return juegoTerminado;
    }

    public boolean jugadorPerdio() {
        return jugadorPerdio;
    }

    public boolean destapar(int fila, int col) {
        Casilla casilla = tablero.getCasilla(fila, col);

        if (casilla.estaDescubierta() || casilla.estaMarcada()) return false;

        casilla.setDescubierta(true);

        if (casilla.tieneMina()) {
            juegoTerminado = true;
            jugadorPerdio = true;
            return false;
        }

        if (casilla.getMinasCerca() == 0) {
            destaparVecinos(fila, col);
        }

        verificarVictoria();
        return true;
    }

    private void destaparVecinos(int fila, int col) {
        for (int i = fila - 1; i <= fila + 1; i++) {
            for (int j = col - 1; j <= col + 1; j++) {
                if (i >= 0 && i < tablero.getTamaño() && j >= 0 && j < tablero.getTamaño()) {
                    Casilla vecino = tablero.getCasilla(i, j);
                    if (!vecino.estaDescubierta() && !vecino.tieneMina()) {
                        vecino.setDescubierta(true);
                        if (vecino.getMinasCerca() == 0) {
                            destaparVecinos(i, j);
                        }
                    }
                }
            }
        }
    }

    public void marcar(int fila, int col) {
        Casilla casilla = tablero.getCasilla(fila, col);
        if (!casilla.estaDescubierta() && !casilla.estaMarcada()) {
            casilla.setMarcada(true);
            if (casilla.tieneMina()) {
                marcadasCorrectamente++;
            }
            verificarVictoria();
        }
    }

    public void desmarcar(int fila, int col) {
        Casilla casilla = tablero.getCasilla(fila, col);
        if (casilla.estaMarcada()) {
            casilla.setMarcada(false);
            if (casilla.tieneMina()) {
                marcadasCorrectamente--;
            }
        }
    }

    private void verificarVictoria() {
        if (marcadasCorrectamente == tablero.getCantidadMinas()) {
            juegoTerminado = true;
        }
    }

    public void revelarMinas() {
        for (int i = 0; i < tablero.getTamaño(); i++) {
            for (int j = 0; j < tablero.getTamaño(); j++) {
                Casilla c = tablero.getCasilla(i, j);
                if (c.tieneMina()) {
                    c.setDescubierta(true);
                }
            }
        }
    }
}
