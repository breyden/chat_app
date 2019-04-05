package thread;

import overidden.ServerSocketM;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by Arjun on 4/5/2017.
 */
public class MessageForwarder extends Thread {
    private ServerSocketM[] serverSockets;
    private Socket socket;
    private int senderIndex;
    private String message;
    private PrintWriter printWriter;

    public MessageForwarder(ServerSocketM[] serverSockets, Socket socket, int senderIndex, String message){
        this.serverSockets = serverSockets;
        this.socket = socket;
        this.senderIndex = senderIndex;
        this.message = message;
    }

    public void run(){
        try{
            System.out.println(message);//"Message to " + serverSockets[i].getAcceptedSocket().getLocalPort());
            for (int i = 0; i < serverSockets.length; i++) {
                if(i != senderIndex && serverSockets[i].isAccepted() && serverSockets[i].isConnected()){
                    OutputStream out = serverSockets[i].getAcceptedSocket().getOutputStream();
                    printWriter = new PrintWriter(out);
                    printWriter.write(message + "\n");
                    printWriter.flush();
                }
            }
        } catch (Exception e){
            //e.printStackTrace();
        }
    }
}
