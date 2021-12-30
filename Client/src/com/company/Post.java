package com.company;

import javax.swing.*;
import java.awt.*;

public class Post {
    private JPanel first;
    private JTextArea textArea1;
    private JLabel zapytaj;
    private JTextField textField1;
    private JButton zatwierdz;
    private JLabel zapytaj1;
    private JTextField nowyPost;
    private JButton wyslij;

    public Post(){
        textArea1.setText("1.Temat1\n2.Temat2\n3.Temat3");
    }

    public static void write(){
        JFrame frame = new JFrame("App");
        frame.setContentPane(new Post().first);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(500,300));
        frame.pack();
        frame.setVisible(true);
    }
}
