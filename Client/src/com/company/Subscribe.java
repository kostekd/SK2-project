package com.company;

import javax.swing.*;
import java.awt.*;

public class Subscribe {


    private JPanel first;
    private JTextArea textArea1;
    private JLabel zapytaj;
    private JTextField textField1;
    private JButton zatwierdz;

    public Subscribe(){
        textArea1.setText("sialala\nARKA GDYNIA");
    }

    public static void sub(){
        JFrame frame = new JFrame("App");
        frame.setContentPane(new Subscribe().first);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(500,300));
        frame.pack();
        frame.setVisible(true);

    }
}
