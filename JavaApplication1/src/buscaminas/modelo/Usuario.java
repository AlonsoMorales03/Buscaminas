package buscaminas.modelo;

import java.util.Objects;

public class Usuario {
    private int id;
    private String nombre;
    private String email;

    public Usuario(int id, String nombre, String email) {
        this.id = id;
        this.nombre = nombre != null ? nombre.trim() : "";
        this.email = email != null ? email.trim() : "";
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getEmail() { return email; }

    public void setId(int id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre != null ? nombre.trim() : ""; }
    public void setEmail(String email) { this.email = email != null ? email.trim() : ""; }

    @Override
    public String toString() {
        return "Usuario{id=" + id + ", nombre='" + nombre + "', email='" + email + "'}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Usuario)) return false;
        Usuario usuario = (Usuario) o;
        // Consideramos único por nombre+email
        return Objects.equals(nombre.toLowerCase(), usuario.nombre.toLowerCase())
            && Objects.equals(email.toLowerCase(), usuario.email.toLowerCase());
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre.toLowerCase(), email.toLowerCase());
    }
}

