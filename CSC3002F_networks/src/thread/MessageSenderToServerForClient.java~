package thread;

import byte_array_size.ByteArraySize;

import java.io.*;
import java.net.Socket;
import java.net.UnknownServiceException;

/**
 * Created by Arjun on 4/5/2017.
 */
public class MessageSenderToServerForClient extends Thread {
    private PrintWriter p;
    private BufferedReader bufferedReader;
    private Socket socket;
    private volatile boolean interrupted = false;

    public MessageSenderToServerForClient(Socket socket){ // Reads message from client and sends it to server
        this.socket = socket;
        this.bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    }

    public void run(){
        while(!interrupted && !socket.isClosed()) {
            try {
                String message = bufferedReader.readLine(); // standard input
                if(message.trim().equalsIgnoreCase("@exit")){ // Exiting the chat
                    this.interrupt();
		} else if (message.trim().equalsIgnoreCase("@help")){
		    String help = "1) To send a message to the whole group, just type a message and press Enter to send."+
		    "\n2) To send a private to someone in the chat, type '@private6789: <message>', where 6789 is the portnumber of the receiver and press Enter."+
		    "\n3) To test file sending, type '@sendfile:', the file 'dog.png' will be sent to the other clients, if they accept it."+
		    "\n4) To send a file to every member in the chat, type '@sendfile: <full path to the file>'"+
		    "\n5) To exit the chat, type '@exit'"
		    ;
		    System.out.println(help);
                } else if (message.toLowerCase().contains("@sendfile:")){ // Sending file to server

                    // Extracting the path of the file
                    String path;
                    File file;
                    try {
                        path = message.substring(message.indexOf(':')+1, message.length()).trim();
                        if(path.trim().equals("")){
                            throw new UnknownServiceException();
                        }
                        file = new File(path);
                        System.out.println("(Path of file being sent: " + path + ")");
                    } catch (Exception e){
                        path = System.getProperty("user.dir") + "/src/files/dog.png";
                        System.out.println("(Path Extraction Error, Assumed path: " + path + ")");
                        file = new File(path);
                    }

                    OutputStream out = socket.getOutputStream();

                    p = new PrintWriter(out); // get the output stream to the server
                    p.println("@ReceiveFileRequest: File Received from port " + socket.getPort()); // sends the message to the server
                    p.flush(); // so that the bytes get to the server
                    System.out.println("(Server has been requested to receive the file)");


                    FileInputStream fileInputStream = new FileInputStream(file);
                    //BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);

                    byte[] bytes = new byte[ByteArraySize.ByteArraySize];
                    System.out.println("(Sending File...)");

                    int count;
                    while ((count = fileInputStream.read(bytes))> 0) {
                        out.write(bytes, 0, count);
                    }

                    System.out.println("(File Sent)");

                    p = new PrintWriter(out);
                    p.println("@FileReceived: File Received from port " + socket.getPort()+"");
                    p.flush();

                    System.out.println("(Server has been notified of file received)");

                } else {
                    p = new PrintWriter(socket.getOutputStream());
                    p.println(message);
                    p.flush();
                    System.out.println("(Message Sent)");
                }
            } catch (java.net.SocketException e1){
                this.interrupt();
                //e1.printStackTrace();
            }catch (Exception e2) {
                //e2.printStackTrace();
            }
        }
        //System.exit(0);
    }

    public void interrupt(){
        this.interrupted = true;
        try{
            if(bufferedReader != null) {
                bufferedReader.close();
            }

            if(p!= null){
                p.close();
            }

            socket.close();
        } catch (Exception e){
            //e.printStackTrace();
        }
    }
}
