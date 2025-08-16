/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package buscaminas.vista;
import buscaminas.modelo.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import buscaminas.modelo.UsuarioService;
import buscaminas.modelo.Usuario;

/**
 *
 * @author Alonso
 */
public class FrmJuego extends javax.swing.JFrame {
     private final UsuarioService usuarioService = new UsuarioService();
     private Usuario usuarioActual = null; // <- usuario logueado
     private int tamaño;
     private JButton[][] botones;
     private Juego juego;
     private Estadisticas estadisticas = new Estadisticas();


     
private Usuario mostrarDialogoAgregarUsuario() {
    JPanel panel = new JPanel(new java.awt.GridLayout(0,1,6,6));
    JTextField txtNombre = new JTextField();
    JTextField txtEmail  = new JTextField();

    panel.add(new JLabel("Nombre (obligatorio):"));
    panel.add(txtNombre);
    panel.add(new JLabel("Email (opcional):"));
    panel.add(txtEmail);

    int r = JOptionPane.showConfirmDialog(
            this, panel, "Crear usuario",
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

    if (r == JOptionPane.OK_OPTION) {
        try {
            Usuario u = usuarioService.crearYEntrar(txtNombre.getText(), txtEmail.getText());
            return u; // <- lo retornamos para “entrar” de una vez
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validación", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar el usuario.", "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    return null;
}

private Usuario mostrarDialogoLogin() {
    JPanel panel = new JPanel(new java.awt.GridLayout(0,1,6,6));
    JTextField txtNombre = new JTextField();
    JTextField txtEmail  = new JTextField();

    panel.add(new JLabel("Nombre:"));
    panel.add(txtNombre);
    panel.add(new JLabel("Email (si el usuario tiene email registrado):"));
    panel.add(txtEmail);

    int r = JOptionPane.showConfirmDialog(
            this, panel, "Iniciar sesión",
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

    if (r == JOptionPane.OK_OPTION) {
        try {
            Usuario u = usuarioService.autenticar(txtNombre.getText(), txtEmail.getText());
            return u;
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Acceso", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al iniciar sesión.", "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    return null;
}

private void actualizarTituloConUsuario() {
    if (usuarioActual != null) {
        setTitle("Buscaminas - Usuario: " + usuarioActual.getNombre());
    } else {
        setTitle("Buscaminas");
    }
}


private void iniciarJuego() {
    while (true) {
        try {
            String input = JOptionPane.showInputDialog(this, "Ingrese el tamaño del tablero (mayor a 2):");
            if (input == null) System.exit(0); // Salir si cancela
            tamaño = Integer.parseInt(input);
            if (tamaño > 2) break;
            JOptionPane.showMessageDialog(this, "Debe ingresar un número mayor a 2.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Número inválido");
        }
         }
juego = new Juego(tamaño);
    crearTablero();
    mostrarEstadisticas();
    
    
}
private void crearTablero() {
    panelTablero.removeAll();
    panelTablero.setLayout(new GridLayout(tamaño, tamaño));
    botones = new JButton[tamaño][tamaño];
    
    for (int i = 0; i < tamaño; i++) {
        for (int j = 0; j < tamaño; j++) {
            JButton btn = new JButton();
            btn.setPreferredSize(new Dimension(40, 40));
            int fila = i;
            int col = j;

            btn.addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    if (SwingUtilities.isLeftMouseButton(e)) {
                        manejarDestapar(fila, col);
                    } else if (SwingUtilities.isRightMouseButton(e)) {
                        manejarMarcarDesmarcar(fila, col);
                    }
                }
            });

            botones[i][j] = btn;
            panelTablero.add(btn);
        }
    }

    panelTablero.revalidate();
    panelTablero.repaint();
}
private void manejarDestapar(int fila, int col) {
    if (juego.estaTerminado()) return;

    juego.destapar(fila, col);
    actualizarBotones();

    if (juego.jugadorPerdio()) {
        juego.revelarMinas();
        actualizarBotones();
        estadisticas.registrarDerrota();
        JOptionPane.showMessageDialog(this, "¡Perdiste!", "Fin", JOptionPane.ERROR_MESSAGE);
        preguntarReiniciar();
    } else if (juego.estaTerminado()) {
        estadisticas.registrarVictoria();
        JOptionPane.showMessageDialog(this, "¡Ganaste!", "Fin", JOptionPane.INFORMATION_MESSAGE);
        preguntarReiniciar();
    }
}

private void manejarMarcarDesmarcar(int fila, int col) {
    Casilla c = juego.getTablero().getCasilla(fila, col);
    if (!c.estaDescubierta()) {
        if (c.estaMarcada()) juego.desmarcar(fila, col);
        else juego.marcar(fila, col);
        actualizarBotones();
    }
    if (juego.estaTerminado() && !juego.jugadorPerdio()) {
    estadisticas.registrarVictoria();
    JOptionPane.showMessageDialog(this, "¡Ganaste!", "Fin", JOptionPane.INFORMATION_MESSAGE);
    preguntarReiniciar();
}

}
private void actualizarBotones() {
    for (int i = 0; i < tamaño; i++) {
        for (int j = 0; j < tamaño; j++) {
            Casilla c = juego.getTablero().getCasilla(i, j);
            JButton b = botones[i][j];

            if (c.estaDescubierta()) {
                if (c.tieneMina()) {
                    b.setText("💣");
                    b.setBackground(Color.RED);
                } else {
                    int n = c.getMinasCerca();
                    b.setText(n > 0 ? String.valueOf(n) : "");
                    b.setEnabled(false);
                    b.setBackground(Color.LIGHT_GRAY);
                }
            } else if (c.estaMarcada()) {
                b.setText("🚩");
            } else {
                b.setText("");
            }
        }
    }
}

private void mostrarEstadisticas() {
    JOptionPane.showMessageDialog(this, estadisticas.mostrarEstadisticas(), "Estadísticas", JOptionPane.INFORMATION_MESSAGE);
}

private void preguntarReiniciar() {
    int opcion = JOptionPane.showConfirmDialog(this, "¿Desea jugar de nuevo?", "Reiniciar", JOptionPane.YES_NO_OPTION);
    if (opcion == JOptionPane.YES_OPTION) {
        iniciarJuego();
    } else {
        System.exit(0);
    }
}
private void agregarMenuUsuarios() {
    javax.swing.JMenu menuUsuarios = new javax.swing.JMenu("Usuarios");
    javax.swing.JMenuItem itemAgregar = new javax.swing.JMenuItem("Agregar usuario...");
    javax.swing.JMenuItem itemLogin  = new javax.swing.JMenuItem("Iniciar sesión...");
    javax.swing.JMenuItem itemCambiar = new javax.swing.JMenuItem("Cambiar de usuario...");

    itemAgregar.addActionListener(e -> {
        Usuario u = mostrarDialogoAgregarUsuario(); // ahora devuelve el usuario creado (o null)
        if (u != null) {
            usuarioActual = u;
            JOptionPane.showMessageDialog(this, "Sesión iniciada como: " + usuarioActual.getNombre());
        }
    });

    itemLogin.addActionListener(e -> {
        Usuario u = mostrarDialogoLogin();
        if (u != null) {
            usuarioActual = u;
            JOptionPane.showMessageDialog(this, "Sesión iniciada como: " + usuarioActual.getNombre());
        }
    });

    itemCambiar.addActionListener(e -> flujoInicioSesion());

    menuUsuarios.add(itemAgregar);
    menuUsuarios.add(itemLogin);
    menuUsuarios.add(itemCambiar);

    javax.swing.JMenuBar mb = getJMenuBar();
    if (mb == null) {
        mb = new javax.swing.JMenuBar();
        setJMenuBar(mb);
    }
    mb.add(menuUsuarios);
}

private void flujoInicioSesion() {
    while (usuarioActual == null) {
        Object[] opciones = {"Iniciar sesión", "Crear usuario", "Salir"};
        int r = JOptionPane.showOptionDialog(
                this,
                "Bienvenido. ¿Qué deseas hacer?",
                "Acceso",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]
        );

        if (r == 0) { // Iniciar sesión
            Usuario u = mostrarDialogoLogin();
            if (u != null) {
                usuarioActual = u;
                JOptionPane.showMessageDialog(this, "Sesión iniciada como: " + usuarioActual.getNombre());
            }
        } else if (r == 1) { // Crear usuario
            Usuario u = mostrarDialogoAgregarUsuario(); // crea y entra
            if (u != null) {
                usuarioActual = u;
                JOptionPane.showMessageDialog(this, "Usuario creado e inicio de sesión como: " + usuarioActual.getNombre());
            }
        } else { // Salir o cerrar
            System.exit(0);
        }
    }
}

private void solicitarUsuarioInicial() {
    while (true) {
        String nombre = JOptionPane.showInputDialog(this, "Ingresa tu nombre de usuario:");
        if (nombre == null) break; // cancelar
        nombre = nombre.trim();
        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre no puede estar vacío.");
            continue;
        }
        try {
            usuarioService.agregarUsuario(nombre, ""); // sin email
            JOptionPane.showMessageDialog(this, "¡Bienvenido, " + nombre + "!");
            break;
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validación", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar el usuario.", "Error", JOptionPane.ERROR_MESSAGE);
            break;
        }
    }
}

     
    public FrmJuego() {
        initComponents();
        agregarMenuUsuarios();
        flujoInicioSesion();        // <-- obliga a entrar/crear usuario
        if (usuarioActual != null) {
        actualizarTituloConUsuario();
        iniciarJuego();         // <-- empieza el Buscaminas solo con sesión activa
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelTablero = new javax.swing.JPanel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu3 = new javax.swing.JMenu();
        jMenuItemNuevo = new javax.swing.JMenuItem();
        jMenuItemSalir = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout panelTableroLayout = new javax.swing.GroupLayout(panelTablero);
        panelTablero.setLayout(panelTableroLayout);
        panelTableroLayout.setHorizontalGroup(
            panelTableroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        panelTableroLayout.setVerticalGroup(
            panelTableroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 277, Short.MAX_VALUE)
        );

        jMenu3.setText("Juego");

        jMenuItemNuevo.setText("Juego Nuevo");
        jMenuItemNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemNuevoActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItemNuevo);

        jMenuItemSalir.setText("Salir");
        jMenuItemSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemSalirActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItemSalir);

        jMenuBar1.add(jMenu3);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelTablero, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelTablero, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItemSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemSalirActionPerformed
        System.exit(0);
    }//GEN-LAST:event_jMenuItemSalirActionPerformed

    private void jMenuItemNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemNuevoActionPerformed
        iniciarJuego();
    }//GEN-LAST:event_jMenuItemNuevoActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FrmJuego.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrmJuego.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrmJuego.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrmJuego.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FrmJuego().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItemNuevo;
    private javax.swing.JMenuItem jMenuItemSalir;
    private javax.swing.JPanel panelTablero;
    // End of variables declaration//GEN-END:variables
}
