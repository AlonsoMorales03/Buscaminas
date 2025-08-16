package buscaminas.modelo;

public class UsuarioService {
    private final UsuarioRepositorio repo = new UsuarioRepositorio();

    public Usuario agregarUsuario(String nombre, String email) throws IllegalArgumentException {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio.");
        }
        String n = nombre.trim();
        String e = email == null ? "" : email.trim();

        if (n.length() < 3) {
            throw new IllegalArgumentException("El nombre debe tener al menos 3 caracteres.");
        }
        if (!e.isEmpty() && !e.contains("@")) {
            throw new IllegalArgumentException("El email no es válido (opcional, pero si lo pones debe tener @).");
        }
        if (repo.existePorNombreEmail(n, e)) {
            throw new IllegalArgumentException("Ya existe un usuario con ese nombre y email.");
        }
        Usuario u = new Usuario(0, n, e);
        return repo.guardar(u);
    }
    public Usuario autenticar(String nombre, String email) {
    String n = nombre == null ? "" : nombre.trim();
    String e = email == null ? "" : email.trim();
    UsuarioRepositorio repo = new UsuarioRepositorio();
    Usuario u = repo.buscarPorNombreEmail(n, e);
    if (u == null) {
        throw new IllegalArgumentException("Usuario no encontrado. Verifica nombre y email.");
    }
    return u;
}

public Usuario crearYEntrar(String nombre, String email) {
    Usuario u = agregarUsuario(nombre, email); // valida y guarda
    if (u == null) throw new IllegalStateException("No se pudo crear el usuario.");
    return u;
}

}
