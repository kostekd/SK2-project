package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

public class Subscribe {


    private JPanel first;
    private JTextArea textArea1;
    private JLabel zapytaj;
    private JTextField answer;
    private JButton zatwierdz;
    private int firstChoice;

    byte[] buffer = new byte[7];
    byte[] number = new byte[2];
    byte[] topicString = new byte[100000];
    byte[] postString = new byte[100000];
    int n;

    public static int trimNumber(String tmp) {
        String result = "";
        for(int i=0 ; i<tmp.length() ; i++){
            if((int)tmp.charAt(i) >= 48 && (int)tmp.charAt(i) <= 57 ){
                result += tmp.charAt(i);
            }
        }
        return Integer.valueOf(result);
    }

    public static String trimString(String tmp){
        String result = "1.";
        int cnt = 1;
        for(int i=0 ; i<tmp.length() ; i++){
            if((int)tmp.charAt(i) != 0){
                result += tmp.charAt(i);
                if(tmp.charAt(i) == '\n'){
                    cnt++;
                    result += Integer.toString(cnt) + '.';
                }
            }
        }
        result = result.substring(0,result.length() - 3);
        return result;
    }

    public static String convertToString(byte[] buffer){
        String tmp = "";
        for(int i=0 ; i<buffer.length ; i++){
            tmp += (char)buffer[i];
        }
        return tmp;
    }


    public Subscribe(Socket clientSocket, String nickname) throws IOException {
        //textArea1.setText("sialala\nARKA GDYNIA");
        BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
        InputStream is = clientSocket.getInputStream();

        is.read(number);
        n = trimNumber(convertToString(number));
        System.out.println(n);
        is.read(topicString);
        textArea1.setText(trimString(convertToString(topicString)));
        System.out.println();


        zatwierdz.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!answer.getText().equals("")){
                   firstChoice = Integer.valueOf(answer.getText());
                   writer.println(firstChoice);
                   writer.println(nickname);
                    System.out.println("Udalo sie dodac temat do usera");
                    System.exit(0);
                }
            }
        });
    }

    public static void sub(Socket socket, String nickname) throws IOException{
        JFrame frame = new JFrame("App");
        frame.setContentPane(new Subscribe(socket, nickname).first);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(500,300));
        frame.pack();
        frame.setVisible(true);

    }
}
