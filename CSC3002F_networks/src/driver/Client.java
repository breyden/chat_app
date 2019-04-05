package driver;

import thread.MessageReaderFromServerForClient;
import thread.MessageSenderToServerForClient;

import java.net.Socket;
import java.util.Scanner;

/**
 * Created by Arjun on 4/5/2017.
 */
public class Client {
    private static String portNumber = "abcd";
    public static int DefaultPortNumber = 6781;

    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in); // to get port number and messages from user

        try{
            while(!isDigit(portNumber)){
                System.out.print("Enter a 4-digits port number:\n");
                portNumber = scanner.next();
                portNumber = portNumber.trim();
            }
            System.out.println("PortNumber: " + portNumber);
        } catch (Exception e){
            portNumber = (DefaultPortNumber++) + "";
        }

        System.out.println("Connecting to server...");

        Socket socket = null;
        boolean connected = false;
        int connectionTrialLimit = 0;

        while(!connected){
            try{
                socket = new Socket("localhost", Integer.parseInt(portNumber));

                // server is online, connected = true;
                connected = true;
                System.out.println("Type '@help' for help messages");
                System.out.println("----- Chat Begin -----");

            } catch (Exception e){
                // Server is offline
                connectionTrialLimit++;
                if(connectionTrialLimit > 1000){
                    System.out.println("Server not online yet. Please try again later.");
                    System.exit(0);
                }
            }
        }

        try {
            MessageReaderFromServerForClient r = new MessageReaderFromServerForClient(socket);
            MessageSenderToServerForClient s = new MessageSenderToServerForClient(socket);
            r.start(); // returns immediately
            s.start(); // returns immediately
            r.join();
            s.join();
            System.out.println("----- Chat End -----");
        } catch (Exception e){
            //e.printStackTrace();
        }
    }

    private static boolean isDigit(String s){
        char c;
        for(int i = 0; i < s.length(); i++){
            c = s.charAt(i);
            if (!Character.isDigit(c)){
                return false;
            }
        }
        return true;
    }
}

