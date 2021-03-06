package thread;

import byte_array_size.ByteArraySize;
import overidden.ServerSocketM;

import java.io.*;
import java.net.Socket;

/**
 * Created by Arjun on 4/5/2017.
 */
public class FileForwarder extends  Thread{
    private Socket socket;
    private PrintWriter p;
    private File file;

    private ServerSocketM serverSocket;

    public FileForwarder(ServerSocketM serverSocket) {
        this.serverSocket = serverSocket;
        if(serverSocket.isAccepted()){
            socket = serverSocket.getAcceptedSocket();
        } else {
            socket = null;
            System.out.println("FileForwarder: Socket is null");
        }
        this.file = new File(serverSocket.getFilePath()); // file to be sent
    }

    public void run(){
        try{

            System.out.println("FileForwarder: File is being sent to " + socket.getLocalPort());
            OutputStream out = socket.getOutputStream();

            p = new PrintWriter(out); // get the output stream to the server
            p.println("@ReceiveFileRequest:"); // sends the message to the server
            p.flush(); // so that the bytes get to the server

            FileInputStream fileReader = new FileInputStream(file);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(fileReader);
            byte[] bytes = new byte[ByteArraySize.ByteArraySize];
            System.out.println("(Sending File...)");

            int count;
            while ((count = bufferedInputStream.read(bytes)) > 0) {
                out.write(bytes, 0, count); // if error, remove bytes.length and put count
            }
            out.flush();

            System.out.println("(File Sent)");
            serverSocket.fileSent(); //therefore this socket will now be able to accept any other file now.

        } catch (Exception e){
            //e.printStackTrace();
        }
    }
}
