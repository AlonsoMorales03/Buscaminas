/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package buscaminas.modelo;

/**
 *
 * @author Jefferson
 */
public class Estadisticas {
    private int juegosJugados;
    private int juegosGanados;
    private int juegosPerdidos;
 
      // Constructor
    public Estadisticas() {
        this.juegosJugados = 0;
        this.juegosGanados = 0;
        this.juegosPerdidos = 0;
    }
 // Registrar cuando el jugador gana
    public void registrarVictoria() {
        juegosJugados++;
        juegosGanados++;
    }
// Registrar cuando el jugador pierde
    public void registrarDerrota() {
        juegosJugados++;
        juegosPerdidos++;
    }
// Mostrar los datos como texto para JOptionPane
    public String mostrarEstadisticas() {
        return "Juegos Jugados: " + juegosJugados + "\n"
             + "Juegos Ganados: " + juegosGanados + "\n"
             + "Juegos Perdidos: " + juegosPerdidos;
    }
// Métodos opcionales si los necesitás por separado
    public int getJuegosJugados() {
        return juegosJugados;
    }

    public int getJuegosGanados() {
        return juegosGanados;
        
    }
 public int getJuegosPerdidos() {
        return juegosPerdidos;
 }
}
