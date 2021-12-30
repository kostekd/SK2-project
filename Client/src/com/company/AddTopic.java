package com.company;

import javax.swing.*;
import java.awt.*;

public class AddTopic {
    private JTextField textField1;
    private JLabel what;
    private JPanel first;
    private JButton zatwierdz;

    public AddTopic(){

    }


        public static void add(){
            JFrame frame = new JFrame("App");
            frame.setContentPane(new AddTopic().first);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setPreferredSize(new Dimension(500,140));
            frame.pack();
            frame.setVisible(true);
        }
    }

