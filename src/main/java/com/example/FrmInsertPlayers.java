package com.example;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;


public class FrmInsertPlayers extends JDialog {

    private JTextField txtNombre;
    private JTextField txtApellido;
    private JComboBox<String> cbGenero;
    private JTextField txtDireccion, txtTelefono;
    private JTextField txtCorreo, txtEdad;

    private Database db;
    private Runnable onSuccess;

    public FrmInsertPlayers(Frame parent, Database db) {
        super(parent, "Insertar Player", true);
        this.db = db;

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


        JButton btnInsertar = EstiloForm.crearBoton("Insertar");
        btnInsertar.addActionListener(e -> insertar());
        panel.add(new JLabel()); // celda vacía para centrar
        panel.add(EstiloForm.centrar(btnInsertar));


        cargarCombos();
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

    private void insertar() {

        String firstName = txtNombre.getText();
        String lastName = txtApellido.getText();
        String gender = cbGenero.getSelectedItem().toString();
        String address = txtDireccion.getText();
        String phone = txtTelefono.getText();
        String email = txtCorreo.getText();
        int age = Integer.parseInt(txtEdad.getText());

        String sql = """
        INSERT INTO public.players 
            (first_name, last_name, gender, address, telephone_number, email, age)
        VALUES ( ?, ?, ?, ?, ? , ? , ?)
    """;

        Connection conn = null;

        try {
            conn = db.getConnection();
            conn.setAutoCommit(false); // INICIA TRANSACCIÓN

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, firstName);
            ps.setString(2, lastName);
            ps.setString(3, gender);
            ps.setString(4, address);
            ps.setString(5, phone);
            ps.setString(6, email);
            ps.setInt(7, age);

            ps.executeUpdate();

            conn.commit();

            JOptionPane.showMessageDialog(this, "Insertado correctamente");
            if (onSuccess != null) {
                onSuccess.run();
            }
            dispose();

        } catch (Exception e) {

            try {
                if (conn != null) conn.rollback();
            } catch (Exception ex2) {
                ex2.printStackTrace();
            }

            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            e.printStackTrace();

        } finally {
            try {
                if (conn != null) conn.setAutoCommit(true);
            } catch (Exception ex3) {
                ex3.printStackTrace();
            }
        }
    }
}

