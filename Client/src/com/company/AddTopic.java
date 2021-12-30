package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

public class AddTopic {
    private JTextField content;
    private JLabel what;
    private JPanel first;
    private JButton zatwierdz;

    public AddTopic(Socket clientSocket) throws IOException{

        BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
        InputStream is = clientSocket.getInputStream();

        zatwierdz.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!content.getText().equals("")){
                    writer.println(content.getText());
                    System.out.println("Udalo sie");
                    try {
                        clientSocket.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }

                    System.exit(0);
                }
            }
        });
    }


        public static void add(Socket socket) throws IOException {
            JFrame frame = new JFrame("App");
            frame.setContentPane(new AddTopic(socket).first);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setPreferredSize(new Dimension(500,140));
            frame.pack();
            frame.setVisible(true);
        }
    }

