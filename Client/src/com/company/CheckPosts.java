package com.company;

import javax.swing.*;
import java.awt.*;

public class CheckPosts {
    private JPanel first;
    private JTextArea TextArea;
    private JLabel message;

    public CheckPosts(){
        TextArea.setText("Tu będzie\nSkrzynka Odbiorcza\ni duzo postow");
    }

    public static void checkPost(){
        JFrame frame = new JFrame("App");
        frame.setContentPane(new CheckPosts().first);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(500,300));
        frame.pack();
        frame.setVisible(true);
    }
}
