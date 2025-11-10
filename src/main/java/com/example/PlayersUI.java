package com.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PlayersUI extends JFrame {

    private final Database db;
    private final Players playersLogic;

    private final DefaultTableModel tableModel;
    private final JTable table;

    // Campos del formulario, casillas las cuales el usuario va a llenar para insertar datos o updatearlos
    private final JTextField tfPlayerId = new JTextField(10);
    private final JTextField tfFirstName = new JTextField(15);
    private final JTextField tfLastName = new JTextField(15);
    private final JTextField tfGender = new JTextField(5);
    private final JTextField tfAddress = new JTextField(20);
    private final JTextField tfTelephone = new JTextField(12);
    private final JTextField tfEmail = new JTextField(20);
    private final JTextField tfAge = new JTextField(4);

    public PlayersUI(Database db) {
        super("Players - CRUD");//nombre
        this.db = db;//referencia de l db
        this.playersLogic = new Players(db);//

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);//el boton de salir funcional
        setSize(900, 600);//tamanio
        setLocationRelativeTo(null);//centrado
        setLayout(new BorderLayout(8, 8));

        // Tabla y modelo
        String[] columns = {"player_id", "first_name", "last_name", "gender", "address", "telephone", "email", "age"};
        tableModel = new DefaultTableModel(columns, 0) { //se crea la tabla con los nombres que le pase con el string
            // Hacemos todas las celdas no editables directamente, si no con el formulario
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);//crea la tabla
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);//solo se puede seleccionar una fila

        JScrollPane scroll = new JScrollPane(table);//panel con  cosito para poder desplazarse
        add(scroll, BorderLayout.CENTER);//coloca la tabla dentro del scroll al centro de la ventana

        // Panel formulario abajo
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4, 4, 4, 4);
        c.anchor = GridBagConstraints.WEST;

        int row = 0;
        // First row: player_id solo lectura + first/last name
        tfPlayerId.setEditable(false); // como se incrementa solo , no editable al insertar
        addToForm(form, c, row++, "ID:", tfPlayerId, "First name:", tfFirstName, "Last name:", tfLastName);

        // Second row: gender, age, telephone
        addToForm(form, c, row++, "Gender:", tfGender, "Age:", tfAge, "Telephone:", tfTelephone);

        // Third row: address (wide)
        addToForm(form, c, row++, "Address:", tfAddress, null, null, "Email:", tfEmail);

        add(form, BorderLayout.SOUTH);

        // Panel de botones derecha seran las acciones CRUD
        JPanel buttons = new JPanel(new GridLayout(4, 1, 6, 6));
        JButton btnSelect = new JButton("Select");
        JButton btnInsert = new JButton("Insert");
        JButton btnUpdate = new JButton("Update");
        JButton btnDelete = new JButton("Delete");

        buttons.add(btnSelect);
        buttons.add(btnInsert);
        buttons.add(btnUpdate);
        buttons.add(btnDelete);

        add(buttons, BorderLayout.EAST);//indica que se colocan los botones en la parte derecha de la ventana

        // Eventos
        // activa el select para que aparezca la tabla apenas se abre la ventana
        SwingUtilities.invokeLater(this::cargartablaplayers);

        // Cuando seleccionen fila, llenar formulario con sus datos es como el SelectedRow del c#,
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) llenarSelectedRow();
        });

        btnSelect.addActionListener(e -> cargartablaplayers());//si se selecciona el boton select carga la tabla (refresca)
       // btnInsert.addActionListener(e -> insertarBackground());// es el evento de los otros pero aun no existen
       // btnUpdate.addActionListener(e -> actualizarBackground());
       // btnDelete.addActionListener(e -> eliminarBackground());
    }

    // Helper para layout del form (3 pares por fila)
    private void addToForm(JPanel panel, GridBagConstraints c, int row,
                           String label1, JComponent comp1,
                           String label2, JComponent comp2,
                           String label3, JComponent comp3) {
        c.gridy = row;
        c.gridx = 0;
        if (label1 != null) panel.add(new JLabel(label1), c);
        c.gridx = 1;
        if (comp1 != null) panel.add(comp1, c);

        c.gridx = 2;
        if (label2 != null) panel.add(new JLabel(label2), c);
        c.gridx = 3;
        if (comp2 != null) panel.add(comp2, c);

        c.gridx = 4;
        if (label3 != null) panel.add(new JLabel(label3), c);
        c.gridx = 5;
        if (comp3 != null) panel.add(comp3, c);
    }

    // meotod para Llenar formulario con fila seleccionada, me lo saque de internet XD
    private void llenarSelectedRow() {
        int sel = table.getSelectedRow();
        if (sel == -1) return;
        tfPlayerId.setText(String.valueOf(tableModel.getValueAt(sel, 0)));
        tfFirstName.setText(String.valueOf(tableModel.getValueAt(sel, 1)));
        tfLastName.setText(String.valueOf(tableModel.getValueAt(sel, 2)));
        tfGender.setText(String.valueOf(tableModel.getValueAt(sel, 3)));
        tfAddress.setText(String.valueOf(tableModel.getValueAt(sel, 4)));
        tfTelephone.setText(String.valueOf(tableModel.getValueAt(sel, 5)));
        tfEmail.setText(String.valueOf(tableModel.getValueAt(sel, 6)));
        tfAge.setText(String.valueOf(tableModel.getValueAt(sel, 7)));
    }

    // Metodo que carga la tabla de players
    private void cargartablaplayers() {
        new Thread(() -> {
            try {
                List<String[]> rows = playersLogic.listarPlayers();//llama al metodo en players
                SwingUtilities.invokeLater(() -> {
                    tableModel.setRowCount(0);
                    for (String[] r : rows) {
                        tableModel.addRow(r);
                    }
                });
            } catch (Exception ex) {
                ex.printStackTrace();
                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this,
                        "Error al obtener datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE));
            }
        }).start();
    }

}
