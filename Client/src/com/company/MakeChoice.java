package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MakeChoice {
    private JPanel first;
    private JRadioButton sprTablica;
    private JRadioButton zasubskrybuj;
    private JRadioButton dodajTemat;
    private JLabel what;
    private JRadioButton wyslij;
    private JButton zatwierdz;

    public MakeChoice() {


        sprTablica.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dodajTemat.setSelected(false);
                zasubskrybuj.setSelected(false);
                wyslij.setSelected(false);
            }
        });
        zasubskrybuj.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dodajTemat.setSelected(false);
                sprTablica.setSelected(false);
                wyslij.setSelected(false);
            }
        });

        dodajTemat.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                zasubskrybuj.setSelected(false);
                sprTablica.setSelected(false);
                wyslij.setSelected(false);
            }
        });

        wyslij.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dodajTemat.setSelected(false);
                zasubskrybuj.setSelected(false);
                sprTablica.setSelected(false);

            }
        });

        zatwierdz.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(dodajTemat.isSelected()){
                    System.out.println("Dodaj temat");
                    AddTopic.add();
                }
                if(zasubskrybuj.isSelected()){
                    System.out.println("Zasubskrybuj");
                    Subscribe.sub();
                }
                if(sprTablica.isSelected()){
                    System.out.println("Sprawdz Tablice");
                    CheckPosts.checkPost();
                }
                if(wyslij.isSelected()){
                    System.out.println("Wyslij posta");
                    Post.write();
                }

            }
        });
    }

    public static void inteface() {
        JFrame frame1 = new JFrame("Menu");
        frame1.setContentPane(new MakeChoice().first);
        frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame1.setPreferredSize(new Dimension(600,200));
        frame1.pack();
        frame1.setVisible(true);
    }

}
