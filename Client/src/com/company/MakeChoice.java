package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

public class MakeChoice {
    private JPanel first;
    private JRadioButton sprTablica;
    private JRadioButton zasubskrybuj;
    private JRadioButton dodajTemat;
    private JLabel what;
    private JRadioButton wyslij;
    private JButton zatwierdz;
    int firstChoice;

    public MakeChoice(Socket clientSocket, String nickname, JFrame frame) throws IOException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
        InputStream is = clientSocket.getInputStream();

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
                    firstChoice = Integer.valueOf("1");
                    writer.println(firstChoice);


                    try {
                        AddTopic.add(clientSocket);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }


                }
                if(zasubskrybuj.isSelected()){
                    System.out.println("Zasubskrybuj");
                    firstChoice = Integer.valueOf("2");
                    writer.println(firstChoice);
                    try {
                        Subscribe.sub(clientSocket, nickname);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
                if(sprTablica.isSelected()){
                    System.out.println("Sprawdz Tablice");
                    firstChoice = Integer.valueOf("4");
                    writer.println(firstChoice);
                    try {
                        CheckPosts.checkPost(clientSocket, nickname);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
                if(wyslij.isSelected()){
                    System.out.println("Wyslij posta");
                    firstChoice = Integer.valueOf("3");
                    writer.println(firstChoice);
                    try {
                        Post.write(clientSocket);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }

                frame.dispose();

            }
        });
    }

    public static void inteface(Socket socket, String nickname) throws IOException {
        JFrame frame1 = new JFrame("Menu");
        frame1.setContentPane(new MakeChoice(socket, nickname, frame1).first);
        frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame1.setPreferredSize(new Dimension(600,200));
        frame1.pack();
        frame1.setVisible(true);
    }

}
