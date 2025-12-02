package com.example;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class EstiloForm {

    //Panel principal
    public static JPanel estiloPanel() {
        JPanel panel = new JPanel(new GridLayout(0, 2, 12, 12));
        panel.setBackground(new Color(245, 245, 250));

        panel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(180, 180, 200), 2, true),
                new EmptyBorder(25, 25, 25, 25)
        ));

        return panel;
    }

    //Botones
    public static JButton crearBoton(String text) {
        JButton btn = new JButton(text);

        btn.setFocusPainted(false);
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(70, 115, 220));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(30, 40, 30, 40));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));

        // Bordes redondeados
        btn.setBorder(BorderFactory.createLineBorder(new Color(55, 90, 185), 2, true));

        // Hover
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(new Color(90, 135, 240));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(new Color(70, 115, 220));
            }
        });

        return btn;
    }

    // botones centrados
    public static JPanel centrar(JButton btn) {
        JPanel p = new JPanel();
        p.setBackground(new Color(245, 245, 250));
        p.add(btn);
        return p;
    }

}
