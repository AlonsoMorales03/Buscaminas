/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package buscaminas.modelo;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Alonso (;
 */
public class UsuarioRepositorio {
   
    private final File archivo;

    public UsuarioRepositorio() {
        File dataDir = new File("data");
        if (!dataDir.exists()) dataDir.mkdirs();
        this.archivo = new File(dataDir, "usuarios.csv");
        if (!archivo.exists()) {
            try (PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(archivo, true), StandardCharsets.UTF_8))) {
                pw.println("id,nombre,email"); // encabezado
            } catch (IOException ignored) {}
        }
    }

    public synchronized List<Usuario> listar() {
        List<Usuario> lista = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(archivo), StandardCharsets.UTF_8))) {
            String linea;
            boolean first = true;
            while ((linea = br.readLine()) != null) {
                if (first) { first = false; continue; } // saltar encabezado
                if (linea.trim().isEmpty()) continue;
                String[] p = linea.split(",", -1);
                if (p.length >= 3) {
                    int id = Integer.parseInt(p[0].trim());
                    String nombre = p[1].trim();
                    String email = p[2].trim();
                    lista.add(new Usuario(id, nombre, email));
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return lista;
    }

    public synchronized Usuario guardar(Usuario u) {
        // Generar ID siguiente
        int nextId = 1;
        for (Usuario x : listar()) nextId = Math.max(nextId, x.getId() + 1);
        u.setId(nextId);

        try (PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(archivo, true), StandardCharsets.UTF_8))) {
            String nombreEsc = u.getNombre().replace(",", " "); // evitar romper CSV con comas
            String emailEsc = u.getEmail().replace(",", " ");
            pw.println(u.getId() + "," + nombreEsc + "," + emailEsc);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return u;
    }

    public boolean existePorNombreEmail(String nombre, String email) {
        String n = nombre == null ? "" : nombre.trim().toLowerCase();
        String e = email == null ? "" : email.trim().toLowerCase();
        for (Usuario u : listar()) {
            if (u.getNombre().toLowerCase().equals(n) && u.getEmail().toLowerCase().equals(e)) {
                return true;
            }
        }
        return false;
    }
    public Usuario buscarPorNombreEmail(String nombre, String email) {
    String n = nombre == null ? "" : nombre.trim().toLowerCase();
    String e = email == null ? "" : email.trim().toLowerCase();
    for (Usuario u : listar()) {
        if (u.getNombre().toLowerCase().equals(n) && u.getEmail().toLowerCase().equals(e)) {
            return u;
        }
    }
    return null;
}

public List<String> listarNombres() {
    List<String> nombres = new ArrayList<>();
    for (Usuario u : listar()) {
        if (!nombres.contains(u.getNombre())) {
            nombres.add(u.getNombre());
        }
    }
    return nombres;
}

}

