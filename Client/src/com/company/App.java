package com.company;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class App {

    private JPanel first;
    private JTextArea password;
    private JTextArea login;
    private JRadioButton rejestracjaRadioButton;
    private JRadioButton logowanieRadioButton;
    private JButton zatwierdzButton;

    public App() {
        zatwierdzButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(rejestracjaRadioButton.isSelected()){
                    System.out.println("Jest selected");
                    rejestracjaRadioButton.setSelected(false);
                    JOptionPane.showMessageDialog(zatwierdzButton, "Pomyslnie dokonano rejestracji!");

                }
                if(logowanieRadioButton.isSelected()){
                    System.out.println("Cos tam");
                    logowanieRadioButton.setSelected(false);
                    JOptionPane.showMessageDialog(zatwierdzButton, "Logowanko!");

                }
            }
        });
        logowanieRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rejestracjaRadioButton.setSelected(false);
            }
        });
        rejestracjaRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logowanieRadioButton.setSelected(false);
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("App");
        frame.setContentPane(new App().first);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400,500);
        frame.pack();
        frame.setVisible(true);
    }
}
