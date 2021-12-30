package com.company;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;


public class App {

    private JPanel first;
    private JPanel second;
    private JTextArea password;
    private JTextArea login;
    private JRadioButton rejestracjaRadioButton;
    private JRadioButton logowanieRadioButton;
    private JButton zatwierdzButton;
    public String nickname;


    byte[] buffer = new byte[7];
    byte[] number = new byte[2];
    byte[] topicString = new byte[100000];
    byte[] postString = new byte[100000];
    int firstChoice;


    public String getNickname() {
        return nickname;
    }

    public static String convertToString(byte[] buffer){
        String tmp = "";
        for(int i=0 ; i<buffer.length ; i++){
            tmp += (char)buffer[i];
        }
        return tmp;
    }

    public App(JFrame frame) throws IOException{

        Socket clientSocket = new Socket("localhost", 1235);
        BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
        InputStream is = clientSocket.getInputStream();

        zatwierdzButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(rejestracjaRadioButton.isSelected()){
                    firstChoice = Integer.valueOf("1");
                    writer.println(firstChoice);
                    System.out.println("Jest selected");

                    System.out.println(buffer);


                    rejestracjaRadioButton.setSelected(false);
                    if(!login.getText().equals("") && !password.getText().equals("")){
                        System.out.println(login.getText());
                        writer.println(login.getText());
                        writer.println(password.getText());
                        System.out.println(password.getText());
                        try {
                            is.read(buffer);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }

                        frame.dispose();
                        System.exit(0);
                        try {
                            clientSocket.close();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }

                    }

                    else{
                        System.out.println("Jebac");
                    }


                    JOptionPane.showMessageDialog(zatwierdzButton, convertToString(buffer));

                }
                if(logowanieRadioButton.isSelected()){
                    System.out.println("Cos tam");

                    logowanieRadioButton.setSelected(false);
                    if(!login.getText().equals("") && !password.getText().equals("")){
                        firstChoice = Integer.valueOf("2");
                        writer.println(firstChoice);
                        System.out.println(login.getText());
                        writer.println(login.getText());
                        writer.println(password.getText());
                        System.out.println(password.getText());
                        try {
                            is.read(buffer);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }

                        nickname = login.getText();
                        //zamknij jakos okienko
                    }
                    JOptionPane.showMessageDialog(zatwierdzButton, convertToString(buffer));
                    try {
                        MakeChoice.inteface(clientSocket, nickname);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }


                }logowanieRadioButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        rejestracjaRadioButton.setSelected(false);
                    }
                });
            }
        });

        rejestracjaRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logowanieRadioButton.setSelected(false);
            }
        });
    }

    public static void main(String[] args) throws IOException {
        JFrame frame = new JFrame("App");
        frame.setContentPane(new App(frame).first);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400,500);
        frame.pack();
        frame.setVisible(true);

    }
}
