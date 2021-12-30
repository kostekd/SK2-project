package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

public class Post {
    private JPanel first;
    private JTextArea textArea1;
    private JLabel zapytaj;
    private JTextField textField1;
    private JButton zatwierdz;
    private JLabel zapytaj1;
    private JTextField nowyPost;
    private JButton wyslij;

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


    public Post(Socket clientSocket) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
        InputStream is = clientSocket.getInputStream();

        is.read(number);
        n = trimNumber(convertToString(number));
        System.out.println(n);
        is.read(topicString);
        textArea1.setText(trimString(convertToString(topicString)));


        wyslij.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!nowyPost.getText().equals("") && !textField1.getText().equals("")){
                    writer.println(textField1.getText());
                    writer.println(nowyPost.getText());
                    System.out.println("Temat pomyslnie dodany");
                    System.exit(0);
                }
            }
        });
    }

    public static void write(Socket socket) throws IOException{
        JFrame frame = new JFrame("App");
        frame.setContentPane(new Post(socket).first);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(500,300));
        frame.pack();
        frame.setVisible(true);
    }
}
