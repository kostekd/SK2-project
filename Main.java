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

    public static void showOption(){
        System.out.println("Witamy w Twitterze po wylewie");
        System.out.println("Co chcialbys teraz zrobic?");
        System.out.println("1.Dodaj nowy temat");
        System.out.println("2.Zaobserwuj nowy temat");
        System.out.println("3.Napisz posta");
        System.out.println("4.Pokaz swoja tablice");
        System.out.println("Twoj wybor:");
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


    public static void main(String[] args) throws IOException {
        //variables
        Scanner scan = new Scanner(System.in);
        String clientMessage = "";
        int firstChoice;
        int optionChoice;
        int n;
        String tmpString = "";
        String username, password, topic;

        //connection handlers
        Socket clientSocket = new Socket("localhost", 1234);
        BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
        InputStream is = clientSocket.getInputStream();
        byte[] buffer = new byte[7];
        byte[] number = new byte[2];
        byte[] topicString = new byte[100000];
        byte[] postString = new byte[100000];

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
                String tmpNapis = convertToString(buffer);

                //log in successful

                if(tmpNapis.equals("Success") == true){
                    showOption();
                    optionChoice = Integer.valueOf(scan.nextLine());
                    //sending the decision
                    writer.println(optionChoice);
                    switch(optionChoice){
                        case 1:
                            System.out.println("Nazwa tematu:");
                            topic = scan.nextLine();
                            writer.println(topic);
                            break;
                        case 2:
                            System.out.println("Lista tematow");
                            is.read(number);
                            n = trimNumber(convertToString(number));
                            System.out.println(n);
                            is.read(topicString);
                            //System.out.println(convertToString(topicString));
                            System.out.println(trimString(convertToString(topicString)));
                            System.out.println("Ktory temat chcesz obserwowac(Podaj numer):");
                            topic = scan.nextLine();
                            writer.println(topic);
                            writer.println(username);
                            System.out.println("Done!");
                            break;

                        case 3:
                            System.out.println("Lista tematow");
                            is.read(number);
                            n = trimNumber(convertToString(number));
                            System.out.println(n);
                            is.read(topicString);
                            System.out.println(trimString(convertToString(topicString)));
                            System.out.println("Ktory temat chcesz obserwowac(Podaj numer):");
                            topic = scan.nextLine();
                            writer.println(topic);
                            System.out.println("Tresc posta:");
                            topic = scan.nextLine();
                            writer.println(topic);

                            break;

                        case 4:
                            System.out.println("Posty na twojej tablicy");
                            writer.println(username);
                            is.read(postString);
                            System.out.println(trimString(convertToString(postString)));
                            break;
                    }


                }
                //failed to log in
                else{

                }

                break;
        }

        clientSocket.close();

    }
}
