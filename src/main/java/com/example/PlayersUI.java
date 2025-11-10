package com.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;//para el Jtable
import java.awt.*;
import java.util.List;

public class PlayersUI extends JFrame {

    private final Database db;//usa a db para la conexion
    private final Players playersclase;//usa la clase players para los CRUD

    private final DefaultTableModel tablaPlayer;//estructuraa y filas (es la tabla)
    private final JTable tabla;//es el disenio de la tabla

    // Campos del formulario, casillas las cuales el usuario va a llenar para insertar datos o updatearlos
    private final JTextField tfPlayerId = new JTextField(10);//el numero que se le manda es el ancho del campo de texto
    private final JTextField tfFirstName = new JTextField(15);
    private final JTextField tfLastName = new JTextField(15);
    private final JTextField tfGender = new JTextField(5);
    private final JTextField tfAddress = new JTextField(20);
    private final JTextField tfTelephone = new JTextField(12);
    private final JTextField tfEmail = new JTextField(20);
    private final JTextField tfAge = new JTextField(4);

    //es la clase de "mezcla" osea une los demas metodos dandole los dtos necesarios y ya los aplica en la ventana
    public PlayersUI(Database db) {
        super("Players - CRUD");//nombre
        this.db = db;//referencia de l db
        this.playersclase = new Players(db);//crea el llamado de la clase players
        // para usar su "logica" de la clase

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);//el boton de salir funcional de la ventana
        setSize(900, 600);//tamanio
        setLocationRelativeTo(null);//centrado
        setLayout(new BorderLayout(8, 8));//todod tendra un borde de 8px entre si (paneles pueh)

        // Tabla y modelo
        String[] columns = {"player_id", "first_name", "last_name", "gender", "address", "telephone", "email", "age"};//columnas que tendra
        tablaPlayer = new DefaultTableModel(columns, 0) { //se crea la tabla con los nombres que le pase con el string
            // Hacemos todas las celdas no editables directamente, si no con el formulario
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabla = new JTable(tablaPlayer);//crea el disenio de la tabla y se le envian sus datos (su estructura)
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);//solo se puede seleccionar una fila

        JScrollPane scroll = new JScrollPane(tabla);//panel con  cosito para poder desplazarse
        add(scroll, BorderLayout.CENTER);//coloca la tabla dentro del scroll al centro de la ventana

        // Panel formulario abajo, con los campos de formulario y sus labels
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();//este ayuda a indicar en que posicion va a ir cada cosa
        c.insets = new Insets(4, 4, 4, 4);//el espacio de cada campo entre si
        c.anchor = GridBagConstraints.WEST;//todos los campos se alinearan a la izquiera (WEST)

        int row = 0;

        tfPlayerId.setEditable(false); // como se incrementa solo , no editable al insertar
        addToForm(form, c, row++, "ID:", tfPlayerId, "First name:", tfFirstName, "Last name:", tfLastName);

        // Second row: gender, age, telephone
        addToForm(form, c, row++, "Gender:", tfGender, "Age:", tfAge, "Telephone:", tfTelephone);

        // Third row: address (wide)
        addToForm(form, c, row++, "Address:", tfAddress, null, null, "Email:", tfEmail);

        add(form, BorderLayout.SOUTH);

        // Panel de botones derecha seran las acciones CRUD
        JPanel buttons = new JPanel(new GridLayout(4, 1, 6, 6));//4 filas, una sola columna (vertical uno debajo del otro), 6x6 px de espacio entre cada boton
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
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) llenarSelectedRow();
        });

        btnSelect.addActionListener(e -> cargartablaplayers());//si se selecciona el boton select carga la tabla (refresca)
       // btnInsert.addActionListener(e -> insertarBackground());// es el evento de los otros pero aun no existen
       // btnUpdate.addActionListener(e -> actualizarBackground());
       // btnDelete.addActionListener(e -> eliminarBackground());
    }

    // basicamente este metodo posiciona los campos de formulario de 3 en 3 por fila, el GridBagLayout utiliza puras coordenadas, con esto
    //se evita tener que repetirlo las 8 veces por cada campo, simplemente obtiene de 3 en 3 y los posiciona en el layout inferior
    private void addToForm(JPanel panel, GridBagConstraints c, int row,
                           String label1, JComponent comp1,
                           String label2, JComponent comp2,
                           String label3, JComponent comp3) {
        // la c es del GridbagConstraints es como el que obtiene la posicion y ya coloca el valor ahi.
        c.gridy = row;//en que fila se van a colocar
        c.gridx = 0;//posicion en la columna
        if (label1 != null) panel.add(new JLabel(label1), c);//si el dato que mandan es distinto de null aniade el label con el nombre que se le mando y con su c(coordenada)
        c.gridx = 1;//campo ira al lado de su label
        if (comp1 != null) panel.add(comp1, c);//aniade el campo mientras haya un dato

        //el resto es lo mismo en distintas posiciones
        c.gridx = 2;
        if (label2 != null) panel.add(new JLabel(label2), c);
        c.gridx = 3;
        if (comp2 != null) panel.add(comp2, c);

        c.gridx = 4;
        if (label3 != null) panel.add(new JLabel(label3), c);
        c.gridx = 5;
        if (comp3 != null) panel.add(comp3, c);
    }

    // este se parece al del dgv en c#
    private void llenarSelectedRow() {
        int sel = tabla.getSelectedRow();//obtiene el numero de la fila que el usuaripo selecciono
        if (sel == -1) return;//si no se selecciono nada no llena nada
        //llena los textfields con los datos que le corresponden de la tabla
        tfPlayerId.setText(String.valueOf(tablaPlayer.getValueAt(sel, 0)));//el id esta en la columna 0 asi que dependiendo de la fila llena el text field
        // con la informacion que este en la columna 0 en la fila ejemplo 1
        //ya el resto es lo mismo
        tfFirstName.setText(String.valueOf(tablaPlayer.getValueAt(sel, 1)));
        tfLastName.setText(String.valueOf(tablaPlayer.getValueAt(sel, 2)));
        tfGender.setText(String.valueOf(tablaPlayer.getValueAt(sel, 3)));
        tfAddress.setText(String.valueOf(tablaPlayer.getValueAt(sel, 4)));
        tfTelephone.setText(String.valueOf(tablaPlayer.getValueAt(sel, 5)));
        tfEmail.setText(String.valueOf(tablaPlayer.getValueAt(sel, 6)));
        tfAge.setText(String.valueOf(tablaPlayer.getValueAt(sel, 7)));
    }

    // Metodo que carga la tabla de players
    private void cargartablaplayers() {
        new Thread(() -> {//crea un hilo que va a hacer el proceso de cargar los datos para evitar que el hilo de la UI se congele y por ende la UI
            try {
                List<String[]> rows = playersclase.listarPlayers();//llama al metodo en players que regresa a
                // todos los jugadores y cada  fila se volvera un arreglo string
                SwingUtilities.invokeLater(() -> {//manda la info obtenida al hilo de la UI (ese hilo se ve en el main por si acaso)
                    tablaPlayer.setRowCount(0);//limpia la tabla antes de volverla a llenar
                    for (String[] r : rows) {//el for llenara fila por fila los valores (arreglo de strings) a la tabla
                        tablaPlayer.addRow(r);
                    }
                });
            } catch (Exception ex) {//cacha si ocurre un error y lanza el mensaje
                ex.printStackTrace();
                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this,
                        "Error al obtener datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE));
            }
        }).start();
    }

}
