
package buscaminas.modelo;

/**
 *
 * @author Jefferson
 */

public class Casilla {
    private boolean tieneMina;
    private boolean descubierta;
    private boolean marcada;
    private int minasCerca;

    public Casilla() {
        this.tieneMina = false;
        this.descubierta = false;
        this.marcada = false;
        this.minasCerca = 0;
    }
 // Getters y Setters
    public boolean tieneMina() {
        return tieneMina;
    }
public void setMina(boolean mina) {
        this.tieneMina = mina;
    }
public boolean estaDescubierta() {
        return descubierta;
    }
 public void setDescubierta(boolean descubierta) {
        this.descubierta = descubierta;
    }
 public boolean estaMarcada() {
        return marcada;
    }

public void setMarcada(boolean marcada) {
        this.marcada = marcada;
    }
 public int getMinasCerca() {
        return minasCerca;
    }
 public void setMinasCerca(int minasCerca) {
        this.minasCerca = minasCerca;
    }
}
