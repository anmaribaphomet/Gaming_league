package com.example;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class FrmUpdatePlayers extends JDialog {

    private JTextField txtNombre;
    private JTextField txtApellido;
    private JComboBox<String> cbGenero;
    private JTextField txtDireccion, txtTelefono;
    private JTextField txtCorreo, txtEdad;

    private Runnable onSuccess;
    private Database db;
    private Object playerID;

    public FrmUpdatePlayers(Frame parent, Database db, Object id) {
        super(parent, "Actualizar Player", true);
        this.db = db;
        this.playerID = id;

        JPanel panel = EstiloForm.estiloPanel();

        txtNombre = new JTextField();
        txtApellido = new JTextField();

        cbGenero = new JComboBox<>();
        txtDireccion = new JTextField();
        txtTelefono = new JTextField();
        txtCorreo = new JTextField();
        txtEdad = new JTextField();

        panel.add(new JLabel("Nombre:"));
        panel.add(txtNombre);
        panel.add(new JLabel("Apellido:"));
        panel.add(txtApellido);
        panel.add(new JLabel("Género:"));
        panel.add(cbGenero);
        panel.add(new JLabel("Dirección:"));
        panel.add(txtDireccion);
        panel.add(new JLabel("Teléfono:"));
        panel.add(txtTelefono);
        panel.add(new JLabel("Correo:"));
        panel.add(txtCorreo);
        panel.add(new JLabel("Edad:"));
        panel.add(txtEdad);


        JButton btnActualizar = EstiloForm.crearBoton("Actualizar");
        btnActualizar.addActionListener(e -> actualizar());
        panel.add(new JLabel()); // celda vacía para centrar
        panel.add(EstiloForm.centrar(btnActualizar));

        cargarCombos();
        cargarDatos();

        add(panel);
        pack();
        setLocationRelativeTo(parent);
    }

    public void setOnSuccess(Runnable onSuccess) {
        this.onSuccess = onSuccess;
    }

    private void cargarCombos() {
        cbGenero.addItem("Masculino");
        cbGenero.addItem("Femenino");
        cbGenero.addItem("Otro");
    }


    private void cargarDatos() {
        try {
            ResultSet rs = db.query(
                    "SELECT player_id, first_name, last_name, gender, address, telephone_number, email, age " +
                            "FROM public.players WHERE player_id = " + playerID);

            if (rs.next()) {

                txtNombre.setText(rs.getString("first_name"));
                txtApellido.setText(rs.getString("last_name"));
                cbGenero.setSelectedItem(rs.getString("gender"));
                txtDireccion.setText(rs.getString("address"));
                txtTelefono.setText(rs.getString("telephone_number"));
                txtCorreo.setText(rs.getString("email"));
                txtEdad.setText(rs.getString("age"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void actualizar() {

        String firstName = txtNombre.getText();
        String lastName = txtApellido.getText();
        String gender = cbGenero.getSelectedItem().toString();
        String address = txtDireccion.getText();
        String phone = txtTelefono.getText();
        String email = txtCorreo.getText();
        int age = Integer.parseInt(txtEdad.getText());

        String sql = """
            UPDATE public.players SET
                first_name = ?,
                last_name = ?,
                gender = ?,
                address = ?,
                telephone_number = ?,
                email = ?,
                age = ?
            WHERE player_id = ?
        """;

        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, firstName);
            ps.setString(2, lastName);
            ps.setString(3, gender);
            ps.setString(4, address);
            ps.setString(5, phone);
            ps.setString(6, email);
            ps.setInt(7, age);
            ps.setInt(8, Integer.parseInt(playerID.toString()));

            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Jugador actualizado correctamente");
            if (onSuccess != null) onSuccess.run();
            dispose();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al actualizar: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
