package com.company;

import java.io.*;
import java.net.Socket;
import java.sql.SQLOutput;
import java.util.Scanner;

public class Main {

    public static String convertToString(byte[] buffer){
        String tmp = "";
        for(int i=0 ; i<buffer.length ; i++){
            tmp += (char)buffer[i];
        }
        return tmp;
    }


    public static void main(String[] args) throws IOException {
        //variables
        Scanner scan = new Scanner(System.in);
        String clientMessage = "";
        int firstChoice;
        String tmpString = "";
        String username, password;

        //connection handlers
        Socket clientSocket = new Socket("localhost", 1235);
        BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
        InputStream is = clientSocket.getInputStream();
        byte[] buffer = new byte[11];

        System.out.println("Co chcesz teraz zrobic?");
        System.out.println("1.Rejestracja");
        System.out.println("2.Zaloguj sie");
        System.out.println("Twoj wybor:");

        firstChoice = Integer.valueOf(scan.nextLine());
        writer.println(firstChoice);
        switch(firstChoice){
            case 1:
                System.out.println("Podaj nazwe uzytkownika:");
                username = scan.nextLine();
                writer.println(username);
                System.out.println("Podaj haslo uzytkownika:");
                password = scan.nextLine();
                writer.println(password);
                is.read(buffer);
                System.out.println(convertToString(buffer));
                break;
            case 2:
                System.out.println("Podaj nazwe uzytkownika:");
                username = scan.nextLine();
                writer.println(username);
                System.out.println("Podaj haslo uzytkownika:");
                password = scan.nextLine();
                writer.println(password);
                is.read(buffer);
                System.out.println(convertToString(buffer));
                break;
        }


/*
        System.out.println("Co ty tam chcesz powiedziec:");
        clientMessage = scan.nextLine();
        writer.println(clientMessage);

 */
/*
        System.out.println("czekam1");
        String serverMessage = reader.readLine();
        System.out.println(serverMessage);
        System.out.println("czekam2");


 */
/*
        InputStream is = clientSocket.getInputStream();
        byte[] buffer = new byte[11];
        is.read(buffer);
        System.out.printf("%s",convertToString(buffer));
        clientSocket.close();
*/
    }
}