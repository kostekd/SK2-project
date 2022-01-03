package com.company;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;

public class CheckPosts {
    private JPanel first;
    private JTextArea TextArea;
    private JLabel message;

    byte[] buffer = new byte[7];
    byte[] number = new byte[2];
    byte[] topicString = new byte[100000];
    byte[] postString = new byte[100000];
    int n;

    public static boolean checkUpper(String myStr){
        boolean flag = true;
        for(int i=0 ; i<myStr.length() - 1; i++){
            if(!Character.isUpperCase(myStr.charAt(i)) && myStr.charAt(i) != ' '){
                flag = false;
            }
        }
        return flag;
    }
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
        String result = "";
        String tmpResult = "";
        int cnt = 1;
        for(int i=0 ; i<tmp.length() ; i++){
            if((int)tmp.charAt(i) != 0){
                tmpResult += tmp.charAt(i);
                if(tmp.charAt(i) == '\n'){
                    if(checkUpper(tmpResult)){
                        result += "Temat: " + tmpResult;
                        tmpResult = "";
                    }
                    else{
                        result += tmpResult;
                        tmpResult = "";
                    }

                }
            }
        }
        result = result.substring(0,result.length());
        return result;
    }

    public static String convertToString(byte[] buffer){
        String tmp = "";
        for(int i=0 ; i<buffer.length ; i++){
            tmp += (char)buffer[i];
        }
        return tmp;
    }

    public CheckPosts(Socket clientSocket, String nickname) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
        InputStream is = clientSocket.getInputStream();

        writer.println(nickname);
        is.read(topicString);
        TextArea.setText(trimString(convertToString(topicString)));
        System.out.println();

    }

    public static void checkPost(Socket socket, String nickname) throws IOException{
        JFrame frame = new JFrame("App");
        frame.setContentPane(new CheckPosts(socket, nickname).first);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(500,300));
        frame.pack();
        frame.setVisible(true);
    }
}
